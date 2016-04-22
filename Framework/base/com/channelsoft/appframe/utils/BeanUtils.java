/**
 * FileName: BeanUtils.java
 */
package com.channelsoft.appframe.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.channelsoft.appframe.exception.utils.BeansException;

/**
 * <dl>
 * <dt>BeanUtils</dt>
 * <dd>Description:Bean操作工具</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Dec 26, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class BeanUtils {
	private static Log logger = LogFactory.getLog(BeanUtils.class);

    /**
     * 根据属性名，取得bean中的属性值
     * @param bean
     * @param field
     * @return
     *
     * @author Fuwenbin
     * @date Dec 2, 2008 2:30:45 PM 
     */
    public static Object getProperty(Object bean, String property){
		try {
			return PropertyUtils.getProperty(bean, property);
		} catch (IllegalAccessException exp) {
            throw new BeansException("获取对象属性异常["+property+"]",exp);
		} catch (InvocationTargetException exp) {
            throw new BeansException("获取对象属性异常["+property+"]",exp);
		} catch (NoSuchMethodException exp) {
            throw new BeansException("获取对象属性异常["+property+"]",exp);
		}
    }
    
    /**
     * 设置bean中指定属性的值
     * @param bean
     * @param name
     * @param value
     *
     * @author Fuwenbin
     * @date Dec 26, 2008 1:46:37 PM 
     */
    public static void setProperty(Object bean, String name, Object value){
		try {
			org.apache.commons.beanutils.BeanUtils.setProperty(bean, name, value);
		} catch (IllegalAccessException exp) {
            throw new BeansException("获取对象属性异常",exp);
		} catch (InvocationTargetException exp) {
            throw new BeansException("获取对象属性异常",exp);
		}
    }
    
    /**
     * 返回指定属性的字符串值
     * @param bean
     * @param name
     * @return
     *
     * @author Fuwenbin
     * @date Dec 26, 2008 1:47:31 PM 
     */
    public static String getSimpleProperty(Object bean, String name){
		try {
			return org.apache.commons.beanutils.BeanUtils.getSimpleProperty(bean, name);
		} catch (IllegalAccessException exp) {
            throw new BeansException("获取对象属性异常",exp);
		} catch (InvocationTargetException exp) {
            throw new BeansException("获取对象属性异常",exp);
		} catch (NoSuchMethodException exp) {
            throw new BeansException("获取对象属性异常",exp);
		}
	}
    
    /**
     * 属性值复制
     * @param source
     * @param target
     *
     * @author Fuwenbin
     * @date Dec 26, 2008 1:48:06 PM 
     */
    public static void copyProperties(Object source, Object target) {
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
		} catch (IllegalAccessException exp) {
			throw new BeansException("复制对象属性异常",exp);
		} catch (InvocationTargetException exp) {
			throw new BeansException("复制对象属性异常",exp);
		}
    }
    
    /**
     * 可指定忽略属性的属性值复制
     * @param source
     * @param target
     * @param ignoreProperties
     *
     * @author Fuwenbin
     * @date Dec 26, 2008 1:48:28 PM 
     */
    public static void copyProperties(Object source, Object target, String[] ignoreProperties) {
		try {
			org.springframework.beans.BeanUtils.copyProperties(target, source);
		} catch (org.springframework.beans.BeansException exp) {
			throw new BeansException("复制对象属性异常",exp);
		}
    }
    
    /**
     * bean克隆
     * @param bean
     * @return
     *
     * @author Fuwenbin
     * @date Dec 26, 2008 1:49:01 PM 
     */
    public static Object cloneBean(Object bean) {
		try {
			return org.apache.commons.beanutils.BeanUtils.cloneBean(bean);
		} catch (IllegalAccessException exp) {
			throw new BeansException("克隆异常",exp);
		} catch (InvocationTargetException exp) {
			throw new BeansException("克隆异常",exp);
		} catch (InstantiationException exp) {
			throw new BeansException("克隆异常", exp);
		} catch (NoSuchMethodException exp) {
			throw new BeansException("克隆异常", exp);
		}
    }
    
    /**
     * 取得"getter"函数的函数名 
     * @param field
     * @return
     *
     * @author Fuwenbin
     * @date Dec 2, 2008 2:16:04 PM 
     */
    public static String getGetMethodName(String field) { 
        return "get" + StringUtils.capitalize(field); 
    } 

    /**
     * 取得"setter"函数的函数名 
     * @param field
     * @return
     *
     * @author Fuwenbin
     * @date Dec 2, 2008 2:16:43 PM 
     */
    public static String getSetMethodName(String field) { 
        return "set" + StringUtils.capitalize(field); 
    } 
}
