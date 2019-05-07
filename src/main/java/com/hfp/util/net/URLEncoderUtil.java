package com.hfp.util.net;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLEncoderUtil {
	
	/**
	 * 编码
	 * @param data
	 * @return
	 */
	public static String encode(String data){
		return encode(data,"UTF-8");
	}
	
	public static String encode(String data, String charset){
		try {
			return URLEncoder.encode(data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decode(String data){
		return decode(data,"UTF-8");
	}
	
	public static String decode(String data, String charset){
		try {
			return URLDecoder.decode(data, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		String msg = "中国";
		String encode = encode(msg);
		System.out.println(encode);
		System.out.println(decode(encode));
	}
}
