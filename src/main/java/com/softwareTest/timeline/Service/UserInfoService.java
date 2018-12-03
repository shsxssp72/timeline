package com.softwareTest.timeline.Service;


import com.softwareTest.timeline.Entity.UserInfo;

import java.sql.Date;
import java.util.List;

public interface UserInfoService
{
	List<UserInfo> retrieveUserInfoByUserId(int userId);
	List<UserInfo> retrieveUserInfoByusername(String username);
	List<UserInfo> retrieveUserInfoByDisplayName(String displayName);
	List<UserInfo> retrieveAll();

	void createNewUserInfo(UserInfo userInfo);

	void updateUserInfoById(int userId,UserInfo newUserInfo);

	void deleteUserInfoById(int userId);
}


/*
 * 查询
 * 		根据用户id
 * 		根据用户名
 * 		根据用户显示名
 * 		全部
 * 创建
 * 		全部信息
 * 更新
 * 		根据ID（基于查询）
 * 删除
 * 		根据ID
 */
