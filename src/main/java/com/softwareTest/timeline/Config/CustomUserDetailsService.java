package com.softwareTest.timeline.Config;

import com.softwareTest.timeline.Utility.CryptoUtility;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomUserDetailsService implements UserDetailsService
{
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		CustomUserDetails userDetails=new CustomUserDetails();
		userDetails.setUsername(username);

		//TODO 此处需要从数据库中获得password
		String password="123";
		userDetails.setPassword(password);

		Set<GrantedAuthority> authoritiesSet=new HashSet<>();
		//TODO 此处需要从数据库中获得用户权限
		GrantedAuthority authority=new SimpleGrantedAuthority("ROLE_ADMIN");

		authoritiesSet.add(authority);
		userDetails.setAuthorities(authoritiesSet);
		return userDetails;
	}
}
