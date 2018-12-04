package com.softwareTest.timeline.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwareTest.timeline.Bean.AjaxResponseBody;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint
{

	@Override
	public void commence(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
						 AuthenticationException e) throws IOException, ServletException
	{
		String jsonString=AjaxHandlerCommons.generateJSONResponse(Constants.LoginEntryPointStatus,Constants.LoginEntryPointMessage);
		httpServletResponse.getWriter().write(jsonString);
	}
}
