package com.hfp.util.common;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MapUtil {
	
	/**
	 * 返回key=value&key=value字符串
	 * @param paramMap
	 * @return
	 */
	public static String map2FormString(Map paramMap, String... ignoreKey) {
		
		if(paramMap == null){
			return null;
		}
		
		List<String> ignoreKeyList = null;
		if(ignoreKey!=null){
			ignoreKeyList = Arrays.asList(ignoreKey);
		}else{
			ignoreKeyList = new ArrayList<>();
		}

		StringBuilder paramString = new StringBuilder();

		Iterator iterator = paramMap.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next().toString();
			if(ignoreKeyList.contains(key))
				continue;
			Object value = paramMap.get(key);
			paramString.append(key)
				.append("=")
				.append(value==null?"":value.toString())
				.append("&");
		}

		return paramString.toString().substring(0, paramString.length()-1);
	}
	
	/**
	 * 返回根据key排序后的key=value&key=value字符串
	 * @param paramMap
	 * @return
	 */
	public static String map2SortFormString(Map paramMap, String... ignoreKey) {
		
		if(paramMap == null){
			return null;
		}
		
		List<String> ignoreKeyList = null;
		if(ignoreKey!=null){
			ignoreKeyList = Arrays.asList(ignoreKey);
		}else{
			ignoreKeyList = new ArrayList<>();
		}

		StringBuilder sort = new StringBuilder();
		
		/*
		List<String> keys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(keys);
        for(int i=0;i<keys.size();i++){
        	String key = keys.get(i);
        	Object value = paramMap.get(key)
        }
        */

		Iterator<String> iter = new TreeSet<String>(paramMap.keySet()).iterator();  // TreeSet => 有序的Set		
		while ( iter.hasNext() ) {
			String key = iter.next();
			if(ignoreKeyList.contains(key))
				continue;
			Object value = paramMap.get(key);
			sort.append(key)
				.append("=")
				.append(value==null?"":value.toString())
				.append("&");
		}
		
		return sort.toString().substring(0, sort.length()-1);
	}
	
	
	/**
	 * 将形如
	 *    "key=value&key=value"
	 *    "{key=value&key=value}"
	 * 的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> formString2Map(String result) {
		Map<String, String> map = new HashMap<String, String>();
		
		if (StringUtil.isBlank(result)) {
			return map;
		}
			
		//"{key=value&key=value}"
		if (result.startsWith("{") && result.endsWith("}")) {
			result = result.substring(1, result.length() - 1);
		}

		int len = result.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		boolean isOpen = false;   //值里有嵌套
		char openName = 0;
		
		if(len == 0)
			return map;

		for (int i = 0; i < len; i++) {  // 遍历整个带解析的字符串
			curChar = result.charAt(i);  // 取当前字符
			if (isKey) {                 // 如果当前生成的是key
				if (curChar == '=') {    // 如果读取到=分隔符,读取key结束
					key = temp.toString();
					temp.setLength(0);       // 清空temp
					isKey = false;
				} else {
					temp.append(curChar);
				}
			} else  {                        // 如果当前生成的是value
				if(isOpen){
					if(curChar == openName){
						isOpen = false;
					}
				}else{                       //如果没开启嵌套
					if(curChar == '{'){      //如果碰到，就开启嵌套， 值是{...},不支持值是"{...{..}..}"
						isOpen = true;
						openName ='}';
					}else if(curChar == '['){  // 值是”[...]"
						isOpen = true;
						openName =']';
					}
				}
				
				// 值可以是{...&...} 或  [...&...]
				if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
					//key=value&   key=&   =&
					putKeyValueToMap(key, temp.toString(), map);
					temp.setLength(0);
					isKey = true;
				}else{
					temp.append(curChar);
				}
			}
		}
		
		// key=value&key=value  ==>  value=temp.toString(); isKey=false
		// key=value&key=       ==>  value=temp.toString(); isKey=false
		// key=value&key        ==>  key=temp.toString      isKey=true
		// key=value&           ==>  key=temp.toString      isKey=true

		if(isKey){
			putKeyValueToMap(temp.toString(),"", map);
		}else {
			putKeyValueToMap(key,temp.toString(), map);
		}


		return map;
	}
	
	private static void putKeyValueToMap(String key, String value, Map<String, String> map){
		if(key.length()>0)          // key是空字符串，扔弃
			map.put(key, value);    // value是空字符串或非空字符串
	}
	
	/**
	 * 将Map数据转型成对象
	 * @param javabean
	 * @param m
	 */
	public static void mapToBean(Object obj, Map<String, Object> m) {
		
		Method[] methods = obj.getClass().getDeclaredMethods();  // 所有方法数组
		
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {    // set开头的方法
				String methodName = method.getName();    // 方法名
				String field = methodName.substring(3);  // set方法对应的字段名
				field = field.toLowerCase().charAt(0) + field.substring(1);

				Object value = m.get(field.toString());  // map中对应字段的值

				Class<?>[] params = method.getParameterTypes();  // 方法的参数

				try {
					if (value != null && !value.equals("")) {
						String pa = params[0].getName().toString();  // xx.xx.Xxx格式的类名
						
						if (pa.equals("java.util.Date")) {
							value = new java.util.Date(((java.util.Date) value).getTime());
						} else if (pa.equals("java.lang.String")) {
							value = new java.lang.String(value.toString());
						} else if (pa.equals("java.lang.Integer") || pa.equals("int")) {
							value = new java.lang.Integer(value.toString());
						} else if (pa.equals("java.lang.Long")) {
							value = new java.lang.Long(value.toString());
						} else if (pa.equals("java.lang.Double")) {
							value = new java.lang.Double(value.toString());
						} else if (pa.equals("java.lang.Float")) {
							value = new java.lang.Float(value.toString());
						} else if (pa.equals("java.lang.Short")) {
							value = new java.lang.Short(value.toString());
						} else if (pa.equals("java.lang.Byte")) {
							value = new java.lang.Byte(value.toString());
						} else if (pa.equals("java.lang.Boolean")) {
							value = new java.lang.Boolean(value.toString());
						}
						method.invoke(obj, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		/*
		Map<String, Object> map = new HashMap<>();
		map.put("id", 112);
		map.put("age", null);
		map.put("name", "tom");
		System.out.println(map2SortFormString(map));
		
		Person person = new Person();
		mapToBean( person, map);
		System.out.println(person.toString());
		*/

		String string1 = "name=hfp&age=12";
		System.out.println(formString2Map(string1).toString());
		String string2 = "{name=hfp&age=}";
		System.out.println(formString2Map(string2).toString());
		String string3 = "name=hfp&=";
		System.out.println(formString2Map(string3).toString());
		String string4 = "name=hfp&";
		System.out.println(formString2Map(string4).toString());
		String string5 = "&name=hfp&";
		System.out.println(formString2Map(string5).toString());
		String string6 = "&name={hfp=12&age=12}&age=[1=3&2=4]";
		System.out.println(formString2Map(string6).toString());
	}
}

class Person{
	private int id;
	private String name;
	private int age;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String toString(){
		return "id="+id+"&age="+age+"&name="+name;
	}
}
