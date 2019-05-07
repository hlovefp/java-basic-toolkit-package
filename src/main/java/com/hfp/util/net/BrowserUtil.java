package com.hfp.util.net;

public class BrowserUtil {
	public static Boolean isWechatBrowser(String userAgent){
		if( userAgent != null && userAgent.contains("MicroMessenger") ){
			return true;
		}
		return false;
	}
	
	public static Boolean isAlipayBrowser(String userAgent){
		if( userAgent != null && userAgent.contains("AliApp") ){
			return true;
		}
		return false;
	}
}
