package com.hfp.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/*
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
*/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

	/**
	 * 使用OkHttp,post发送 application/json 格式数据，接收json格式数据
	 * 请求与响应的Body都是Json对象
	 * @param url
	 * @param dataMap
	 * @return
	 */
	public static JSONObject sendAndRecvJson(String url,String jsonStr){

		OkHttpClient client = new OkHttpClient();// 创建OkHttpClient对象，连接交易系统
		MediaType MediaTypeJSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(MediaTypeJSON, jsonStr);
		Request  requ  = new Request.Builder().url(url).post(body).build();
		Response respo;
		try {
			respo = client.newCall(requ).execute();
		} catch (IOException e) {
			return null;
		}
		
		if (respo.isSuccessful()) {
			JSONObject jsonObj;
			try {
				jsonObj = JSON.parseObject(respo.body().string(), JSONObject.class);
			} catch (IOException e) {
				return null;
			}
						
			return jsonObj;
		}
		
		return null;
	}

	/**
	 * post发送 application/json 格式数据，接收json格式数据
	 * 请求与响应的Body都是Json对象
	 * @param url
	 * @param dataMap
	 * @return
	 */
	public static JSONObject sendAndRecvJson(String url,Map<String, Object> dataMap){
		return sendAndRecvJson(url, JSON.toJSONString(dataMap));
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
        String resultString = "";
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
            resultString = EntityUtils.toString(httpEntity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
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
	public static String httpPost(String url, TreeMap<String, String> paramsMap, String fileName, File file) {
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

    /**
	 * 
	 * HTTP协议GET请求方法
	 */
	public static String httpGet(String url) {

		HttpURLConnection uc = null;
		try {
			URL urls = new URL(url);
			uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("GET");
			
			uc.connect();
			
			StringBuffer sb = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
			String readLine = "";
			while ((readLine = in.readLine()) != null) {
				sb.append(readLine);
			}
			in.close();
			
			return sb.toString();
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
	 * HTTP协议POST请求方法
	 */
	public static String httpPost(String url, String params) {
		StringBuffer sb = new StringBuffer();
		HttpURLConnection uc = null;
		try {
			URL urls = new URL(url);
			uc = (HttpURLConnection) urls.openConnection();
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
			
			return sb.toString();
		} catch (IOException e) {
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
	 * HTTP协议POST请求方法
	 */
	public static String httpPost(String url, TreeMap<String, String> paramsMap) {

		String params = null == paramsMap ? null : getParamStr(paramsMap); // key=value字符串

		StringBuffer sb = new StringBuffer();
		HttpURLConnection uc = null;
		try {
			URL urls = new URL(url);
			uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setRequestMethod("POST");
			uc.setUseCaches(false);
			
			uc.connect();
			
			DataOutputStream out = new DataOutputStream(uc.getOutputStream());
			out.write(params.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			String readLine = "";
			while ((readLine = in.readLine()) != null) {
				sb.append(readLine);
			}
			in.close();

		} catch (IOException e) {
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
	 * HTTP协议POST请求添加参数的封装方法
	 */
	private static String getParamStr(TreeMap<String, String> paramsMap) {
		StringBuilder param = new StringBuilder();
		Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> e = it.next();
			param.append("&")
			     .append(e.getKey())
			     .append("=")
				 .append(e.getValue());
		}
		return param.toString().substring(1);
	}
}
