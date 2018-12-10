package com.softwareTest.timeline.Bean;

import java.io.Serializable;

public class EncryptedRequest implements Serializable
{
	private String uniqueIdentifier;
	private String payload;

	public String getUniqueIdentifier()
	{
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier)
	{
		this.uniqueIdentifier=uniqueIdentifier;
	}

	public String getPayload()
	{
		return payload;
	}

	public void setPayload(String payload)
	{
		this.payload=payload;
	}
}
