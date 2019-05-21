package com.hfp.util.common;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtil {

	/** 
     * 获取对象对应字段的值
     *  
     * @param o 执行对象 
     * @param fieldName 属性 
     */  
    public static Object invokeGet(Object o, String fieldName) {  
        Method method = null;

        try {
            // 方法名: getXxx
            StringBuffer sb = new StringBuffer();
            sb.append("get");  
            sb.append(fieldName.substring(0, 1).toUpperCase());  
            sb.append(fieldName.substring(1));
            
        	method = o.getClass().getMethod(sb.toString());  
        } catch (Exception e) { 
        	// NoSuchMethodException  没有对应get方法
        }
        
        try {  
            return method.invoke(o, new Object[0]); // 等价 o.getXxx()
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }

	/**
	 * 将对象的字段名和值转存到Map中<p>
	 * 可以利用注解来达到指定字段不放入到Map中
	 */
	public static Map<String,Object> object2Map(Object bean) {
		if (bean == null)
            return null;

		Map<String,Object> map = new HashMap<String,Object>();
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(!field.isAnnotationPresent(NotInclude.class)){ // 判断字段上有注解
				field.setAccessible(true);
				String key = field.toString().substring(field.toString().lastIndexOf(".") + 1);
				Object value=null;
				try {
					value = field.get(bean);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				map.put(key, value);
			}
		}
		return map;
	}
	
	public static Map<String,Object> object2Map2(Object bean) {
		if (bean == null)
            return null;

		Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();  // get方法
                Object value = (getter != null) ? getter.invoke(bean) : null;
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
	}

	/**
	 * 判断对象中指定字段不能为null或""
	 * 满足条件返回 "Success", 否则返回告知哪些字段为空
	 * @param obj
	 * @param fieldNames
	 * @return
	 */
	public static String isNullOrEmpty(Object obj, String ...fieldNames){

    	StringBuilder sb = new StringBuilder();
    	sb.append("以下参数不能为空:");
    	
    	int i = 0;
    	for (String fieldName : fieldNames) {
    		if(invokeGet(obj, fieldName) == null){
    			i++;
    			sb.append(fieldName + ",");
    		}
		}
    	if (i > 0) {
			return sb.substring(0, sb.length() - 1).toString();
		}
		return "Success";
    }

	public static void main(String[] args){
		//System.out.println(object2Map(new Test(1, "hfp", 98)).toString());
		//System.out.println(invokeGet(new Test(1, "hfp", 98),"name"));
		System.out.println(isNullOrEmpty(new Test(1, null, 98),"name"));
		System.out.println(isNullOrEmpty(new Test(1, "hfp", 98),"name"));
	}
}

@Target(FIELD)
@Retention(RUNTIME)
@Documented
@interface NotInclude {
	String value() default "";
}

class Test{
	private int id;
	private String name;
	@NotInclude
	private int age;
	public Test(int id,String name,int age){
		this.id = id;
		this.name = name;
		this.age = age;
	}
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public int getAge(){
		return this.age;
	}
}
