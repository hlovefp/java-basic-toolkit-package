package com.hfp.common;

import java.util.Iterator;
import java.util.TreeSet;
import net.sf.json.JSONObject;

public class JsonUtil {
	
	/**
	 * 排序后返回key=value&key=value字符串
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public static String sort(JSONObject jsonObject) {
		
		if(jsonObject == null){
			return null;
		}

		StringBuilder sort = new StringBuilder();

		Iterator iter = new TreeSet(jsonObject.keySet()).iterator();  // TreeSet => 有序的Set		
		while ( iter.hasNext() ) {
			String key = iter.next().toString();
			Object value = jsonObject.get(key);
			sort.append(key)
				.append("=")
				.append(value==null?"":value.toString())
				.append("&");
		}
		
		return sort.toString().substring(0, sort.length()-1);
	}
	
	public static void main(String[] args){
		String jsonString = "{\"name\":\"tom\",\"age\":12,\"birth\":\"2018-19-20\"}";
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		System.out.println(sort(jsonObject));
	}
}
