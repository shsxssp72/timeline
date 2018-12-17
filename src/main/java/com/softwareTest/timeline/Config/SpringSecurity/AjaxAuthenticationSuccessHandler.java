package com.softwareTest.timeline.Config.SpringSecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwareTest.timeline.Bean.ResponseBody;
import com.softwareTest.timeline.Config.Constants;
import com.softwareTest.timeline.Entity.UserInfo;
import com.softwareTest.timeline.Service.Impl.UserInfoServiceImpl;
import com.softwareTest.timeline.Service.UserInfoService;
import com.softwareTest.timeline.Utility.CryptoUtility;
import com.softwareTest.timeline.Utility.JwtTokenUtil;
import com.softwareTest.timeline.Utility.SpringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	private final static Logger logger=LoggerFactory.getLogger(AjaxAuthenticationSuccessHandler.class);

	@Resource(name="userInfoServiceImpl")
	UserInfoService userInfoService;

//	public static AjaxAuthenticationSuccessHandler handler;
//
//	@PostConstruct
//	public void init()
//	{
////		handler=this;
////		handler.userInfoService=this.userInfoService;
//		userInfoService=SpringUtility.getApplicationContext().getBean(UserInfoServiceImpl.class);
//	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
										Authentication authentication) throws IOException, ServletException
	{

		ResponseBody responseBody=new ResponseBody();

		responseBody.setStatus(Constants.LoginSuccessStatus);
		responseBody.setMsg(Constants.LoginSuccessMessage);
//		CustomUserDetails userDetails=(CustomUserDetails)(authentication.getPrincipal());
		String jwtToken=JwtTokenUtil.generateToken((String)authentication
				.getPrincipal(),Constants.TOKEN_EXPIRATION_SECONDS,/*Unused*/Constants.TOKEN_SALT);
		String base64edToken=CryptoUtility.Base64Encoder(jwtToken);
		responseBody.setJwtToken(base64edToken);

		userInfoService=SpringUtility.getApplicationContext().getBean(UserInfoServiceImpl.class);
		List<UserInfo> resultList=userInfoService.retrieveUserInfoByUsername((String)authentication.getPrincipal());
		if(!resultList.isEmpty())
			responseBody.setResult(resultList.get(0).getUserId());
		ObjectMapper mapper=new ObjectMapper();
		String jsonString=mapper.writeValueAsString(responseBody);
		httpServletResponse.getWriter().write(jsonString);
	}
}
