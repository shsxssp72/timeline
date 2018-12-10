package com.softwareTest.timeline.Controller;

import com.softwareTest.timeline.Bean.RegisterSessionKeyRequest;
import com.softwareTest.timeline.Utility.CryptoUtility;
import com.softwareTest.timeline.Utility.Pair;
import com.softwareTest.timeline.Utility.RedisUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import static com.softwareTest.timeline.Config.Constants.*;

@RestController
public class LoginController
{
	private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	RedisUtility redisUtility;

	@RequestMapping(value="/regKey/{uniqueIdentifier}", method=RequestMethod.GET, produces="application/json")
	public Map<String,String> getKeyInit(@NotNull @PathVariable("uniqueIdentifier") String uniqueIdentifier)
	{
		logger.info("ID: "+uniqueIdentifier+" requesting key");
		Pair<RSAPrivateKey,RSAPublicKey> keyPair=CryptoUtility.generateRSAKey();
		//TODO 此处应将privateKey与uniqueIdentifier以键值对方式缓存
		//TODO Redis
		String privateKeyString=CryptoUtility.privateKeyToString(keyPair.getKey());
		redisUtility.setToHash(TEMP_RSA_KEY_HASH_KEY_NAME,uniqueIdentifier,privateKeyString,TEMP_RSA_KEY_EXPIRE_TIME);

		Map<String,String> resultMap=new HashMap<>();
		resultMap.put("publicKey",org.apache.commons.codec.binary.Base64
				.encodeBase64String(keyPair.getValue().getEncoded()));
		return resultMap;
	}

	@RequestMapping(value="/regKey",method=RequestMethod.POST,produces="application/json")
	public Map<String,Object> registerSessionKey(@Valid @RequestBody RegisterSessionKeyRequest registerSessionKeyRequest)
	{
		//TODO 此处应将之前缓存的privateKey取出用于解密
		//TODO Redis
		logger.info("Entering registerSessionKey method.");
		String uniqueIdentifier=registerSessionKeyRequest.getUniqueIdentifier();
		String encryptedKey=registerSessionKeyRequest.getPayload();
		String bufferedPrivateKey=(String)redisUtility.getFromHashByKey(TEMP_RSA_KEY_HASH_KEY_NAME,uniqueIdentifier);
		redisUtility.removeFromHash(TEMP_RSA_KEY_HASH_KEY_NAME,uniqueIdentifier);//删除已使用的密钥
		PrivateKey privateKey=CryptoUtility.getPrivateKeyFromString(bufferedPrivateKey);
		String AESKey_String=CryptoUtility.RSADecrypt(privateKey,encryptedKey);
		//TODO 此处应将该AES密钥缓存
		redisUtility.setToHash(AES_SESSION_KEY_HASH_KEY_NAME,uniqueIdentifier,AESKey_String,AES_SESSION_KEY_EXPIRE_TIME);

		//TODO 确定缓存的有效期
		//TODO 设置全局的解密判定，当无法解密时执行清除Token和AES密钥缓存的倒计时，超过则要求重新登录
		Map<String,Object> resultMap=new HashMap<>();
		resultMap.put("result","success");
		return resultMap;
	}
}
