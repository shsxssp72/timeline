package com.softwareTest.timeline;

import com.softwareTest.timeline.Utility.CryptoUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class TimelineApplication
{
	public static void main(String[] args)
	{

		SpringApplication.run(TimelineApplication.class,args);
	}
}


//TODO Spring Security
//TODO Api and Document
//TODO Error Handler

//TODO 可以映射/login get方法为申请新的临时密钥(RSA)
//TODO 映射/login put方法为发送用临时密钥加密的会话密钥(AES)
//TODO 可以根据UUID将AES密钥存放入数据库
//TODO 按照/logout或超时来删除密钥
//TODO 注意Token使用了Base64编码
//TODO 临时存储可用Redis


//TODO 需要对客户端生成唯一标识来代替UUID，防止同机器的短时间大量请求，也许可用fingerprintjs
//TODO 引入Redis
//TODO 没有注销token，因此每次登陆，生成token放到数据库里边，调用接口的时候，先查有没有这个token，注销时把token删除