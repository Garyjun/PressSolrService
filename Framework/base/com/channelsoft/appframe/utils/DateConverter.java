package com.channelsoft.appframe.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;


/**
 * 提供给Struts用来做数据类型转换的工具类
 * This class is converts a java.util.Date to a String
 * and a String to a java.util.Date. It is used by
 * BeanUtils when copying properties.  Registered
 * for use in BaseAction. 
 * @author liwei
 */
public class DateConverter implements Converter {
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        } else if (type == Date.class) {
            return convertToDate(type, value);
        } else if (type == String.class) {
            return convertToString(type, value);
        } else {
        	// 李咏：
        	// 未注册Converter的Class都会调用 String 的Converter, 也就是调用这个类，所以必须处理
        	// 参见 org.apache.commons.beanutils.ConvertUtilsBean.convert()
        	// 
        	
        	// 这里给出的convert方式是从org.apache.commons.beanutils.converters.StringConverter
        	// 中移过来
            return (value.toString());
        } 
    }

    protected Object convertToDate(Class type, Object value) {
        if (value instanceof String) {
            try {
                if (StringUtils.isEmpty(value.toString())) {
                    return null;
                }
                (new SimpleDateFormat(DateUtil.getDatePattern())).parse((String) value);
            } catch (Exception pe) {
                throw new ConversionException("Error converting String to Date");
            }
        }
        else if (value instanceof Date) {
        	return value;
        }        

        throw new ConversionException("Could not convert " +
                                      value.getClass().getName() + " to " +
                                      type.getName());
    }

    protected Object convertToString(Class type, Object value) {
        if (value instanceof Date) {
            try {
                return (new SimpleDateFormat(DateUtil.getDatePattern())).format(value);
            } catch (Exception e) {
                throw new ConversionException("Error converting Date to String");
            }
        }

        return value.toString();
    }
}
