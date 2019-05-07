package com.hfp.util.common;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.SAXException;
import com.thoughtworks.xstream.XStream;


public class XMLUtil {

	public static JSONObject XML2JSON(String xml){
		try {
			return XML.toJSONObject(xml);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解析请求xml示例 
	 * <xml> 
	 *   <ToUserName><![CDATA[toUser]]></ToUserName>
	 *   <FromUserName><![CDATA[FromUser]]></FromUserName>
	 *   <CreateTime>123456789</CreateTime>
	 *   <MsgType><![CDATA[event]]></MsgType>
	 *   <Event><![CDATA[CLICK]]></Event>
	 *   <EventKey><![CDATA[EVENTKEY]]></EventKey>
	 * </xml>
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXmlToMap(InputStream inputStream) throws Exception {
		
		Map<String, String> map = new HashMap<String, String>();  // 将解析结果存储在HashMap中
		
		Document document = new SAXReader().read(inputStream);            // 读取输入流
		Element root = document.getRootElement();      // 得到xml根元素
		List<Element> elementList = root.elements();   // 得到根元素的所有子节点
		
		// 遍历所有子节点
		for (Element e : elementList){
			map.put(e.getName(), e.getText());
		}
		
		inputStream.close();   // 释放资源
		inputStream = null;
		return map;
	}
	
	public static Map<String, String> parseXmlToMap(String xmlString) throws Exception {		
		return parseXmlToMap(new ByteArrayInputStream(xmlString.getBytes()));
	}
	
	public static SortedMap<Object, Object> parseXmlToSortedMap(InputStream is)
			throws ParserConfigurationException, IOException, SAXException {

		// 这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		org.w3c.dom.Document document = builder.parse(is);
		
		// 获取到document里面的全部结点
		org.w3c.dom.NodeList allNodes = document.getFirstChild().getChildNodes();
		
		SortedMap<Object, Object> map = new TreeMap<Object, Object>();
		for (int i = 0; i < allNodes.getLength(); i++) {
			org.w3c.dom.Node node = allNodes.item(i);
			if (node instanceof org.w3c.dom.Element) {
				map.put(node.getNodeName(), node.getTextContent());
			}
		}
		
		return map;
	}
	
	public static SortedMap<Object, Object> parseXmlToSortedMap(String xml)
			throws ParserConfigurationException, IOException, SAXException{
		return parseXmlToSortedMap(new ByteArrayInputStream(xml.getBytes()));
	}
	
	/**
	 * 示例 
	 * <xml>
	 * <return_code><![CDATA[SUCCESS]]></return_code>
	 * <return_msg><![CDATA[OK]]></return_msg>
	 * <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
	 * <mch_id><![CDATA[10000100]]></mch_id>
	 * </xml>
	 * 
	 * 将xml数据(<![CDATA[SUCCESS]]>格式)映射到java对象中
	 * 
	 * @param xml
	 *            待转换的xml格式的数据
	 * @param toClass
	 *            待转换为的java对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObjectFromXML(String xml, Class<T> t) {
		// 将从API返回的XML数据映射到Java对象
		XStream xstream = XStreamFactroy.init(true);
		xstream.alias("xml", t);
		xstream.ignoreUnknownElements();// 暂时忽略掉一些新增的字段
		return (T) xstream.fromXML(xml);
	}
	
	/**
	 * 将java对象转换为xml(<![CDATA[SUCCESS]]>格式)
	 * 
	 * @param obj
	 * @return
	 */
	public static String toXml(Object obj) {
		XStream xstream = XStreamFactroy.init(true);
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj);
	}
	
	/**
	 * 将java对象转换为xml文件,并去除 _ 应用场景是 去除实体中有_划线的实体, 默认会有两个_,调用该方法则会去除一个
	 * 
	 * @param obj
	 * @return
	 */
	public static String toSplitXml(Object obj) {
		XStream xstream = XStreamFactroy.initSplitLine();
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj);
	}
	
	public static String readProPertiesFile(String file, String key){
		Properties props = new Properties();
		try {
			props.loadFromXML(new FileInputStream(file));
			return props.getProperty(key);
		} catch ( Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static Set<Map.Entry<Object,Object>> readProPertiesFileKeySet(String file){
		Properties props = new Properties();
		try {
			props.loadFromXML(new FileInputStream(file));
			return props.entrySet();
			//return props.stringPropertyNames();
		} catch ( Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static void main(String[] args) throws Exception{
		String xml1="<Ips><RefundReq><head><Version>1.0.1</Version><SignType>16</SignType></head><body><MerBillNo>20190412105146066788</MerBillNo><OrgMerTime>20190412</OrgMerTime></body></RefundReq></Ips>";
		//System.out.println(XML2JSON(xml1).toString());
		
		//String xml2="<?xml version=\"1.0\" encoding=\"UTF-8\"?><Ips><RefundRsp><head><ReferenceID>20190412105146066788</ReferenceID><RspCode>000000</RspCode><RspMsg><![CDATA[申请成功]]></RspMsg></head><body><IpsBillNo>BO20190412104954040500</IpsBillNo><Status>P</Status></body></RefundRsp></Ips>";
		//System.out.println(XML2JSON(xml2).toString());
		
		/*
		String file = "E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\test.xml";
		
		String key = "Key";
		System.out.println(key+"="+readProPertiesFile(file, key));
		
		Set<Map.Entry<Object,Object>> keySet = readProPertiesFileKeySet(file);
		for(Map.Entry<Object, Object> entry: keySet){
			System.out.println(entry.getKey()+"="+entry.getValue());			
		}
		*/
		
		/*
		 String xml = "<xmlt>  <ToUserName><![CDATA[toUser]]></ToUserName> <FromUserName><![CDATA[FromUser]]></FromUserName> <CreateTime>123456789</CreateTime>  <MsgType><![CDATA[event]]></MsgType>  <Event><![CDATA[CLICK]]></Event>  <EventKey><![CDATA[EVENTKEY]]></EventKey>  </xmlt>";
		 System.out.println(parseXmlToMap(xml).toString());
	     System.out.println(XML2JSON(xml).toString());
		 System.out.println(parseXmlToSortedMap(xml).toString());
		 */
		 
		 String xmlString = " <xml> <return_code><![CDATA[SUCCESS]]></return_code> <returnMsg><![CDATA[OK]]></returnMsg> <appid><![CDATA[wx2421b1c4370ec43b]]></appid><mch_id><![CDATA[10000100]]></mch_id> </xml>";
		 Result result = getObjectFromXML(xmlString, Result.class);
		 System.out.println(result.toString());
		 System.out.println(toXml(result));
		 System.out.println(toSplitXml(result));
		 
	}
}

class Result{
	private String return_code;
	private String returnMsg;
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	@Override
	public String toString() {
		return "Result [return_code=" + return_code + ", returnMsg=" + returnMsg + "]";
	}
}
