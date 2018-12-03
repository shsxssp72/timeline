package com.softwareTest.timeline.Utility;


import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MybatisGeneratorUtility
{
	public static void main(String[] args)
	{
		try
		{
			generate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	private static void generate()throws Exception
	{
		List<String> warnings = new ArrayList<>();
		boolean overwrite = true;
		File configFile = new File("src/main/resources/generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);

		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
		for(String item:warnings)
			System.out.println(item);
	}
}
