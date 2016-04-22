package com.channelsoft.appframe.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.channelsoft.appframe.exception.ServiceException;

/**
 * <dl>
 * <dt></dt>
 * <dd>Description:时间工具类</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>@author 李炜</dd>
 * <dd>@author 张乐雷</dd>
 * <dd>@date 2007-2-25 22:39:32</dd>
 * </dl>
 */
/**
 * <dl>
 * <dt>DateUtil</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2008-9-28</dd>
 * </dl>
 * 
 * @author xxxxx
 */
public class DateUtil {

private static Log log = LogFactory.getLog(DateUtil.class);
    
    private static String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    private static String datePattern = "yyyy-MM-dd";
    private static String timePattern = "HH:mm";
    
    public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY; //中国周一是一周的第一天 

    /**
     * Return default datePattern (yyyy-MM-dd)
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return datePattern;
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to yyyy-MM-dd.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        if (aDate == null) {
        	return "";        	
        }
        SimpleDateFormat df = new SimpleDateFormat(datePattern);
        return df.format(aDate);        
    }



    /** 
     * 按aMask的格式定义，把日期格式的字符串strDate转换为Date对象
     * @param aMask
     * @param strDate
     * @return     如果日期格式的字符串strDate为空/null，则返回null
     * @throws ParseException
     */
    public static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '"
					+ aMask + "'");
		}
		if (StringUtils.isEmpty(strDate)) {
			//throw new ParseException("待转换的日期格式字符串不能为空！",-999);
			return null;
		} 
		return new SimpleDateFormat(aMask).parse(strDate); 
	}

    /**
	 * This method returns the current date time in the format: MM/dd/yyyy HH:MM
	 * a
	 * 
	 * @param theTime
	 *            the current time
	 * @return the current date/time
	 */
    public static String convertTimeToString(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: yyyy-MM-dd
     * 
     * @return the current date
     * @throws ParseException
     */
    public static Date getToday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * This method returns the yesterday date in the format: yyyy-MM-dd
     * @return
     *
     * @author Fuwenbin
     * @date Jun 8, 2010 9:41:08 AM 
     */
    public static Date getYesterday(){
        Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * @return 明天日期，0时0分0秒
     *
     * @author qinbq
     * @date 2008-9-28 上午09:41:16 
     */
    public static Date getTomorrow(){
    	return getNextDate(getToday());
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
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     * 
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(datePattern, aDate);
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
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     * 
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateTimeToString(Date aDate) {
        return getDateTime(dateTimePattern, aDate);
    }
    
    /**
     * This method converts a String to a date using the datePattern
     * 
     * @param strDate the date to convert (in format yyyy-MM-dd)
     * @return a date object
     * 
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate)
			throws ParseException {
		if (log.isDebugEnabled()) {
			log.debug("converting string ["+strDate+"] to Date with pattern[" + datePattern+"]");
		}

		return convertStringToDate(datePattern, strDate);
	}
    
    /**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate
	 *            the date to convert (in format yyyy-MM-dd HH:mm:ss )
	 * @return a date object
	 * 
	 * @throws ParseException
	 */
    public static Date convertStringToDateTime(String strDate) throws ParseException {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + dateTimePattern);
            }

            aDate = convertStringToDate(dateTimePattern, strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate
                      + "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(),
                                     pe.getErrorOffset());
                    
        }
        return aDate;
    }
    
    /**
     * 返回一个指定日期的下一天
     *
     * @param date 初始日期
     * @return 初始日期加一天后的日期
     * @author zhanglelei
     * @date 2007-2-25 22:30:29 
     */
    public static Date getNextDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
    }
    
    /**
     * 返回一个指定日期的下days天
     * @param date
     * @param days
     * @return
     *
     * @author Fuwenbin
     * @date May 14, 2010 10:22:59 AM 
     */
    public static Date getNextDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
    }
    
    /**
     * 返回一个指定日期的前一天
     *
     * @param date 初始日期
     * @return 初始日期加一天后的日期
     * @author zhanglelei
     * @date 2007-2-25 22:30:29 
     */
    public static Date getPrevDay(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.DAY_OF_MONTH, -1);
    	return calendar.getTime();
    }
    
    /**
     * 取得一个指定日期所在月的第一天
     * 
     * @return
     * @author liuwenu
     */
    public static Date getFirstDateOf(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.set(Calendar.DATE, 1);
		calender.set(Calendar.HOUR_OF_DAY, 0);
		calender.set(Calendar.MINUTE, 0);
		calender.set(Calendar.SECOND, 0);
		return calender.getTime();
    }
    
    /**
     * 获得一个指定日期所在月的下个月的第一天
     * @param date
     * @return
     */
    public static Date getFirstDateInNextMonthOf(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		return calendar.getTime();
    }
    /**
     * 取得一个指定日期所在月的最后一天
     * 
     * @return
     * @author liuwenu
     */
    public static Date getLastDateOf(Date date) {
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
    }
    
    /**
     * 取得以yyyyMMddHHmmss格式的时间戳字符串
     * 
     * @return
     * @author 王志明
     */
    public static final String getCompactTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }
    
    /**
     * 取得指定日期所在的月份
     * @param date
     * @return
     * @author 王志明
     */
    public static final int getMonthOf(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
    }
    
    /**
     * 取得指定日期所在的年份
     * @param date
     * @return
     * @author 刘彦军
     */
    public static final int getYearOf(Date date){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	return calendar.get(Calendar.YEAR);
    }
    
    /**
     * 获取指定日期的日子
     * @param date
     * @return
     *
     * @author 刘彦军   
     * 2007-10-11
     */
    public static final int getDayOf(Date date){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 取得指定日期所在的小时
     * @param date
     * @return
     * @author 王志明
     */
    public static final int getHourOf(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR);
    }
    
    /**
     * 比较源日期是否在目标日期之前
     *
     * @param srcDateString 源日期字符串
     * @param targetDateString 目标日期字符串
     * @param pattern 字符串日期格式
     * @return 
     * @author zhanglelei
     * @date 2007-2-25 18:40:37 
     */
    public static final boolean before(String srcDateString,
    		String targetDateString, String pattern) {
    	Date srcDate;
    	try {
    		srcDate = DateUtil.convertStringToDate(pattern, srcDateString);
    	} catch (ParseException e) { 
    		throw new ServiceException("源日期格式转换异常：["+srcDateString+"]",e);
    	}
    	Date targetDate;
    	try {
    		targetDate = DateUtil.convertStringToDate(pattern, targetDateString);
    	} catch (ParseException e) {
    		throw new ServiceException("目标日期格式转换异常：["+targetDateString+"]",e);
    	}
    	return before(srcDate, targetDate);
    }
    
    /**
     * 比较源日期是否在目标日期之前,日期格式为 yyyy-MM-dd
     * @param srcDateString
     * @param targetDateString
     * @return  
     */
    public static final boolean before(String srcDateString,
			String targetDateString) {
    	Date srcDate;
		try {
			srcDate = DateUtil.convertStringToDate(srcDateString);
		} catch (ParseException e) { 
			throw new ServiceException("源日期格式转换异常：["+srcDateString+"]",e);
		}
    	Date targetDate;
		try {
			targetDate = DateUtil.convertStringToDate(targetDateString);
		} catch (ParseException e) {
			throw new ServiceException("目标日期格式转换异常：["+targetDateString+"]",e);
		}
		return before(srcDate, targetDate);
	}
    
    /**
     * 比较源日期是否在目标日期之前,日期格式为 yyyy-MM-dd
     * @param srcDateString
     * @param targetDate
     * @return
     */
    public static final boolean before(String srcDateString,Date targetDate) { 
		try {
			Date srcDate = DateUtil.convertStringToDate(srcDateString);
			return before(srcDate, targetDate);
		} catch (ParseException e) { 
			throw new ServiceException("源日期格式转换异常：["+srcDateString+"]",e);
		}
    }
    /**
     * 比较源日期是否在目标日期之后,日期格式为 yyyy-MM-dd
     * @param srcDateString
     * @param targetDate
     * @return
     */
    public static final boolean after(String srcDateString,Date targetDate) { 
		try {
			Date srcDate = DateUtil.convertStringToDate(srcDateString);
			return after(srcDate, targetDate);
		} catch (ParseException e) { 
			throw new ServiceException("源日期格式转换异常：["+srcDateString+"]",e);
		}
    }
    /**
     * 比较源日期是否在当前日期之前,日期格式为 yyyy-MM-dd
     * @param srcDateString
     * @return
     */
    public static final boolean beforeCurrentDate(String srcDateString) {
    	return before(srcDateString, new Date());	
    }
    /**
     * 比较源日期是否在当前日期之后,日期格式为 yyyy-MM-dd
     * @param srcDateString
     * @return
     */
    public static final boolean afterCurrentDate(String srcDateString) {
    	return after(srcDateString, new Date());	
    }
    /**
     * 比较源日期是否在目标日期之前,日期格式为 yyyy-MM-dd
     * @param srcDate
     * @param targetDate
     * @return
     */
    public static final boolean before(Date srcDate,Date targetDate) {
    	return srcDate.before(targetDate);
    }
    /**
     * 比较源日期是否在目标日期之后,日期格式为 yyyy-MM-dd
     * @param srcDate
     * @param targetDate
     * @return
     */
    public static final boolean after(Date srcDate,Date targetDate) {
    	return srcDate.after(targetDate);
    }
    
    /**
     * 比较源日期是否在目标日期之后,日期格式为 yyyy-MM-dd
     * @param srcDateString
     * @param targetDateString
     * @return
     */
    public static final boolean after(String srcDateString,
    		String targetDateString) {
    	return before(targetDateString, srcDateString);
    }

    /**
     * 比较源日期是否在目标日期之后
     *
     * @param srcDateString 源日期字符串
     * @param targetDateString 目标日期字符串
     * @param pattern 字符串日期格式
     * @return
     * @author zhanglelei
     * @date 2007-2-25 18:48:27 
     */
    public static final boolean after(String srcDateString,
			String targetDateString, String pattern) {
    	return before(targetDateString, srcDateString, pattern);
    }

    /**
     * 按aMask的格式定义，从日期格式的字符串strDate中提取年月信息。
	 * @param aMask
	 * @param strDate
	 * @return
	 * @throws ParseException
	 *
	 * @author Fuwenbin
	 * @date Jul 6, 2007 12:53:53 PM 
	 */
	public static String getYearMonth(String aMask, String strDate) {
		try {
			return DateUtil.convertDateToString(aMask,
					convertStringToDate(strDate));
		} catch (ParseException e) {
			throw new ServiceException("日期格式转换异常：[" + strDate + "]", e);
		}
	}
	
	/**
	 * 比较日期格式的两个字符串中年月信息是否相同。
	 * @param srcDateString
	 * @param targetDateString
	 * @return
	 * @throws ParseException
	 *
	 * @author Fuwenbin
	 * @date Jul 6, 2007 12:51:20 PM 
	 */
	public static boolean equalsYearMonth(String srcDateString,
			String targetDateString) {
		return StringUtils.equals(getYearMonth("yyyyMM", srcDateString),
				getYearMonth("yyyyMM", targetDateString));
	}
	
    //**************************************************************** xsm报表统计使用
	
    /**
     * 返回一个指定日期的前一天
     *
     * @param date 初始日期
     * @return 初始日期减一天后的日期
     * @author wangzm
     * @date 2007-9-5 
     */
    public static Date getPreDate(String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(convertStringToDate(date));
		} catch (ParseException e) {
			log.error(e);
			return null;
		}
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
    }
    
    /**
     * 返回前days天
     * @param date
     * @param days
     * @return
     *
     * @author Fuwenbin
     * @date Jun 21, 2010 5:32:35 PM 
     */
    public static Date getPreDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTime();
    }
    
	/**
	 * 从日期获得上月月份(格式：yyyy-MM)
	 * @param strDate(格式：yyyy-MM-dd)
	 * @return
	 *
	 * @author 王志明
	 * CreateDate: 2007-6-25
	 */
	@SuppressWarnings("deprecation")
	public static String getLastMonth(String strDate){
		try {
			Date date = DateUtil.convertStringToDate(strDate);
			date.setMonth(date.getMonth()-1);
			String month = DateFormatUtils.format(date, "yyyy-MM");
			return month;
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 从日期获得当月月份(格式：yyyy-MM)
	 * @param strDate(格式：yyyy-MM-dd)
	 * @return
	 *
	 * @author 王志明
	 * CreateDate: 2007-6-25
	 */
	public static String getCurrentMonth(String strDate){
		try {
			Date date = DateUtil.convertStringToDate(strDate);
			String month = DateFormatUtils.format(date, "yyyy-MM");
			return month;
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 返回本周周一日期
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Jun 8, 2010 1:32:17 PM 
	 */
	public static Date getWeekFirstDay(){
		String currentDate = DateUtil.convertDateToString(getToday());
		try {
			return DateUtil.convertStringToDate(getWeekFirstDay(currentDate));
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 返回本周周日日期
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Jun 8, 2010 1:34:12 PM 
	 */
	public static Date getWeekLastDay(){
		try {
			return DateUtil.convertStringToDate(getWeekLastDay(getToday()));
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 从日期获得当周周一时间(格式：yyyy-MM-dd)
	 * @param strDate(格式：yyyy-MM-dd)
	 * @return
	 *
	 * @author 王志明
	 * CreateDate: 2007-6-25
	 */
	public static String getWeekFirstDay(String strDate){
		try {
			Date date = DateUtil.convertStringToDate(strDate);
			return getWeekFirstDay(date);
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 重载getWeekFirstDay方法
	 * @param date
	 * @return
	 */
	public static String getWeekFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);   
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		return DateUtil.convertDateToString(cal.getTime());
	}
	
	
	/**
	 * 返回周末最后一天
	 * @param date
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Jun 8, 2010 11:13:13 AM 
	 */
	public static String getWeekLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);   
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  

		return DateUtil.convertDateToString(cal.getTime());
	}
	
	/**
	 * 返回上周周日日期
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Jun 8, 2010 1:37:53 PM 
	 */
	public static Date getLastWeekLastDay() {
		try {
			return DateUtil.convertStringToDate(DateUtil
					.getLastWeekLastDay(new Date()));
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 返回上周周一日期
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Jun 8, 2010 1:39:47 PM 
	 */
	public static Date getLastWeekFirstDay() {
		try {
			return DateUtil.convertStringToDate(DateUtil
					.getLastWeekFirstDay(new Date()));
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 从日期获得上周周末时间(格式：yyyy-MM-dd)
	 * 
	 * @param strDate(格式：yyyy-MM-dd)
	 * @return
	 * 
	 * @author 王志明 CreateDate: 2007-6-25
	 */
	public static String getLastWeekLastDay(String strDate){
		try {
			Date date = DateUtil.convertStringToDate(strDate);
			return getLastWeekLastDay(date);
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 重载getLastWeekLastDay方法
	 * @param date
	 * @return
	 */
	public static String getLastWeekLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int week = cal.get(Calendar.DAY_OF_WEEK);
		if (week == 1) {
			cal.add(Calendar.DATE, -7);
		} else {
			cal.set(Calendar.DAY_OF_WEEK, 1);
		}
		return DateUtil.convertDateToString(cal.getTime());
	}
	
	/**
	 * 从日期获得上周周一时间(格式：yyyy-MM-dd)
	 * 
	 * @param strDate(格式：yyyy-MM-dd)
	 * @return
	 * 
	 * @author 王志明 CreateDate: 2007-6-25
	 */
	public static String getLastWeekFirstDay(String strDate){
		try {
			Date date = DateUtil.convertStringToDate(strDate);
			return getLastWeekFirstDay(date);
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 重载getLastWeekFirstDay方法
	 * @param date
	 * @return
	 */
	public static String getLastWeekFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(2);
		cal.setTime(date);

		int week = cal.get(Calendar.DAY_OF_WEEK);
		log.debug(week);
		cal.set(Calendar.DAY_OF_WEEK, 2);

		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 7);

		return DateUtil.convertDateToString(cal.getTime());
	}
	/**
	 * 从日期获得周(从1开始，最大值为7)暂定周日为一周最后一天
	 * 
	 * @param strDate(格式：yyyy-MM-dd)
	 * @return
	 * 
	 * @author 王志明 CreateDate: 2007-6-25
	 */
	public static int getWeek(String strDate){
		try {
			Date date = DateUtil.convertStringToDate(strDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			int week = cal.get(Calendar.DAY_OF_WEEK);
			
			return week;
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 返回上月第一天
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Jun 8, 2010 1:41:58 PM 
	 */
	public static Date getFirstDateOfLastMonth() {
		try {
			String strDate = DateUtil.convertDateToString(new Date());
			return DateUtil.convertStringToDate(DateUtil
					.getFirstDateOfLastMonth(strDate));
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
	/**
	 * 返回上月的最后一天
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Jun 8, 2010 1:44:01 PM 
	 */
	public static Date getLastDateOfLastMonth() {
		try {
			String strDate = DateUtil.convertDateToString(new Date());
			return DateUtil.convertStringToDate(DateUtil
					.getLastDateOfLastMonth(strDate));
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
	}
	
    /**
     * 返回上月第一天
     * @param strDate
     * @return
     *
     * @author Fuwenbin
     * @date Jun 8, 2010 11:35:43 AM 
     */
    public static String getFirstDateOfLastMonth(String strDate) {
    	Date date;
		try {
			date = DateUtil.convertStringToDate(strDate);
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		return DateUtil.convertDateToString(calendar.getTime());
    }
	
    /**
     * 取得一个指定日期上月的最后一天
     * 
     * @return
     * @author liuwenu
     */
    public static String getLastDateOfLastMonth(String strDate) {
    	Date date;
		try {
			date = DateUtil.convertStringToDate(getLastMonth(strDate) + "-01");
		} catch (ParseException e) {
			throw new ServiceException("日期格式不正确:" + e);
		}
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return DateUtil.convertDateToString(calendar.getTime());
    }
    
    /**
     * 是否为当前月份的最后一天
     * @param strDate
     * @return
     */
    public static boolean isLastDateOfCurrentMonth(){
    	Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		String date = DateFormatUtils.format(calendar.getTime(),"MMdd");
		if(date.substring(2).equals("01"))
			return true;
		return false;
    }
    
    /**
     * 去掉日期之间的分隔符
     * @param strDate
     * @return
     *
     * @author 王志明
     * CreateDate: 2007-7-18
     */
    public static String convertToNoSeparateStringDate(String strDate){
    	return strDate.substring(0,4) + strDate.substring(5,7) + strDate.substring(8,10);
    }
    
    /**
     * 取得两个日期的间隔天数
     * @param date1
     * @param date2
     * @return
     */
    public static int getOffsetDays(Date date1, Date date2){
    	Calendar cal1 = Calendar.getInstance();
    	cal1.setTime(date1);
    	cal1.set(Calendar.HOUR_OF_DAY, 0);
    	cal1.set(Calendar.MINUTE, 0);
    	cal1.set(Calendar.SECOND, 0);
    	
    	Calendar cal2 = Calendar.getInstance();
    	cal2.setTime(date2);
    	cal2.set(Calendar.HOUR_OF_DAY, 0);
    	cal2.set(Calendar.MINUTE, 0);
    	cal2.set(Calendar.SECOND, 0);
    	
    	long offsetMs = cal2.getTimeInMillis() - cal1.getTimeInMillis();
    	
    	return Math.abs(new Long(offsetMs / (3600000L * 24)).intValue());
    }
    
    /**
     * 取得季度第一天
     * @param date
     * @return
     *
     * @author Fuwenbin
     * @date Jun 8, 2010 11:48:22 AM 
     */
    public static Date getFirstDateOfSeason(Date date) {   
        return getFirstDateOf(getSeasonDate(date)[0]);   
    }   
  
    /**
     * 取得季度最后一天 
     * @param date
     * @return
     *
     * @author Fuwenbin
     * @date Jun 8, 2010 11:48:31 AM 
     */
    public static Date getLastDateOfSeason(Date date) {   
        return getLastDateOf(getSeasonDate(date)[2]);   
    }   
    
    /**
     * 取得上季度第一天
     * @param date
     * @return
     *
     * @author Fuwenbin
     * @date Jun 8, 2010 11:48:22 AM 
     */
    public static Date getFirstDateOfLastSeason(Date date) {
        return getFirstDateOf(getSeasonDate(getLastDateOfLastSeason(date))[0]);   
    }   
  
    /**
     * 取得上季度最后一天 
     * @param date
     * @return
     *
     * @author Fuwenbin
     * @date Jun 8, 2010 11:48:31 AM 
     */
    public static Date getLastDateOfLastSeason(Date date) {
    	Date firstDate = getFirstDateOfSeason(date);
        Calendar c = Calendar.getInstance();   
        c.setTime(firstDate);   
        c.add(Calendar.DAY_OF_MONTH, -1);
        
        return c.getTime();   
    }   
    
    /**
     * 取得季度月 
     * @param date
     * @return
     *
     * @author Fuwenbin
     * @date Jun 8, 2010 11:49:26 AM 
     */
    public static Date[] getSeasonDate(Date date) {   
        Date[] season = new Date[3];   
  
        Calendar c = Calendar.getInstance();   
        c.setTime(date);   
  
        int nSeason = getSeason(date);   
        if(nSeason == 1) {//第一季度   
            c.set(Calendar.MONTH, Calendar.JANUARY);   
            season[0] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.FEBRUARY);   
            season[1] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.MARCH);   
            season[2] = c.getTime();   
        } else if(nSeason == 2) {//第二季度   
            c.set(Calendar.MONTH, Calendar.APRIL);   
            season[0] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.MAY);   
            season[1] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.JUNE);   
            season[2] = c.getTime();   
        } else if(nSeason == 3) {//第三季度   
            c.set(Calendar.MONTH, Calendar.JULY);   
            season[0] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.AUGUST);   
            season[1] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);   
            season[2] = c.getTime();   
        } else if(nSeason == 4) {//第四季度   
            c.set(Calendar.MONTH, Calendar.OCTOBER);   
            season[0] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.NOVEMBER);   
            season[1] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.DECEMBER);   
            season[2] = c.getTime();   
        }   
        return season;   
    }   

    /**
     * 1 第一季度  2 第二季度 3 第三季度 4 第四季度  
     * @param date
     * @return
     *
     * @author Fuwenbin
     * @date Jun 8, 2010 11:50:14 AM 
     */
    public static int getSeason(Date date) {   
  
        int season = 0;   
  
        Calendar c = Calendar.getInstance();   
        c.setTime(date);   
        int month = c.get(Calendar.MONTH);   
        switch (month) {   
            case Calendar.JANUARY:   
            case Calendar.FEBRUARY:   
            case Calendar.MARCH:   
                season =  1;   
                break;   
            case Calendar.APRIL:   
            case Calendar.MAY:   
            case Calendar.JUNE:   
                season =  2;   
                break;   
            case Calendar.JULY:   
            case Calendar.AUGUST:   
            case Calendar.SEPTEMBER:   
                season =  3;   
                break;   
            case Calendar.OCTOBER:   
            case Calendar.NOVEMBER:   
            case Calendar.DECEMBER:   
                season =  4;   
                break;   
            default:   
                break;   
        }   
        return season;   
    }   

    
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -70);
		
		
		System.out.println(DateUtil.convertDateTimeToString(DateUtil.getFirstDateOfSeason(cal.getTime())));
		System.out.println(DateUtil.convertDateTimeToString(DateUtil.getLastDateOfSeason(cal.getTime())));
		System.out.println(DateUtil.convertDateTimeToString(DateUtil.getFirstDateOfLastSeason(cal.getTime())));
		System.out.println(DateUtil.convertDateTimeToString(DateUtil.getLastDateOfLastSeason(cal.getTime())));
//		System.out.println(DateUtil.getWeekLastDay(cal.getTime()));
	}
}