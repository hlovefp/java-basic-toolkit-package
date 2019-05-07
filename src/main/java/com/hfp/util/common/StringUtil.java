package com.hfp.util.common;

import java.text.MessageFormat;
import java.util.Date;

public class StringUtil {
	/**
	 * 驼峰命名法工具: 将"hello_world" 转换成 "helloWorld"
	 * @return
	 */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
	 * 驼峰命名法工具: 将"hello_world" 转换成 "HelloWorld"
	 * @return
	 */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    /**
	 * 驼峰命名法工具: 将"helloWorld" 转换成 "hello_world"
	 * @return
	 */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append('_');
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
    
    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     * @param objectString 对象串
     *   例如："row.user.id"
     *   返回："!row?'':!row.user?'':!row.user.id?'':row.user.id"
     */
    public static String jsGetVal(String objectString){
    	StringBuilder result = new StringBuilder();
    	StringBuilder val = new StringBuilder();
    	
    	String[] vals = objectString.split("\\.");
    	for (int i=0; i<vals.length; i++){
    		val.append("." + vals[i]);
    		result.append("!"+ val.substring(1) +"?'':");
       	}
    	result.append(val.substring(1));
    	
    	return result.toString();
    }
    
    /**
     * 拼接生成字符串
     * @param result
     * @param arrs
     * @return
     */
    public static String messageFormat(String result, Object[] arrs){
/*
FormatElement:
 { ArgumentIndex }   => "{0}"
 { ArgumentIndex , FormatType }   => "{0,number}"
 { ArgumentIndex , FormatType , FormatStyle }  => "{0,number,currency}"

FormatType: number、date、time、choice(需要使用ChoiceFormat)
FormatStyle: short、medium、long、full、integer、currency、percent、SubformatPattern(子模式)

String format ="{0,number,###.##}, {1}, {2,number,currency}";
Object[] arr = {189.328, "helo", 193.32};
System.out.println(MessageFormat.format(format,arr));  // 189.33, helo, $193.32
*/
        return MessageFormat.format(result, arrs);
    }
    
    /**
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = false
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
	 * 
	 * @param s
	 *            待判断的字符串数据
	 * @return 判断结果 true-是 false-否
	 */
	public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
	}
	
	public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
	}
	
	
	public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
	
	/**
     * StringUtil.isNotBlank(null)      = false<br>
     * StringUtil.isNotBlank("")        = false<br>
     * StringUtil.isNotBlank(" ")       = false<br>
     * StringUtil.isNotBlank("bob")     = true<br>
     * StringUtil.isNotBlank("  bob  ") = true<br>
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static void main(String[] args){
    	System.out.println(jsGetVal("row.user.id"));
    	
    	StringBuilder sb=new StringBuilder();
        sb.append("insert into test_tb ( createTime, name ) values (''{0}'', ''{1}'')");
        Object[] arr={new Date(),"tom"};
        // insert into test_tb ( createTime, name ) values ('19-4-12 下午5:33', 'tom')
        System.out.println(MessageFormat.format(sb.toString(), arr));
    }
}
