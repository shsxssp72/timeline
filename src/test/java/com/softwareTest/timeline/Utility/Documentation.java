package com.softwareTest.timeline.Utility;


import io.github.robwin.markup.builder.MarkupLanguage;
import io.github.robwin.swagger2markup.GroupBy;
import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import springfox.documentation.staticdocs.SwaggerResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir="target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class Documentation
{
	private String snippetDir="target/generated-snippets";
	private String outputDir="target/asciidoc";

	@Autowired
	private MockMvc mockMvc;

//	@After
	@Test
	public void Test() throws Exception
	{
		mockMvc.perform(get("/testPath/get").accept(MediaType.APPLICATION_JSON))
//				.andDo(SwaggerResultHandler.outputDirectory(outputDir).build())
				.andDo(document("home"))
				.andExpect(status().isOk())
				.andReturn();

//		Swagger2MarkupConverter.from(outputDir+"/swagger.json")
//				.withPathsGroupedBy(GroupBy.TAGS)// 按tag排序
//				.withMarkupLanguage(MarkupLanguage.ASCIIDOC)// 格式
//				.withExamples(snippetDir)
//				.build()
//				.intoFolder(outputDir);// 输出
	}


}
