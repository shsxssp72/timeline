package com.softwareTest.timeline.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.softwareTest.timeline.Utility.JsonVisibilityLevel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class Content implements Comparable<Content>
{
	private Integer contentId;

	@NotNull(message="UserId can not be blank.")
	private Integer userId;

	private Date publishTime;

	@NotBlank(message="Content can not be blank.")
	private String content;

	private String displayName;

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

	public Content(Integer contentId,
				   @NotNull(message="UserId can not be blank.") Integer userId,Date publishTime,
				   @NotBlank(message="Content can not be blank.") String content,String displayName)
	{
		this.contentId=contentId;
		this.userId=userId;
		this.publishTime=publishTime;
		this.content=content;
		this.displayName=displayName;
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

	@JsonView(JsonVisibilityLevel.NormalView.class)
	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName=displayName;
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

	@Override
	public int compareTo(Content another)
	{
		if(this.publishTime.getTime()<=another.getPublishTime().getTime())
			return 1;
		else
			return -1;
	}
}