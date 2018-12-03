package com.softwareTest.timeline.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.softwareTest.timeline.Entity.Content;
import com.softwareTest.timeline.Service.ContentService;
import com.softwareTest.timeline.Utility.JsonVisibilityLevel;
import com.softwareTest.timeline.Utility.QueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
public class ContentApiController
{
	@Autowired
	ContentService contentService;

	private static Logger logger=LoggerFactory.getLogger(ContentApiController.class);


	//TODO 使用自定义校验逻辑判断传入参数中userId是否存在于数据库中
	//TODO Ref:https://www.jianshu.com/p/320cee90391f
	//TODO 定义全局的非空参数校验
	//TODO Ref:https://www.jianshu.com/p/c13a530d97f7
	@ApiOperation(value="创建新的content条目")
	@ApiImplicitParam(name="content", required=true, dataType="Content")
	@RequestMapping(value="/create", method=RequestMethod.POST,
			produces="application/json")
	public Map<String,Object> createContent(@Valid @RequestBody Content content,
											BindingResult errors)
	{
		if(errors.hasErrors())
		{
			errors.getAllErrors().stream().forEach(error->logger.error(error.getDefaultMessage()));
		}
		contentService.createNewContent(content);
		Map<String,Object> resultMap=new HashMap<>();
		resultMap.put("result","success");
		resultMap.put("content_id",content.getContentId());
		resultMap.put("publish_time",content.getPublishTime());
		return resultMap;
	}

	@ApiOperation(value="根据content_id查询content")
	@ApiImplicitParam(name="content_id", required=true, dataType="int")
	@RequestMapping(value="/detail/{content_id:\\d+}",
			method=RequestMethod.GET, produces="application/json")
	@JsonView(JsonVisibilityLevel.NormalView.class)
	public Map<String,Object> getDetailedContentById(@NotNull @PathVariable("content_id") String content_id)
	{
		//此处返回列表只应有一个元素
		Map<String,Object> resultMap=new HashMap<>();
		List<Content> queryResult=contentService.retrieveContentByContentId(Integer.parseInt(content_id));
		if(queryResult.size()==0)
		{
			resultMap.put("result","failed");
			return resultMap;
		}
		Content content=queryResult.get(0);
		resultMap.put("result","success");
		resultMap.put("entity",content);
		return resultMap;
	}

	@ApiOperation(value="在两个时间点之间的范围内查询content", notes="仅需要提供start和end")
	@ApiImplicitParam(name="queryEntity", required=true, dataType="QueryEntity")
	@RequestMapping(value="/detail/by_period",
			method=RequestMethod.POST, produces="application/json")
	@JsonView(JsonVisibilityLevel.NormalView.class)
	public Map<String,Object> getContentBetweenTime(@NotNull @RequestBody QueryEntity queryEntity)
	{
		//TODO 需要进行结果可用性验证

		Date start=new Date(queryEntity.getStart().getTime());
		Date end=new Date(queryEntity.getEnd().getTime());
//		DateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Map<String,Object> resultMap=new HashMap<>();
//		try
//		{
//			Date start_date=new Date(format.parse(start).getTime());
//			Date end_date=new Date(format.parse(end).getTime());
		List<Content> contentList=contentService.retrieveContentBetweenTime(start,end);
		resultMap.put("result","success");
		resultMap.put("entity_list",contentList);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			resultMap.put("result","failed");
//		}
		return resultMap;
	}

	@ApiOperation(value="根据user_id查询该用户发表的content", notes="仅需要提供user_id")
	@ApiImplicitParam(name="queryEntity", required=true, dataType="QueryEntity")
	@RequestMapping(value="/detail/by_user",
			method=RequestMethod.POST, produces="application/json")
	@JsonView(JsonVisibilityLevel.NormalView.class)
	public Map<String,Object> getContentByUser(@NotNull @RequestBody QueryEntity queryEntity)
	{
		//TODO 需要进行结果可用性验证
		Integer user_id=queryEntity.getUser_id();
		List<Content> contentList=contentService.retrieveContentByUserId(user_id);
		Map<String,Object> resultMap=new HashMap<>();
		resultMap.put("result","success");
		resultMap.put("entity_list",contentList);
		return resultMap;
	}

	@ApiOperation(value="修改已存在的content条目")
	@ApiImplicitParam(name="content", required=true, dataType="Content")
	@RequestMapping(value="/update/",
			method=RequestMethod.PUT, produces="application/json")
	public Map<String,Object> updateContentById(@Valid @RequestBody Content content,
												BindingResult errors)
	{
		if(errors.hasErrors())
		{
			errors.getAllErrors().stream().forEach(error->logger.error(error.getDefaultMessage()));
		}

		//TODO 需要进行结果可用性验证
		//TODO 需要对content_id进行验证

		contentService.updateContentById(content.getContentId(),content);
		Map<String,Object> resultMap=new HashMap<>();
		resultMap.put("result","success");
		resultMap.put("entity",content);
		return resultMap;
	}

	@ApiOperation(value="根据content_id删除content")
	@ApiImplicitParam(name="content_id", required=true, dataType="int")
	@RequestMapping(value="/delete/{content_id:\\d+}",
			method=RequestMethod.DELETE, produces="application/json")
	public Map<String,Object> deleteContentById(@NotNull @PathVariable("content_id") String content_id)
	{
		//TODO 需要进行结果可用性验证
		//TODO 需要对content_id进行验证
		//此处返回列表只应有一个元素
		int content_id_int=Integer.parseInt(content_id);
		contentService.deleteContentById(content_id_int);
		Map<String,Object> resultMap=new HashMap<>();
		resultMap.put("result","success");
		return resultMap;
	}


	//TODO 完成权限管理

}
