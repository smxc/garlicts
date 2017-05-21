package com.garlicts.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期操作工具类
 * SimpleDateFormat非线程安全
 * @author wangwei
 */
public class DateUtil {

	private static ThreadLocal<DateFormat> dateFormatThreadLocal = new ThreadLocal<DateFormat>(){
		
		@Override
		protected DateFormat initialValue(){
			return new SimpleDateFormat("yyyyMMddHHmmss");
		}
		
	};
	
	private static ThreadLocal<DateFormat> dbDateFormatThreadLocal = new ThreadLocal<DateFormat>(){
		
		@Override
		protected DateFormat initialValue(){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		
	};
    
    /**
     * 转换日期格式
     * @param 时间字符串  将yyyy-MM-dd HH:mm:ss 转成 yyyyMMddHHmmss
     */
    public static String convert2StandardFormat(String dateTimeStr) {
    	
    	DateFormat dbDateFormat = dbDateFormatThreadLocal.get();
    	DateFormat dateFormat = dateFormatThreadLocal.get();
    	Date date;
    	String formatedDateStr = null;
		try {
			date = dbDateFormat.parse(dateTimeStr);
			formatedDateStr = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		};
    	
    	return formatedDateStr;
    }    

    /**
     * 获取当前日期与时间  yyyyMMddHHmmss
     */
    public static String getCurrentDatetime() {
    	DateFormat datetimeFormat = dateFormatThreadLocal.get();
        return datetimeFormat.format(new Date());
    }

	/**
	 * 将 long 数值转成  date类型
	 */
	/*public static Date formatDate(String format, long millSec){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date(millSec);
		return sdf.parse();
	}*/
    
    /**
     * 获取当前日期与时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrDatetimeWithDbFormat() {
    	DateFormat dbDateFormat = dbDateFormatThreadLocal.get();
        return dbDateFormat.format(new Date());
    }    

    
    /**
     * 生成流水号 
     */
    public static String generateTransId(){
    	String transId = new StringBuffer().append(getCurrentDatetime()).toString();
    	return transId;
    }

	/**
	 * 转化将对应的 日期类型转化成 日期字符串
	 * @param args
	 */
	public static String dateTransStr(String format, Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
    
    public static void main(String[] args) {
		
//    	System.out.println(DateUtil.generateTransId());
    	System.out.println("----------------------------------------------");
    	System.out.println(DateUtil.convert2StandardFormat("2016-02-02 12:00:01"));
    	System.out.println("----------------------------------------------");
//    	System.out.println(DateUtil.getCurrentDatetime());
//    	System.out.println(DateUtil.getCurrDatetimeWithDbFormat());
    	
	}

}
