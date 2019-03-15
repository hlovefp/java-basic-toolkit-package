package com.hfp.code;

import java.nio.charset.Charset;

public class BASE64Util {
	/**
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
	 * 使用commons-io包进行Base解密,输入的加密数据包括字符'+','/'<p>
	 * 输入数据: "5pyA6L+R5YaZ5LqG5Liq5bel5YW3LOWuouaIt+mcgOimgeeUqGphcui/kOihjA=="<p>
	 * 返回数据: "最近写了个工具,客户需要用jar运行"
	 * @param source
	 * @return
	 */
	public static String decode(String source) {
		byte[] input = source.getBytes(Charset.forName("UTF-8"));
		return decode(input);
	}

	public static String decode(byte[] source) {
		byte[] output = new org.apache.commons.codec.binary.Base64().decode(source);
		return new String(output);
	}

	protected static String printByte(byte[] values) {
		StringBuffer sb = new StringBuffer();
		for (byte b : values) {
			sb.append(b).append(" ");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static void main(String[] args) {
		String source = "最近写了个工具,客户需要用jar运行";

		String encodedStr = BASE64Util.encode(source);
		System.out.println("BASE64加密结果:"+encodedStr);

		String decodedStr = BASE64Util.decode(encodedStr);
		System.out.println("BASE64解密结果:"+decodedStr);
	}
}
