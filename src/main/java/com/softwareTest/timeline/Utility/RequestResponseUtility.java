package com.softwareTest.timeline.Utility;

import javax.servlet.ServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

//Ref:https://blog.csdn.net/zj20142213/article/details/80012221
public class RequestResponseUtility
{
	public static String getRequestBodyString(ServletRequest request)
	{
		StringBuilder stringBuilder=new StringBuilder();
		InputStream inputStream=null;
		BufferedReader reader=null;
		try
		{
			inputStream=request.getInputStream();
			reader=new BufferedReader(new InputStreamReader(inputStream,Charset.forName("UTF-8")));
			String line;
			for(;(line=reader.readLine())!=null;)
			{
				stringBuilder.append(line);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(inputStream!=null)
			{
				try
				{
					inputStream.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return stringBuilder.toString();
	}

	public static String appendCustomMsgToReqBody(String newRequestBodyString)
	{
		StringBuilder stringBuilder=new StringBuilder();
		InputStream inputStream=null;
		BufferedReader reader=null;
		String newRequestBody=null;
		try
		{
			//通过字符串构造输入流;
			inputStream=String2InputStream(newRequestBodyString);
			reader=new BufferedReader(new InputStreamReader(inputStream,Charset.forName("UTF-8")));
			String line="";
			while((line=reader.readLine())!=null)
			{
				stringBuilder.append(line);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(inputStream!=null)
			{
				try
				{
					inputStream.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(reader!=null)
			{
				try
				{
					reader.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		newRequestBody=stringBuilder.toString();
		return newRequestBody;
	}

	public static InputStream String2InputStream(String string)
	{
		return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
	}

}
