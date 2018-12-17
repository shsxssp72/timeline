package com.softwareTest.timeline.Config.SpringSecurity;

import com.softwareTest.timeline.Entity.UserInfo;
import com.softwareTest.timeline.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CustomUserDetailsService implements UserDetailsService
{
	@Autowired
	UserInfoService userInfoService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		CustomUserDetails userDetails=new CustomUserDetails();
		userDetails.setUsername(username);

		List<UserInfo> matchUsers=userInfoService.retrieveUserInfoByUsername(username);
		String password;
		if(matchUsers.isEmpty())
			password="";
		else
			password=matchUsers.get(0).getUserPassword();//此处应该只有一个元素
		userDetails.setPassword(password);

		Set<GrantedAuthority> authoritiesSet=new HashSet<>();
		//TODO 此处需要从数据库中获得用户权限
		GrantedAuthority authority=new SimpleGrantedAuthority("ROLE_USER");

		authoritiesSet.add(authority);
		userDetails.setAuthorities(authoritiesSet);
		return userDetails;
	}
}
