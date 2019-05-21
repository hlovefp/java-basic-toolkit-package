package com.hfp.util.net;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class HttpServletUtil {
	/**
	 * 获得客户端IP
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		
		/*
		 * request.getHeader("x-forwarded-for") : 10.47.103.13,4.2.2.2,10.96.112.230
		 * request.getHeader("X-Real-IP") : 10.47.103.13
		 * request.getRemoteAddr():10.96.112.230
		 */
        
		// X-Forwarded-For: Squid 开发的字段,通过了HTTP代理或者负载均衡服务器时才会添加该项。
		// 格式为X-Forwarded-For:client1,proxy1,proxy2，一般情况下，第一个ip为客户端真实ip，后面的为经过的代理服务器ip。
        String ip = request.getHeader("x-forwarded-for");  
		if (ip != null && ip.trim().length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
			if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
		}

		/*
		if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    // 用apache http做代理时一般会加上Proxy-Client-IP请求头
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			// WL-Proxy-Client-IP是apache http的weblogic插件加上的头
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		
		if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  // 有些代理服务器会加上此请求头。
        }
		
		if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }
        */
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-Real-IP");  // nginx代理一般会加上此请求头 
        }

		if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
        
        return ip;
	}
	
	public static void httpWrite(HttpServletResponse response, String body){
		StringBuilder message = new StringBuilder();
		message.append("<html>");
		message.append("<head>");
		message.append("<meta name='viewport' content='width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no'/>");
		message.append("</head><body><h4>");
		message.append(body);
		message.append("</h4></body></html>");
		
		try {
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.getWriter().write(message.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void httpWriteBody(HttpServletResponse response,String body){
		System.out.println("应答: "+body);
		try {
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.getWriter().write(body);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void httpWriteJSON2Body(HttpServletResponse response,String respCode,String respMsg){
		JSONObject object = new JSONObject();
		object.put("respCode", respCode);
		object.put("respMsg", respMsg);
		String message = object.toJSONString();
		
		System.out.println("应答: "+message);
		try {
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.getWriter().write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void httpWriteHTML2Body(HttpServletResponse response,String respCode,String respMsg){
		JSONObject object = new JSONObject();
		object.put("respCode", respCode);
		object.put("respMsg", respMsg);
		String message = null;
		message = "<html><head><meta name='viewport' content='width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no'/></head><body><h4>";
		message = message + object.toJSONString();
		message = message + "</h4></body></html>";
		
		System.out.println("应答: "+message);
		try {
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.getWriter().write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
