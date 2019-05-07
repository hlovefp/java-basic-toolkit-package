package com.hfp.util.net;

import javax.net.ssl.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.hfp.util.common.MapUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class HttpsUtil {

	private static class MyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		public void checkServerTrusted(X509Certificate[] chain, String authType)  throws CertificateException {
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
		HttpsURLConnection httpsURLConnection = null;
		BufferedReader in = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new java.security.SecureRandom());
			URL urls = new URL(url);
			
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
	 * Content-Type = application/x-www-form-urlencoded
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
	
	private static CloseableHttpClient createSSLClientDefault() {
		try {
			HttpClientBuilder b = HttpClientBuilder.create();
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslSocketFactory).build();
			PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			b.setConnectionManager(connMgr);
			return b.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}
	
	
	
	/**
	 * POST方法，application/json
	 * @param url    http和https都支持
	 * @param body
	 * @param header
	 * @return
	 * @throws Exception
	 */
	public static String httpsPostWithJSON(String url, String jsonStr, Map<String, String> header) {
		HttpPost httpPost = new HttpPost(url);

		StringEntity entity = new StringEntity(jsonStr.toString(), "utf-8");// 解决中文乱码问题
		if (header != null) {
			for (String key : header.keySet()) {
				httpPost.addHeader(key, header.get(key));
			}
		}
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		
		httpPost.setEntity(entity);
		
		CloseableHttpClient client = createSSLClientDefault();
		
		String respContent = null;
		try{
			HttpResponse resp = client.execute(httpPost);
			if (resp.getStatusLine().getStatusCode() == 200) {
				HttpEntity he = resp.getEntity();
				respContent = EntityUtils.toString(he, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return respContent;
	}
	
	/**
	 * POST方法，application/json
	 * @param url    http和https都支持
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static String httpsPostWithJSON(String url, String jsonStr) throws Exception {
		return httpsPostWithJSON(url, jsonStr, null);
	}
	
	/**
	 * Get  ?key=value&key=value
	 * @param url   http和https都支持
	 * @param bodyMap
	 * @param headerMap
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 */
	public static String httpsGet(String url, Map<String, String> bodyMap, Map<String, String> headerMap) throws Exception {
		CloseableHttpClient httpClient = createSSLClientDefault();

		HttpGet get = new HttpGet(new URI(url + "?" + MapUtil.map2FormString(bodyMap)));
		if (headerMap != null) {
			for (String key : headerMap.keySet()) {
				get.addHeader(key, headerMap.get(key));
			}
		}
		get.addHeader("Content-Type", "application/json");
		get.addHeader("Accept-Charset", "utf-8");
		
		String respContent = null;
		try {
			CloseableHttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity he = response.getEntity();
				respContent = EntityUtils.toString(he, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return respContent;
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
	public static String httpClient(String method, String url, int timeout, int readTimeout, String data) throws Exception {
		String result = "";

		HttpURLConnection conn = getConnection(new URL(url));
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
			InputStream input = conn.getInputStream();
			StringBuffer buffer = new StringBuffer();
			int len = 0;
			byte[] array = new byte[1024];
			while ((len = input.read(array)) != -1) {
				buffer.append(new String(array, 0, len, "UTF-8"));
			}
			result = buffer.toString();
			input.close();
		}
		
		conn.disconnect();

		return result;
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
			SSLContext context = null;

			context = SSLContext.getInstance("SSL", "SunJSSE");
			context.init(new KeyManager[0], 
					new TrustManager[] { new MyTrustManager() },
					new SecureRandom());

			HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
			connHttps.setSSLSocketFactory(context.getSocketFactory());
			connHttps.setHostnameVerifier(new MyHostnameVerifier());  // 全部信任
			conn = connHttps;
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		return conn;
	}
	
	public static void main(String[] args){
		
	}
}
