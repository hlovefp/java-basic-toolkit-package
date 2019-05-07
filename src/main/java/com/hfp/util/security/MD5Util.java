package com.hfp.util.security;

import java.security.MessageDigest;
import java.security.Security;
import java.util.Iterator;
import java.util.Set;

public class MD5Util {
	
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
	 * 
	 * @param message  默认的字符编码是UTF-8
	 * @return 返回hex字符串(长度32)
	 */
	public static String MD5(String message) {
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
	public static String MD2(String message) {
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
		//System.out.println(MD2("&QuickPaySe机号码不一致"));
		//System.out.println(MD5("&QuickPaySe机号码不一致"));
	}
}
