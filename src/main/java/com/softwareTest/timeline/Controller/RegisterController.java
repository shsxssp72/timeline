package com.softwareTest.timeline.Controller;

import com.softwareTest.timeline.Bean.RegisterBean;
import com.softwareTest.timeline.Entity.UserInfo;
import com.softwareTest.timeline.Service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.softwareTest.timeline.Config.Constants.*;

@RestController
@RequestMapping("/register")
public class RegisterController
{
	@Autowired
	UserInfoService userInfoService;

	private static Logger logger=LoggerFactory.getLogger(RegisterController.class);


	@RequestMapping(value="", method=RequestMethod.POST, produces="application/json")
	public Map<String,Object> registerNewAccount(@Valid @RequestBody RegisterBean registerBean,
												 BindingResult errors)
	{
		if(errors.hasErrors())
		{
			errors.getAllErrors().stream()
					.forEach(error->logger.error(error.getDefaultMessage()));
		}
		Map<String,Object> resultMap=new HashMap<>();

		if(registerBean.getUsername().length()>MAX_USERNAME_LENGTH
				||registerBean.getDisplayName().length()>MAX_DISPLAYNAME_LENGTH
				||registerBean.getPassword().length()>MAX_PASSWORD_LENGTH)
		{
			resultMap.put("result","failure");
			return resultMap;
		}

		UserInfo info=new UserInfo();
		info.setUsername(registerBean.getUsername());
		info.setDisplayName(registerBean.getDisplayName()==null?registerBean.getUsername():registerBean
				.getDisplayName());
		info.setUserPassword(registerBean.getPassword());
		boolean result=userInfoService.createNewUserInfo(info);

		resultMap.put("result",result?"success":"failure");
		return resultMap;
	}
}
