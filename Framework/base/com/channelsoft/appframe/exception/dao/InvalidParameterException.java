package com.channelsoft.appframe.exception.dao;

import com.channelsoft.appframe.exception.DaoException;

/**
 * 服务层异常，表示参数不正确
 * 
 * @author liwei
 */
public class InvalidParameterException extends DaoException {

	public InvalidParameterException() {
		super();
	}

	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParameterException(String message) {
		super(message);
	}

	public InvalidParameterException(Throwable cause) {
		super(cause);
	}

}
