/**
 * FileName: DateUtil.java
 */
package com.brainsoon.solr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
	private static String defaultTimePattern = "yyyy-MM-dd HH:mm:ss:SSS";	
	public static String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
	
	public static String getDateTime(Date date) {
		return getDateTime(date, defaultTimePattern);
	}
	
	public static Date parseTime(String time) throws ParseException {
		return parseTime(time, defaultTimePattern);
	}
	public static String getDateBy8Hours(Date date){
		SimpleDateFormat df=new SimpleDateFormat(dateTimePattern);   
		String newDate = null;
		long time = date.getTime();
		long hours = 8*60*60*1000;
		long newTime = time-hours;
		newDate = df.format(new Date(newTime));
		return newDate;
	}
	
    public static final String getDateTime(Date date, String aMask) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (date != null) {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(date);
        }

        return (returnValue);
    }
    
    public static final Date parseTime(String time, String aMask) throws ParseException {
        SimpleDateFormat df = null;
        Date returnValue = null;

        if (time != null) {
            df = new SimpleDateFormat(aMask);
			returnValue = df.parse(time);
        }

        return (returnValue);
    }
    
	public static Date add(Date date, int type, int val) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, val);
		
		return calendar.getTime();
	}
	
	
	/**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     * 
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(String datePattern, Date aDate) {
        return getDateTime(datePattern, aDate);
    }
    
    
    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * 
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        if (aDate == null) {
        	return "";
        }
        
        return (new SimpleDateFormat(aMask)).format(aDate);
    }
    
    /** 
     * 将yyyy-MM-dd格式的日期字符串转换为Date(yyyy-MM-dd HH:mm:ss:SSS)对象
     * @param strShortDate 
     * @throws ParseException
     */
    public static final Date convertShortStringDateToDate(String strShortDate)
			throws ParseException {
    	Date d = null;
		if (StringUtils.isBlank(strShortDate)) {
			return d;
		}else{
			if(strShortDate.length() == 10){
				strShortDate += " 00:00:00:000";
				d = DateUtil.parseTime(strShortDate);
			}else{
				d = parseTime(strShortDate,"yyyy-MM-dd HH:mm:ss:SSS");
			}
		}
		return d; 
	}
    
    public static void main(String args[]) {
    	String keywords = "Application of";
    	keywords = keywords.replaceAll(" ", "\\\\ ");
    	System.out.println(keywords);
//    	Date currentDate=new Date();
//    	System.out.println("current Date="+currentDate.getTime());    	
    	Date date;
    	String dateStr ="2012-12-11 01:00:00";
		try {
			//Date d = new Date(dateStr);
			//date =  DateUtil.convertDateToString(defaultTimePatternsolr, d);
			date = parseTime("2015-12-09", "yyyy-MM-dd");
			System.out.println("date======== " + date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
