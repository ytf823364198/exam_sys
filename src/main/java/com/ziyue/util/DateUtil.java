package com.ziyue.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * util型的日期数据和sql型的日期数据进行互换，并
 * 提供日期数据的格式化输出。
 * @author Fangrn
 * @since 2006-08-02
 * @version 1.0.1
 */
public class DateUtil {
	private static final String Simple_Date_Format = "yyyy-MM-dd";
	private static final int Simple_Date_Format_Length = Simple_Date_Format.length();
	private static final String Simple_DateTime_Format = "yyyy-MM-dd HH:mm:ss";
	private static final String Chain_Simple_Date_Format = "yyyy年MM月dd日";

	/**
	 * 字符串转换为普通的日期
	 * @param str 日期格式Simple_Date_Format，Simple_DateTime_Format
	 * @return  java.util.Date
	 */
	public static java.util.Date strToSysDate(String str) {
		if (null != str && str.length() > 0) {
			try {
				if (str.length() <= Simple_Date_Format_Length) { // 只包含日期。
					return (new SimpleDateFormat(Simple_Date_Format))
							.parse(str);
				} else { // 包含日期时间
					return (new SimpleDateFormat(Simple_DateTime_Format))
							.parse(str);
				}
			} catch (ParseException error) {
				return null;
			}
		} else
			return null;
	}
	
	
	/**
	 * 字符串转换为sql的日期
	 * @param str String
	 * @return java.sql.Date
	 */
	public static java.sql.Date strToSqlDate(String str) {
		if (strToSysDate(str) == null || str.length() < 1)
			return null;
		else
			return new java.sql.Date(strToSysDate(str).getTime());
	}

	/**
	 * sql日期型转换为带时间的字符串
	 * @param dDate　java.sql.Date
	 * @return String "yyyy-MM-dd HH:mm:ss";
	 */
	public static String toDateTimeStr(java.sql.Date dDate) {
		if (dDate == null) {
			return null;
		} else {
			return (new SimpleDateFormat(Simple_DateTime_Format)).format(dDate);
		}
	}
	
	/**
	 * 普通日期型转换为带时间的字符串
	 * @param dDate java.util.Date
	 * @return String "yyyy-MM-dd HH:mm:ss";
	 */
	public static String toDateTimeStr(java.util.Date dDate) {
		if (dDate == null) {
			return null;
		} else {
			return (new SimpleDateFormat(Simple_DateTime_Format)).format(dDate);
		}
	}
	
	/**
	 * 中文时间
	 * @param dDate java.util.Date
	 * @return String "yyyy年MM月dd日";
	 */
	public static String toChainDateTimeStr(java.util.Date dDate) {
		if (dDate == null) {
			return null;
		} else {
			return (new SimpleDateFormat(Chain_Simple_Date_Format)).format(dDate);
		}
	}
	
	/**
	 * 中文时间
	 * @param date java.util.String
	 * @return String "yyyy年MM月dd日";
	 */
	public static String toChainDateTimeStr(String date) {
		if (date == null || "".equals(date)) {
			return "";
		} else {
			Date d = strToSysDate(date);
			return toChainDateTimeStr(d);
		}
	}	
	
	/**
	 * sql日期型转换为不带时间的字符串
	 * @param d java.sql.Date
	 * @return String "yyyy-MM-dd"
	 */
	public static String toDateStr(java.sql.Date d) {
		if (d == null) {
			return "";
		} else {
			return (new SimpleDateFormat(Simple_Date_Format)).format(d);
		}
	}
	
	public static String toDateStr(java.util.Date d, String format ){
		return (new SimpleDateFormat(format)).format(d);
	}
	
	
	/**
	 * 普通日期型转换为不带时间的字符串
	 * @param d java.util.Date
	 * @return　String String "yyyy-MM-dd"
	 */
	public static String toDateStr(java.util.Date d) {
		if (d == null) {
			return "";
		} else {
			return (new SimpleDateFormat(Simple_Date_Format)).format(d);
		}
	}
	
	/**
	 * 获得当时的时间
	 * @return java.sql.Date
	 */
	public static java.sql.Date getCurrentDate() {
		return new java.sql.Date(new java.util.Date().getTime());
	}
	
	/**
	 * 获得当前的日期和时间（日历）
	 * @return java.util.Date
	 */
	public static java.util.Date getCurrentDateTime(){
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * 将util型的日期型的数据转换为sql型的日期数据
	 * @param date java.util.Date
	 * @return java.sql.Date
	 */
	public static java.sql.Date utilToSql(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}
	
	/**
	 * 将sql型的日期型的数据转换为util型的日期数据
	 * @param date java.sql.Date
	 * @return java.util.Date
	 */
	public static java.util.Date sqlToUtil(java.sql.Date date) {
		return new java.util.Date(date.getTime());
	}
	
	/**
	 * 将日期和时间复合（组合）起来。
	 * @param date java.sql.Date
	 * @param time java.sql.Time
	 * @return java.sql.Date
	 */
	public static java.sql.Date compositeDateTime(java.sql.Date date,java.sql.Time time) {
		if (null == date || null == time)
			return null;
		Calendar calDate = new GregorianCalendar();
		calDate.setTimeInMillis(date.getTime());
		Calendar calTime = new GregorianCalendar();
		calTime.setTimeInMillis(time.getTime());
		Calendar calCompositeDateTime = new GregorianCalendar();
		int iYear = calDate.get(Calendar.YEAR);
		int iMonth = calDate.get(Calendar.MONTH);
		int iDay = calDate.get(Calendar.DATE);
		int iHour = calTime.get(Calendar.HOUR_OF_DAY);
		int iMin = calTime.get(Calendar.MINUTE);
		int iSec = calTime.get(Calendar.SECOND);
		int iMSec = calTime.get(Calendar.MILLISECOND);
		calCompositeDateTime.set(iYear, iMonth, iDay, iHour, iMin, iSec);
		calCompositeDateTime.set(Calendar.MILLISECOND, iMSec);
		return utilToSql(calCompositeDateTime.getTime());
	}
	
	/**
	 * 解析字符串型的日期型数据
	 * @param strDate 类似于：
	 * @return 解析后的日期对象
	 */
	public static java.util.Date parseDate(String strDate) {
		long r=0;
		try{
			StringTokenizer token = new StringTokenizer(strDate," ");
			String date=token.nextToken();
			Date now=java.sql.Date.valueOf(date);
			r = now.getTime();
			try {
				String time=token.nextToken();
				StringTokenizer tkTime= new StringTokenizer(time,":");
				r += Integer.parseInt(tkTime.nextToken())*60*60*1000;
				r += Integer.parseInt(tkTime.nextToken())*60*1000;
				r += Integer.parseInt(tkTime.nextToken())*1000;
			}
			catch(Exception ex) {
				r = now.getTime();
			}
		}
		catch(Exception ex)
		{
			return new Date();
		}
		return new Date(r);
	}
	
	
	public static String fullTimeNoFormat() {
		return fullTimeNoFormat(new Date());
	}
	
	public static String fullTimeNoFormat(long date) {
		return fullTimeNoFormat(new Date(date));
	}
	
	public static String shortDateForChina(long date) {
		return shortDateForChina(new Date(date));
	}
	/**
	 * 日期数据格式化
	 * @param date
	 * @return yyyy 年 MM 月 dd 日
	 */
	public static String shortDateForChina(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
		try {
			r = formater.format(date);
		} 
		catch(Exception ex) {
			r = formater.format(new Date());
		}
		return r;
	}
	
	/**
	 * 将日期数据对象格式化为字符串。
	 * @param date java.util.Date
	 * @return 格式类似于：yyyyMMddHHmmss。
	 */
	public static String fullTimeNoFormat(java.util.Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			r = formater.format(date);
		} 
		catch(Exception ex) {
			r = formater.format(new Date());
		}
		return r;
	}
	
	/**
	 * 将日期数据对象格式化为固定字符串格式。
	 * @param date 
	 * @return 格式类似于：yyyy-MM-dd HH:mm:ss
	 */
	public static String fullTime(java.util.Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			r = formater.format(date);
		} 
		catch(Exception ex) {
			r = formater.format(new Date());
		}
		return r;
	}
	
	/**
	 * 将当前日期数据对象格式化为固定字符串格式。
	 * @return 格式类似于：yyyy-MM-dd HH:mm:ss
	 */
	public static String fullTime() {
		return fullTime(new Date());
	}
	
	/**
	 * 将long日期数据对象格式化为固定字符串格式。
	 * @param long 
	 * @return 格式类似于：yyyy-MM-dd HH:mm:ss
	 */
	public static String fullTime(long date) {
		return fullTime(new Date(date));
	}
	
	/**
	 * 将日期数据对象格式化为固定字符串格式。
	 * @param date
	 * @return 格式类似于：yyyy-MM-dd
	 */
	public static String shortDate(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		try {
			r = formater.format(date);
		} 
		catch(Exception ex) {
			r = "" ;
		}
		return r;
	}
	
	/**
	 * 将当前日期数据对象格式化为固定字符串格式。
	 * @return 格式类似于：yyyy-MM-dd
	 */
	public static String shortDate() {
		return shortDate(new Date());
	}
	
	/**
	 * 将long数据对象格式化为固定字符串格式。
	 * @param long
	 * @return 格式类似于：yyyy-MM-dd
	 */
	public static String shortDate(long date) {
		return shortDate(new Date(date));
	}
	
	/**
	 * 将日期数据对象格式化为固定字符串格式。
	 * @param date
	 * @return 格式类似于：HH:mm:ss
	 */
	public static String shortTime(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
		try {
			r = formater.format(date);
		} 
		catch(Exception ex) {
			r = formater.format(new Date());
		}
		return r;
	}
	
	/**
	 * 将当前日期数据对象格式化为固定字符串格式。
	 * @return 格式类似于：HH:mm:ss
	 */
	public static String shortTime() {
		return shortTime(new Date());
	}
	
	/**
	 * 将long日期数据对象格式化为固定字符串格式。
	 * @param long
	 * @return 格式类似于：HH:mm:ss
	 */
	public static String shortTime(long date) {
		return shortTime(new Date(date));
	}
	
	/**
	 * 获得某个月的第一天0时0分0秒的时间
	 * @param year 
	 * @param month
	 * @return java.util.Date
	 */
	public  static java.util.Date getFirstDateOfMonth(int year,int month){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		return cal.getTime();
	}
	
	/**
	 * 获取某个月的某一天的0时0分0秒的时间
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public  static java.util.Calendar getFirstTimeOfDay(int year,int month,int day){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		return cal;
	}
	
	/**
	 * 将java.util.Date 转换为java.util.Calendar
	 * @param date java.util.Date date
	 * @return java.util.Calendar
	 */
	public static java.util.Calendar DateToCalendar(java.util.Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Date addOneSecond(Date date) {    
	    Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(date);    
	    calendar.add(Calendar.SECOND, 1);    
	    return calendar.getTime();    
	}   
	
	public static Date addSecond(Date date,int second ) {    
	    Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(date);    
	    calendar.add(Calendar.SECOND, second);   
	    return calendar.getTime();    
	}
	
	public static Date beforeDay(Date date,int day ) {    
	    Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(date);    
	    calendar.add(Calendar.DATE, -day);   
	    return calendar.getTime();    
	}
	
	public static Date beforeDay(int day ) {    
	    Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(new Date());    
	    calendar.add(Calendar.DATE, -day);   
	    return calendar.getTime();    
	}
	
	public static String getYear(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy");
		try {
			r = formater.format(date);
		} 
		catch(Exception ex) {
			r = "" ;
		}
		return r;
	}   

	public static String getDateLong() {
		return String.valueOf(new Date().getTime());
	}   
	
}

