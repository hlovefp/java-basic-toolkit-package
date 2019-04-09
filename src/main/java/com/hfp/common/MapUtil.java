package com.hfp.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class MapUtil {
	
	/**
	 * 返回排序后的key=value&key=value字符串
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public static String sort(Map paramMap) {
		
		if(paramMap == null){
			return null;
		}

		StringBuilder sort = new StringBuilder();

		Iterator iter = new TreeSet(paramMap.keySet()).iterator();  // TreeSet => 有序的Set		
		while ( iter.hasNext() ) {
			String key = iter.next().toString();
			Object value = paramMap.get(key);
			sort.append(key)
				.append("=")
				.append(value==null?"":value.toString())
				.append("&");
		}
		
		return sort.toString().substring(0, sort.length()-1);
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
		Map<String, Object> map = new HashMap<>();
		map.put("id", 112);
		map.put("age", null);
		map.put("name", "tom");
		System.out.println(sort(map));
		
		Person person = new Person();
		mapToBean( person, map);
		System.out.println(person.toString());
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
