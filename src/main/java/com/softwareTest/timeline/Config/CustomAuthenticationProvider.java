package com.softwareTest.timeline.Config;

import com.softwareTest.timeline.Utility.CryptoUtility;
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

	@Autowired
	CustomUserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		String userName = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		//TODO 此处需要从数据库中获得salt和迭代次数
		//TODO 可在此处进行解密
//		String hashedPassword=CryptoUtility.getHashedPassword(password,userName,"",1);
		String hashedPassword=password;
		System.out.println("authenticate: "+authentication+"/\n	"+authentication.getCredentials());
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
