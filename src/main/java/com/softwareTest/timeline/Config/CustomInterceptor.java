package com.softwareTest.timeline.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomInterceptor implements HandlerInterceptor
{
	private final static Logger logger = LoggerFactory.getLogger(CustomInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception
	{
		logger.info("PreHandle: "+request.toString());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,
						   ModelAndView modelAndView) throws Exception
	{
		logger.info("postHandle: "+request.toString());

	}

	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex)
			throws Exception
	{
		logger.info("afterCompletion: "+request.toString());

	}
}
