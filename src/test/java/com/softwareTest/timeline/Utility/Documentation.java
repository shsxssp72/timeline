package com.softwareTest.timeline.Utility;


import io.github.robwin.markup.builder.MarkupLanguage;
import io.github.robwin.swagger2markup.GroupBy;
import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springfox.documentation.staticdocs.SwaggerResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir="target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class Documentation
{
	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
	@Autowired
	private WebApplicationContext context;

	private String snippetDir="target/generated-snippets";
	private String outputDir="target/asciidoc";

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)).build();
	}



	//	@After
	@Test
	public void Test() throws Exception
	{
//		mockMvc.perform(getByKey("/testPath/getByKey").accept(MediaType.APPLICATION_JSON))
////				.andDo(SwaggerResultHandler.outputDirectory(outputDir).build())
//				.andDo(document("home"))
//				.andExpect(status().isOk())
//				.andReturn();

		mockMvc.perform(get("/api/content/detail/1")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(SwaggerResultHandler.outputDirectory(outputDir).build())
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
