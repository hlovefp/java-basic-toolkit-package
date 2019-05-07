package com.hfp.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class InputStreamUtil {
	/**
	 * 将输入流转换字节数组
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream2Byte(InputStream inputStream) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] array = new byte[1024];
		while ((len = inputStream.read(array)) != -1) {
			out.write(array, 0, len);
		}
        out.close();
        inputStream.close();
		return out.toByteArray();
	}

	/**
	 * 将输入流转换为字符串
	 * 待转换为字符串的输入流
	 * @param
	 * @return 由输入流转换String的字符串
	 * @throws IOException
	 */
	public static String readInputStream2String(InputStream inputStream) throws IOException {
		return new String(readInputStream2Byte(inputStream));
	}
	
	/**
	 * 将字符串转换为输入流
	 * 
	 * @param inputString 待转换为输入流的字符串
	 * @return
	 */
	public static InputStream parseString2InputStream(String inputString) {
		ByteArrayInputStream is = null;
		if (inputString != null && !inputString.trim().equals("")) {
			try {
				is = new ByteArrayInputStream(inputString.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return is;
	}
}
