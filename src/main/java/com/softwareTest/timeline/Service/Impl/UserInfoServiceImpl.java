package com.softwareTest.timeline.Service.Impl;

import com.softwareTest.timeline.Config.SpringSecurity.JsonCustomAuthenticationFilter;
import com.softwareTest.timeline.Entity.UserInfo;
import com.softwareTest.timeline.Mapper.UserInfoMapper;
import com.softwareTest.timeline.Service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService
{
	private final static Logger logger=LoggerFactory.getLogger(UserInfoServiceImpl.class);


	@Autowired
	UserInfoMapper userInfoMapper;

	@Override
	public List<UserInfo> retrieveUserInfoByUserId(int userId)
	{
		Map<String,Object> map=new HashMap<>();
		map.put("user_id",userId);
		return userInfoMapper.selectByParams(map);
	}

	@Override
	public List<UserInfo> retrieveUserInfoByUsername(@NotNull String username)
	{
		Map<String,Object> map=new HashMap<>();
		map.put("username",username);
		return userInfoMapper.selectByParams(map);
	}

	@Override
	public List<UserInfo> retrieveUserInfoByDisplayName(@NotNull String displayName)
	{
		Map<String,Object> map=new HashMap<>();
		map.put("display_name",displayName);
		return userInfoMapper.selectByParams(map);
	}

	@Override
	public List<UserInfo> retrieveAll()
	{
		Map<String,Object> map=new HashMap<>();
		return userInfoMapper.selectByParams(map);
	}

	@Override
	public boolean createNewUserInfo(@NotNull UserInfo userInfo)
	{
		int availableUserId=userInfoMapper.getAvailableUserId();
		userInfo.setUserId(availableUserId+1);

		if(checkInfoLegal(userInfo))
			return false;
		userInfoMapper.insert(userInfo);
		return true;
	}

	@Override
	public boolean updateUserInfoById(int userId,@NotNull UserInfo newUserInfo)
	{
		newUserInfo.setUserId(userId);

		if(checkInfoLegal(newUserInfo))
			return false;
		userInfoMapper.updateByPrimaryKey(newUserInfo);
		return true;
	}

	@Override
	public void deleteUserInfoById(int userId)
	{
		userInfoMapper.deleteByPrimaryKey(userId);
	}

	private boolean checkInfoLegal(@NotNull UserInfo userInfo)
	{
		Map<String,Object> map=new HashMap<>();
		if(userInfo.getUsername()!=null)
		{
			map.put("username",userInfo.getUsername());
			if(!userInfoMapper.selectByParams(map).isEmpty())
				return true;
		}
		if(userInfo.getDisplayName()!=null)
		{
			map.put("display_name",userInfo.getDisplayName());
			if(!userInfoMapper.selectByParams(map).isEmpty())
				return true;
		}
		return false;
	}
}
