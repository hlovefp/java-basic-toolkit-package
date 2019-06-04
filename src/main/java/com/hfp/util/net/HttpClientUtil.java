package com.hfp.util.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import com.hfp.util.common.MapUtil;

/*
//工具包下的HttpClient，  最后更新 2007年    
<dependency>
    <groupId>commons-httpclient</groupId>
    <artifactId>commons-httpclient</artifactId>
    <version>3.1</version>
</dependency>
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
*/

public class HttpClientUtil {
	
	private static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContextBuilder sslContextBuilder = new SSLContextBuilder().loadTrustMaterial( null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;  // 信任所有
				}
			});
			SSLContext context = sslContextBuilder.build();
			HostnameVerifier verifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory( context, verifier);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslSocketFactory).build();
			PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(registry);
			
			return HttpClientBuilder.create().setConnectionManager(connMgr).build();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return HttpClients.createDefault();
	}
	
	/*
	public static void doPost(){
		
    	BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpResponse resp = client.execute(HttpPost);
    	 
	}
*/
	
    
    /**
     * 
     * @param url       http或https
     * @param content
     * @param contentType
     *    text/html         HTML格式
     *    text/plain        纯文本格式 
     *    application/json  JSON数据格式
     *    application/x-www-form-urlencoded
     *    application/xml   XML数据格式
     * @return
     */
    public static String doPost(String url, String content, String contentType) {
    	CloseableHttpClient httpClient = createSSLClientDefault();
    	CloseableHttpResponse response = null;
    	HttpPost httpPost = new HttpPost(url);
        try {

            if (content != null) {
                StringEntity stringEntity = new StringEntity( content, "utf-8");
                stringEntity.setContentType(contentType); // 默认值是 text/plain
                httpPost.setEntity(stringEntity);
                //httpPost.addHeader(name, value);
            }

            response = httpClient.execute(httpPost);  // 执行http请求
            //response.getStatusLine().getStatusCode();
            HttpEntity httpEntity = response.getEntity();
            //InputStream is = httpEntity.getContent();
            return EntityUtils.toString(httpEntity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            	response.close();
            	httpPost.releaseConnection();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * application/x-www-form-urlencoded表单上传
     */
    public static String doPostForm(String url, Map<String, String> paramsMap){
    	CloseableHttpClient httpClient = createSSLClientDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            List<NameValuePair> paramList = new ArrayList<>();
            
            for (String key : paramsMap.keySet()) {
            	paramList.add(new BasicNameValuePair( key, paramsMap.get(key)));
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "UTF-8");
            //entity.setChunked(true);
            //entity.setContentType("");
            
            httpPost.setEntity(entity);
            // httpPost.addHeader(name, value);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception ex) {
        	ex.printStackTrace();
        } finally {
            try {
            	httpPost.releaseConnection();
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return null;
    }

	/**
	 * 使用httpclient发送post请求
	 * @param url      http或https
	 * @param reqMap
	 * @return
	 */
	public static String doPostFile(String url, File file, Map<String, String> reqMap) {
    	CloseableHttpClient httpClient = createSSLClientDefault();
    	CloseableHttpResponse response = null;
    	HttpPost httpPost = new HttpPost(url);
        try {
             MultipartEntityBuilder builder = MultipartEntityBuilder.create();
             builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            // 第一个参数name的值，是服务器已经定义好的，服务器会根据这个字段来读取我们上传的文件流
            //builder.addBinaryBody( String name, byte[] b)
            //builder.addBinaryBody( String name, byte[] b, ContentType contentType, String filename)
            //builder.addBinaryBody( String name, File file)
            //builder.addBinaryBody( String name, File file, ContentType contentType, String filename)
            //builder.addBinaryBody( String name, InputStream stream, ContentType contentType, String filename) {
            //builder.addBinaryBody( String name, InputStream stream)
            //builder.addPart(FormBodyPart bodyPart)
            //builder.addPart(String name, ContentBody contentBody)
            //builder.addTextBody( String name, String text, ContentType contentType)
            //builder.addTextBody( String name, String text)
            //builder.addBinaryBody("file",new byte[]{},ContentType.DEFAULT_BINARY,"fileName.jpg");
            builder.addBinaryBody("file", file);
            
            //builder.addPart("file", new FileBody(file));
            //builder.addPart("text1", new StringBody("message 1", ContentType.MULTIPART_FORM_DATA));
            //builder.addPart("text2", new StringBody("message 2", ContentType.MULTIPART_FORM_DATA));
            //builder.addTextBody("text3", "message 3", ContentType.DEFAULT_BINARY);
            //builder.addBinaryBody("upstream", inputStream, ContentType.create("application/zip"), zipFileName);
             
            HttpEntity entity = builder.build();
            
            // 服务端获取
            // CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
            // if(multipartResolver.isMultipart(request)){ // 多文件上传: 方法为POST,contentType以"mutipart/"开头
            //    MultipartHttpServletRequest multiRequest=((MultipartHttpServletRequest) request);
            
    		//    List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            //    for(int i=0; i<files.size(); i++){
			//      MultipartFile file = files.get(0);
			//      file.getOriginalFilename();
			//      file.getBytes();
            //    }
            //
            //    Map<String,Object> map=multiRequest.getParameterMap();
            //    Iterator<String> iterator=multiRequest.getFileNames();//获取multiRequest中所有的文件名
            //    //遍历所有文件
            //     while(iterator.hasNext()){
            //       MultipartFile file = multiRequest.getFile(iterator.next().toString());
            //       ...
            //    }
            // }
            
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);  // 执行http请求
            //response.getStatusLine().getStatusCode();
            HttpEntity httpEntity = response.getEntity();
            //InputStream is = httpEntity.getContent();
            return EntityUtils.toString(httpEntity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
	
	
	/**
	 * POST方法，application/json
	 * @param url    http和https都支持
	 * @param body
	 * @param header
	 * @return
	 * @throws Exception
	 */
	public static String doPostJSON(String url, String jsonStr, Map<String, String> header) {
		CloseableHttpClient client = createSSLClientDefault();
		HttpPost httpPost = new HttpPost(url);

		if (header != null) {
			for (String key : header.keySet()) {
				httpPost.addHeader(key, header.get(key));
			}
		}
		
		StringEntity entity = new StringEntity(jsonStr.toString(), "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		
		httpPost.setEntity(entity);

		try{
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString( response.getEntity(), "UTF-8");
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
		
		return null;
	}
	
	/**
	 * POST方法，application/json
	 * @param url    http和https都支持
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static String doPostJSON(String url, String jsonStr){
		return doPostJSON(url, jsonStr, null);
	}
	
	/**
	 * 
	 * @param url   http或https
	 * @return
	 */
    public static String doGet(String uri){
    	CloseableHttpClient client = createSSLClientDefault();
    	HttpGet httpGet = new HttpGet(uri);
    	try {
			CloseableHttpResponse response = client.execute(httpGet);
			InputStream is = response.getEntity().getContent();
			return IOUtils.toString(is,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpGet.releaseConnection();
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return null;
    }
	
	/**
	 * Get  ?key=value&key=value
	 * @param url   http和https都支持
	 * @param bodyMap
	 * @param headerMap
	 * @return
	 */
	public static String doGet(String url, Map<String, String> bodyMap, Map<String, String> headerMap) throws Exception {
		CloseableHttpClient httpClient = createSSLClientDefault();
		
		URI uri = null;
		if(bodyMap!=null){
			uri = new URI(url + "?" + MapUtil.map2FormString(bodyMap));
		} else{
			uri = new URI(url);
		}
		
		HttpGet get = new HttpGet(uri);
		
		if (headerMap != null) {
			for (String key : headerMap.keySet()) {
				get.addHeader(key, headerMap.get(key));
			}
		}

		try {
			CloseableHttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString( response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				get.releaseConnection();
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public static void main(String[] args) throws Exception {
		//System.out.println(doGet("https://www.baidu.com"));
		//System.out.println(httpsGet("http://www.baidu.com", null, null));
		//System.out.println(httpsGet("https://www.baidu.com", null, null));
	}
}
