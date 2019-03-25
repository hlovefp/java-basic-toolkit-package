package com.hfp.code;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESUtil {
	
	/**
	 * 生成密钥
	 * @param keysize
	 *     java.security.InvalidParameterException: Wrong keysize: must be equal to 128, 192 or 256
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static SecretKeySpec generateSecretKeySpec(int keysize) {  
		//Security.addProvider(new BouncyCastleProvider());  
		KeyGenerator kgen;
		try {
			kgen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		String seed = String.valueOf(System.currentTimeMillis());
		kgen.init( keysize, new SecureRandom(seed.getBytes()));  
		SecretKey key = kgen.generateKey();  
		SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");  
		
		// SecretKeySpec keySpec = new SecretKeySpec(PWD.getBytes("UTF-8"), "AES");  
		return keySpec;  
	}
	
	/**
	 * 从[0-9a-zA-Z]中生成指定长度字符串密钥
	 * @param length 16/24/32
	 * @return
	 */
	public static String generateKeyString(int length) {
		String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString();
	}
	
	/**
	 * 加密
	 * @param data
	 * @param key 长度必须是16/24/32否则报异常：java.security.InvalidKeyException: Invalid AES key length: xx bytes
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key, String algorithm) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
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
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance(algorithm); // 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, secretKey);   // 初始化
			return cipher.doFinal(data); // 解密
		} catch (Exception e){
			throw new RuntimeException("decrypt fail!", e);
		}
	}
	
	/**
	 * AES加密 (AES/ECB/PKCS5Padding)
	 * @param data 数据长度可以不是16的倍数
	 * @param key
	 * @return 返回hex字符串
	 */
	public static String encryptAES(String data, String key){
		return HexUtil.toHex(encrypt(data.getBytes(), key.getBytes(), "AES")); // 或者 "AES/ECB/PKCS5Padding"
	}
	
	/**
	 * AES解密
	 * @param data
	 * @param key
	 * @return
	 */
	public static String decryptAES(String data, String key){
		return new String(decrypt(HexUtil.toByteArray(data), key.getBytes(), "AES"));
	}

	/**
	 * AES/ECB/NoPadding加密
	 * @param data  数据必须是16的倍数,否则报异常 <p>
	 * 		javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes
	 * @param key
	 * @return
	 */
	public static String encryptAESNoPadding(String data, String key){
		return HexUtil.toHex(encrypt(data.getBytes(), key.getBytes(), "AES/ECB/NoPadding"));
	}
	
	public static String decryptAESNoPadding(String data, String key){
		return new String(decrypt(HexUtil.toByteArray(data), key.getBytes(), "AES/ECB/NoPadding"));
	}
	

	/**
	 * AES/ECB/ZeroPadding加密<p>
	 * 缺点：原始数据末尾就是0x00，解密数据分不出来
	 * @param data
	 * @param key
	 * @return
	 */
	public static String encryptAESZeroPadding(String data, String key){
		// zeropadding填充方式，它是使用0x00作为填充数据的填充方式
		// Cipher没有ZeroPadding填充方式,自己补充数据，然后使用NoPadding
		int paddingLength = 16-data.length()%16;  // 填充1-16个长度的数据
		StringBuilder builder = new StringBuilder(paddingLength);
		for(int i=0; i<paddingLength; i++){
			builder.append((char)0x00);
		}
		data = data+builder.toString();
		return encryptAESNoPadding( data, key);
	}
	
	public static String decryptAESZeroPadding(String data, String key){
		byte[] bytes = decrypt(HexUtil.toByteArray(data), key.getBytes(), "AES/ECB/NoPadding");
		int paddingLength=0;
		for(int i=1;i<=16;i++){
			if(bytes[bytes.length-i]==0x00){
				paddingLength++;
			}
		}
		byte[] dataByte = new byte[bytes.length-paddingLength];
		System.arraycopy(bytes, 0, dataByte, 0, dataByte.length);
		return new String(dataByte);
	}
	



	/**
	 * 加密算法填充方式:
	 * 
	 * 加密算法模式:
	 *   ECB模式：电子密码本模式
     *   CBC模式：密码分组连接模式
     *   CFB模式：密文反馈模式
     *   OFB模式：输出反馈模式
     *   CTR模式：计数器模式
	 * 列出支持的算法
     *   PBEWITHHMACSHA224ANDAES_256
     *   PBEWITHHMACSHA256ANDAES_256
     *   PBEWITHHMACSHA384ANDAES_128
     *   PBEWITHHMACSHA512ANDAES_128
     *   AES_128/CBC/NOPADDING       16*8 = 128    AES是16/24/32字节分组
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
		Security.addProvider(new BouncyCastleProvider());   // 第三方安全库
		
		Set<String> s = Security.getAlgorithms(serviceName);
        Iterator<String> i = s.iterator();
        while( i.hasNext() ){
            System.out.println(i.next());
        }
	}
	
	
	public static void main(String[] args){
		//listAlgorithm("Cipher");
		//addAlgorithm("Cipher");
		//generateSecretKeySpec(255);
		
		//String aes = encryptAES("6228481143765162711","pgTyCu7OqIvyzDj2");
		//System.out.println(aes); // 8ebf491dee412111a6a55db7f6f37cc6012870fc5759b7374eb558645aaa126b
		//System.out.println(decryptAES(aes,"GA2Sd3x7aMmTkQrU"));
		
		//String aes = encryptAES("6228481143765162711","pgTyCu7OqIv3yzDj2vyzDj2y");
		//System.out.println(aes); // 4f34632a92b70faea91b9894bd5f27a4fd0ae7c6bc2a337d8b1c84c79095630e
		//System.out.println(decryptAES(aes,"pgTyCu7OqIv3yzDj2vyzDj2y"));
		
		//String aes = encryptAES("6228481143765162711","pgTyCu7OqIvyzDj2pgTyCu7OqIvyzDj2");
		//System.out.println(aes);  // e3096710aea9b7fe42fd505ec5e02d8fc34bae5820bc6b5870d227e9288d7e08
		//System.out.println(decryptAES(aes,"pgTyCu7OqIvyzDj2pgTyCu7OqIvyzDj2"));
		
		//String aes = encryptAESZeroPadding("1234567890123456","pgTyCu7OqIvyzDj2");
		//System.out.println(aes); // bdd71f373b44a08dbc977844696d2f08140fc1d895e35b69e520f65f7e21e854
		//System.out.println(decryptAESZeroPadding(aes,"pgTyCu7OqIvyzDj2"));

		//String aes = encryptAESZeroPadding("12345678901234567","pgTyCu7OqIvyzDj2");
		//System.out.println(aes); // bdd71f373b44a08dbc977844696d2f082b49919a9412a017544cff5512aa2bac
		//System.out.println(decryptAESZeroPadding(aes,"pgTyCu7OqIvyzDj2"));
		
		String aes = encryptAESNoPadding("1234567890123456","pgTyCu7OqIvyzDj2");
		System.out.println(aes); // bdd71f373b44a08dbc977844696d2f08
		System.out.println(decryptAESNoPadding(aes,"pgTyCu7OqIvyzDj2"));

	}
}
