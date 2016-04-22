package com.channelsoft.appframe.utils;

import java.math.BigDecimal;
/**
 * 数学运算的工具类
 * @author liwei
 *
 */
public class MathUtil {
	//对double数值num取length位小数
	public static double getRound(double num, int length) {
		int t_int = 1;
		for (int i = 0; i < length; i++) {
			t_int = t_int * 10;
		}
		if (length > 0) {
			num =  Math.round(num * t_int) / (1.0d * t_int);
		}
		return num;
	}
	
	//对double数据取最大整数
	public static long getMaxLong(double num){
		long ret = (long) num;
		if(ret < num)
			ret = ret + 1;
		return ret;
	}
	/**
	 * 比较两个BigDecimal对象,源对象是否小于目标对象
	 * @param src
	 * @param target
	 * @return
	 */
	public static boolean smallerThan(BigDecimal src,BigDecimal target) {
		return src.compareTo(target)<0;		
	}
	/**
	 * 比较两个BigDecimal对象是否相等
	 * @param src
	 * @param target
	 * @return
	 */
	public static boolean equals(BigDecimal src,BigDecimal target) {
		return src.compareTo(target)==0;		
	}/**
	 * 比较两个BigDecimal对象是否不相等
	 * @param src
	 * @param target
	 * @return
	 */
	public static boolean notEquals(BigDecimal src,BigDecimal target) {
		return src.compareTo(target)!=0;		
	}
	/**
	 * 比较两个BigDecimal对象,源对象是否大于目标对象
	 * @param src
	 * @param target
	 * @return
	 */
	public static boolean biggerThan(BigDecimal src,BigDecimal target) {
		return src.compareTo(target)>0;		
	} 
}
