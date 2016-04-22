package com.channelsoft.appframe.exception.utils;

import com.channelsoft.appframe.exception.ServiceException;

/**
 * Bean对象复制异常，将IllegalAccessException和InvocationTargetException 转换为业务异常
 * 
 * @author 李炜
 */
public class ConvertException extends ServiceException {

	public ConvertException() {
		super();
	}

	public ConvertException(String message) {
		super(message);
	}

	public ConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConvertException(Throwable cause) {
		super(cause);
	}
}
