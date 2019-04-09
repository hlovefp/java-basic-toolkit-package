package com.hfp.net;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class HttpsUtil {

	private static class MyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class MyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	/** 
	 * HTTPS协议GET请求方法
	 */
	public static String httpsGet(String url) {
		StringBuffer result = new StringBuffer();
		URL urls;
		HttpsURLConnection httpsURLConnection = null;
		BufferedReader in = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new java.security.SecureRandom());
			urls = new URL(url);
			
			httpsURLConnection = (HttpsURLConnection) urls.openConnection();
			httpsURLConnection.setSSLSocketFactory(sc.getSocketFactory());
			httpsURLConnection.setHostnameVerifier(new MyHostnameVerifier()); // 信任所有域名
			httpsURLConnection.setRequestMethod("GET");
			
			httpsURLConnection.connect();
			
			in = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), "utf-8"));
			String readLine = "";
			while ((readLine = in.readLine()) != null) {
				result.append(readLine);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpsURLConnection != null) {
				httpsURLConnection.disconnect();
			}
		}
		return result.toString();
	}

	/**
	 * HTTPS协议GET请求方法,返回文件的二进制数据
	 */
	public static byte[] httpsGetFile(String url) {
		HttpsURLConnection uc = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new java.security.SecureRandom());
			URL urls = new URL(url);
			uc = (HttpsURLConnection) urls.openConnection();
			uc.setSSLSocketFactory(sc.getSocketFactory());
			uc.setHostnameVerifier(new MyHostnameVerifier());
			uc.setRequestMethod("GET");
			
			uc.connect();
			
			InputStream inStream = uc.getInputStream(); // 获取文件流二进制数据
			ByteArrayOutputStream outStream = new ByteArrayOutputStream(); // 缓存
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			inStream.close();
			return outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (uc != null) {
				uc.disconnect();
			}
		}
		return null;
	}

	/**
	 * 
	 * HTTPS协议POST请求方法
	 */
	public static String httpsPost(String url, String params) {

		StringBuffer sb = new StringBuffer();
		HttpsURLConnection uc = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new java.security.SecureRandom());
			URL urls = new URL(url);
			uc = (HttpsURLConnection) urls.openConnection();
			uc.setSSLSocketFactory(sc.getSocketFactory());
			uc.setHostnameVerifier(new MyHostnameVerifier());
			uc.setRequestMethod("POST");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			uc.connect();
			
			DataOutputStream out = new DataOutputStream(uc.getOutputStream());
			out.write(params.getBytes());
			out.flush();
			out.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String readLine = "";
			while ((readLine = in.readLine()) != null) {
				sb.append(readLine);
			}
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (uc != null) {
				uc.disconnect();
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * HTTPS协议POST请求方法
	 */
	public static byte[] httpsPostFile(String url, String params) {

		HttpsURLConnection uc = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new java.security.SecureRandom());
			URL urls = new URL(url);
			uc = (HttpsURLConnection) urls.openConnection();
			uc.setSSLSocketFactory(sc.getSocketFactory());
			uc.setHostnameVerifier(new MyHostnameVerifier());
			uc.setRequestMethod("POST");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			uc.connect();
			
			DataOutputStream out = new DataOutputStream(uc.getOutputStream());
			out.write(params.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			InputStream inStream = uc.getInputStream(); // 获取文件流二进制数据
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			inStream.close();

			return outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (uc != null) {
				uc.disconnect();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * HTTPS协议POST请求添加参数的封装方
	 */
	private static String getParamStr(TreeMap<String, String> map) {
		StringBuilder param = new StringBuilder();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while ( it.hasNext() ) {
			Map.Entry<String, String> e = it.next();
			param.append("&")
				 .append(e.getKey())
				 .append("=")
				 .append(e.getValue());
		}
		return param.toString().substring(1);
	}

	/**
	 * 
	 * HTTPS协议POST请求方法
	 */
	@SuppressWarnings("deprecation")
	public static String httpsPost(String url, TreeMap<String, String> paramsMap) {

		String params = null == paramsMap ? null : getParamStr(paramsMap); // key=value字符串

		StringBuffer sb = new StringBuffer();
		HttpsURLConnection uc = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new java.security.SecureRandom());
			URL urls = new URL(url);
			uc = (HttpsURLConnection) urls.openConnection();
			uc.setSSLSocketFactory(sc.getSocketFactory());
			uc.setHostnameVerifier(new MyHostnameVerifier());
			uc.setRequestMethod("POST");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			uc.connect();
			
			DataOutputStream out = new DataOutputStream(uc.getOutputStream());
			out.write(params.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
			String readLine = "";
			while ((readLine = in.readLine()) != null) {
				sb.append(readLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (uc != null) {
				uc.disconnect();
			}
		}
		return URLDecoder.decode(sb.toString());
	}

	/**
	 * 
	 * HTTPS协议POST请求方法
	 */
	public static byte[] httpsPostFile(String url, TreeMap<String, String> paramsMap) {

		String params = null == paramsMap ? null : getParamStr(paramsMap); // key=value字符串

		HttpsURLConnection uc = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new java.security.SecureRandom());
			URL urls = new URL(url);
			uc = (HttpsURLConnection) urls.openConnection();
			uc.setSSLSocketFactory(sc.getSocketFactory());
			uc.setHostnameVerifier(new MyHostnameVerifier());
			uc.setRequestMethod("POST");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			uc.connect();
			
			DataOutputStream out = new DataOutputStream(uc.getOutputStream());
			out.write(params.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			InputStream inStream = uc.getInputStream(); // 获取文件流二进制数据
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			inStream.close();
			
			return outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (uc != null) {
				uc.disconnect();
			}
		}
		return null;
	}
	
	public static void main(String[] args){
		
	}
}
