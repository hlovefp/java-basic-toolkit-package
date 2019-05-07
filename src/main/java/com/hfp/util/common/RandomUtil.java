package com.hfp.util.common;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

	/**
	 * 获取一个指定十进制位数长度的随机正整数
	 * 
	 * @param length
	 * @return
	 */
	public static int buildRandom(int length) {
		
		double random = Math.random();  // 0到1之间的数
		random = (random<0.1) ? (random+0.1) : random;
		
		int num = 1;
		for (int i = 0; i < length; i++) {
			num *= 10;
		}
		
		return (int)(random * num);
	}
	
	/**
	 * 32个字符长度的 UUID:  c63971e44e804c49a044fa6da96058e3
	 */
	public static String randomUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static void main(String[] args){
		//System.out.println(buildRandom(4));
		//System.out.println(randomUUID());
		Random random = new Random();
		System.out.println(random.nextInt());
	}
}
