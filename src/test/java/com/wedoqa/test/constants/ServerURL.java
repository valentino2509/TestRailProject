package com.wedoqa.test.constants;

/**
* 
* @author wedoqa
* 
* Class with all the server URL informations for all instances used in the project 
*/
public class ServerURL {

	public static final String VERSION = "3.0";
	public static final String QA_URL = ((null == System.getProperty("SERVER_HOSTNAME")) 
			|| System.getProperty("SERVER_HOSTNAME")
			.isEmpty()) ? "https://www.google.com/" : System.getProperty("SERVER_HOSTNAME");	//FIXME Change to your server URL
}