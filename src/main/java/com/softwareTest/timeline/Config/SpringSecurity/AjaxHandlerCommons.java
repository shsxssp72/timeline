package com.softwareTest.timeline.Config.SpringSecurity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwareTest.timeline.Bean.ResponseBody;

public class AjaxHandlerCommons
{
	static String generateJSONResponse(String status,String message) throws JsonProcessingException
	{
		ResponseBody responseBody = new ResponseBody();

		responseBody.setStatus(status);
		responseBody.setMsg(message);

		ObjectMapper mapper=new ObjectMapper();
		return mapper.writeValueAsString(responseBody);
	}
}
