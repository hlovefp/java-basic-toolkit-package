/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28     SSLSocket 链接工具类(用于https)
 * =============================================================================
 */
package com.hfp.util.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
/**
 * 
 * @ClassName BaseHttpSSLSocketFactory
 * @Description 忽略验证ssl证书
 * @date 2016-7-22 下午4:10:14
 *
 */
public class HttpSSLSocketFactory extends SSLSocketFactory {
	private SSLContext getSSLContext() {
		return createEasySSLContext();
	}
	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
	}
	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}
	public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
	}
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}
	public Socket createSocket(Socket arg0, String arg1, int arg2, boolean arg3) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}

	private SSLContext createEasySSLContext() {
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[] { new TrustAllManager() }, null);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String[] getSupportedCipherSuites() {
		return null;
	}
	public String[] getDefaultCipherSuites() {
		return null;
	}

	public static class TrustAllManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
			//return new X509Certificate[] {};
		}
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException{
		}
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException{
		}
	}

	/**
	 * 解决由于服务器证书问题导致HTTPS无法访问的情况 PS:HTTPS hostname wrong: should be <localhost>
	 */
	public static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;   //直接返回true
		}
	}
}
