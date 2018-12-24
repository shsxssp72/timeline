package com.softwareTest.timeline.Config;

import java.util.Set;

public class Constants
{
	//Token
	public final static String TOKEN_SALT="_secret";//Unused
	public final static int TOKEN_EXPIRATION_SECONDS=3600;
	public final static String KEYSTORE_FILENAME="server.keystore";
	public final static String KEYSTORE_PASSWORD="commonProject";
	public final static String KEYSTORE_ALIAS_NAME="serverkey";

	//AjaxHandlerMessage
	public final static String LoginSuccessStatus="200";
	public final static String LoginSuccessMessage="Login Success";
	public final static String AccessDeniedStatus="403";
	public final static String AccessDeniedMessage="Permission Denied";
	public final static String LoginEntryPointStatus=AccessDeniedStatus;
	public final static String LoginEntryPointMessage=AccessDeniedMessage;
	public final static String LoginFailureStatus="401";
	public final static String LoginFailureMessage="Login Failed";
	public final static String LogoutSuccessStatus="100";
	public final static String LogoutSuccessMessage="Logout Success";

	//Redis
	public final static String TEMP_RSA_KEY_HASH_KEY_NAME="tempRSAPrivateKeys";
	public final static int TEMP_RSA_KEY_EXPIRE_TIME=1800;
	public final static String AES_SESSION_KEY_HASH_KEY_NAME="AESSessionKey";
	public final static int AES_SESSION_KEY_EXPIRE_TIME=1800;

	//Security
	public final static Set<String> IGNORE_PATH=Set.of("/login","/regKey");

	//Content
	public final static int MAX_CONTENT_SIZE=1000;

	//User
	public final static int MAX_USERNAME_LENGTH=15;
	public final static int MAX_DISPLAYNAME_LENGTH=MAX_USERNAME_LENGTH;
	public final static int MAX_PASSWORD_LENGTH=255;


}
