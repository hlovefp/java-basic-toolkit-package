package com.hfp.util.security;

import java.security.Security;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MacUtil {
	public static String disguise(String message, String key, String algorithm){
    	message = message.trim();
    	SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), algorithm); // "HmacMD5"
    	Mac mac=null;
		try {
			mac = Mac.getInstance(algorithm);
			mac.init(secretKey);
		} catch ( Exception e1) {
			e1.printStackTrace();
			return null;
		}
        return HexUtil.toHex(mac.doFinal(message.getBytes()));
    }
	
	/**
	 * 
	 * @param messess
	 * @param key
	 * @return hex字符串(长度32)
	 */
	public static String disguiseHmacMD5(String messess,String key){
		return disguise( messess, key, "HmacMD5");
	}
	
	/**
	 * 
	 * @param messess
	 * @param key
	 * @return hex字符串(长度40)
	 */
	public static String disguiseHmacSHA1(String messess,String key){
		return disguise( messess, key, "HmacSHA1");
	}
	
	/**
	 * 列出支持的算法
     *   PBEWITHHMACSHA1
     *   PBEWITHHMACSHA224
     *   PBEWITHHMACSHA256
     *   PBEWITHHMACSHA384
	 *   PBEWITHHMACSHA512
     *   HMACSHA1
     *   HMACSHA224
     *   HMACSHA256
     *   HMACSHA384
     *   HMACSHA512
     *   HMACPBESHA1
     *   HMACMD5
     *   SSLMACSHA1
     *   SSLMACMD5
	 */
	public static void listAlgorithm(String serviceName){
		Set<String> s = Security.getAlgorithms(serviceName);
        Iterator<String> i = s.iterator();
        while( i.hasNext() ){
            System.out.println(i.next());
        }
	}
	// HmacMD5、HmacSHA1、HmacSHA256、HmacSHA384和HmacSHA512
	public static void main(String[] args){
		listAlgorithm("Mac");
		System.out.println(disguiseHmacMD5("&QuickPaySe机号码不一致","DDADBB69B5BF232EDB1B2537F61457DC"));
		System.out.println(disguiseHmacSHA1("&QuickPaySe机号码不一致","DDADBB69B5BF232EDB1B2537F61457DC"));
	}
}
