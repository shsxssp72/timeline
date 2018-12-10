package com.softwareTest.timeline.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

//Ref:https://blog.csdn.net/plei_yue/article/details/79362372
//Ref:https://blog.csdn.net/jzd1997/article/details/79277541
@Component
public class RedisUtility
{
	@Autowired
	RedisTemplate<String,Object> redisTemplate;


	/**
	 * 指定缓存失效时间
	 *
	 * @param key  键
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean setExpireByKey(String key,long time)
	{
		try
		{
			if(time>0)
			{
				redisTemplate.expire(key,time,TimeUnit.SECONDS);
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据key 获取过期时间
	 *
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public long getExpireByKey(String key)
	{
		return redisTemplate.getExpire(key,TimeUnit.SECONDS);
	}

	/**
	 * 判断key是否存在
	 *
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key)
	{
		try
		{
			return redisTemplate.hasKey(key);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除缓存
	 *
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public void deleteByKey(String... key)
	{
		if(key!=null&&key.length>0)
		{
			if(key.length==1)
			{
				redisTemplate.delete(key[0]);
			}
			else
			{
				redisTemplate.delete(CollectionUtils.arrayToList(key));
			}
		}
	}

	/**
	 * 普通缓存获取
	 *
	 * @param key 键
	 * @return 值
	 */
	public Object getByKey(String key)
	{
		return key==null?null:redisTemplate.opsForValue().get(key);
	}

	public List<Object> multiGet(Collection<String> keys)
	{
		return redisTemplate.opsForValue().multiGet(keys);
	}

	/**
	 * 普通缓存放入
	 *
	 * @param key   键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public boolean set(String key,Object value)
	{
		try
		{
			redisTemplate.opsForValue().set(key,value);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key,Object value,long time)
	{
		try
		{
			if(time>0)
			{
				redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
			}
			else
			{
				set(key,value);
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递增
	 *
	 * @param key   键
	 * @param delta 要增加几(大于0)
	 * @return
	 */
	public long increaseValueByKey(String key,long delta)
	{
		if(delta<0)
		{
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key,delta);
	}

	/**
	 * 递减
	 *
	 * @param key   键
	 * @param delta 要减少几(小于0)
	 * @return
	 */
	public long decreaseValueByKey(String key,long delta)
	{
		if(delta<0)
		{
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key,-delta);
	}

	/**
	 * HashGet
	 *
	 * @param key  键 不能为null
	 * @param hashKey 项 不能为null
	 * @return 值
	 */
	public Object getFromHashByKey(String key,String hashKey)
	{
		return redisTemplate.opsForHash().get(key,hashKey);
	}

	/**
	 * 获取hashKey对应的所有键值
	 *
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map<Object,Object> getAllByHashKey(String key)
	{
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * HashSet
	 *
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public boolean setAllToHash(String key,Map<String,Object> map)
	{
		try
		{
			redisTemplate.opsForHash().putAll(key,map);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * HashSet 并设置时间
	 *
	 * @param key  键
	 * @param map  对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public boolean setAllToHash(String key,Map<String,Object> map,long time)
	{
		try
		{
			redisTemplate.opsForHash().putAll(key,map);
			if(time>0)
			{
				setExpireByKey(key,time);
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param hashKey  项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public boolean setToHash(String key,String hashKey,Object value)
	{
		try
		{
			redisTemplate.opsForHash().put(key,hashKey,value);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param hashKey  项
	 * @param value 值
	 * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public boolean setToHash(String key,String hashKey,Object value,long time)
	{
		try
		{
			redisTemplate.opsForHash().put(key,hashKey,value);
			if(time>0)
			{
				setExpireByKey(key,time);
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 *
	 * @param key  键 不能为null
	 * @param hashKey 项 可以使多个 不能为null
	 */
	public void removeFromHash(String key,Object... hashKey)
	{
		redisTemplate.opsForHash().delete(key,hashKey);
	}

	/**
	 * 判断hash表中是否有该项的值
	 *
	 * @param key  键 不能为null
	 * @param hashKey 项 不能为null
	 * @return true 存在 false不存在
	 */
	public boolean hashHasKey(String key,String hashKey)
	{
		return redisTemplate.opsForHash().hasKey(key,hashKey);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 *
	 * @param key  键
	 * @param hashKey 项
	 * @param by   要增加几(大于0)
	 * @return
	 */
	public double hashIncreaseValueByKey(String key,String hashKey,double by)
	{
		return redisTemplate.opsForHash().increment(key,hashKey,by);
	}

	/**
	 * hash递减
	 *
	 * @param key  键
	 * @param hashKey 项
	 * @param by   要减少记(小于0)
	 * @return
	 */
	public double hashDecreaseValueByKey(String key,String hashKey,double by)
	{
		return redisTemplate.opsForHash().increment(key,hashKey,-by);
	}

	/**
	 * 根据key获取Set中的所有值
	 *
	 * @param key 键
	 * @return
	 */
	public Set<Object> getFromSetByKey(String key)
	{
		try
		{
			return redisTemplate.opsForSet().members(key);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 *
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean setHasKey(String key,Object value)
	{
		try
		{
			return redisTemplate.opsForSet().isMember(key,value);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long setToSet(String key,Object... values)
	{
		try
		{
			return redisTemplate.opsForSet().add(key,values);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将set数据放入缓存
	 *
	 * @param key    键
	 * @param time   时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long setToSet(String key,long time,Object... values)
	{
		try
		{
			Long count=redisTemplate.opsForSet().add(key,values);
			if(time>0) setExpireByKey(key,time);
			return count;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取set缓存的长度
	 *
	 * @param key 键
	 * @return
	 */
	public long getSetSizeByKey(String key)
	{
		try
		{
			return redisTemplate.opsForSet().size(key);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 移除值为value的
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long removeFormSet(String key,Object... values)
	{
		try
		{
			Long count=redisTemplate.opsForSet().remove(key,values);
			return count;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取list缓存的内容
	 *
	 * @param key   键
	 * @param start 开始
	 * @param end   结束  0 到 -1代表所有值
	 * @return
	 */
	public List<Object> getFromList(String key,long start,long end)
	{
		try
		{
			return redisTemplate.opsForList().range(key,start,end);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 *
	 * @param key 键
	 * @return
	 */
	public long getListSize(String key)
	{
		try
		{
			return redisTemplate.opsForList().size(key);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 *
	 * @param key   键
	 * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public Object getListByKeyIndex(String key,long index)
	{
		try
		{
			return redisTemplate.opsForList().index(key,index);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean setToList(String key,Object value)
	{
		try
		{
			redisTemplate.opsForList().rightPush(key,value);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean setToList(String key,Object value,long time)
	{
		try
		{
			redisTemplate.opsForList().rightPush(key,value);
			if(time>0) setExpireByKey(key,time);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean setToList(String key,List<Object> value)
	{
		try
		{
			redisTemplate.opsForList().rightPushAll(key,value);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean setToList(String key,List<Object> value,long time)
	{
		try
		{
			redisTemplate.opsForList().rightPushAll(key,value);
			if(time>0) setExpireByKey(key,time);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 *
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public boolean updateListIndex(String key,long index,Object value)
	{
		try
		{
			redisTemplate.opsForList().set(key,index,value);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 *
	 * @param key   键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public long removeFromList(String key,long count,Object value)
	{
		try
		{
			return redisTemplate.opsForList().remove(key,count,value);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 操作队列
	 */
	public void pushFromLeft(String key,String value)
	{
		redisTemplate.opsForList().leftPush(key,value);
	}

	public List<Object> range(String key,int start,int end)
	{
		return redisTemplate.opsForList().range(key,start,end);
	}

	public Object popToRight(String key)
	{
		return redisTemplate.opsForList().rightPop(key);
	}



}
