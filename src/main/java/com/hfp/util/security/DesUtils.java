package com.hfp.util.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES 对称、分组加密 ：置换替换
 */
public class DesUtils {
		
	/**
	 * 3DES Cbc模式 加密
	 * @param key 密钥
	 * @param data 明文
	 * @param padMode 填充方式
	 * @return 密文
	 */
	public static byte[] desEdeCbcEncrypt(byte[] key, byte[] data, String padMode){
		byte[] res = null;
		String algorithm = "DESede/CBC/" + padMode;
		try {
			Cipher cipher = Cipher.getInstance(algorithm);  		
	        //SecretKeySpec spec = getDesEdeKey(key);//附加密码修复 
	        SecretKeySpec spec = new SecretKeySpec(key, "DESede");  
	        IvParameterSpec ips = getIv(cipher.getBlockSize());
	        data = padding(data, cipher.getBlockSize());
	        cipher.init(Cipher.ENCRYPT_MODE, spec, ips);  
	        res = cipher.doFinal(data); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 3DES Cbc模式 解密
	 * @param key 密钥
	 * @param data 密文
	 * @param padMode 填充方式
	 * @return 明文
	 */
	public static byte[] desEdeCbcDecrypt(byte[] key, byte[] data, String padMode){
		byte[] res = null;
		String algorithm = "DESede/CBC/" + padMode;
		try {
			Cipher cipher = Cipher.getInstance(algorithm);  		
			//SecretKeySpec spec = getDesEdeKey(key);  
	        SecretKeySpec spec = new SecretKeySpec(key, "DESede");  
			IvParameterSpec ips = getIv(cipher.getBlockSize());
			byte[] padData = padding(data, cipher.getBlockSize());
			cipher.init(Cipher.DECRYPT_MODE, spec, ips);  
			res = cipher.doFinal(padData); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 3DES Ecb模式 加密
	 * @param key 密钥
	 * @param data 明文
	 * @param padMode 填充方式
	 * @return 密文
	 */
	public static byte[] desEdeEcbEncrypt(byte[] key, byte[] data, String padMode){	
		byte[] res = null;
		String algorithm = "DESede/ECB/" + padMode;
		try {
			Cipher cipher = Cipher.getInstance(algorithm);  
//			SecretKeySpec spec = getDesEdeKey(key);
			SecretKeySpec spec = new SecretKeySpec(key, "DESede");  
			byte[] padData = padding(data, cipher.getBlockSize());
			cipher.init(Cipher.ENCRYPT_MODE, spec);
			res = cipher.doFinal(padData); 		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 3DES Ecb模式 解密
	 * @param key 密钥
	 * @param data 密文
	 * @param padMode 填充方式
	 * @return 明文
	 */
	public static byte[] desEdeEcbDecrypt(byte[] key, byte[] data, String padMode) {		
		byte[] res = null;
		String algorithm = "DESede/ECB/" + padMode;
		try {
			Cipher cipher = Cipher.getInstance(algorithm);  
//			SecretKeySpec spec = getDesEdeKey(key);
			SecretKeySpec spec = new SecretKeySpec(key, "DESede");  
			byte[] padData = padding(data, cipher.getBlockSize());
			cipher.init(Cipher.DECRYPT_MODE, spec);
			res = cipher.doFinal(padData); 		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 生成DesKey：DES，密钥为 64bit(8byte)
	 * @param key
	 * @return
	 */
	public static SecretKeySpec getDesKey(byte[] key) {
		//3 * 64 bit, 3*8 byte
		byte[] deskey = new byte[8];
		if (key.length >= 8) {
			System.arraycopy(key, 0, deskey, 0, 8);
		} else {
			System.arraycopy(key, 0, deskey, 0, key.length);
		}
		return new SecretKeySpec(deskey, "DES");
	}
	

	/**
	 * 生成DesEdeKey:3DES，秘钥为 192 bit，3*8byte
	 * @param key
	 * @return
	 */
	public static SecretKeySpec getDesEdeKey(byte[] key) {
		//3 * 64 bit, 3*8 byte
		byte[] deskey = new byte[24];
		if (key.length == 8) {
			System.arraycopy(key, 0, deskey, 0, 8);
			System.arraycopy(key, 0, deskey, 8, 8);
			System.arraycopy(key, 0, deskey,16, 8);
		} else if (key.length == 16) {
			System.arraycopy(key, 0, deskey, 0, 16);
			System.arraycopy(key, 0, deskey, 16, 8);
		} else {
			System.arraycopy(key, 0, deskey, 0, 24);
		}
		return new SecretKeySpec(deskey, "DESede");
	}
	
	/**
	 * 生成初始化向量
	 * @param len 长度
	 * @return
	 */
	public static IvParameterSpec getIv(int len) {
		//使用 IV 的例子是反馈模式中的密码，如，CBC 模式中的 DES 和使用 OAEP 编码操作的 RSA 密码
		return new IvParameterSpec(new byte[len]);
	}

	/**
	 * 补足长度
	 * @param src
	 * @param len
	 * @return
	 */
	public static byte[] padding(byte[] src, int len) {
		int paddingLength = len - src.length % len;
		if (len == paddingLength) {
			return src;
		}
		byte[] newsrc = new byte[src.length + paddingLength];
		System.arraycopy(src, 0, newsrc, 0, src.length);
		return newsrc;
	}
}
