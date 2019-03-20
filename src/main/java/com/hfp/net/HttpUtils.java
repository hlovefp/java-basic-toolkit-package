package com.hfp.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

	/**
	 * post发送 application/json 格式数据，接收json格式数据
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
}
