
package com.hfp.util.security;

import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;


/**
 * RSA 非对称、公钥加密,签名验签：基于数学函数
 * 	公钥(n,e)加密: m^e=c(mod n)，m明文，c密文
 *	私钥(n,d)解密: c^d=m(mod n)，c密文，m明文
 */
public class RsaUtils {


	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 算法常量
	 */
	public static final String SIGN_ALGORITHM_SHA256RSA = "SHA256withRSA";

	/**
	 * RSA Ecb模式 公钥加密
	 * @param publicKey 公钥
	 * @param data 明文
	 * @param padMode 填充模式
	 * @return 密文
	 */
	public static byte[] rsaEcbEncrypt(RSAPublicKey publicKey, byte[] data, String padMode) {
		//
		String algorithm = "RSA/ECB/" + padMode;
		byte[] res = null;
		if (publicKey == null) {
			//logger.error("publicKey is null");
		}
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			res = cipher.doFinal(data);
		} catch (Exception e) {
			//logger.error("Fail: RSA Ecb Encrypt",e);
		} 
		return res;
	}

	/**
	 * RSA Ecb 私钥解密
	 * @param privateKey 私钥
	 * @param data 密文
	 * @param padMode 填充模式
	 * @return 明文
	 */
	public static byte[] rsaEcbDecrypt(RSAPrivateKey privateKey, byte[] data, String padMode) {
		if (privateKey == null) {
			//logger.error("privateKey is null");
		}
		String algorithm = "RSA/ECB/" + padMode;
		byte[] res = null;
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			res = cipher.doFinal(data);
		} catch (Exception e) {
			//logger.error("Fail: RSA Ecb Decrypt",e);
		} 
		return res;
	}

	/**
	 * RSA Sha256摘要  私钥签名
	 * @param privateKey 私钥
	 * @param data 消息
	 * @return 签名
	 */
	public static byte[] signWithSha256(RSAPrivateKey privateKey, byte[] data) {
		byte[] result = null;
		Signature st;
		try {
			st = Signature.getInstance(SIGN_ALGORITHM_SHA256RSA);
			st.initSign(privateKey);
			st.update(data);
			result = st.sign();
		} catch (Exception e) {
			//logger.error("Fail: RSA with sha256 sign",e);
		} 
		return result;
	}

	/**
	 * RSA Sha256摘要  公钥验签
	 * @param pubKey 公钥
	 * @param data 消息
	 * @param sign 签名
	 * @return 验签结果
	 */
	public static boolean verifyWithSha256(RSAPublicKey publicKey, byte[] data, byte[] sign) {
		boolean correct = false;
		try {
			Signature st = Signature.getInstance(SIGN_ALGORITHM_SHA256RSA);
			st.initVerify(publicKey);
			st.update(data);
			correct = st.verify(sign);
		} catch (Exception e) {
			//logger.error("Fail: RSA with sha256 verify",e);
		} 
		return correct;
	}
}
