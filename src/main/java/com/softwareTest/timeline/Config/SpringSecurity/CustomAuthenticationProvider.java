package com.softwareTest.timeline.Config.SpringSecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider
{

	private final static Logger logger=LoggerFactory.getLogger(CustomAuthenticationProvider.class);

	@Autowired
	CustomUserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		logger.info("Entering CustomAuthenticationProvider.");
		String userName = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		//TODO 此处需要从数据库中获得salt和迭代次数
		//TODO 可在此处进行解密
//		String hashedPassword=CryptoUtility.getHashedPassword(password,userName,"",1);
		String hashedPassword=password;
		UserDetails userDetails=userDetailsService.loadUserByUsername(userName);

		if(!userDetails.getPassword().equals(hashedPassword))
		{
			throw new BadCredentialsException("Invalid username or password.");
		}
		return new UsernamePasswordAuthenticationToken(userName,password,userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> aClass)
	{
		return true;
	}
}
