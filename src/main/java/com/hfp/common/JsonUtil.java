package com.hfp.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;

public class JsonUtil {
	
	/**
	 * 排序后返回key=value&key=value字符串
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public static String map2SortFormString(JSONObject jsonObject) {
		
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
	
	public static <T> T jsonString2JavaBean(String json, Class<T> c){
		//return new com.google.gson.Gson().fromJson(json, c);
		return com.alibaba.fastjson.JSON.parseObject(json, c);
	}
	
	public static <T> T jsonString2JavaBean(String json, TypeReference<T> type){
		return com.alibaba.fastjson.JSON.parseObject(json, type);
	}
	
	public static String javaBean2JsonString(Object obj){
		return com.alibaba.fastjson.JSON.toJSONString(obj);
		//return com.alibaba.fastjson.JSON.toJSONString(obj,true); // 有换行和缩进
	}
	
	public static com.alibaba.fastjson.JSON javaBean2Json(Object obj){
		return (com.alibaba.fastjson.JSON)com.alibaba.fastjson.JSON.toJSON(obj);
	}
	
	public static void main(String[] args){
		//String jsonString = "{\"name\":\"tom\",\"age\":12,\"birth\":\"2018-19-20\"}";
		//JSONObject jsonObject = JSONObject.parseObject(jsonString);
		//System.out.println(sort(jsonObject));
		
		//String json = "{\"teacherName\":\"jim\",\"teacherAge\":56,\"students\":[{\"name\":\"tom\",\"age\":12,\"birth\":\"2018-19-20\",\"list\":[\"a\",\"b\"]}]}";
		//Teacher t = jsonString2JavaBean(json, Teacher.class);
		//System.out.println(t.toString());
		//Teacher ts = jsonString2JavaBean(json, new TypeReference<Teacher>() {});
		//System.out.println(ts.toString());
		//System.out.println(javaBean2JsonString(ts));

		//String arr = "[{\"name\":\"tom\",\"age\":12,\"birth\":\"2018-19-20\",\"list\":[\"a\",\"b\"]}]";
        //ArrayList<Student> students = jsonString2JavaBean(arr, new TypeReference<ArrayList<Student>>() {});
		//System.out.println(students.toString());
		//System.out.println(javaBean2JsonString(students));
		
		//String as = "{\"name\":\"tom\",\"age\":12,\"birth\":\"2018-19-20 21:13:56\",\"list\":[\"a\",\"b\"]}";
		//Man tt = jsonString2JavaBean(as, new TypeReference<Man>() {});
		//System.out.println(tt.toString());
		//System.out.println(javaBean2JsonString(tt));
		//System.out.println(javaBean2Json(tt).toJSONString());
		
		//HashMap<String, Object> map = new HashMap<>();
		//map.put("name", "tom");
		//map.put("age", 12);
		//System.out.println(javaBean2Json(map).toJSONString());
		
		//ArrayList<String> arrayList = new ArrayList<>();
		//arrayList.add("name");
		//arrayList.add("age");
		//System.out.println(javaBean2Json(arrayList).toJSONString()); // ["name","age"]

	}
}

class Man{
	private String name;
	private int age;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date birth; 
	public int getAge() {  return age; }
	public void setAge(int age) {  this.age = age; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Date getBirth() { return birth; }
	public void setBirth(Date birth) { this.birth = birth; }
	public String toString(){
		return "name:"+name+" age:"+age+" birth:"+birth.toString();
	}
}

class Student{
	private String name;
	private int age;
	private List<String> list; 
	public int getAge() {  return age; }
	public void setAge(int age) {  this.age = age; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public List<String> getList() { return list; }
	public void setList(List<String> list) { this.list = list; }
	public String toString(){
		return "name:"+name+" age:"+age+" list:"+list.toString();
	}
}

class Teacher {

    private String teacherName;
    private Integer teacherAge;
    private List<Student> students;

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public Integer getTeacherAge() { return teacherAge; }
    public void setTeacherAge(Integer teacherAge) { this.teacherAge = teacherAge; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
    
    public String toString(){
		return "teacherName:"+teacherName+" teacherAge:"+teacherAge+" students:"+students.toString();
	}
}
