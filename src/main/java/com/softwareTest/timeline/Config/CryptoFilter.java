package com.softwareTest.timeline.Config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwareTest.timeline.Bean.EncryptedRequest;
import com.softwareTest.timeline.Utility.CryptoUtility;
import com.softwareTest.timeline.Utility.RedisUtility;
import com.softwareTest.timeline.Utility.RequestResponseUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.softwareTest.timeline.Config.Constants.*;

@Component
public class CryptoFilter extends OncePerRequestFilter
{

	private final static Logger logger=LoggerFactory.getLogger(CryptoFilter.class);

	@Autowired
	RedisUtility redisUtility;

	@Override
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException
	{
		logger.info("Entering CryptoFilter");
		CustomHttpServletRequestWrapper requestWrapper=new CustomHttpServletRequestWrapper(request);
		if(!IGNORE_PATH.contains(request.getRequestURI()))
		{
			if(request.getContentType()!=null&&(request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
					||request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)))
			{
				System.out.println("Decrypt Filter");
				//TODO 解密request
				String requestBodyString=RequestResponseUtility.getRequestBodyString(requestWrapper);
				EncryptedRequest encryptedRequest=getEncryptedRequestFromString(requestBodyString);
				if(encryptedRequest==null)
				{
					logger.info("Illegal Request. Aborting.");
					filterChain.doFilter(requestWrapper,response);
					return;
				}
				String identifier=encryptedRequest.getUniqueIdentifier();
				String key=(String)redisUtility.getFromHashByKey(AES_SESSION_KEY_HASH_KEY_NAME,identifier);
				if(key==null)
				{
					logger.info("No matching key. Aborting.");
					filterChain.doFilter(requestWrapper,response);
					return;
				}
				String decryptedRequestBodyString=CryptoUtility.AESDecrypt(encryptedRequest.getPayload(),key);
				String realRequestBodyString=CryptoUtility.Base64Decoder(decryptedRequestBodyString);
				//TODO 处理解密失败的异常
				requestWrapper.rewriteRequestBody(decryptedRequestBodyString);

				System.out.println(decryptedRequestBodyString);
			}
		}

		filterChain.doFilter(requestWrapper,response);

		if(!IGNORE_PATH.contains(request.getRequestURI()))
		{
			if(response.getContentType()!=null&&(response.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
					||response.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)))
			{
				System.out.println("Encrypt Filter");
				//TODO 加密response


			}
		}
	}


	public static EncryptedRequest getEncryptedRequestFromString(String requestBodyString)
	{
		ObjectMapper mapper=new ObjectMapper();

		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,true);


		EncryptedRequest encryptedRequest=null;
		try
		{
			encryptedRequest=mapper.readValue(requestBodyString,EncryptedRequest.class);
		}
		catch(Exception e)
		{
			logger.info("Cannot convert to type EncryptedRequest");

		}

		return encryptedRequest;
	}
}
