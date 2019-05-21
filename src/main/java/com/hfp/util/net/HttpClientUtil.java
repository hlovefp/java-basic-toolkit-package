package com.hfp.util.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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

	/**
	 * 使用httpclient发送post请求
	 * @param url
	 * @param reqMap
	 * @return
	 */
    public static String doPost(String url, Map<String, String> reqMap) {
    	/*
    	BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpResponse resp = client.execute(HttpPost);
    	 */
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);   // 创建Http Post请求
            //httpPost.setHeader(name, value);

            // 创建参数列表
            if (reqMap != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : reqMap.keySet()) {
                    paramList.add(new BasicNameValuePair(key, reqMap.get(key)));
                }
                // 键值对,类似于传统的application/x-www-form-urlencoded表单上传
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList);
                //formEntity.setChunked(true);
                //formEntity.setContentType("");
                httpPost.setEntity(formEntity);
                
                /*
                JSONObject postData = new JSONObject();
                postData.put("supervisor", "1");
                // StringEntity可以自己指定ContentType，而默认值是 text/plain，
              	// 数据的形式就非常自由了，可以组织成自己想要的任何形式，一般用来存储json数据
                StringEntity stringEntity = new StringEntity(postData.toString(), "utf-8");
                //stringEntity.setContentType("");
                httpPost.setEntity(stringEntity); 
                */

                /*
                 // HttpCient4.3之后上传文件主要使用的类是位于org.apache.http.entity.mime下的MultipartEntityBuilder
                 MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                // 第一个参数name的值，是服务器已经定义好的，服务器会根据这个字段来读取我们上传的文件流
                
                //builder.addBinaryBody(name, filePath, ContentType.create(mimeType), filename)
                HttpEntity entity = builder.build();
                
                //  在HttpCient4.3之前上传文件主要使用MultipartEntity这个类
                // MultipartEntity文件上传用到，类似于表单的类型为multipart/form-data
				MultipartEntity entity = new MultipartEntity();
 				// 上传文本，"key" 为字段名,后边new StringBody(text,Charset.forName(CHARSET))为参数值，
 				// 其实就是正常的值转换成utf-8的编码格式  
				entity.addPart("key",new StringBody(text, Charset.forName("UTF-8")));
				entity.addPart("audio", new FileBody(new File("path"), "audio/*"));  // 上传音频文件  
				entity.addPart("fileimg", new FileBody(new File("path1"), "image/*"));// 上传图片1  
				entity.addPart("fileimg", new FileBody(new File("path2"), "image/*"));// 上传图片2  
                httpPost.setEntity(entity); 
                
                // 服务端获取
                //MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        		//List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
				// MultipartFile file = files.get(0);
				// file.getOriginalFilename();
				// file.getBytes();
                */
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
    static class SSLClient extends DefaultHttpClient {
        SSLClient() throws Exception {
            super();
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = this.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
        }
    }
    */

    public static String doPostForm(String url, Map<String, Object> paramsMap){
        HttpPost httppost = new HttpPost(url);   //post method
        try {
        	HttpClient client = new DefaultHttpClient();
        	//client = new SSLClient();

            //参数添加
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            
            HttpResponse httpResponse = client.execute(httppost);   // 发送请求
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception ex) {
        	ex.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
        return null;
    }
    
    /**
	 * 
	 * 
	 * @Description: 上传文件
	 * @param @param url
	 * @param @param paramsMap
	 * @param @param fileName
	 * @param @param file
	 * @param @return
	 * @return String
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	public static String doPostFile(String url, TreeMap<String, String> paramsMap, String fileName, File file) {
		try {
			HttpPost post = new HttpPost(url);
			MultipartEntity reEntity = new MultipartEntity();
			FileBody filebody = new FileBody(file);
			reEntity.addPart(fileName, filebody);
			Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator();
			while ( it.hasNext()) {
				Map.Entry<String, String> e = it.next();
				reEntity.addPart(e.getKey(), new StringBody(e.getValue()));
			}

			post.setEntity(reEntity);
			HttpResponse httpResponse = HttpClients.createDefault().execute(post);
			HttpEntity httpEntity = httpResponse.getEntity();
			return EntityUtils.toString(httpEntity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
