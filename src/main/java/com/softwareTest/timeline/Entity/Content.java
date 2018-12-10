package com.softwareTest.timeline.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.softwareTest.timeline.Utility.JsonVisibilityLevel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class Content
{
	private Integer contentId;

	@NotNull(message="UserId can not be blank.")
	private Integer userId;

	private Date publishTime;

	@NotBlank(message="Content can not be blank.")
	private String content;

	public Content(Integer contentId,Integer userId,Date publishTime)
	{
		this.contentId=contentId;
		this.userId=userId;
		this.publishTime=publishTime;
	}

	public Content(Integer contentId,Integer userId,Date publishTime,String content)
	{
		this.contentId=contentId;
		this.userId=userId;
		this.publishTime=publishTime;
		this.content=content;
	}

	public Content()
	{
		super();
	}

	@JsonView(JsonVisibilityLevel.AbstractView.class)
	public Integer getContentId()
	{
		return contentId;
	}

	public void setContentId(Integer contentId)
	{
		this.contentId=contentId;
	}

	@JsonView(JsonVisibilityLevel.BasicView.class)
	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId=userId;
	}

	@JsonView(JsonVisibilityLevel.BasicView.class)
	public Date getPublishTime()
	{
		return publishTime;
	}

	public void setPublishTime(Date publishTime)
	{
		this.publishTime=publishTime;
	}

	@JsonView(JsonVisibilityLevel.NormalView.class)
	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content=content==null?null:content.trim();
	}
}