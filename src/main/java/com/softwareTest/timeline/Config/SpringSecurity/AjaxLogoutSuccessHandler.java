package com.softwareTest.timeline.Config.SpringSecurity;

import com.softwareTest.timeline.Config.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler
{
	@Override
	public void onLogoutSuccess(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
								Authentication authentication) throws IOException, ServletException
	{
		String jsonString=AjaxHandlerCommons.generateJSONResponse(Constants.LogoutSuccessStatus,Constants.LogoutSuccessMessage);
		httpServletResponse.getWriter().write(jsonString);
	}
}
