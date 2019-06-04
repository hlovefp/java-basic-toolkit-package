package com.hfp.util.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.hfp.util.common.MapUtil;
import com.hfp.util.net.HttpSSLSocketFactory.TrustAnyHostnameVerifier;

public class HttpUtil {
	
	private static HttpURLConnection createConnect(String url, String method) throws IOException{
		URL urls = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) urls.openConnection();
		if("POST".equals(method)){
			httpURLConnection.setDoInput(true);    // 可读
			httpURLConnection.setDoOutput(true);   // 可写
			httpURLConnection.setRequestMethod("POST");
		} else{
			httpURLConnection.setRequestMethod("GET");
		}
		
		if ("https".equalsIgnoreCase(urls.getProtocol())) {
			HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
			//不验证https证书, 信任所有域名
			husn.setSSLSocketFactory(new HttpSSLSocketFactory());
			husn.setHostnameVerifier(new TrustAnyHostnameVerifier());//解决由于服务器证书问题导致HTTPS无法访问的情况
			
			/*
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new HttpSSLSocketFactory.TrustAllManager() }, new SecureRandom());
			husn.setSSLSocketFactory(sc.getSocketFactory());
			husn.setHostnameVerifier(new HttpSSLSocketFactory.TrustAnyHostnameVerifier());
			 */
			
			return husn;
		}
		
		return httpURLConnection;
	}
    /**
	 * 
	 * http 或 https GET请求方法
	 */
	public static String doGet(String url) {

		HttpURLConnection connection = null;
		BufferedReader in = null;
		try {
			connection = createConnect(url,"GET");
			
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setUseCaches(false);// 取消缓存
			
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			//connection.setRequestProperty("User-Agent", "t");

			connection.connect();

			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}

			StringBuffer result = new StringBuffer();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			String readLine = null;
			while ((readLine = in.readLine()) != null) {
				result.append(readLine);
			}			
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if( null != in )
					in.close();
				if (null != connection) {
					connection.disconnect();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * HTTPS协议GET请求方法,返回文件的二进制数据
	 */
	public static byte[] doGetFile(String url) {
		HttpURLConnection uc = null;
		InputStream inStream = null;
		try {
			uc = createConnect(url, "GET");
			uc.connect();
			inStream = uc.getInputStream(); // 获取文件流二进制数据
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream(); // 缓存
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			return outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(null != inStream)
					inStream.close();
				if (uc != null) {
					uc.disconnect();
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * http或https POST请求方法
	 */
	public static String doPost(String url, String params) {
		params = (params==null)?"":params;
		
		HttpURLConnection uc = null;
		BufferedReader in = null;
		try {
			uc = createConnect(url,"POST");
			
			uc.setConnectTimeout(5000);
			uc.setReadTimeout(5000);
			uc.setUseCaches(false);// 取消缓存
			
			//uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			uc.connect();
			
			DataOutputStream out = new DataOutputStream(uc.getOutputStream());
			out.write(params.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
			StringBuffer sb = new StringBuffer();
			String readLine = null;
			while ((readLine = in.readLine()) != null) {
				sb.append(readLine);
			}			
			return URLDecoder.decode(sb.toString(),"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in!=null)
					in.close();
				if (uc!=null)
					uc.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	
	
	/**
	 * 
	 * http获取https POST请求方法
	 */
	public static String doPost(String url, TreeMap<String, String> paramsMap) {
		return doPost(url, (null == paramsMap) ? null : MapUtil.map2FormString(paramsMap));
	}
	
	/**
	 * 
	 * HTTPS协议POST请求方法
	 */
	public static byte[] doPostFile(String url, String params) {

		HttpURLConnection uc = null;
		InputStream inStream = null;
		try {
			uc = createConnect(url, "POST");
			uc.setUseCaches(false);
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			uc.connect();
			
			DataOutputStream out = new DataOutputStream(uc.getOutputStream());
			out.write(params.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			inStream = uc.getInputStream(); // 获取文件流二进制数据
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			

			return outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(null != inStream)
					inStream.close();
				if (uc != null) {
					uc.disconnect();
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	
	


	/**
	 * http请求,支持http和https
	 * 
	 * @param method 请求方法GET/POST
	 * @param path  请求路径
	 * @param timeout  连接超时时间 默认为5000
	 * @param readTimeout  读取超时时间 默认为5000
	 * @param data  数据
	 * @return
	 */
	public static String httpClient(String method, String url, String data, int timeout, int readTimeout) {
		HttpURLConnection conn = null;
		InputStream input = null;
		try{
			conn = getConnection(new URL(url));
			conn.setRequestMethod(method);
			conn.setConnectTimeout(timeout == 0 ? 5000 : timeout);
			conn.setReadTimeout(readTimeout == 0 ? 5000 : readTimeout);
			
			if (data != null && !"".equals(data)) {
				OutputStream output = conn.getOutputStream();
				output.write(data.getBytes("UTF-8"));
				output.flush();
				output.close();
			}
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				input = conn.getInputStream();
				StringBuffer buffer = new StringBuffer();
				int len = 0;
				byte[] array = new byte[1024];
				while ((len = input.read(array)) != -1) {
					buffer.append(new String(array, 0, len, "UTF-8"));
				}
				return buffer.toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			try{
				if(null!=input)
					input.close();
				if( null != conn )
					conn.disconnect();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 根据url的协议选择对应的请求方式 
	 * @param method 请求的方法
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection getConnection(URL url) throws Exception {
		HttpURLConnection conn = null;
		if ("https".equals(url.getProtocol())) {
			SSLContext context = SSLContext.getInstance("SSL", "SunJSSE");
			context.init(new KeyManager[0], 
					new TrustManager[] { new HttpSSLSocketFactory.TrustAllManager() },
					new SecureRandom());

			HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
			connHttps.setSSLSocketFactory(context.getSocketFactory());
			connHttps.setHostnameVerifier(new HttpSSLSocketFactory.TrustAnyHostnameVerifier());  // 全部信任
			conn = connHttps;
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		return conn;
	}
	
	
	public static void main(String[] args) {
		//System.out.println(doGet("http://www.baidu.com"));
		//System.out.println(doGet("https://www.baidu.com"));
		System.out.println(httpClient("GET", "http://www.baidu.com", null, 0, 0));
	}
}
