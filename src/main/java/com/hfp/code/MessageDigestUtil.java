package com.hfp.code;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Iterator;
import java.util.Set;

public class MessageDigestUtil {
	
	public static String disguise(String message, String algorithm, String encoding){
    	message = message.trim();
        byte value[];
        try{
            value = message.getBytes(encoding);
        }
        catch(UnsupportedEncodingException e){
            value = message.getBytes();
        }
        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance(algorithm);
        }catch(NoSuchAlgorithmException e){
        	e.printStackTrace();
            return null;
        }
        return HexUtil.toHex(md.digest(value));
    }
	
	/**
	 * SHA1摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串
	 */
	public static String disguiseSHA1(String message,String encoding){
    	return disguise(message, "SHA-1", encoding);  // "SHA" 或者 "SHA-1" 结果一致
    }
	
	/**
	 * 
	 * @param message 默认UTF-8编码
	 * @return 返回hex字符串(长度40)
	 */
	public static String disguiseSHA1(String message){
    	return disguiseSHA1(message,"UTF-8");
    }
	
	/**
	 * SHA224摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串(长度56)
	 */
	public static String disguiseSHA224(String message,String encoding){
    	return disguise(message, "SHA-224", encoding);
    }
	
	public static String disguiseSHA224(String message){
    	return disguiseSHA224(message,"UTF-8");
    }
	
	/**
	 * SHA256摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串(长度40)
	 */
	public static String disguiseSHA256(String message,String encoding){
    	return disguise(message, "SHA-256", encoding);
    }
	
	public static String disguiseSHA256(String message){
    	return disguiseSHA256(message,"UTF-8");
    }
	
	/**
	 * SHA384摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串(长度96)
	 */
	public static String disguiseSHA384(String message,String encoding){
    	return disguise(message, "SHA-384", encoding);
    }
	
	public static String disguiseSHA384(String message){
    	return disguiseSHA384(message,"UTF-8");
    }

	/**
	 * SHA512摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串(长度128)
	 */
	public static String disguiseSHA512(String message,String encoding){
    	return disguise(message, "SHA-512", encoding);
    }
	
	public static String disguiseSHA512(String message){
    	return disguiseSHA512(message,"UTF-8");
    }
	
	/**
	 * 
	 * @param message  默认的字符编码是UTF-8
	 * @return 返回hex字符串(长度32)
	 */
	public static String disguiseMD5(String message) {
		if (null == message) {
			return null;
		}
		return disguise(message, "MD5", "UTF-8");
	}

	/**
	 * 
	 * @param message  默认的字符编码是UTF-8
	 * @return 返回hex字符串(长度32)
	 */
	public static String disguiseMD2(String message) {
		if (null == message) {
			return null;
		}
		return disguise(message, "MD2", "UTF-8");
	}

	/**
	 * 列出支持的  MessageDigest 算法
     *   SHA-384
     *   SHA-224
     *   SHA-256
     *   MD2
     *   SHA
     *   SHA-512
     *   MD5
	 */
	public static void listAlgorithm(String serviceName){
		Set<String> s = Security.getAlgorithms(serviceName);
        Iterator<String> i = s.iterator();
        while( i.hasNext() ){
            System.out.println(i.next());
        }
	}
	
	public static void main(String[] args){
		//listAlgorithm("MessageDigest");
		//System.out.println(disguiseMD2("&QuickPaySe机号码不一致"));
		//System.out.println(disguiseMD5("&QuickPaySe机号码不一致"));
		System.out.println(disguiseSHA1("&QuickPaySe机号码不一致"));
		//System.out.println(disguiseSHA224("&QuickPaySe机号码不一致"));
		//System.out.println(disguiseSHA256("&QuickPaySe机号码不一致"));
		//System.out.println(disguiseSHA384("&QuickPaySe机号码不一致"));
		//System.out.println(disguiseSHA512("&QuickPaySe机号码不一致"));
	}
}
