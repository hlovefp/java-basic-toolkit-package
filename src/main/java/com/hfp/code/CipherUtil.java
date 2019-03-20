package com.hfp.code;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CipherUtil {
	
	/**
	 * 生成密钥
	 * @param seed
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static SecretKeySpec genKey(String seed, String algorithm) throws NoSuchAlgorithmException {  
		//Security.addProvider(new BouncyCastleProvider());  
		KeyGenerator kgen = KeyGenerator.getInstance("AES");  
		kgen.init(256, new SecureRandom(seed.getBytes()));  
		SecretKey key = kgen.generateKey();  
		SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");  
		
		// SecretKeySpec keySpec = new SecretKeySpec(PWD.getBytes("UTF-8"), "AES");  
		return keySpec;  
}  
	
	/**
	 * 加密
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key, String algorithm) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(algorithm); // 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);  // 初始化
			return cipher.doFinal(data);               // 加密
		} catch (Exception e){
			throw new RuntimeException("encrypt fail!", e);
		}
	}
	
	/**
	 * 解密
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] decrypt(byte[] data, byte[] key, String algorithm) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(algorithm); // 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, secretKey);   // 初始化
			return cipher.doFinal(data); // 解密
		} catch (Exception e){
			throw new RuntimeException("decrypt fail!", e);
		}
	}
	
	/**
	 * AES加密
	 * @param data
	 * @param key
	 * @return
	 */
	public static String encryptAES(String data, String key){
		return HexUtil.toHex(encrypt(data.getBytes(), key.getBytes(), "AES"));
	}
	
	public static String decryptAES(String data, String key){
		return new String(decrypt(HexUtil.toByteArray(data), key.getBytes(), "AES"));
	}

	/**
	 * 列出支持的算法
     *   PBEWITHHMACSHA224ANDAES_256
     *   PBEWITHHMACSHA256ANDAES_256
     *   PBEWITHHMACSHA384ANDAES_128
     *   PBEWITHHMACSHA512ANDAES_128
     *   AES_128/CBC/NOPADDING
     *   AES_128/ECB/NOPADDING
     *   AES_128/CFB/NOPADDING
     *   AES_128/OFB/NOPADDING
     *   AES_128/GCM/NOPADDING
     *   AES_192/ECB/NOPADDING
     *   AES_192/GCM/NOPADDING
     *   AES_192/CBC/NOPADDING
     *   AES_192/OFB/NOPADDING
     *   AES_192/CFB/NOPADDING
     *   AES_256/GCM/NOPADDING
     *   AES_256/CBC/NOPADDING
     *   AES_256/ECB/NOPADDING
     *   AES_256/CFB/NOPADDING
     *   AES_256/OFB/NOPADDING
     *   AESWRAP_192
     *   AESWRAP
     *   DESEDEWRAP
     *   RC2
     *   PBEWITHSHA1ANDRC4_40
     *   RSA
     *   RSA/ECB/PKCS1PADDING
     *   AESWRAP_128
     *   AESWRAP_256
     *   DESEDE
     *   BLOWFISH
     *   ARCFOUR
     *   AES
     *   DES
     *   PBEWITHSHA1ANDDESEDE
     *   PBEWITHSHA1ANDRC2_40
     *   PBEWITHSHA1ANDRC2_128
     *   PBEWITHSHA1ANDRC4_128
     *   PBEWITHMD5ANDDES
     *   PBEWITHMD5ANDTRIPLEDES
     *   PBEWITHHMACSHA1ANDAES_128
     *   PBEWITHHMACSHA1ANDAES_256
     *   PBEWITHHMACSHA224ANDAES_128
     *   PBEWITHHMACSHA256ANDAES_128
     *   PBEWITHHMACSHA384ANDAES_256
     *   PBEWITHHMACSHA512ANDAES_256
	 */
	public static void listAlgorithm(String serviceName){
		Set<String> s = Security.getAlgorithms(serviceName);
        Iterator<String> i = s.iterator();
        while( i.hasNext() ){
            System.out.println(i.next());
        }
	}
	
	public static void addAlgorithm(String serviceName){
		// 第三方安全库
		Security.addProvider(new BouncyCastleProvider());
		Set<String> s = Security.getAlgorithms(serviceName);
        Iterator<String> i = s.iterator();
        while( i.hasNext() ){
            System.out.println(i.next());
        }
	}
	
	
	public static void main(String[] args){
		//listAlgorithm("Cipher");
		addAlgorithm("Cipher");
		
		//String aes = encryptAES("6228481143765162711","GA2Sd3x7aMmTkQrU");
		//System.out.println(aes); // c461ef7ec6399bab943ce8023af23d0c112d31a5043c1506a545b522bc2ae98b
		//System.out.println(decryptAES(aes,"GA2Sd3x7aMmTkQrU"));
	}
}
