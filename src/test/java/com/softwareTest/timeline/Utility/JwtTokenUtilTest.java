package com.softwareTest.timeline.Utility;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JwtTokenUtilTest
{
	@Test
	public void check_if_symmetric()
	{
		String token=JwtTokenUtil.generateToken("username",1000,"");
		String result=JwtTokenUtil.parseToken(token,"");
		assertEquals("username",result);
	}


}
