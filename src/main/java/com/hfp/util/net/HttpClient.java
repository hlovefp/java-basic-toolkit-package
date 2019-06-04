package com.hfp.util.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import com.hfp.util.common.MapUtil;
import com.hfp.util.net.HttpSSLSocketFactory.TrustAnyHostnameVerifier;

public class HttpClient {
	private URL url;                // 目标地址: http或https
	private int connectionTimeout;  // 通信连接超时时间
	private int readTimeOut;        // 通信读超时时间
	private String result;          // 通信结果

	public String getResult() {
		return result;
	}

	/**
	 * 构造函数
	 * @param url 目标地址
	 * @param connectionTimeout HTTP连接超时时间,单位毫秒
	 * @param readTimeOut HTTP读写超时时间,单位毫秒
	 * @throws MalformedURLException 
	 */
	public HttpClient(String url, int connectionTimeout, int readTimeOut) throws MalformedURLException {
		try {
			this.url = new URL(url);
			this.connectionTimeout = connectionTimeout;
			this.readTimeOut = readTimeOut;
		} catch (MalformedURLException e) {
			throw e;
		}
	}

	/**
	 * 发送信息到服务端
	 * @param data
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public int doPost(Map<String, String> data, String encoding) throws Exception {
		try {
			HttpURLConnection httpURLConnection = createConnection(encoding, "POST");
			if (null == httpURLConnection) {
				throw new Exception("Create httpURLConnection Failure");
			}
			String sendData = MapUtil.map2FormString(data);
			this.sendMsg(httpURLConnection, sendData, encoding);
			this.result = this.recvMsg(httpURLConnection, encoding);
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 发送信息到服务端 GET方式
	 * @param data
	 * @param encoding
	 * @return  HTTP 状态码  200 404
	 * @throws Exception
	 */
	public int doGet(String encoding) throws Exception {
		try {
			HttpURLConnection httpURLConnection = createConnection(encoding,"GET");
			if(null == httpURLConnection){
				throw new Exception("创建联接失败");
			}
			this.result = this.recvMsg(httpURLConnection, encoding);
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * HTTP Post发送消息
	 *
	 * @param connection
	 * @param message
	 * @throws IOException
	 */
	private void sendMsg(final URLConnection connection, String message, String encoder) 
			throws Exception {
		PrintStream out = null;
		try {
			connection.connect();
			out = new PrintStream(connection.getOutputStream(), false, encoder);
			out.print(message);
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 读取Response消息
	 *
	 * @param connection
	 * @param CharsetName
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private String recvMsg(final HttpURLConnection connection, String encoding)
			throws URISyntaxException, IOException, Exception {
		InputStream in = null;
		StringBuilder sb = new StringBuilder(1024);
		BufferedReader br = null;
		try {
			if (200 == connection.getResponseCode()) {
				in = connection.getInputStream();
				sb.append(new String(read(in), encoding));
			} else {
				in = connection.getErrorStream();
				sb.append(new String(read(in), encoding));
			}
			return sb.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != br) {
				br.close();
			}
			if (null != in) {
				in.close();
			}
			if (null != connection) {
				connection.disconnect();
			}
		}
	}
	
	public static byte[] read(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int length = 0;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while ((length = in.read(buf, 0, buf.length)) > 0) {
			bout.write(buf, 0, length);
		}
		bout.flush();
		return bout.toByteArray();
	}
	
	/**
	 * 创建连接
	 *
	 * @return
	 * @throws IOException 
	 */
	private HttpURLConnection createConnection(String encoding, String method) throws IOException {
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			throw e;
		}
		httpURLConnection.setConnectTimeout(this.connectionTimeout);// 连接超时时间
		httpURLConnection.setReadTimeout(this.readTimeOut);// 读取结果超时时间
		httpURLConnection.setUseCaches(false);// 取消缓存
		httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=" + encoding);
		
		if("POST".equals(method)){
			httpURLConnection.setDoInput(true); // 可读
			httpURLConnection.setDoOutput(true); // 可写
			httpURLConnection.setRequestMethod("POST");
		} else{
			httpURLConnection.setRequestMethod("GET");
		}
		
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			System.out.println("https");
			HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
			//不验证https证书
			husn.setSSLSocketFactory(new HttpSSLSocketFactory());
			husn.setHostnameVerifier(new TrustAnyHostnameVerifier());//解决由于服务器证书问题导致HTTPS无法访问的情况
			return husn;
		}
		
		return httpURLConnection;
	}
	
	public static void main(String[] args) throws Exception {
		//HttpClient client = new HttpClient("http://www.baidu.com", 1000, 1000);
		HttpClient client = new HttpClient("https://www.baidu.com", 1000, 1000);
		System.out.println(client.doGet("UTF-8"));
		System.out.println(client.getResult());
	}
}
