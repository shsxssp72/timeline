package com.softwareTest.timeline.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwareTest.timeline.Bean.QueryBean;
import com.softwareTest.timeline.Entity.Content;
import com.softwareTest.timeline.Service.ContentService;
import org.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentApiControllerTest
{
	private static ObjectMapper mapper=new ObjectMapper();

	@Autowired
	ContentApiController controller;//=new ContentApiController();

	@Autowired
	ContentService contentService;

	@Mock
	BindingResult bindingResult;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test_getContentBetweenTime_success() throws Exception
	{
		Map<String,Object> map=new HashMap<>();
		map.put("start","2018-12-03T05:23:26.000Z");
		map.put("end","2019-12-03T05:23:26.000Z");
		QueryBean bean=mapper.readValue(mapper.writeValueAsString(map),QueryBean.class);
		Map<String,Object> result=controller.getContentBetweenTime(bean);

		JSONObject jsonObject=new JSONObject(mapper.writeValueAsString(result));
		String entityListString=jsonObject.getJSONArray("entity_list").toString();

		List<Content> contentList=mapper.readValue(entityListString,new TypeReference<List<Content>>()
		{
		});
		List<Content> realList=contentService
				.retrieveContentBetweenTime(new Timestamp(bean.getStart().getTime()),new Timestamp(bean.getEnd()
						.getTime()));


		realList.sort(Comparator.naturalOrder());
		assertEquals(contentList.size(),realList.size());
		for(int i=0;i<contentList.size();i++)
		{
			assertEquals(realList.get(i).getContentId(),contentList.get(i).getContentId());
			assertEquals(realList.get(i).getContent(),contentList.get(i).getContent());
			assertEquals(realList.get(i).getPublishTime(),contentList.get(i).getPublishTime());
			assertEquals(realList.get(i).getUserId(),contentList.get(i).getUserId());
		}
	}

	@Test
	public void test_getContentBetweenTime_failed() throws Exception
	{
		Map<String,Object> map=new HashMap<>();
		QueryBean bean=mapper.readValue(mapper.writeValueAsString(map),QueryBean.class);
		Map<String,Object> result=controller.getContentBetweenTime(bean);

		JSONObject jsonObject=new JSONObject(mapper.writeValueAsString(result));
		String resultString=jsonObject.getString("result").toString();
		assertEquals(resultString,"failure");
	}


	@Test
	public void test_createContent_AND_deleteContentById_success() throws Exception
	{
		Content content=new Content();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		content.setPublishTime(format.parse("2018-01-01 23:48:12"));
		content.setContent("{\"data\":\"Test\"}");
		content.setUserId(1);

		when(bindingResult.hasErrors()).thenReturn(true);
		when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(new ObjectError("Test","TestMessage")));

		Map<String,Object> result=controller.createContent(content,bindingResult);
		int content_id=(int)result.get("content_id");
		List<Content> retrievedContentList=contentService.retrieveContentByContentId(content_id);
		assertFalse(retrievedContentList.isEmpty());
		Content retrievedContent=retrievedContentList.get(0);
		assertEquals(retrievedContent.getContent(),content.getContent());
		result=controller.deleteContentById(content_id+"");
		assertEquals(result.get("result"),"success");
		assertEquals(contentService.retrieveContentByContentId(content_id).size(),0);
	}

	@Test
	public void test_createContent_contentExceed() throws Exception
	{
		Content content=new Content();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{\"data\":\"");
		for(int i=0;i<1001;i++)
		{
			stringBuilder.append("q");
		}
		stringBuilder.append("\"}");

		content.setPublishTime(format.parse("2018-01-01 23:48:12"));
		content.setContent(stringBuilder.toString());
		content.setUserId(1);


		when(bindingResult.hasErrors()).thenReturn(true);
		when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(new ObjectError("Test","TestMessage")));

		Map<String,Object> result=controller.createContent(content,bindingResult);

		assertEquals(result.get("result"),"failure");
	}


	@Test
	public void test_getDetailedContentById_success() throws Exception
	{
		String contentId="1";
		Map<String,Object> result=controller.getDetailedContentById(contentId);

		Content content=(Content)result.get("entity");
		List<Content> realList=contentService
				.retrieveContentByContentId(Integer.parseInt(contentId));

		assertEquals(realList.size(),1);

		assertEquals(realList.get(0).getContentId(),content.getContentId());
		assertEquals(realList.get(0).getContent(),content.getContent());
		assertEquals(realList.get(0).getPublishTime(),content.getPublishTime());
		assertEquals(realList.get(0).getUserId(),content.getUserId());
	}

	@Test
	public void test_getDetailedContentById_failed() throws Exception
	{
		String contentId="21";
		Map<String,Object> result=controller.getDetailedContentById(contentId);

		String resultStatus=(String)result.get("result");
		assertEquals(resultStatus,"failure");
	}

	@Test
	public void test_getContentByIdRange_success() throws Exception
	{
		Map<String,Object> map=new HashMap<>();
		map.put("contentStartId","4");
		map.put("numberToRetrieve","3");
		QueryBean bean=mapper.readValue(mapper.writeValueAsString(map),QueryBean.class);
		Map<String,Object> result=controller.getContentByIdRange(bean);

		JSONObject jsonObject=new JSONObject(mapper.writeValueAsString(result));
		String entityListString=jsonObject.getJSONArray("entity_list").toString();

		List<Content> contentList=mapper.readValue(entityListString,new TypeReference<List<Content>>()
		{
		});

		int endId=bean.getContentStartId()-1;
		int startId=bean.getContentStartId()
				-bean.getNumberToRetrieve() >= 0?
				bean.getContentStartId()-bean.getNumberToRetrieve():0;

		List<Content> realList=contentService
				.retrieveContentByIdRange(startId,endId);


		realList.sort(Comparator.naturalOrder());
		assertEquals(contentList.size(),realList.size());
		for(int i=0;i<contentList.size();i++)
		{
			assertEquals(realList.get(i).getContentId(),contentList.get(i).getContentId());
			assertEquals(realList.get(i).getContent(),contentList.get(i).getContent());
			assertEquals(realList.get(i).getPublishTime(),contentList.get(i).getPublishTime());
			assertEquals(realList.get(i).getUserId(),contentList.get(i).getUserId());
		}
	}

	@Test
	public void test_getContentByIdRange_failed() throws Exception
	{
		Map<String,Object> map=new HashMap<>();
		QueryBean bean=mapper.readValue(mapper.writeValueAsString(map),QueryBean.class);
		Map<String,Object> result=controller.getContentByIdRange(bean);

		JSONObject jsonObject=new JSONObject(mapper.writeValueAsString(result));
		String resultString=jsonObject.getString("result").toString();
		assertEquals(resultString,"failure");
	}



}
