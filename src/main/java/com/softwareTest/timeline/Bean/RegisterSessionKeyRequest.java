package com.softwareTest.timeline.Bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel
public class RegisterSessionKeyRequest implements Serializable
{
	@ApiModelProperty("负载")
	@NotBlank
	private String payload;

	@ApiModelProperty("用户唯一标识符")
	@NotBlank
	private String uniqueIdentifier;

	public String getPayload()
	{
		return payload;
	}

	public void setPayload(String payload)
	{
		this.payload=payload;
	}

	public String getUniqueIdentifier()
	{
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier)
	{
		this.uniqueIdentifier=uniqueIdentifier;
	}
}
