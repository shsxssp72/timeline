package com.softwareTest.timeline.Bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel
public class QueryBean implements Serializable
{
	@ApiModelProperty("用户ID")
	private Integer user_id;
	@ApiModelProperty("起始时间")
	private Date start;
	@ApiModelProperty("终止时间")
	private Date end;


	public Integer getUser_id()
	{
		return user_id;
	}

	public void setUser_id(Integer user_id)
	{
		this.user_id=user_id;
	}

	public Date getStart()
	{
		return start;
	}

	public void setStart(Date start)
	{
		this.start=start;
	}

	public Date getEnd()
	{
		return end;
	}

	public void setEnd(Date end)
	{
		this.end=end;
	}
}
