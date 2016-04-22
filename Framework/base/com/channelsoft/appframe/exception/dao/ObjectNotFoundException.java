/**
 * @(#)ObjectNotFoundException.java 1.00 2005-3-9 青牛软件 2005，版权所有.
 */
package com.channelsoft.appframe.exception.dao;

import java.io.Serializable;

import com.channelsoft.appframe.exception.DaoException;

/**
 * DAO层异常，表示对象不存在
 * 
 * @author 李炜
 * @version 1.0
 */
public class ObjectNotFoundException extends DaoException {
	public ObjectNotFoundException(String message) {
		super(message);
	}

	public ObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public static ObjectNotFoundException createObjectNotFoundException(
			Class clazz, Serializable id) {
		StringBuffer msg = new StringBuffer(200);
		msg.append("主键为[").append(id).append("]的PO类{");
		msg.append(clazz.getName()).append("}不存在");
		return new ObjectNotFoundException(msg.toString());
	}
}
