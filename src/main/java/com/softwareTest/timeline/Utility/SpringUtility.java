package com.softwareTest.timeline.Utility;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtility implements ApplicationContextAware
{

	private static ApplicationContext applicationContext=null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		if(SpringUtility.applicationContext==null)
		{
			SpringUtility.applicationContext=applicationContext;
		}
	}

	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}
}
