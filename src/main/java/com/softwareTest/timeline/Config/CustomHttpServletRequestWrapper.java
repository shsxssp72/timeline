package com.softwareTest.timeline.Config;


import com.softwareTest.timeline.Utility.RequestResponseUtility;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper
{
	private byte[] data;

	public CustomHttpServletRequestWrapper(HttpServletRequest request)
	{
		super(request);

		data=RequestResponseUtility.getRequestBodyString(request).getBytes(Charset.forName("UTF-8"));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		final ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(data);
		return new ServletInputStream()
		{
			@Override
			public boolean isFinished()
			{
				return false;
			}

			@Override
			public boolean isReady()
			{
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener)
			{

			}

			@Override
			public int read() throws IOException
			{
				return byteArrayInputStream.read();
			}
		};
	}

	public void appendCustomMsgToRequestBody(String customMsg)
	{
		String oldBodyString=RequestResponseUtility.getRequestBodyString(this);//oldBodyString一定是通过当前对象的输入流解析得来的，否则接收时会报EOFException；
		String appendMsg=RequestResponseUtility.appendCustomMsgToReqBody(customMsg);
		String requestBodyAfterAppend=appendMsg+","+oldBodyString;
		this.data=RequestResponseUtility.appendCustomMsgToReqBody(requestBodyAfterAppend).getBytes(Charset.forName("UTF-8"));
	}

	public void rewriteRequestBody(String newRequestBody)
	{
		String newMsg=RequestResponseUtility.appendCustomMsgToReqBody(newRequestBody);
		this.data=RequestResponseUtility.appendCustomMsgToReqBody(newMsg).getBytes(Charset.forName("UTF-8"));
	}

	public byte[] getData()
	{
		return data;
	}
}
