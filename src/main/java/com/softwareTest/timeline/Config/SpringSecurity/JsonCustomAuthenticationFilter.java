package com.softwareTest.timeline.Config.SpringSecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwareTest.timeline.Bean.AuthenticationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static com.softwareTest.timeline.Config.Constants.IGNORE_PATH;

//Ref:https://www.jianshu.com/p/693914564406
public class JsonCustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
	private final static Logger logger=LoggerFactory.getLogger(JsonCustomAuthenticationFilter.class);


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response)
			throws AuthenticationException
	{
		if(request.getContentType()!=null
				&&request.getMethod().equals("POST")
				&&
				(request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
						||request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)))
		{
			logger.info("Entering JsonCustomAuthenticationFilter for JSON Login.");

			ObjectMapper mapper=new ObjectMapper();
			UsernamePasswordAuthenticationToken authRequest=null;
			try(InputStream is=request.getInputStream())
			{
				AuthenticationBean authenticationBean=mapper.readValue(is,AuthenticationBean.class);
				authRequest=new UsernamePasswordAuthenticationToken(
						authenticationBean.getUsername(),authenticationBean.getPassword());
			}
			catch(IOException e)
			{
				e.printStackTrace();
				authRequest=new UsernamePasswordAuthenticationToken(
						"","");
			}

			setDetails(request,authRequest);
			return this.getAuthenticationManager().authenticate(authRequest);

		}
		else
		{
			return super.attemptAuthentication(request,response);
		}

	}
}
