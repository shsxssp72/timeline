package com.softwareTest.timeline.Utility;


import com.softwareTest.timeline.Config.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.UUID;


public class JwtTokenUtil
{
	private final static Logger logger=LoggerFactory.getLogger(JwtTokenUtil.class);

	private static InputStream inputStream=Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(Constants.KEYSTORE_FILENAME); // 寻找证书文件
	private static PrivateKey privateKey=null;
	private static PublicKey publicKey=null;
	private static SignatureAlgorithm  signatureAlgorithm=SignatureAlgorithm.RS512;

	static
	{ // 将证书文件里边的私钥公钥拿出来
		try
		{
			KeyStore keyStore=KeyStore.getInstance("JKS"); // java key store 固定常量
			keyStore.load(inputStream,Constants.KEYSTORE_PASSWORD.toCharArray());
			privateKey=(PrivateKey)keyStore.getKey(Constants.KEYSTORE_ALIAS_NAME,Constants.KEYSTORE_PASSWORD
					.toCharArray()); // jwt 为 命令生成证书文件时的别名
			publicKey=keyStore.getCertificate(Constants.KEYSTORE_ALIAS_NAME).getPublicKey();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String generateToken(String subject,int expirationSeconds,String salt)
	{
		//生成JWT的时间
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		JwtBuilder builder = Jwts.builder()
				//如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				.setClaims(null)
				//设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				.setId(UUID.randomUUID().toString())
				//iat: jwt的签发时间
				.setIssuedAt(now)
				//代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
				.setSubject(subject)
				//设置签名使用的签名算法和签名使用的秘钥
				.signWith(signatureAlgorithm, privateKey);
		if (expirationSeconds >= 0) {
			long expMillis = nowMillis + expirationSeconds*1000;
			Date exp = new Date(expMillis);
			//设置过期时间
			builder.setExpiration(exp);
		}
		return builder.compact();
	}

	public static String parseToken(String token,String salt)//TODO 处理过期等无效Token的异常
	{
		String subject=null;
		try
		{
			Claims claims = Jwts.parser()
					//设置签名的秘钥
					.setSigningKey(publicKey)
					//设置需要解析的jwt
					.parseClaimsJws(token).getBody();
			return claims.getSubject();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return subject;
	}
}
