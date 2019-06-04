package com.hfp.util.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/*
调用：
jdk原生的WebService方式（基础）  JWS
使用Apache的CXF框架（常用）
使用Apache的axis框架（多语言，重量级）
使用XFire框架（已没落）
 */
public class WebServiceClient {

	/**
	 * 使用本地源码发送基于SOAP的POST请求，收到的响应也是基于SOAP的响应
	 * SoapUI 工具  获取请求数据格式
	 * 缺点：自行构造源码，获得响应后需要自行解析响应体
	 * 
	 * WebService是基于SOAP(Simple Object Access Protocal 简单对象访问)协议
	 * SOAP是一种简单的基于xml的协议和规范，它使应用程序通过http来交换信息
     *
	 * WSDL(Web Services Description Language)，即网络服务描述语言，可描述WebService服务。
	 * 
	 */
	public static void soapClient(){
		// 获得腾讯QQ在线状态  输入QQ号码   返回数据：String，Y = 在线；N = 离线；E = QQ号码错误；A = 商业用户验证失败；V = 免费用户超过数量
		String url = "http://ws.webxml.com.cn/webservices/qqOnlineWebService.asmx?wsdl";

		StringBuilder sb = new StringBuilder();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://WebXml.com.cn/\">")
		  .append("<soapenv:Header/>")
		  .append("<soapenv:Body>")
		  .append("<web:qqCheckOnline>")
		  .append("<web:qqCode>").append("93103194").append("</web:qqCode>")
		  .append("</web:qqCheckOnline>")
		  .append("</soapenv:Body>")
		  .append("</soapenv:Envelope>");
		
		String result = HttpClientUtil.doPost(url, sb.toString(), "text/xml;charset=UTF-8");
		System.out.println(result);
/*
<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <soap:Body>
    <qqCheckOnlineResponse xmlns="http://WebXml.com.cn/">
      <qqCheckOnlineResult>N</qqCheckOnlineResult>
    </qqCheckOnlineResponse>
  </soap:Body>
</soap:Envelope>
*/
		Document document = Jsoup.parse(result);
		String qqCheckOnlineResult = document.getElementsByTag("qqCheckOnlineResult").text();
		System.out.println(qqCheckOnlineResult);
	}
	
	

	
	public static void main(String[] args) {
		test2();
	}
	
	
	public static void test2(){
		String url = "http://localhost/jwsprovider?wsdl";        // SoapUI 工具  获取请求数据格式
		StringBuilder sb = new StringBuilder();
		sb.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:jws=\"http://jws.hfp.com\">")
		  .append("<soap:Header/>")
		  .append("<soap:Body>")
		  
		  /*
		  .append("<jws:getRandomCode>")
		  .append("<message>").append("random is").append("</message>")
		  .append("</jws:getRandomCode>")
		  */
		  .append("<jws:getUserName>")
		  .append("<arg0>").append("93103194").append("</arg0>")
		  .append("</jws:getUserName>")

		  .append("</soap:Body>")
		  .append("</soap:Envelope>");
		
		String result = HttpClientUtil.doPost(url, sb.toString(), "text/xml;charset=UTF-8");
		System.out.println(result);

		/**
	<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
	<S:Body>
	  <ns2:getRandomCodeResponse xmlns:ns2="http://jws.hfp.com">
	     <return>random: 0.7150816584508586</return>
	  </ns2:getRandomCodeResponse>
	</S:Body>
	</S:Envelope>

	<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
	<S:Body>
	  <ns2:getUserNameResponse xmlns:ns2="http://jws.hfp.com">
	     <returnMessage>My name is 123</returnMessage>
	  </ns2:getUserNameResponse>
	</S:Body>
	</S:Envelope>
		 */
		
		org.jsoup.nodes.Document document = org.jsoup.Jsoup.parse(result);
		String qqCheckOnlineResult = document.getElementsByTag("returnMessage").text();
		System.out.println(qqCheckOnlineResult);
	}

}
