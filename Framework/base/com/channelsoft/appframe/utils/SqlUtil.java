package com.channelsoft.appframe.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * <dl>
 * <dt></dt>
 * <dd>Description: 创建Sql的工具类</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * </dl>
 */
public class SqlUtil {

	/**
	 * 构建数据库IN条件 
	 * @param strBuffer
	 * @param column
	 * @param value
	 */
	public static void appendIn(StringBuffer strBuffer, String column, List<String> list) {
		if(!list.isEmpty()) {
			strBuffer.append(column).append(" IN (");
			int i = 1;
			for (String value : list) {
				strBuffer.append("'");
				strBuffer.append(escapeSql(value.trim()));
				strBuffer.append("'");
				if(i++ != list.size()) {
					strBuffer.append(",");
				}
			}
			strBuffer.append(")");
		}
	}
	
	public static void appendLike(StringBuffer strBuffer, String column,String value) {
		if (StringUtils.isNotEmpty(value)) {
			strBuffer.append(" AND ").append(column).append(" LIKE '");
			strBuffer.append(escapeSql(value.trim()));
			strBuffer.append("%'");
		}
	}
	
	public static void appendEqual(StringBuffer strBuffer, String column,String value) {
		if (StringUtils.isNotEmpty(value)) {
			strBuffer.append(" AND ").append(column).append(" = '");
			strBuffer.append(escapeSql(value.trim()));
			strBuffer.append("'");
		}
	}
	
	/**
	 * 避免sql注入的转义
	 * @param sql
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Jul 12, 2010 9:58:03 AM 
	 */
	public static String escapeSql(String sql) {
//		if (StringUtils.isBlank(sql)) {
			return sql;
//		}
		
//		String temp= StringUtils.replace(sql, "'", "\\'");
//		temp = StringUtils.replace(temp, "\"", "\\\"");
//		temp = StringUtils.replace(temp, ";", "\\;");
//		temp = StringUtils.replace(temp, "-", "－");
//				  
//		return temp;
	}
}
