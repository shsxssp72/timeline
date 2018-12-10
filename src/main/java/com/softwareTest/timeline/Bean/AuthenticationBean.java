package com.softwareTest.timeline.Bean;

import java.io.Serializable;

public class AuthenticationBean implements Serializable
{
	private String username;
	private String password;
	//TODO 可能会加入更多参数如UUID

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username=username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password=password;
	}
}
