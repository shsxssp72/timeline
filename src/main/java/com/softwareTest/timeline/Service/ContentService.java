package com.softwareTest.timeline.Service;

import com.softwareTest.timeline.Entity.Content;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public interface ContentService
{
	List<Content> retrieveContentByContentId(int contentId);
	List<Content> retrieveContentByUserId(int userId);
	List<Content> retrieveContentByTime(Date date);
	List<Content> retrieveContentAfterTime(Date date);
	List<Content> retrieveContentBetweenTime(Date start,Date end);
	List<Content> retrieveContentByIdRange(int startId,int endId);
	List<Content> retrieveAll();

	void createNewContent(Content content);

	void updateContentById(int contentId,Content newContent);

	void deleteContentById(int contentId);
}

/*
 * 查询
 * 		根据用户
 * 		根据时间
 * 		全部
 * 创建
 * 		全部信息
 * 更新
 * 		根据ID（基于查询）
 * 删除
 * 		根据ID
 */
