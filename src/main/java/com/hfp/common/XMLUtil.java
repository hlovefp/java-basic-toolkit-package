package com.hfp.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class XMLUtil {

	public static JSONObject XML2JSON(String xml){
		try {
			return XML.toJSONObject(xml);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
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
	
	public static Set<String> readProPertiesFileKeySet(String file){
		Properties props = new Properties();
		try {
			props.loadFromXML(new FileInputStream(file));
			return props.stringPropertyNames();
		} catch ( Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static void main(String[] args){
		//String xml1="<Ips><RefundReq><head><Version>1.0.1</Version><SignType>16</SignType></head><body><MerBillNo>20190412105146066788</MerBillNo><OrgMerTime>20190412</OrgMerTime></body></RefundReq></Ips>";
		//System.out.println(XML2JSON(xml1).toString());
		//String xml2="<?xml version=\"1.0\" encoding=\"UTF-8\"?><Ips><RefundRsp><head><ReferenceID>20190412105146066788</ReferenceID><RspCode>000000</RspCode><RspMsg><![CDATA[申请成功]]></RspMsg></head><body><IpsBillNo>BO20190412104954040500</IpsBillNo><Status>P</Status></body></RefundRsp></Ips>";
		//System.out.println(XML2JSON(xml2).toString());
		
		String file = "E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\test.xml";
		Set<String> keySet = readProPertiesFileKeySet(file);
		for(String key: keySet){
			System.out.println(key+"="+readProPertiesFile(file, key));
		}

	}
}
