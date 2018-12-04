package com.softwareTest.timeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.util.Calendar;
import java.util.Date;


@SpringBootApplication
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