package com.softwareTest.timeline.Service.Impl;

import com.softwareTest.timeline.Entity.UserInfo;
import com.softwareTest.timeline.Mapper.UserInfoMapper;
import com.softwareTest.timeline.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService
{
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
	public List<UserInfo> retrieveUserInfoByusername(@NotNull String username)
	{
		Map<String,Object> map=new HashMap<>();
		map.put("username",username);
		return userInfoMapper.selectByParams(map);
	}

	@Override
	public List<UserInfo> retrieveUserInfoByDisplayName(@NotNull String displayName)
	{
		Map<String,Object> map=new HashMap<>();
		map.put("displayName",displayName);
		return userInfoMapper.selectByParams(map);
	}

	@Override
	public List<UserInfo> retrieveAll()
	{
		Map<String,Object> map=new HashMap<>();
		return userInfoMapper.selectByParams(map);
	}

	@Override
	public void createNewUserInfo(@NotNull UserInfo userInfo)
	{
		int availableUserId=userInfoMapper.getAvailableUserId();
		userInfo.setUserId(availableUserId+1);
		userInfoMapper.insert(userInfo);
	}

	@Override
	public void updateUserInfoById(int userId,@NotNull UserInfo newUserInfo)
	{
		newUserInfo.setUserId(userId);
		userInfoMapper.updateByPrimaryKey(newUserInfo);
	}

	@Override
	public void deleteUserInfoById(int userId)
	{
		userInfoMapper.deleteByPrimaryKey(userId);
	}
}
