package com.softwareTest.timeline.Config.SpringSecurity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwareTest.timeline.Bean.AjaxResponseBody;

public class AjaxHandlerCommons
{
	static String generateJSONResponse(String status,String message) throws JsonProcessingException
	{
		AjaxResponseBody responseBody = new AjaxResponseBody();

		responseBody.setStatus(status);
		responseBody.setMsg(message);

		ObjectMapper mapper=new ObjectMapper();
		return mapper.writeValueAsString(responseBody);
	}
}
