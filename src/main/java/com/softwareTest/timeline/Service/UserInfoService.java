package com.softwareTest.timeline.Service;


import com.softwareTest.timeline.Entity.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

//@Service
public interface UserInfoService
{
	List<UserInfo> retrieveUserInfoByUserId(int userId);//即要求全局唯一
	List<UserInfo> retrieveUserInfoByUsername(String username);//即要求全局唯一
	List<UserInfo> retrieveUserInfoByDisplayName(String displayName);//即要求全局唯一
	List<UserInfo> retrieveAll();

	boolean createNewUserInfo(UserInfo userInfo);

	boolean updateUserInfoById(int userId,UserInfo newUserInfo);

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
