package com.hfp.util.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.hfp.util.common.MapUtil;

public class HttpUtil {
    /**
	 * 
	 * HTTP协议GET请求方法
	 */
	public static String doGet(String url) {

		HttpURLConnection uc = null;
		try {
			uc = (HttpURLConnection) new URL(url).openConnection();
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
	
	public static String doGet(String url, String param) {
		BufferedReader in = null;
		try {			
			URLConnection connection = new URL(url + "?" + param).openConnection();   // 打开和URL之间的连接
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			connection.connect();  // 建立连接
			
			/*
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			*/
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			StringBuilder result = new StringBuilder();
			String line = null;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * HTTP协议POST请求方法
	 */
	public static String doPost(String url, String params) {
		params = (params==null)?"":params;
		
		HttpURLConnection uc = null;
		BufferedReader in = null;
		try {
			uc = (HttpURLConnection) new URL(url).openConnection();
			uc.setRequestMethod("POST");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
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
			return sb.toString();
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
	 * HTTP协议POST请求方法
	 */
	public static String doPost(String url, TreeMap<String, String> paramsMap) {
		return doPost(url, (null == paramsMap) ? null : MapUtil.map2FormString(paramsMap));
	}
}
