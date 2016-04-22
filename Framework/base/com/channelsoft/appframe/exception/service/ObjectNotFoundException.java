/**
 * @(#)FailDeleteObjectException.java 1.00 2005-3-9 青牛软件 2005，版权所有.
 */
package com.channelsoft.appframe.exception.service;

import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.po.BaseHibernateObject;

/**
 * 服务层异常，表示对象不存在 在修改/删除一个PO对象的时候，需要检查该对象是否存在，包括物理存在和逻辑存在。
 * 如果已经不存在，则不能修改/删除，应该抛出此异常，调用方需要处理此异常。
 */
public class ObjectNotFoundException extends ServiceException {
	private BaseHibernateObject businessObject;

	public ObjectNotFoundException() {
		super();
	}

	/**
	 * @param message
	 */
	public ObjectNotFoundException(String message) {
		super(message);
	}

	public ObjectNotFoundException(String message,
			BaseHibernateObject businessObject) {
		super(message);
		this.businessObject = businessObject;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ObjectNotFoundException(Throwable cause) {
		super(cause);
	}

	public BaseHibernateObject getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(BaseHibernateObject businessObject) {
		this.businessObject = businessObject;
	}
}
