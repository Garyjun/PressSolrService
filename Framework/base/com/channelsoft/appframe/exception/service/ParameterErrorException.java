/**
 * @(#)ParameterErrorException.java 1.00 2005-4-4 青牛软件 2005，版权所有.
 */
package com.channelsoft.appframe.exception.service;

import com.channelsoft.appframe.exception.ServiceException;

/**
 * <dl>
 * <dt>ParameterErrorException</dt>
 * <dd>Description:参数异常</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-10-23</dd>
 * </dl>
 */
public class ParameterErrorException extends ServiceException {
	public ParameterErrorException() {
		super();
	}

	public ParameterErrorException(String message) {
		super(message);
	}

	public ParameterErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterErrorException(Throwable cause) {
		super(cause);
	}
}
