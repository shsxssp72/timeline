package com.softwareTest.timeline.Controller;

import com.softwareTest.timeline.Bean.RegisterBean;
import com.softwareTest.timeline.Mapper.UserInfoMapper;
import com.softwareTest.timeline.Service.UserInfoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterControllerTest
{

	@Autowired
	RegisterController registerController;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	UserInfoMapper userInfoMapper;

	@Mock
	BindingResult bindingResult;

	private int currentUserNumber;

	@Before
	public void record_old_account_number()
	{
		currentUserNumber=userInfoMapper.getAvailableUserId();
	}

	@Test
	public void test_registerNewAccount_successfully()
	{
		RegisterBean bean=new RegisterBean();
		bean.setDisplayName("TestDisplayName");
		bean.setUsername("TestUsername");
		bean.setPassword("TestPassword");

		when(bindingResult.hasErrors()).thenReturn(false);
//		when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(new ObjectError("Test","TestMessage")));

		Map<String,Object> result=registerController.registerNewAccount(bean,bindingResult);
		assertEquals(result.get("result"),"success");
	}

	@Test
	public void test_registerNewAccount_usernameExceed()
	{
		RegisterBean bean=new RegisterBean();
		bean.setDisplayName("TestDisplayName1");
		bean.setUsername("TestUsername");
		bean.setPassword("TestPassword");

		when(bindingResult.hasErrors()).thenReturn(true);
		when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(new ObjectError("Test","TestMessage")));

		Map<String,Object> result=registerController.registerNewAccount(bean,bindingResult);
		assertEquals(result.get("result"),"failure");
	}

	@Test
	public void test_registerNewAccount_displayNameExceed()
	{
		RegisterBean bean=new RegisterBean();
		bean.setDisplayName("TestDisplayName");
		bean.setUsername("TestUsername1111111111111111");
		bean.setPassword("TestPassword");

		when(bindingResult.hasErrors()).thenReturn(true);
		when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(new ObjectError("Test","TestMessage")));

		Map<String,Object> result=registerController.registerNewAccount(bean,bindingResult);
		assertEquals(result.get("result"),"failure");
	}


	@After
	public void restoreAccount()
	{
		int userNumber=userInfoService.retrieveAll().size();
		for(int i=userNumber;i>currentUserNumber;i--)
		{
			userInfoMapper.deleteByPrimaryKey(i);
		}
	}
}