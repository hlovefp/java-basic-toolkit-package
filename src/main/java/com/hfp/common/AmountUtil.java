package com.hfp.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class AmountUtil {
    
	/** 将元转换成分
	 * @param amount  字符串表示的元,null返回0
	 * @return 整数的分
	 */
	public static int ytf(String amount){
		if(amount == null){
			return 0;
		}
    	return (int)(Double.valueOf(amount) * 100.0);
	}

	/**  
     * 将分为单位的转换为元 （除100）  : "100"转换成 "1"; "101"转换成 "1.01"
     * @param amount  ,不能有小数点
     * @return
     * @throws Exception   
     */    
    public static String fty(String amount) throws Exception{    
        if(!amount.matches("\\-?[0-9]+")) {    
            throw new Exception("金额格式有误");    
        }    
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();    
    }
    
    /**
     * 将分为单位的转换为元 （除100）  : "100"转换成 "1.00"; "101"转换成 "1.01"
     * @param amount
     * @return
     * @throws Exception
     */
    public static String fty2(String amount) throws Exception{    
        if(!amount.matches("\\-?[0-9]+")) {    
            throw new Exception("金额格式有误");    
        }    
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).setScale(2).toString();    
    }  
	
    /**
     * 将分转换成指定长度的字符串，前补0
     * @param length
     * @param fen
     * @return
     */
	public static String formatLeftZero(int length, int fen){
		return String.format("%0"+length+"d",  fen);
	}
	
	/**
	 * 
	 * @param amt
	 * @return
	 *   金额为负 false
	 */
	public static boolean checkAmt(String amt){
		int b;
		try {
		    b = Integer.valueOf(amt).intValue();
		} catch (NumberFormatException e) {
		    return false;
		}
		if(b < 0){
	    	return false;
	    }
		return true;
	}
	
	public static int feeSplit(String amount, String feeRate){
		double f = Double.parseDouble(feeRate);
		double b = Double.parseDouble("1000000");
		DecimalFormat dec = new DecimalFormat("0.00000");
		String s = dec.format(Integer.parseInt(amount) * f/b);
		int fee = 0;
		if(s.indexOf(".") > 0){
			s = s.replaceAll("0+?$", "");// 去掉后面无用的零
	        s = s.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
	        if(s.indexOf(".") > 0){
	        	String[] split = s.split("\\.");
	        	String s0 = split[0];
	        	String s1 = split[1];
	        	if(Integer.parseInt(s1) > 4){
	        		fee = Integer.parseInt(s0) + 1;
	        	}else{
	        		fee = Integer.parseInt(s0);
	        	}
	        }
		}
		return fee;
	}
	
	public static String minusAmount(String amt1, String amt2){
		String ret = "";
		int imt1 = Integer.parseInt(amt1);
		int imt2 = Integer.parseInt(amt2);
		int minus = imt1 - imt2;
		if(minus < 0){
			ret = "-1";
		}else{
			ret = String.valueOf(minus);
		}
		return ret;
	}
	
	public static String addAmount(String amt1, String amt2){
		int imt1 = Integer.parseInt(amt1);
		int imt2 = Integer.parseInt(amt2);
		int adds = imt1 + imt2;
		return String.valueOf(adds);
	}
	
	public static void main(String[] args) throws Exception{
		//System.out.println(ytf("1230.0"));
		//System.out.println(changeF2Y("123.4"));
		//System.out.println(changeF2Y("1234"));
		System.out.println(formatLeftZero(5,1234));
		System.out.println(fty("100"));  // 1
		System.out.println(fty("101"));  // 1.01

		System.out.println(fty2("100")); // 1.00
	}
}
