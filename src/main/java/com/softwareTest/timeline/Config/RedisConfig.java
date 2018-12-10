package com.softwareTest.timeline.Config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport
{
	@Bean
	public KeyGenerator keyGenerator()
	{
		return (target,method,params)->
		{
			StringBuilder builder=new StringBuilder();
			builder.append(target.getClass().getName());
			builder.append(method.getName());
			for(Object object:params)
			{
				builder.append(object.toString());
			}
			return builder.toString();
		};
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory)
	{
		RedisCacheConfiguration redisCacheConfiguration=RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofHours(1));
		return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
				.cacheDefaults(redisCacheConfiguration).build();
	}

	@Bean
	public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory)
	{
		RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory)
	{
		StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer= new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper mapper=new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(mapper);
		stringRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		stringRedisTemplate.setConnectionFactory(factory);
		stringRedisTemplate.afterPropertiesSet();
		return stringRedisTemplate;
	}
}
