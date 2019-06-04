package com.hfp.util.net;

import java.io.File;
import java.io.IOException;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.TlsVersion;

public class OkHttpUtils {
	//private static final OkHttpClient client = new OkHttpClient();
	private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.SECONDS)  // 默认 10s
			.readTimeout(10, TimeUnit.SECONDS)
			.writeTimeout(10, TimeUnit.SECONDS)
			.build();
	

	
	/* 
	//响应缓存
	int cacheSize = 10 * 1024 * 1024; // 10M   缓存的大小限制
	Cache cache = new Cache( new File(cacheDirectory), cacheSize); // 缓存目录来进行读取和写入
	private static final OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).build();
	Response response = client.newCall(request).execute()
	response.body().string()
	response.cacheResponse()
	response.networkResponse()
	 */
	
	/*
	// 认证
	private static final String credential = Credentials.basic("jesse", "password1");
	private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
		.authenticator(new Authenticator() {
			public Request authenticate(Route route, Response response) throws IOException {
				//System.out.println("Authenticating for response: " + response);
				//System.out.println("Challenges: " + response.challenges());
				return response.request().newBuilder()
						.header("Authorization", credential).build();
				}
			})
			.build();
	// Response response = client.newCall(request).execute();
	// credential.equals(response.request().header("Authorization"))
	*/
	
	/*
	// 拦截器    (application interception)  (network interception)
	              request                    request
	Application <--------->  OKHttp Core   <--------->   WebSite
	               reponse   ( 包含Cache )     reponse
	
	OkHttpClient client = new OkHttpClient.Builder()
	.addInterceptor(new LoggingInterceptor())          // 应用程序拦截器，重定向只会打印一次日志，且请求和响应的url不同
	//.addNetworkInterceptor(new LoggingInterceptor()) // 重定向会打印多次请求的日志
	.build();
	
	class LoggingInterceptor implements Interceptor {
		public Response intercept(Interceptor.Chain chain) throws IOException {
			Request request = chain.request();

			long t1 = System.nanoTime();
			logger.info(String.format("Sending request %s on %s%n%s",
							request.url(), chain.connection(), request.headers()));

			Response response = chain.proceed(request);

			long t2 = System.nanoTime();
			logger.info(String.format("Received response for %s in %.1fms%n%s",
							response.request().url(), (t2 - t1) / 1e6d, response.headers()));

			return response;
		}
	}
	 */
	
	private static String doPost(String url, RequestBody requestBody, Map<String, String> headerMap) throws IOException{
		Request.Builder builder = new Request.Builder().url(url);
		for( Map.Entry<String, String> entry: headerMap.entrySet()){
			//addHeader "Accept" 两次,value值拼接在一起，header则会直接覆盖
			//builder..header("Accept", "application/json; q=0.5, application/vnd.github.v3+json")
			//builder..addHeader("Accept", "application/json; q=0.5")
			//builder..addHeader("Accept", "application/vnd.github.v3+json")
			builder.addHeader( entry.getKey(), entry.getValue());
		}
				
		Request request = builder.post(requestBody).build();

		Call call = okHttpClient.newCall(request);
		Response response = call.execute();
				
		if (!response.isSuccessful()) {
			throw new IOException("Unexpected code " + response);
		}
				
		//System.out.println( response.header("Server") );
		//Headers headers = response.headers()
		//for (int i = 0; i < headers.size(); i++) {
		//	System.out.println( headers.name(i) + ": " + headers.value(i));
		//}

		return response.body().string();
	}
	
	private static String doPost(String url, RequestBody requestBody) throws IOException{

		/* 每个指定超时时间
		 * OkHttpClient copy = okHttpClient.newBuilder()
		 * 	.readTimeout( timeout, TimeUnit.MILLISECONDS)
		 *  .build();
		 */
		Request request = new Request.Builder().url(url).post( requestBody ).build();

		// 同步获取
		Response response = okHttpClient.newCall(request).execute();  
		if (!response.isSuccessful()) {
			throw new IOException("Unexpected code " + response);
		}
		return response.body().string();

		/*
		// 异步获取
		client.newCall(request).enqueue(new Callback() {
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
			}
			public void onResponse(Call call, Response response) throws IOException {
				if (!response.isSuccessful()){
					throw new IOException("Unexpected code " + response);
				}
				// response.body().string()
			}
		});
		*/
	}

	/**
	 * @param url
	 * @param content
	 * @param contentType   "application/json; charset=utf-8"
	 * @return
	 * @throws IOException 
	 */
	public static String doPost(String url, String content, String contentType) throws IOException{
		RequestBody requestBody = RequestBody.create( MediaType.parse(contentType), content);
		/*
		RequestBody requestBody = new RequestBody() {
			public void writeTo(BufferedSink sink) throws IOException {
				sink.writeUtf8("Releases\n");
				sink.writeUtf8("-------\n\n");
				sink.writeUtf8(" * _1.0_ May 6, 2013\n");
				sink.writeUtf8(" * _1.1_ June 15, 2013\n");
			}
			public MediaType contentType() {
				return MediaType.parse("text/x-markdown; charset=utf-8");
			}
		};
		*/
		return doPost(url, requestBody);
	}
	
	public static String doPostForm(String url, Map<String, String> map) throws IOException{
		FormBody.Builder builder = new FormBody.Builder();
		for( Map.Entry<String, String> entry: map.entrySet()){
			builder.add( entry.getKey(), entry.getValue());
		}
		return doPost(url, builder.build());
	}
	

	/**
	 * doPostFile(url, "/website/static/logo-square.png", "image/png");
	 * @param url
	 * @param filePath
	 * @param contentType
	 * @return
	 * @throws IOException
	 */
	public static String doPostFile(String url, String filePath, String contentType) throws IOException{
		MediaType mediaType = MediaType.parse(contentType);
		
		String fileName = filePath.substring(filePath.lastIndexOf("/")); // "logo-square.png"
		String name = contentType.split("/")[0];  // "image"
		
		MultipartBody.Builder builder = new MultipartBody.Builder();
		builder.setType(MultipartBody.FORM);
		//builder.addFormDataPart("title", "Square Logo");
		builder.addFormDataPart( name, fileName, RequestBody.create( mediaType, new File(filePath)));

		return doPost(url, builder.build());
	}
	
	/*
	 * Post markdown文档
	 */
	public static String doPostMarkdownFile(String url, String filePath) throws IOException{
		File file = new File( filePath );
		MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
		RequestBody requestBody = RequestBody.create( mediaType, file);
		return doPost(url, requestBody);
	}

	public static String doGet(String url) throws IOException{
		Request request = new Request.Builder().url(url).build();
		Response response = okHttpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			throw new IOException("Unexpected code " + response);
		}
		return response.body().string();
	}
	
	/**
	 * 使用OkHttp,post发送 application/json 格式数据，接收json格式数据
	 * 请求与响应的Body都是Json对象
	 * @param url
	 * @param dataMap
	 * @return
	 */
	public static JSONObject sendAndRecvJson(String url, String jsonStr){
		try {
			String responseContent = doPost(url, jsonStr, "application/json; charset=utf-8");
			return JSON.parseObject( responseContent, JSONObject.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * post发送 application/json 格式数据，接收json格式数据
	 * 请求与响应的Body都是Json对象
	 * @param url
	 * @param map
	 * @return
	 */
	public static JSONObject sendAndRecvJson(String url,Map<String, Object> map){
		return sendAndRecvJson(url, JSON.toJSONString(map));
	}
	
	/**
	 * post发送 application/json 格式数据，接收json格式数据
	 * 请求与响应的Body都是Json对象
	 * @param url
	 * @param jsonStr
	 * @return
	 */
	public static JSONObject sendAndRecvJson(String url,JSONObject reqJson){

		String jsonDataStr = reqJson.toJSONString();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("method", "notify");
		map.put("notify_data", jsonDataStr);

		return sendAndRecvJson(url, map);
	}
	
	public static String doGetHttps(String url) throws IOException{
		
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		/*
		ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
				.tlsVersions(TlsVersion.TLS_1_2)
				.cipherSuites(
					CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
					CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
					CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
				.build();
		builder.connectionSpecs(Collections.singletonList(spec));
		*/
		
		/*
		// OkHttp默认信任所有的证书
		// 使用CertificatePinner来限制哪些证书和证书颁发机构是可信任的
		CertificatePinner certificatePinner = new CertificatePinner.Builder()
			.add("publicobject.com", "sha256/afwiKY3RxoMmLkuRW1l7QsPZTJPwDS2pdDROQjXw8ig=")
			.build();
		builder.certificatePinner( certificatePinner );
		 */
		
		/*
		 // 信任所有证书
		SSLSocketFactory sslSocketFactory = null;
	    try {
	        SSLContext sc = SSLContext.getInstance("TLS");
	        sc.init(null, new TrustManager[]{new HttpSSLSocketFactory.TrustAllManager()}, new SecureRandom());
	        sslSocketFactory = sc.getSocketFactory();
	    } catch (Exception e) {
	    }
		builder.sslSocketFactory(sslSocketFactory);
		builder.hostnameVerifier(new HttpSSLSocketFactory.TrustAnyHostnameVerifier());
		 */
		
		/*
		// 定制信任证书
		InputStream trustedCertificatesInputStream() {
		}
		SSLContext sslContextForTrustedCertificates(InputStream in){
		}
		SSLContext sslContext = sslContextForTrustedCertificates(trustedCertificatesInputStream());
		builder.sslSocketFactory(sslContext.getSocketFactory());
		 */
		
		
		
		
		OkHttpClient client = builder.build();
		
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful())
			throw new IOException("Unexpected code " + response);
		
		/*
		for (Certificate certificate : response.handshake().peerCertificates()) {
			System.out.println("certificate: "+CertificatePinner.pin(certificate));
		}
		*/

		return response.body().string();
	}
	
	public static void main(String[] args) throws IOException {
		System.out.print("response:"+doGetHttps("https://publicobject.com/robots.txt"));
	}
}
