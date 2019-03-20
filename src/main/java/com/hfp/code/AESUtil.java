package com.hfp.code;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES加解密工具类
 * @version:
 */
public class AESUtil {

	public static final String CHAR_ENCODING = "UTF-8";

	/**
	 * 加密
	 * @param data
	 * @param key 长度必须是16个字节
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key) {
		if(key.length!=16){
			throw new RuntimeException("Invalid AES key length (must be 16/24/32 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES"); // 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, seckey);  // 初始化
			return cipher.doFinal(data);               // 加密
		} catch (Exception e){
			throw new RuntimeException("encrypt fail!", e);
		}
	}

	/**
	 * 解密
	 * @param data
	 * @param key 长度必须是16个字节
	 * @return
	 */
	public static byte[] decrypt(byte[] data, byte[] key) {
		if(key.length!=16){
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, seckey);// 初始化
			byte[] result = cipher.doFinal(data);
			return result; // 加密
		} catch (Exception e){
			throw new RuntimeException("decrypt fail!", e);
		}
	}
	
	/**
	 * 加密
	 * @param data
	 * @param key 16个字节
	 * @return base64字符串
	 */
	public static String encryptToBase64(String data, String key){
		try {
			byte[] valueByte = encrypt(data.getBytes(CHAR_ENCODING), key.getBytes(CHAR_ENCODING));
			return BASE64Util.encode(valueByte);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encrypt fail!", e);
		}
		
	}
	
	public static String encryptToHex(String data, String key){
		try {
			byte[] valueByte = encrypt(data.getBytes(CHAR_ENCODING), key.getBytes(CHAR_ENCODING));
			return HexUtil.toHex(valueByte);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encrypt fail!", e);
		}
	}
	
	/**
	 * 解密
	 * @param data  base64字符串
	 * @param key 16个字节
	 * @return
	 */
	public static String decryptFromBase64(String data, String key){
		try {
			byte[] originalData = BASE64Util.decode(data.getBytes());
			byte[] valueByte = decrypt(originalData, key.getBytes(CHAR_ENCODING));
			return new String(valueByte, CHAR_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}

	/**
	 * 从[0-9a-zA-Z]中生成指定长度字符串
	 * @param length
	 * @return
	 */
	public static String generateKey(int length) {
		String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString();
	}

	public static void main(String[] args) {

		String key = "GA2Sd3x7aMmTkQrU";
		System.out.println(key);
		String s = encryptToBase64("6228481143765162711", key);
		System.out.println(s);
		//xGHvfsY5m6uUPOgCOvI9DBEtMaUEPBUGpUW1Irwq6Ys=
		//xGHvfsY5m6uUPOgCOvI9DBEtMaUEPBUGpUW1Irwq6Ys=
		String e = decryptFromBase64(s, key);
		System.out.println(e);
		
		String hex=encryptToHex("6228481143765162711", key);
		System.out.println(hex.toUpperCase());
	}

}
