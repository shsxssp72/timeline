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

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void test_getContentBetweenTime_returnsCorrectValue() throws Exception
	{
		Map<String,Object> map=new HashMap<>();
		map.put("start","2018-12-03T05:23:26.000Z");
		map.put("end","2068-12-03T05:23:26.000Z");
		QueryBean bean=mapper.readValue(mapper.writeValueAsString(map),QueryBean.class);
		Map<String,Object> result=controller.getContentBetweenTime(bean);

		JSONObject jsonObject=new JSONObject(mapper.writeValueAsString(result));
		String entityListString=jsonObject.getJSONArray("entity_list").toString();

		List<Content> contentList=mapper.readValue(entityListString,new TypeReference<List<Content>>(){});
		List<Content> realList=contentService
				.retrieveContentBetweenTime(new Timestamp(bean.getStart().getTime()),new Timestamp(bean.getEnd().getTime()));

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
	public void test_createContent_AND_deleteContentById_successfully() throws Exception
	{
		Content content=new Content();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		content.setPublishTime(format.parse("2018-01-01 23:48:12"));
		content.setContent("Test");
		content.setUserId(1);
		when(bindingResult.hasErrors()).thenReturn(false);
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

}
