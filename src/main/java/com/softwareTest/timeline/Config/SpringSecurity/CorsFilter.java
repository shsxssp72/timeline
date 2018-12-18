package com.softwareTest.timeline.Config.SpringSecurity;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter
{
	@Override
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
			throws IOException, ServletException
	{
		HttpServletResponse httpServletResponse=(HttpServletResponse)response;
		HttpServletRequest httpServletRequest=(HttpServletRequest)request;
		httpServletResponse.setHeader("Access-Control-Allow-Origin","*");

		if(httpServletRequest.getMethod().equals("OPTIONS"))
		{
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
			httpServletResponse
					.setHeader("Access-Control-Allow-Methods","POST, GET, DELETE, OPTIONS, DELETE");//当判定为预检请求后，设定允许请求的方法
			httpServletResponse
					.setHeader("Access-Control-Allow-Headers","Content-Type, x-requested-with, Authorization"); //当判定为预检请求后，设定允许请求的头部类型
			httpServletResponse.addHeader("Access-Control-Max-Age","1");
		}
		chain.doFilter(request,response);
	}
}
