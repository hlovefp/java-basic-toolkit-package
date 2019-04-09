package com.hfp.code;

import javax.xml.bind.DatatypeConverter;

/**
 * 
 * @ClassName: ConvertUtils
 * @Description: 格式转化工具类
 *
 */
public abstract class HexUtil {

	/**
	 * 字节数组转换成Hex字符串<p>
	 * 0x1B 0x1A ==> "1b1a"
	 * @param input
	 * @return
	 */
	public static String toHex(byte input[]) {
		if (input == null)
			return "";
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			//Integer.toHexString(current);
			output.append(Integer.toString(current, 16));
		}
		return output.toString();
	}
	public static String toHex(byte input[], int len) {
		if (input == null)
			return "";
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < len; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			//Integer.toHexString(current);
			output.append(Integer.toString(current, 16));
		}
		return output.toString();
	}
	
	/**
	 * Hex字符串转换成字节数组<p>
	 * "1b1a"  ==> 0x1B 0x1A
	 * @param hex
	 * @return
	 */
	public static byte[] toByteArray(String hex){
		byte[] b = new byte[hex.length()/2];
		int hexIndex = 0;
        for (int bIndex = 0; bIndex < b.length; bIndex++) {
            char high = hex.charAt(hexIndex++);
            char low  = hex.charAt(hexIndex++);
            b[bIndex] = (byte) ((parse(high) << 4) | parse(low));
        }
        return b;
	}
	
	/**
	 * 将16进制的字符转换成十进制的值
	 * @param c
	 * @return
	 */
    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;

    }

	public static void main(String[] args){
		byte[] input = new byte[2];
		input[0] = 0x1B;  // 1*16 + 11 = 27
		input[1] = 0x1A;
		
		/*
		String hex = toHex(input);
		System.out.println(hex);  // 1b1a
		byte[] out = toByteArray(hex);
		System.out.println(out[0]);  //27
		System.out.println(out[1]);
		*/
		
		
		String hex = DatatypeConverter.printHexBinary(input);
		System.out.println(hex); // 1B1A
		byte[] out = DatatypeConverter.parseHexBinary(hex);
		System.out.println(out[0]);  //27
		System.out.println(out[1]);
		
	}
}
