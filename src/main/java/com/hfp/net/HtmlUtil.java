package com.hfp.net;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
	/**
	 * 去掉HTML中的所有标签
	 */
	public static String replaceHtml(String html) {
		if ( html == null || html.trim().length() == 0){
			return "";
		}
		/*
		Pattern p = Pattern.compile("<.+?>");
		Matcher m = p.matcher(html);
		return m.replaceAll("");
		*/
		return html.replaceAll("<.+?>", "");
	}
	
	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 */
	public static String replaceMobileHtml(String html){
		if (html == null || html.trim().length()==0){
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}
	
	public static void main(String[] args){
		String html = "<html><head><title>title</title></head><body> 测 试 </body></html>";
		System.out.println(replaceHtml(html)); // "title 测 试 "
		System.out.println(replaceMobileHtml(html));
	}
	
}
