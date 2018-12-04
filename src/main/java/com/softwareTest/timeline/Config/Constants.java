package com.softwareTest.timeline.Config;

public class Constants
{
	//Token
	final static String TOKEN_SALT="_secret";//Unused
	final static int TOKEN_EXPIRATION_SECONDS=300;
	public final static String KEYSTORE_FILENAME="server.keystore";
	public final static String KEYSTORE_PASSWORD="commonProject";
	public final static String KEYSTORE_ALIAS_NAME="serverkey";

	//AjaxHandlerMessage
	final static String LoginSuccessStatus="200";
	final static String LoginSuccessMessage="Login Success";
	final static String AccessDeniedStatus="403";
	final static String AccessDeniedMessage="Permission Denied";
	final static String LoginEntryPointStatus=AccessDeniedStatus;
	final static String LoginEntryPointMessage=AccessDeniedMessage;
	final static String LoginFailureStatus="401";
	final static String LoginFailureMessage="Login Failed";
	final static String LogoutSuccessStatus="100";
	final static String LogoutSuccessMessage="Logout Success";

}
