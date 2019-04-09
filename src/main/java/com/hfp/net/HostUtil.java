package com.hfp.net;

import javax.servlet.http.HttpServletRequest;

public class HostUtil {
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
}
