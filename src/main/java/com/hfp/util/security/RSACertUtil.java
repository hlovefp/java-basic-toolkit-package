package com.hfp.util.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

public class RSACertUtil {
	
	/**
	 * 将证书文件读取为证书存储对象：证书文件类型可为：JKS（.keystore等），PKCS12（.pfx）
	 * 
	 * @param keyfile  证书文件名
	 * @param keypwd   证书密码
	 * @param type     证书类型  JKS  PKCS12
	 * @return 证书对象
	 */
	private static KeyStore getKeyStore(String keyfile, String keypwd, String type) {
		try {
			KeyStore keyStore = null;
			if ("JKS".equals(type)) {
				keyStore = KeyStore.getInstance(type);
			} else if ("PKCS12".equals(type)) {
				//Security.insertProviderAt(new BouncyCastleProvider(), 1);
				//Security.addProvider(new BouncyCastleProvider());
				keyStore = KeyStore.getInstance(type);
			}
			FileInputStream fis = new FileInputStream(keyfile);
			char[] nPassword = (null == keypwd || "".equals(keypwd.trim())) ? null : keypwd.toCharArray();
			keyStore.load(fis, nPassword);
			fis.close();
			return keyStore;
		} catch (Exception e) {
			//if (Security.getProvider("BC") == null) {
				//logger.info("BC Provider not installed.");
			//}
			//logger.error("Fail: load privateKey certificate", e);
		}
		return null;
	}
	
	/**
	 * 从.pfx文件中获取公钥
	 * @param password 没有密钥传入null
	 */
	public static PublicKey getPublicKey(String path, String password) {
        try {
        	/*
            //KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(path);
            char[] nPassword = null;
            if ( (password != null) && !("".equals(password.trim()))) {
                nPassword = password.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            */
        	KeyStore ks = getKeyStore(path, password, "PKCS12");

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
        	/*
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(path);
            char[] nPassword = null;
            if ( (password != null) && !("".equals(password.trim()))) {
                nPassword = password.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            */
            KeyStore ks = getKeyStore(path, password, "PKCS12");
            Enumeration<String> enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()){ // we are readin just one certificate.
                keyAlias = enumas.nextElement();
            }
            return (PrivateKey) ks.getKey(keyAlias, password.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 从.cer文件中获取公钥
	 */
	public static PublicKey getPublicKeyByCer(String path) {
		try {
			CertificateFactory cff = CertificateFactory.getInstance("X.509");
			FileInputStream fis = new FileInputStream(new File(path));
	        return cff.generateCertificate(fis).getPublicKey();
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
            
			return RSAUtil.getPublicKey(sb.toString());
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
            
			return RSAUtil.getPrivateKey(sb.toString());
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
	
	
	
	public static void main(String[] args){
    	PublicKey publicKey = getPublicKeyByPem("E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\880000199_public_key.pem");
    	PrivateKey privateKey = getPrivateKeyByPem("E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\880000199_private_key.pem");
   	
	}
}
