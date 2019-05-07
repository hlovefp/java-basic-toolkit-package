package com.hfp.util.security;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES是一种对称加密算法，加密和解密使用相同密钥的算法。
 * 因为DES使用56位密钥，以现代计算能力，24小时内即可被破解，近些年使用越来越少。
 * 虽然如此，在某些简单应用中，我们还是可以使用DES加密算法。
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 */
public class DESUtil {


    // static {
    // 	Security.addProvider(new com.sun.crypto.provider.SunJCE());
    // }

    /**
     * 生成密钥
     * @param algorithm
     *    DES        生产密钥长度是8
     *    Blowfish   生产密钥长度是16
     *    DESede     生产密钥长度是24
     */
    public static byte[] getKey(String algorithm) {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
			keygen.init(new SecureRandom());
	        return keygen.generateKey().getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * 加密
     */
    public static void encode(InputStream is, OutputStream os, byte[] key, String algorithm){
    	SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm.split("/")[0]);
    	try{
	        Cipher cipher = Cipher.getInstance(algorithm);
	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
	        CipherInputStream cin = new CipherInputStream(is, cipher);
	        int readLength=0;
	        byte[] buffer = new byte[1024];
	        while ((readLength = cin.read(buffer)) != -1) {
	        	System.out.print(HexUtil.toHex(buffer,readLength));
	            os.write(buffer, 0, readLength);
	        }
	        
	        os.close();
	        cin.close();
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }


    /**
     *  解密
     */
    public static void decode(InputStream is, OutputStream os, byte[] key, String algorithm)  {
    	try{
    		/*
	        // 生成指定秘密密钥算法的 SecretKeyFactory 对象。
	        SecretKeyFactory factroy = SecretKeyFactory.getInstance(algorithm);
	        // 根据提供的密钥规范（密钥材料）生成 SecretKey 对象,利用密钥工厂把DESKeySpec转换成一个SecretKey对象
	        DESKeySpec ks = new DESKeySpec(key);    // 创建一个 DESKeySpec 对象,指定一个 DES 密钥
	        SecretKey sk = factroy.generateSecret(ks);
	        */
	        SecretKeySpec sk = new SecretKeySpec(key, algorithm.split("/")[0]);
	        
	        // 生成一个实现指定转换的 Cipher 对象。Cipher对象实际完成加解密操作
	        Cipher c = Cipher.getInstance(algorithm);
	        
	        SecureRandom sr = new SecureRandom();   // DES算法要求有一个可信任的随机数源
	        c.init(Cipher.DECRYPT_MODE, sk, sr);   // 用密钥和随机源初始化此 cipher
	
	        CipherOutputStream cout = new CipherOutputStream(os, c);
	
	        byte[] buffer = new byte[1024];
	        int readLength=0;
	        while ((readLength = is.read(buffer)) != -1) {
	            cout.write(buffer, 0, readLength);
	        }
	        cout.close();
	        is.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public static void main(String[] args) throws Exception {
    	
    	/*
        System.out.println(HexUtil.toHex(getKey("DES")));
        System.out.println(HexUtil.toHex(getKey("DESede")));
        System.out.println(HexUtil.toHex(getKey("Blowfish")));
        */
        
        /*
        byte[] key = new String("abcdefgh").getBytes();
        System.out.println(HexUtil.toHex(key));
        encode(new FileInputStream("F:\\a.txt"), new FileOutputStream("F:\\b.txt"), key, "DES/ECB/PKCS5Padding"); // 结果同  "DES"
        // 123456789012345 => 21c60da534248bce5e6b576a919a6ad6
        decode(new FileInputStream("F:\\b.txt"), new FileOutputStream("F:\\c.txt"), key, "DES/ECB/PKCS5Padding");
        */
    	
    	/*
        byte[] key = new String("abcdefgh12345678abcdefgh").getBytes();
        System.out.println(HexUtil.toHex(key));
        encode(new FileInputStream("F:\\a.txt"), new FileOutputStream("F:\\b.txt"), key, "DESede/ECB/PKCS5Padding"); // 结果同  "DESede"  ==> 3DES
        // 123456789012345 => 05a804e9435a0a1edd02daf88f84fc7f
        decode(new FileInputStream("F:\\b.txt"), new FileOutputStream("F:\\c.txt"), key, "DESede/ECB/PKCS5Padding");
        */

    }
}
