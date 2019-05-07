
package com.hfp.util.security.sm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.CipherParameters;
import com.hfp.util.security.Constants;
import com.hfp.util.security.DesUtils;
import com.hfp.util.security.RsaUtils;
import com.hfp.util.security.SHAUtil;

public class CupSec {

	/**
	 * 敏感信息3DES加密，敏感信息密钥RSA加密
	 * 
	 * 过程说明：先用对称密钥对敏感信息进行加密并进行base64编码， 然后对该对称密钥进行加密并进行base64编码。
	 * 敏感信息是位于XML标签 &amplt;SensInf></SensInf>内的内容(不包括<SensInf>与</SensInf>标签)
	 * 
	 * @param publicKey
	 *            加密敏感信息密钥的公钥
	 * @param sensInf
	 *            敏感信息明文
	 * @param encKey
	 *            敏感信息对称密钥明文
	 * @return 数组 {加密且base64的敏感信息, 加密且base64的敏感信息对称密钥}
	 */
	public static String[] sensInf3DEKeySM4SEncrypt(PublicKey publicKey, byte[] sensInf, byte[] encKey) {
		// sensInf: 3des cbc nopadding
		//logger.debug("sensInfo hex:" + Hex.toHexString(sensInf));
		byte[] sensEnc = DesUtils.desEdeCbcEncrypt(encKey, sensInf, Constants.PAD_NO);
		//logger.debug("sensInfo enc hex:" + Hex.toHexString(sensEnc));
		String sensInfEB = Base64.encodeBase64String(sensEnc);
		//logger.debug("sensInfo enc base64:" + sensInfEB);
		// EncKey: rsa ecb PKCS1Padding
		//logger.debug("encKey hex:" + Hex.toHexString(encKey));
		//logger.debug("public key hex: " + Hex.toHexString(publicKey.getEncoded()));
		byte[] encKeyE = RsaUtils.rsaEcbEncrypt((RSAPublicKey) publicKey, encKey, Constants.PAD_PKCS1);
		//logger.debug("encKey enc hex:" + Hex.toHexString(encKeyE));
		String encKeyEB = Base64.encodeBase64String(encKeyE);
		//logger.debug("encKey enc base64:" + encKeyEB);

		return new String[] { sensInfEB, encKeyEB };
	}

	/**
	 * 敏感信息3DES解密，敏感信息密钥RSA解密
	 * 
	 * 过程说明： 先解密出所用密钥（即用于敏感信息解密的密钥），再对报文中<SensInf></SensInf>标签中的所有内容
	 * （不包括包括<SensInf>和</SensInf>标签自身）进行解密
	 * 
	 * @param privateKey
	 *            解密敏感信息的私钥
	 * @param sensInfEB
	 *            敏感信息密文
	 * @param encKeyEB
	 *            敏感信息对称密钥密文
	 * @return 数组 {敏感信息, 敏感信息对称密钥}
	 */
	public static byte[][] sensInf3DESKeySM4Decrypt(PrivateKey privateKey, String sensInfEB, String encKeyEB) {
		// EncKey: rsa ecb PKCS1Padding
		//logger.debug("encKey ciphertext:" + encKeyEB);
		byte[] encKeyE = Base64.decodeBase64(encKeyEB);
		byte[] encKey = RsaUtils.rsaEcbDecrypt((RSAPrivateKey) privateKey, encKeyE, Constants.PAD_PKCS1);
		//logger.debug("encKey hex:" + Hex.toHexString(encKey));
		// sensInf: 3des cbc nopadding
		//logger.debug("sensInfo ciphertext:" + sensInfEB);
		byte[] sensInfE = Base64.decodeBase64(sensInfEB);
		byte[] sensInf = DesUtils.desEdeCbcDecrypt(encKey, sensInfE, Constants.PAD_NO);
		//logger.debug("sensInfo hex:" + Hex.toHexString(sensInf));

		return new byte[][] { sensInf, encKey };
	}

	/**
	 * 敏感信息SM4加密，敏感信息密钥SM2加密
	 * 
	 * 过程说明：先对报文中<SensInf></SensInf>标签中的所有内容（不包括包括<SensInf>和</SensInf>标签自身）
	 * 进行加密，再对加密所用密钥进行加密，最后对加密后的敏感信息与加密后的敏感信息加密密钥进行base64编码
	 * 
	 * @param publicKey
	 *            加密敏感信息密钥的公钥
	 * @param sensInf
	 *            敏感信息明文
	 * @param encKey
	 *            敏感信息对称密钥明文
	 * @return 数组 {加密且base64的敏感信息, 加密且base64的敏感信息对称密钥}
	 */
	public static String[] sensInfSM4KeySM2Encrypt(CipherParameters publicKey, byte[] sensInf, byte[] encKey) {
		// sensInf: SM4 cbc nopadding
		//logger.debug("sensInfo hex:" + Hex.toHexString(sensInf));
		byte[] sensEnc = Sm4Utils.sm4CbcEncrypt(encKey, sensInf, Constants.PAD_NO);
		//logger.debug("sensInfo enc hex:" + Hex.toHexString(sensEnc));
		String sensInfEB = Base64.encodeBase64String(sensEnc);
		//logger.debug("sensInfo enc base64:" + sensInfEB);
		// EncKey: SM2
		//logger.debug("encKey hex:" + Hex.toHexString(encKey));
		//logger.debug("public key: " + publicKey);
		byte[] encKeyE = Sm2Utils.Encrypt(publicKey, encKey);
		//logger.debug("encKey enc hex:" + Hex.toHexString(encKeyE));
		String encKeyEB = Base64.encodeBase64String(encKeyE);
		//logger.debug("encKey enc base64:" + encKeyEB);

		return new String[] { sensInfEB, encKeyEB };
	}

	/**
	 * 敏感信息SM4解密，敏感信息密钥SM2解密
	 * 
	 * 过程说明： 先解密出所用密钥（即用于敏感信息解密的密钥），再对报文中<SensInf></SensInf>标签中的所有内容
	 * （不包括包括<SensInf>和</SensInf>标签自身）进行解密
	 * 
	 * @param privateKey
	 *            解密敏感信息的私钥
	 * @param sensInfEB
	 *            敏感信息密文
	 * @param encKeyEB
	 *            敏感信息对称密钥密文
	 * @return 数组 {敏感信息, 敏感信息对称密钥}
	 */
	public static byte[][] sensInfSM4KeySM2Decrypt(CipherParameters privateKey, String sensInfEB, String encKeyEB) {
		// EncKey: SM2
		//logger.debug("encKey ciphertext:" + encKeyEB);
		byte[] encKeyE = Base64.decodeBase64(encKeyEB);
		byte[] encKey = Sm2Utils.Decrypt(privateKey, encKeyE);
		//logger.debug("encKey hex:" + Hex.toHexString(encKey));
		// sensInf: SM4 cbc nopadding
		//logger.debug("sensInfo ciphertext:" + sensInfEB);
		byte[] sensInfE = Base64.decodeBase64(sensInfEB);
		byte[] sensInf = Sm4Utils.sm4CbcDecrypt(encKey, sensInfE, Constants.PAD_NO);
		//logger.debug("sensInfo hex:" + Hex.toHexString(sensInf));

		return new byte[][] { sensInf, encKey };
	}

	/**
	 * RSA私钥签名
	 * 
	 * 过程说明： 先对报文中<root></root>标签中的所有内容（包括<root>和</root>标签自身）
	 * 生成SHA-256摘要，再对摘要进行RSA签名，最后对生成的签名进行base64编码
	 * 
	 * @param privateKey
	 *            签名私钥
	 * @param msg
	 *            待签名消息
	 * @return base64后的签名结果
	 */
	public static String rsaSignWithSha256(PrivateKey privateKey, byte[] msg) {
		byte[] shaMsg = SHAUtil.SHA256(msg);
		byte[] sign = RsaUtils.signWithSha256((RSAPrivateKey) privateKey, shaMsg);
		return Base64.encodeBase64String(sign);
	}

	/**
	 * RSA公钥验签
	 * 
	 * 过程说明：先对报文中<root></root>标签中的所有内容（包括<root>和</root>标签自身）
	 * 生成SHA-256摘要，然后将base64编码后的签名数据进行base64解码， 最后使用摘要和base64解码后的签名进行RSA验签
	 * 
	 * @param publicKey
	 *            验签公钥
	 * @param msg
	 *            被签名消息
	 * @param signB
	 *            待验证签名
	 * @return 验签结果：true(验签成功) 或 false(验签失败)
	 */
	public static boolean rsaVerifyWithSha256(PublicKey publicKey, byte[] msg, String signB) {
		byte[] shaMsg = SHAUtil.SHA256(msg);
		byte[] sign = Base64.decodeBase64(signB);
		return RsaUtils.verifyWithSha256((RSAPublicKey) publicKey, shaMsg, sign);
	}

	/**
	 * SM2私钥签名
	 * 
	 * 过程说明：先对报文中<root></root>标签中的所有内容（包括<root>和</root>标签自身）
	 * 生成SM3摘要，再对摘要进行SM2签名，最后对生成的签名进行base64编码
	 * 
	 * @param privateKey
	 *            签名私钥
	 * @param userId
	 *            用户标识
	 * @param msg
	 *            待签名消息
	 * @return base64后的签名结果
	 */
	public static String sm2SignWithSm3(CipherParameters privateKey, byte[] userId, byte[] msg) {
		byte[] sm3Msg = Sm3Utils.sm3(msg);   // 数据摘要
		byte[] sign = Sm2Utils.SignWithSm3(privateKey, userId, sm3Msg);  // 签名
		return Base64.encodeBase64String(sign);
	}

	/**
	 * SM2公钥验签
	 * 
	 * 过程说明：先对报文中<root></root>标签中的所有内容（包括<root>和</root>标签自身）
	 * 生成SM3摘要，然后将base64编码后的签名数据进行base64解码， 最后使用摘要和base64解码后的签名进行SM2验签
	 * 
	 * @param publicKey
	 *            验签公钥
	 * @param userId
	 *            用户标识
	 * @param msg
	 *            被签名消息
	 * @param signB
	 *            待验证签名
	 * @return 验签结果：true(验签成功) 或 false(验签失败)
	 */
	public static boolean sm2VerifyWithSm3(CipherParameters publicKey, byte[] userId, byte[] msg, String signB) {
		byte[] sm3Msg = Sm3Utils.sm3(msg);
		byte[] sign = Base64.decodeBase64(signB);
		return Sm2Utils.VerifyWithSm3(publicKey, userId, sm3Msg, sign);
	}
}
