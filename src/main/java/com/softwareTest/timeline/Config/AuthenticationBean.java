package com.softwareTest.timeline.Config;

public class AuthenticationBean
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
