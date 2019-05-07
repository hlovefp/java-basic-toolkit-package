package com.hfp.util.security;

import java.security.MessageDigest;
import java.security.Security;
import java.util.Iterator;
import java.util.Set;

public class SHAUtil {
	
	public static byte[] disguise(byte[] message, String algorithm){
        try{
        	MessageDigest md = MessageDigest.getInstance(algorithm);
        	md.reset();
        	md.update(message);
        	return md.digest();
        	//return md.digest(message);
        } catch(Exception e){
        	e.printStackTrace();
        }
        return null;
    }
	
	public static String disguise(String message, String algorithm, String encoding){
        try{
        	byte value[] = message.trim().getBytes(encoding);
        	MessageDigest md = MessageDigest.getInstance(algorithm);
        	return HexUtil.toHex(md.digest(value));
        } catch(Exception e){
        	e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * SHA1摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串
	 */
	public static String SHA1(String message,String encoding){
    	return disguise(message, "SHA-1", encoding);  // "SHA" 或者 "SHA-1" 结果一致
    }
	
	/**
	 * 
	 * @param message 默认UTF-8编码
	 * @return 返回hex字符串(长度40)
	 */
	public static String SHA1(String message){
    	return SHA1(message,"UTF-8");
    }
	
	/**
	 * SHA224摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串(长度56)
	 */
	public static String SHA224(String message,String encoding){
    	return disguise(message, "SHA-224", encoding);
    }
	
	public static String SHA224(String message){
    	return SHA224(message,"UTF-8");
    }
	
	/**
	 * SHA256摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串(长度40)
	 */
	public static String SHA256(String message,String encoding){
    	return disguise(message, "SHA-256", encoding);
    }
	
	public static String SHA256(String message){
    	return SHA256(message,"UTF-8");
    }
	
	public static byte[] SHA256(byte[] message){
    	return disguise(message, "SHA-256");
    }
	
	/**
	 * SHA384摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串(长度96)
	 */
	public static String SHA384(String message,String encoding){
    	return disguise(message, "SHA-384", encoding);
    }
	
	public static String SHA384(String message){
    	return SHA384(message,"UTF-8");
    }

	/**
	 * SHA512摘要，
	 * @param message
	 * @param encoding
	 * @return 返回hex字符串(长度128)
	 */
	public static String SHA512(String message,String encoding){
    	return disguise(message, "SHA-512", encoding);
    }
	
	public static String SHA512(String message){
    	return SHA512(message,"UTF-8");
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
		System.out.println(SHA1("&QuickPaySe机号码不一致"));
		//System.out.println(SHA224("&QuickPaySe机号码不一致"));
		//System.out.println(SHA256("&QuickPaySe机号码不一致"));
		//System.out.println(SHA384("&QuickPaySe机号码不一致"));
		//System.out.println(SHA512("&QuickPaySe机号码不一致"));
	}
}
