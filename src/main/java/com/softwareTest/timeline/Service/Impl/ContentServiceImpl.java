package com.softwareTest.timeline.Service.Impl;

import com.softwareTest.timeline.Entity.Content;
import com.softwareTest.timeline.Mapper.ContentMapper;
import com.softwareTest.timeline.Service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentServiceImpl implements ContentService
{
	@Autowired
	ContentMapper contentMapper;

	@Override
	public List<Content> retrieveContentByContentId(int contentId)
	{
		Map<String,Object> map=new HashMap<>();
		map.put("content_id",contentId);
		return contentMapper.selectByParams(map);
	}

	@Override
	public List<Content> retrieveContentByUserId(int userId)
	{
		Map<String,Object> map=new HashMap<>();
		map.put("user_id",userId);
		return contentMapper.selectByParams(map);
	}

	@Override
	public List<Content> retrieveContentByTime(@NotNull Timestamp timestamp)
	{
		Map<String,Object> map=new HashMap<>();
		map.put("publish_time",timestamp);
		return contentMapper.selectByParams(map);
	}

	@Override
	public List<Content> retrieveContentAfterTime(@NotNull Timestamp timestamp)
	{
		return contentMapper.selectByTimeRange(timestamp,new Timestamp(new java.util.Date().getTime()));
	}

	@Override
	public List<Content> retrieveContentBetweenTime(@NotNull Timestamp start,@NotNull Timestamp end)
	{
		return contentMapper.selectByTimeRange(start,end);
	}

	@Override
	public List<Content> retrieveContentByIdRange(int startId,int endId)
	{
		return contentMapper.selectByIdRange(startId,endId);
	}

	@Override
	public List<Content> retrieveAll()
	{
		Map<String,Object> map=new HashMap<>();
		return contentMapper.selectByParams(map);
	}

	@Override
	public void createNewContent(@NotNull Content content)
	{
		int availableContentId=contentMapper.getAvailableContentId();
		content.setContentId(availableContentId+1);
//		content.setPublishTime(new Date(new java.util.Date().getTime()));
		contentMapper.insert(content);
	}

	@Override
	public void updateContentById(int contentId,@NotNull Content newContent)
	{
		newContent.setContentId(contentId);
		contentMapper.updateByPrimaryKey(newContent);
	}


	@Override
	public void deleteContentById(int contentId)
	{
		contentMapper.deleteByPrimaryKey(contentId);
	}
}
