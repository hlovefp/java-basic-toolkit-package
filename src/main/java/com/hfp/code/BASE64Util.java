package com.hfp.code;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.util.encoders.Base64Encoder;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;


/**
 * base64的工具：
 *   1. 第三方包   commons-io
 *   2. jdk自带的 javax.xml.bind.DatatypeConverter
 *   3. jdk自带的 sun.misc.BASE64Encoder/BASE64Decoder(不是API，结果中存在换行)
 * 创建时间：2019年3月19日 下午2:43:21
 * 文件名称：BASE64Util.java
 * @author 贺飞平
 * @version 1.0
 *
 */
public class BASE64Util {
	/**
	 * 编码<p>
	 * 使用commons-io包进行Base加密,加密后的数据包括字符'+','/'<p>
	 * 输入数据: "最近写了个工具,客户需要用jar运行"<p>
	 * 返回数据: "5pyA6L+R5YaZ5LqG5Liq5bel5YW3LOWuouaIt+mcgOimgeeUqGphcui/kOihjA=="<p>
	 * @param source
	 * @return
	 */
	public static String encode(String source) {
		byte[] input = source.getBytes(Charset.forName("UTF-8"));
		return encode(input);
	}

	public static String encode(byte[] source) {
		byte[] output = new org.apache.commons.codec.binary.Base64().encode(source);
		return new String(output);
	}

	/**
	 * http传输中存在'+'变空格问题
	 *   方法一：传输前将原有的字符表里面的"+"、"/"替换"-"和"_",传输后还原
	 *   方法二：base64的结果进行URLcode
	 * 输入数据: "最近写了个工具,客户需要用jar运行"<p>
	 * 返回数据: "5pyA6L-R5YaZ5LqG5Liq5bel5YW3LOWuouaIt-mcgOimgeeUqGphcui_kOihjA=="<p>
	 * @param source
	 * @return
	 */
	public static String encodeForHttp(String source){
		return encode(source).replaceAll("\\+","-").replaceAll("/","_");
	}

	/**
	 * 解码<p>
	 * 使用commons-io包进行Base解密,输入的加密数据包括字符'+','/'<p>
	 * 输入数据: "5pyA6L+R5YaZ5LqG5Liq5bel5YW3LOWuouaIt+mcgOimgeeUqGphcui/kOihjA=="<p>
	 * 返回数据: "最近写了个工具,客户需要用jar运行"
	 * @param source
	 * @return
	 */
	public static String decode(String source) {
		byte[] input = source.getBytes(Charset.forName("UTF-8"));
		return new String(decode(input));
	}

	public static byte[] decode(byte[] source) {
		return new org.apache.commons.codec.binary.Base64().decode(source);
	}
	
	public static String decodeFromHttp(String source){
		return decode(source.replaceAll("-","+").replaceAll("_", "/"));
	}

	protected static String printByte(byte[] values) {
		StringBuffer sb = new StringBuffer();
		for (byte b : values) {
			sb.append(b).append(" ");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static void main(String[] args) {
		//byte[] bs = new byte[2];
		//bs[0] = 0x1a;
		//bs[1] = 0x1b;
		//System.out.println(printByte(bs));  //26 27
		
		String source = "最近写了个工具,客户需要用jar运行";
		
		//String encodedStr = BASE64Util.encodeForHttp(source);
		//System.out.println("BASE64加密结果:"+encodedStr);
		//5pyA6L-R5YaZ5LqG5Liq5bel5YW3LOWuouaIt-mcgOimgeeUqGphcui_kOihjA==
		//String decodedStr = BASE64Util.decodeFromHttp(encodedStr);
		//System.out.println("BASE64解密结果:"+decodedStr);
		
		/*
		String code = DatatypeConverter.printBase64Binary(source.getBytes());
		System.out.println(code);
		//5pyA6L+R5YaZ5LqG5Liq5bel5YW3LOWuouaIt+mcgOimgeeUqGphcui/kOihjA==
		System.out.println(new String(DatatypeConverter.parseBase64Binary(code)));
		//最近写了个工具,客户需要用jar运行
		*/
		

        /*
        String str = "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890a";
        String encoding = new BASE64Encoder().encode( str.getBytes() );  // 结果存在换行
        System.out.println(encoding);
        System.out.println( encoding.replaceAll("\n","\\\\n").replaceAll("\r","\\\\r") );
        try {
			System.out.println( new String(new BASE64Decoder().decodeBuffer( encoding )) );
		} catch (IOException e) {
			e.printStackTrace();
		}
        */

	}
}
