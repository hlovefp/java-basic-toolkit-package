package com.hfp.util.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName: DateUtil
 * @Description: 时间工具类
 */
public class DateUtil {
	
	/**
	 * 生成时间戳,19700101到现在的毫秒数
	 */
	public static String createTimeStamp() {
		return String.valueOf(System.currentTimeMillis());
	}

	/**
	 * 把输入时间转换成对应格式的字符串
	 * 
	 * 年-月-日 时:分:秒 毫秒 时区代号 时区编号
	 * "yyyy-MM-dd hh:mm:ss SSS z Z"
	 * 2017-12-09 03:19:58 009 CST +0800
	 * 
	 */
	public static String getFormatTime(String format, Date date) {
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * ISODateTimeFormat 2019-05-15T14:18:09.832
	 */
	public static String getISODateTimeFormat() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
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
	 * 
	 * @param time
	 *     格式 HHmmss
	 * @return
	 *   返回的日期是  1970/01/01 HHmmss的Date对象
	 */
	public static Date parseTime2Date(String time){
		DateFormat df = new SimpleDateFormat("HHmmss");
		try {
			return df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param datetime
	 *     格式 yyyyMMddHHmmss
	 * @return
	 */
	public static Date parse2Date(String datetime){
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			return df.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param str
	 * @param position
	 * @param format
	 * @return
	 */
	public static Date parse2Date(String str, int position, String format){
		ParsePosition p = new ParsePosition(position); // 从第position位开始处理
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(str,p);

	}
	
	public static Date parse2Date(String str, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
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
		Date dt1 = parseTime2Date(time1);    // 将字符串转换为date类型
		Date dt2 = parseTime2Date(time2);
		return dt1.getTime() >= dt2.getTime();
	}
	
	
	public static void main(String[] args) throws Exception {
		//Locale.setDefault(Locale.CHINA);

		// System.out.println(getSdfTimes()+":"+getDelayTime(-15));
		//System.out.println(checkTimeShortCompare("112136","112136"));
		//System.out.println(parseTime2Date("120623").toString());        // Thu Jan 01 12:06:23 CST 1970
		//System.out.println(parse2Date("20190413120623").toString());      // Sat Apr 13 12:06:23 CST 2019
		//System.out.println(parse2Date("abc 2017-10-23 15:15:23x", 4, "yyyy-MM-dd").toString());          // Mon Oct 23 00:00:00 CST 2017
		//System.out.println(parse2Date("abc 2017-10-23 15:15:23x", 4, "yyyy-MM-dd HH:mm:ss").toString()); // Mon Oct 23 15:15:23 CST 2017
		//System.out.println(parse2Date("2017-10-23 15:15:23x", "yyyy-MM-dd HH:mm:ss").toString());        // Mon Oct 23 15:15:23 CST 2017

		// 年-月-日 时:分:秒 毫秒 时区代号 时区编号
		//System.out.println(DateFormat.getInstance().format(new Date()));        // 19-4-13 下午12:42
		
		//System.out.println(DateFormat.getDateInstance().format(new Date()));                  // 2019-4-13
		//System.out.println(DateFormat.getDateInstance(DateFormat.LONG).format(new Date()));   // 2019年4月13日
		//System.out.println(DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()));  // 19-4-13
		//System.out.println(DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(new Date())); // April 13, 2019

		//System.out.println(DateFormat.getTimeInstance().format(new Date()));                    // 12:47:11
		//System.out.println(DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()));    // 下午12:55
		//System.out.println(DateFormat.getTimeInstance(DateFormat.LONG).format(new Date()));     // 下午12时55分58秒
		
        //System.out.println(DateFormat.getDateTimeInstance().format(new Date()));                // 2019-4-13 12:48:32
        //System.out.println(DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG).format(new Date()));  // 2019年4月13日 下午12时57分17秒
        //System.out.println(DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.LONG).format(new Date())); // 19-4-13 下午12时57分17秒
        //System.out.println(DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT).format(new Date())); // 2019年4月13日 下午12:57
        //System.out.println(DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(new Date()));// 19-4-13 下午12:57
	}
}
