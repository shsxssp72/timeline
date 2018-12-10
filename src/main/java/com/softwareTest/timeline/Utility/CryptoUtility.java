package com.softwareTest.timeline.Utility;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.Sha2Crypt;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

public class CryptoUtility
{
	public static String Base64Encoder(String input)
	{
		byte[] encodeBytes=Base64.encodeBase64(input.getBytes());
		return new String(encodeBytes);
	}

	public static String Base64Decoder(String input)
	{
		byte[] decodeBytes=Base64.decodeBase64(input.getBytes());
		return new String((decodeBytes));
	}

	public static String Sha2Encoder(String input,String salt)
	{
		return Sha2Crypt.sha512Crypt((input+salt).getBytes());
	}

	public static String generateAESKey()
	{
		String seed=new Date().toString();
		try
		{
			SecureRandom secureRandom=SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(seed.getBytes());
			KeyGenerator generator=KeyGenerator.getInstance("AES");
			generator.init(256,secureRandom);
			return encodeBase64String(generator.generateKey().getEncoded());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}


	public static String AESEncrypt(String input,String key)
	{
		try
		{
			byte[] inBytes=decodeBase64(input);
			byte[] keyBytes=decodeBase64(key);
			SecretKeySpec skeySpec=new SecretKeySpec(keyBytes,"AES");
			Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
			cipher.init(Cipher.ENCRYPT_MODE,skeySpec);
			byte[] encrypted=cipher.doFinal(inBytes);
			return encodeBase64String(encrypted);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String AESDecrypt(String input,String key)
	{
		try
		{
			byte[] inBytes=decodeBase64(input);
			byte[] keyBytes=decodeBase64(key);
			SecretKeySpec skeySpec=new SecretKeySpec(keyBytes,"AES");
			Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
			cipher.init(Cipher.DECRYPT_MODE,skeySpec);
			byte[] encrypted=cipher.doFinal(inBytes);
			return encodeBase64String(encrypted);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}


	public static String getRandomString(int length)
	{
		String KeyString="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		return getRandomFromStringElement(length,KeyString);
	}

	public static String getRandomNumber(int length)
	{
		String KeyString="0123456789";
		return getRandomFromStringElement(length,KeyString);
	}

	private static String getRandomFromStringElement(int length,String keyString)
	{
		StringBuilder stringBuffer=new StringBuilder();
		int len=keyString.length();
		SecureRandom secureRandom=new SecureRandom();
		for(int i=0;i<length;i++)
		{
			stringBuffer.append(keyString.charAt(secureRandom.nextInt(len-1)));
		}
		return stringBuffer.toString();
	}


	public static Pair<RSAPrivateKey,RSAPublicKey> generateRSAKey()
	{
		try
		{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator keyPairGenerator=KeyPairGenerator
					.getInstance("RSA","BC");
			keyPairGenerator.initialize(2048);
			KeyPair keyPair=keyPairGenerator.generateKeyPair();
			RSAPublicKey publicKey=(RSAPublicKey)keyPair.getPublic();
			RSAPrivateKey privateKey=(RSAPrivateKey)keyPair.getPrivate();

			return new Pair<>(privateKey,publicKey);
		}
		catch(NoSuchAlgorithmException|NoSuchProviderException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String RSAEncrypt(PublicKey publicKey,String input)
	{
		try
		{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
//					("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE,publicKey);
			return encodeBase64String(cipher.doFinal(input.getBytes()));
//			return new String(cipher.doFinal(input.getBytes()));
		}
		catch(NoSuchAlgorithmException|NoSuchPaddingException|InvalidKeyException|IllegalBlockSizeException|BadPaddingException|NoSuchProviderException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String RSADecrypt(PrivateKey privateKey,String input)
	{
		try
		{
			byte[] encryptedText=Base64.decodeBase64(input);
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
//					("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE,privateKey);
			return new String(cipher.doFinal(encryptedText));
		}
		catch(NoSuchAlgorithmException|NoSuchPaddingException|InvalidKeyException|IllegalBlockSizeException|BadPaddingException|NoSuchProviderException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String publicKeyToString(PublicKey publicKey)
	{
		return org.apache.commons.codec.binary.Base64.encodeBase64String(publicKey.getEncoded());
	}

	public static String privateKeyToString(PrivateKey privateKey)
	{
		return org.apache.commons.codec.binary.Base64.encodeBase64String(privateKey.getEncoded());
	}

	public static PublicKey getPublicKeyFromString(String input)
	{
		byte[] keyBytes=Base64.decodeBase64(input);
		X509EncodedKeySpec keySpec=new X509EncodedKeySpec(keyBytes);
		try
		{
			KeyFactory keyFactory=KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(keySpec);
		}
		catch(NoSuchAlgorithmException|InvalidKeySpecException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static PrivateKey getPrivateKeyFromString(String input)
	{
		try
		{
			byte[] keyBytes=Base64.decodeBase64(input);
			PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory=KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(keySpec);
		}
		catch(NoSuchAlgorithmException|InvalidKeySpecException e)
		{
			e.printStackTrace();
		}
		return null;
	}
//	Deprecated
//	public static SecretKeySpec getAESKeyFromString(String input)
//	{
//		return new SecretKeySpec(input.getBytes(),"AES");
//	}

	public static String getPKCS1PublicKey(RSAPublicKey publicKey)
	{
		try
		{
			SubjectPublicKeyInfo pkInfo=SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
			ASN1Primitive primitive=pkInfo.parsePublicKey();
			byte[] publ=primitive.getEncoded();
//			return Base64.encodeBase64String(publ);
			PemObject pemObject=new PemObject("RSA PUBLIC KEY",publ);
			StringWriter stringWriter=new StringWriter();
			PemWriter pemWriter=new PemWriter(stringWriter);
			pemWriter.writeObject(pemObject);
			pemWriter.close();
			return stringWriter.toString();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String getHashedPassword(String password,String username,String salt,int iterateTime)
	{
		String alg="SHA-512";
		Object salt_real=username+salt;
		Object result=new SimpleHash(alg,password,salt_real,iterateTime);
		return result.toString();
	}

	/**
	 * 将普通字符串用16进制描述
	 * 如"WAZX-B55SY6-S6DT5" 描述为："57415a582d4235355359362d5336445435"
	 */
	public static String strToHex(String str)
	{
		byte[] bytes=str.getBytes();
		return bytesToHex(bytes);
	}

	/**
	 * 将16进制描述的字符串还原为普通字符串
	 * 如"57415a582d4235355359362d5336445435" 还原为："WAZX-B55SY6-S6DT5"
	 */
	public static String hexToStr(String hex)
	{
		byte[] bytes=hexToBytes(hex);
		return new String(bytes);
	}


	/**
	 * 16进制转byte[]
	 */
	public static byte[] hexToBytes(String hex)
	{
		int length=hex.length()/2;
		byte[] bytes=new byte[length];
		for(int i=0;i<length;i++)
		{
			String tempStr=hex.substring(2*i,2*i+2);//byte:8bit=4bit+4bit=十六进制位+十六进制位
			bytes[i]=(byte)Integer.parseInt(tempStr,16);
		}
		return bytes;
	}

	/**
	 * byte[]转16进制
	 */
	public static String bytesToHex(byte[] bytes)
	{
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<bytes.length;i++)
		{
			int tempI=bytes[i]&0xFF;//byte:8bit,int:32bit;高位相与.
			String str=Integer.toHexString(tempI);
			if(str.length()<2)
			{
				sb.append(0).append(str);//长度不足两位，补齐：如16进制的d,用0d表示。
			}
			else
			{
				sb.append(str);
			}
		}
		return sb.toString();
	}

}
