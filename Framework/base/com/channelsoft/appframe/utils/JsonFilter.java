package com.channelsoft.appframe.utils;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.googlecode.jsonplugin.annotations.JSON;


/**
 * <dl>
 * <dt>JsonFilter</dt>
 * <dd>Description:json属性过滤器。基于Google json插件标签@JSON(serialize=false)进行过滤。</dd>
 * <dd>Copyright: Copyright (C) 2009</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 1, 2009</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class JsonFilter {
	private final static String ESC = "*np#";//&
	
	public static JsonConfig getConfig() {
		JsonConfig config = new JsonConfig();
		
		config.setJsonPropertyFilter(new PropertyFilter(){ 
            public boolean apply(Object source, String name, Object value) {
            	PropertyDescriptor proDesc = BeanUtils.getPropertyDescriptor(
						source.getClass(), name);
            	Method readMethod = proDesc.getReadMethod();
            	if (readMethod == null) {
            		return false;
            	}
            	JSON json = readMethod.getAnnotation(JSON.class);
                if (json != null && !json.serialize()) {
                    return true;
                }
                
                if (value == null) {
                	return true;
                }
            	
            	return false; 
            }
		});
		
		return config;
	}
	
	/**
	 * 特殊字符转义
	 * @param json
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Dec 8, 2009 10:55:56 AM 
	 */
	public static String formatJsonStr(JSONObject json) {
		return StringUtils.replace(json.toString(), "&", ESC);
	}
	
	/**
	 * 特殊字符解析
	 * @param jsonStr
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Dec 8, 2009 10:56:10 AM 
	 */
	public static String parseJsonStr(String jsonStr) {
		if (StringUtils.isBlank(jsonStr)) {
			return "";
		}
		
		return StringUtils.replace(jsonStr, ESC, "&");
	}
	
//	private static JsonConfig config = new JsonConfig();
//	static {
//		config.setJsonPropertyFilter(new PropertyFilter(){ 
//            public boolean apply(Object source, String name, Object value) {
//            	PropertyDescriptor proDesc = BeanUtils.getPropertyDescriptor(
//						source.getClass(), name);
//            	Method readMethod = proDesc.getReadMethod();
//            	if (readMethod == null) {
//            		return false;
//            	}
//            	JSON json = readMethod.getAnnotation(JSON.class);
//                if (json != null && !json.serialize()) {
//                    return true;
//                }
//            	
//            	return false; 
//            }
//		});
//	}
}
