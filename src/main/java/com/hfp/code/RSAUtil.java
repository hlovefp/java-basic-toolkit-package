package com.hfp.code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
//import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;

import org.apache.commons.lang3.ArrayUtils;

import sun.misc.BASE64Encoder;

public class RSAUtil {
    public static final String NOPADDING = "RSA/NONE/NoPadding";
    public static final String RSANONEPKCS1PADDING = "RSA/NONE/PKCS1Padding";
    public static final String RSAECBPKCS1PADDING  = "RSA/ECB/PKCS1Padding";
    //public static final String PROVIDER = "BC";

	static {
		// 引入第三方安全库
        //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());
    }
	
	/**
     * 验证签名
     *
     * @param data      数据
     * @param sign      签名
     * @param publicKey 公钥
     * @return
     */
    public static boolean verifySign(byte[] data, byte[] sign, PublicKey publicKey, String algorithm) {
        try {
            Signature signature = Signature.getInstance(algorithm);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean verifySignMD5withRSA(byte[] data, byte[] sign,PublicKey publicKey){
    	return verifySign( data, sign, publicKey, "MD5withRSA");
    }
    
    /**
     * 验证签名
     *
     * @param data     数据
     * @param sign     签名 Hex字符串
     * @param pubicKey 公钥
     * @return
     */
    public static boolean verifySignMD5withRSA(String data, String sign, PublicKey publicKey) {
        try {
            byte[] dataByte = data .getBytes("UTF-8");
            byte[] signByte = HexUtil.toByteArray(sign);
            return verifySignMD5withRSA(dataByte, signByte, publicKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean verifySignSHA1WithRSA(byte[] data, byte[] sign,PublicKey publicKey){
    	return verifySign( data, sign, publicKey, "SHA1WithRSA");
    }
    
    public static boolean verifySignSHA1WithRSA(String data, String sign, PublicKey publicKey) {
        try {
            byte[] dataByte = data .getBytes("UTF-8");
            byte[] signByte = HexUtil.toByteArray(sign);
            return verifySignSHA1WithRSA(dataByte, signByte, publicKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean verifySignSHA256withRSA(byte[] data, byte[] sign,PublicKey publicKey){
    	return verifySign( data, sign, publicKey, "SHA256withRSA");
    }
    
    /**
     * 
     * @param data
     * @param sign  Hex字符串
     * @param publicKey
     * @return
     */
    public static boolean verifySignSHA256withRSA(String data, String sign, PublicKey publicKey) {
        try {
            byte[] dataByte = data .getBytes("UTF-8");
            byte[] signByte = HexUtil.toByteArray(sign);
            return verifySignSHA256withRSA(dataByte, signByte, publicKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 
     * @param data
     * @param sign   BASE64字符串
     * @param publicKey
     * @return
     */
    public static boolean verifySignSHA256withRSAWithBASE64(String data, String sign, PublicKey publicKey) {
        try {
            byte[] dataByte = data .getBytes("UTF-8");
            byte[] signByte = BASE64Util.decode(sign.getBytes());
            return verifySignSHA256withRSA(dataByte, signByte, publicKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    /**
     * 签名
     * @param data
     * @param key
     * @return
     */
    public static byte[] sign(byte[] data, PrivateKey key, String algorithm) {
        try {
            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(key);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 签名
     * @param data
     * @param key
     * @return Hex字符串
     */
    public static String signMD5withRSA(String data, PrivateKey key) {
    	
        try {
            byte[] dataByte = data.getBytes("UTF-8");
            byte[] signByte = sign(dataByte, key, "MD5withRSA");
            return (signByte == null) ? null : HexUtil.toHex(signByte);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String signSHA1WithRSA(String data, PrivateKey key) {
    	
        try {
            byte[] dataByte = data.getBytes("UTF-8");
            byte[] signByte = sign(dataByte, key, "SHA1WithRSA");
            return (signByte == null) ? null : HexUtil.toHex(signByte);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 
     * @param data
     * @param key
     * @return
     *    返回Hex字符串
     */
    public static String signSHA256WithRSA(String data, PrivateKey key) {
    	
        try {
            byte[] dataByte = data.getBytes("UTF-8");
            byte[] signByte = sign(dataByte, key, "SHA256WithRSA");
            return (signByte == null) ? null : HexUtil.toHex(signByte);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 
     * @param data
     * @param key
     * @return
     *    返回Base64字符串
     */
    public static String signSHA256WithRSA2BASE64(String data, PrivateKey key) {
    	
        try {
            byte[] dataByte = data.getBytes("UTF-8");
            byte[] signByte = sign(dataByte, key, "SHA256WithRSA");
            return (signByte == null) ? null : new String(BASE64Util.encode(signByte));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 加密
     * @param data
     * @param key
     * @return
     */
    public static byte[] encrypt(byte[] data, Key key, String padding) {
        try {
            //Cipher cipher = Cipher.getInstance(padding, PROVIDER);
        	Cipher cipher = Cipher.getInstance(padding);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 加密
     * @param data
     * @param key
     * @return 返回Hex字符串
     */
    public static String encrypt(String data, Key key, String padding) {
        try {
        	byte[] ret = encrypt(data.getBytes("UTF-8"),key, padding);
            return ret == null ? null : HexUtil.toHex(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] decrypt(byte[] data, Key key, String padding) {
        try {
            //final Cipher cipher = Cipher.getInstance(padding, PROVIDER);
        	Cipher cipher = Cipher.getInstance(padding);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param data Hex字符串
     * @param key
     * @return
     */
    public static String decrypt(String data, Key key, String padding) {
        try {
        	byte[] dataBytes = HexUtil.toByteArray(data);
        	byte[] ret = decrypt(dataBytes,key,padding);
            return ret == null ? null : new String(ret,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 加密<p>
     * 每117个字节为一组加密数据
     * @param data
     * @param key
     * @param padding
     * @return
     * @throws Exception
     */
    public static byte[] encode(byte[] data, Key key, String padding) {
		try {
			//Cipher cipher = Cipher.getInstance(padding, PROVIDER);
			Cipher cipher = Cipher.getInstance(padding);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			int dataLen = ((RSAPublicKey)key).getModulus().bitLength() / 8 - 11;
			// 1024是117
	        byte[] encodedByteArray = new byte[]{};
	        for (int i = 0; i < data.length; i += dataLen) {
	            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i+117));
	            encodedByteArray = ArrayUtils.addAll(encodedByteArray, doFinal);
	        }
	        return encodedByteArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    
    public static byte[] encode(String encodeString, Key key, String padding){
		try {
			byte[] data = encodeString.getBytes("UTF-8");
			return encode(data, key, padding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String encodeToBase64(String data, Key key, String padding) {
    		byte[] ret = encode(data,key, padding);
            return ret == null ? null : new String(Base64.encode(ret));
    }

    /**
     * 解密<p>
     * 每组数据128个字节进行解密
     */
    public static String decode(byte[] data, Key key, String padding) {
		try {
			//Cipher cipher = Cipher.getInstance(padding, PROVIDER);
			Cipher cipher = Cipher.getInstance(padding);
			cipher.init(Cipher.DECRYPT_MODE, key);
	        StringBuilder sb = new StringBuilder();
			int dataLen = ((RSAPublicKey)key).getModulus().bitLength() / 8 ;

	        for (int i = 0; i < data.length; i += dataLen) {
	            sb.append(cipher.doFinal(ArrayUtils.subarray(data, i, i + dataLen)));
	        }
	        return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * 解密
     *
     * @param data  Base64编码的字符串
     * @param key
     * @return
     */
    public static String decodeFromBase64(String data, Key key, String padding) {
    	return decode(Base64.decode(data.getBytes()), key, padding);
    }
    
    /**
     * 将Base64编码的公钥字符串转换成PublicKey对象
     */
    public static PublicKey getPublicKey(String base64EncodePublicKey) throws Exception {
        //KeyFactory keyf = KeyFactory.getInstance("RSA", PROVIDER);
    	KeyFactory keyf = KeyFactory.getInstance("RSA");
    	byte[] buffer = Base64.decodeBase64(base64EncodePublicKey.getBytes());
        X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(buffer);
        return keyf.generatePublic(pubX509);
    }
    
    public static PrivateKey getPrivateKey(String base64EncodePrivateKey) throws Exception {
        //KeyFactory keyf = KeyFactory.getInstance("RSA", PROVIDER);
    	byte[] buffer = Base64.decodeBase64(base64EncodePrivateKey.getBytes());
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(buffer);
    	KeyFactory keyf = KeyFactory.getInstance("RSA");
        return keyf.generatePrivate(priPKCS8);
    }
    
    /**
	 * 从.cer文件中获取公钥
	 */
	public static PublicKey getPublicKeyByCer(String path) {
		try {
			CertificateFactory cff = CertificateFactory.getInstance("X.509");
			FileInputStream fis = new FileInputStream(new File(path));
	        Certificate cf = cff.generateCertificate(fis);
	        return cf.getPublicKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	
	
	
	
	
	/**
	 * 从pem文件中获取公钥
	 * -----BEGIN PRIVATE KEY-----
	 * .......
	 * -----END PRIVATE KEY-----
	 * @param path
	 * @return
	 */
	public static PublicKey getPublicKeyByPem(String path){
        FileInputStream fin = null;
		try {
			fin = new FileInputStream(new File(path));
			
			BufferedReader br= new BufferedReader(new InputStreamReader(fin));
			
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            boolean begin = false;
            while((readLine= br.readLine())!=null){
            	if(readLine.equals("-----BEGIN PUBLIC KEY-----")){
            		begin=true;
            		continue;
            	}
            	if(begin){
            		sb.append(readLine).append("\r");
            	}
            	if(readLine.equals("-----END PUBLIC KEY-----")){
            		break;
            	}
            }
            
			return getPublicKey(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( fin != null){
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 从pem文件中获取私钥
	 * -----BEGIN PRIVATE KEY-----
	 * .......
	 * -----END PRIVATE KEY-----
	 * @param path
	 * @return
	 */
	public static PrivateKey getPrivateKeyByPem(String path){
        FileInputStream fin = null;
		try {
			fin = new FileInputStream(new File(path));
			
			BufferedReader br= new BufferedReader(new InputStreamReader(fin));
			
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            boolean begin = false;
            while((readLine= br.readLine())!=null){
            	if(readLine.equals("-----BEGIN PRIVATE KEY-----")){
            		begin=true;
            		continue;
            	}
            	if(begin){
            		sb.append(readLine).append("\r");
            	}
            	if(readLine.equals("-----END PRIVATE KEY-----")){
            		break;
            	}
            }
            
			return getPrivateKey(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( fin != null){
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 从.pfx文件中获取公钥
	 * @param password 没有密钥传入null
	 */
	public static PublicKey getPublicKey(String path, String password) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(path);
            char[] nPassword = null;
            if ( (password != null) && !("".equals(password.trim()))) {
                nPassword = password.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            Enumeration<String> enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = enumas.nextElement();
            }
            
            return ks.getCertificate(keyAlias).getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * 从.pfx文件中获取私钥
	 */
	public static PrivateKey getPrivateKey(String path, String password) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(path);

            char[] nPassword = null;
            if ( (password != null) && !("".equals(password.trim()))) {
                nPassword = password.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            Enumeration<String> enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()){ // we are readin just one certificate.
                keyAlias = enumas.nextElement();
            }

            return (PrivateKey) ks.getKey(keyAlias, nPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到密钥字符串（经过base64编码,有换行）
     */
	public static String getKeyStringWithLine(Key key) {
        return new BASE64Encoder().encode(key.getEncoded());
    }
	
	public static String getKeyString(Key key) {
        return new String(Base64.encodeBase64(key.getEncoded()));
    }
	
	public static String getKeyHexString(Key key) {
        return HexUtil.toHex(key.getEncoded());
    }

	/**
	 * 创建一组指定长度的RSA公私钥
	 * @param size   1024  2048
	 * @throws Exception
	 */
	public static Map<String, Key> createKeyPairs(int size) throws Exception {
        //KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", PROVIDER);
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(size, new SecureRandom());
        //generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PublicKey publicKey   = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();
        
        Map<String, Key> map = new HashMap<>();
        map.put("publicKey", publicKey);
        map.put("privateKey", privateKey);
        return map;
    }
	
	/**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，
     * 如Android默认是RSA/None/NoPadding】
     *
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return
     */
    public static PublicKey createPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return
     */
    public static PrivateKey createPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
	@SuppressWarnings("unused")
	private static byte[] buildPKCS8Key(String privateKey) throws IOException {
        if (privateKey.contains("-----BEGIN PRIVATE KEY-----")) {
            return Base64.decode(privateKey.replaceAll("-----\\w+ PRIVATE KEY-----", "").getBytes());
        } else if (privateKey.contains("-----BEGIN RSA PRIVATE KEY-----")) {
            final byte[] innerKey = Base64.decode(privateKey.replaceAll("-----\\w+ RSA PRIVATE KEY-----", "").getBytes());
            final byte[] result = new byte[innerKey.length + 26];
            System.arraycopy(Base64.decode("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKY="), 0, result, 0, 26);
            System.arraycopy(BigInteger.valueOf(result.length - 4).toByteArray(), 0, result, 2, 2);
            System.arraycopy(BigInteger.valueOf(innerKey.length).toByteArray(), 0, result, 24, 2);
            System.arraycopy(innerKey, 0, result, 26, innerKey.length);
            return result;
        } else {
            return Base64.decode(privateKey.getBytes());
        }
    }
	
    public static void main(String[] args) throws Exception {
    	/*
    	PublicKey publicKey1 = getPublicKeyByCer("E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\helipay.cer");
    	System.out.println(getKeyString(publicKey1));
    	
    	PublicKey publicKey = getPublicKey("E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\C1800321766.pfx","1234qwer");
    	System.out.println(getKeyString(publicKey));
    	PrivateKey privateKey = getPrivateKey("E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\C1800321766.pfx","1234qwer");
    	System.out.println(getKeyString(privateKey));
    	*/
    	
    	/*
    	Map<String, Key> map = createKeyPairs(2048);
    	PublicKey publicKey = (PublicKey)map.get("publicKey");
    	PrivateKey privateKey = (PrivateKey)map.get("privateKey");
    	System.out.println(getKeyString(publicKey));
    	System.out.println(getKeyString(privateKey));
    	*/
    	
    	PublicKey publicKey = getPublicKeyByPem("E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\880000199_public_key.pem");
    	PrivateKey privateKey = getPrivateKeyByPem("E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\880000199_private_key.pem");
   	
    	/*
    	String data="welcome";
    	String sign = signMD5withRSA( data, privateKey);
    	System.out.println("sign:"+sign);
    	System.out.println(verifySignMD5withRSA(data, sign, publicKey));
    	*/
    	
    	/*
    	String data="welcome";
    	String sign = signSHA1WithRSA( data, privateKey);
    	System.out.println("sign:"+sign);
    	System.out.println(verifySignSHA1WithRSA(data, sign, publicKey));
    	*/
    	
    	/*
    	String data="welcome";
    	String sign = signSHA256WithRSA( data, privateKey);
    	System.out.println("sign:"+sign);
    	System.out.println(verifySignSHA256withRSA(data, sign, publicKey));
    	*/
    	
    	String data="welwelcomewelco设立了商量商量商量商量了omewelcomewelcomewelcomewelcomewelcomewelcomecome";
    	String sign = signSHA256WithRSA2BASE64( data, privateKey);
    	System.out.println("sign:"+sign);
    	System.out.println(verifySignSHA256withRSAWithBASE64(data, sign, publicKey));
    	
    	/*
    	String data="welcome";
        String encrypt = encrypt( data, publicKey, RSAECBPKCS1PADDING);
        System.out.println(encrypt);
        System.out.println(decrypt(encrypt, privateKey, RSAECBPKCS1PADDING));
        */
    }
}
