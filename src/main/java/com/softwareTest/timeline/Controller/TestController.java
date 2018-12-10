package com.softwareTest.timeline.Controller;


import com.softwareTest.timeline.Entity.Content;
import com.softwareTest.timeline.Utility.RedisUtility;
import com.softwareTest.timeline.Mapper.ContentMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/testPath")
public class TestController
{
	@Autowired
	ContentMapper contentMapper;

	@Autowired
	RedisUtility redisUtility;

	@ApiOperation(value="获得信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="用户ID", required=true, dataType="Int"),
			@ApiImplicitParam(name="name", value="用户名", required=true, dataType="String")
	})
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public Map<String,Object> get()
	{
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> toSelect=new HashMap<>();
		toSelect.put("content_id",1);
		Content content=contentMapper.selectByParams(toSelect).get(0);
		redisUtility.setToHash("content","1",content.getContent());
		map.put("user_id",content.getUserId().toString());
		map.put("content_id",content.getContentId().toString());
		map.put("publish_time",content.getPublishTime());
		map.put("content",content.getContent());
		System.out.println(redisUtility.getFromHashByKey("content","1"));
		redisUtility.removeFromHash("content","1");
		return map;
	}

}
