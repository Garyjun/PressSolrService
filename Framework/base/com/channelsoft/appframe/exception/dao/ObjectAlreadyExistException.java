package com.channelsoft.appframe.exception.dao;

import com.channelsoft.appframe.exception.DaoException;

/**
 * DAO层异常，表示对象已经存在
 * 
 * @author liwei
 */
public class ObjectAlreadyExistException extends DaoException {

	public ObjectAlreadyExistException() {
		super();
	}

	public ObjectAlreadyExistException(String message) {
		super(message);
	}

	public ObjectAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectAlreadyExistException(Throwable cause) {
		super(cause);
	}

}
