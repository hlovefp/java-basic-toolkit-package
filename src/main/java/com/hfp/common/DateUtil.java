package com.hfp.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName: DateUtil
 * @Description: 时间工具类
 */
public class DateUtil {

	/**
	 * 把输入时间转换成对应格式的字符串
	 * 
	 */
	public static String getFormatTime(String format, Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 当前时间延后 hour个小时后的时间,hour可以为负
	 * @return 返回格式 yyyyMMddHHmmss
	 */
	public static String getDelayHour(int hour) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR_OF_DAY, hour);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(ca.getTime());
	}
	
	/**
	 * 当前时间延后minute个分钟后的时间,minute可以为负
	 * @return 返回格式 yyyyMMddHHmmss
	 * 
	 */
	public static String getDelayMinute(int minute) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MINUTE, minute);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(ca.getTime());
	}

	/**
	 * 获取当前时间
	 * @return 返回YYYYMMDDHHMMSS格式字符串
	 */
	public static String getSdfTimes() {
		return getFormatTime("yyyyMMddHHmmss",new Date());
	}

	/**
	 * 获取当前时间
	 * @return 返回YYYYMMDD格式字符串
	 */
	public static String getDays() {
		return getFormatTime("yyyyMMdd",new Date());
	}

	/**
	 * 获取当前时间
	 * @return 返回HHmmss格式字符串
	 * 
	 */
	public static String getTime() {
		return new SimpleDateFormat("HHmmss").format(new Date());
	}

	/**
	 * 获取当前时间
	 * @return 返回HH:mm:ss格式字符串
	 * 
	 */
	public static String getFormatTime() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}

	/**
	 * 获取今天是星期几，返回数据(1~7)
	 */
	public static int getWeekDay() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 比较日期大小，输入字符串必须是纯数字，不能有空格
	 * 
	 * @return time1 >= time2 返回 true
	 */
	public static boolean checkTimeCompare(String time1, String time2) {
		Long long1 = Long.parseLong(time1);
		Long long2 = Long.parseLong(time2);
		return long1 >= long2;
	}

	/**
	 * 比较时间大小
	 * 
	 * @param time1  格式 HHmmss
	 * @param time2  格式 HHmmss
	 * @return
	 * @throws ParseException
	 */
	public static boolean checkTimeShortCompare(String time1, String time2) throws ParseException {
		DateFormat df = new SimpleDateFormat("HHmmss");
		Date dt1 = df.parse(time1);    // 将字符串转换为date类型
		Date dt2 = df.parse(time2);
		return dt1.getTime() >= dt2.getTime();
	}
	
	public static void main(String[] args) throws Exception {
		// System.out.println(getSdfTimes()+":"+getDelayTime(-15));
		System.out.println(checkTimeShortCompare("112136","112136"));
	}
}
