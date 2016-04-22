package com.channelsoft.appframe.utils;

import org.apache.commons.lang.StringUtils;
/**
 * 工具类,提供对数组的特殊处理
 */
public class StringArrayUtil {

	public StringArrayUtil() {
		super();
	}
	
	/**
	 * 判断多选框中是否包含全部(""代表全部)
	 * @param values
	 * @return
	 */
	private static boolean isContainAll(String[] values) { 
		for (int i = 0; i < values.length; i++) {
			if (StringUtils.isBlank(values[i])){
				return true;
			}
		} 
		return false;
	}
	/**
	 * 对多选值的处理
	 * 
	 * @param values
	 *            选择的值
	 * @param fieldName
	 *            对应的字段
	 * @return
	 */
	public static StringBuffer getMutiValueField(String[] values, String fieldName) {
		StringBuffer ret = new StringBuffer(100); 
		if (values != null && values.length > 0) {
			if (!isContainAll(values)) {
				ret.append(" and ").append(fieldName).append(" in (");
				for (int i = 0; i < values.length; i++) {
					if (i != 0)
						ret.append(", ");
					ret.append("'").append(values[i]).append("'");
				}
				ret.append(") ");
			}
		}
		return ret;
	}

	public static StringBuffer getMutiValueField(String values, String fieldName) {
		String[] v = splic(values);
		return getMutiValueField(v, fieldName);
	}
	
	/**
	 * 把字符串按照逗号分解为字符串数组,并且去掉多余的空格
	 * @param str
	 * @return
	 */
	private static String[] splic(String str) {
        String[] oldArray = str.split(",");
        String[] array = new String[oldArray.length];
        for (int i = 0; i < oldArray.length; i++) { 
            array[i] = oldArray[i].trim();
        }
        return array;
    }
	 
}
