package com.softwareTest.timeline.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport
{
	@Autowired
	CustomInterceptor customInterceptor;

	@Override
	protected void addInterceptors(InterceptorRegistry registry)
	{
//		registry.addInterceptor(customInterceptor).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
}
