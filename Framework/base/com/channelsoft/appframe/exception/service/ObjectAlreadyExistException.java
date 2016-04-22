package com.channelsoft.appframe.exception.service;

import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.po.BaseHibernateObject;

/**
 * 服务层异常，表示对象已经存在。 在增加一个PO对象的时候，需要检查该对象是否已经存在，包括物理存在和逻辑存在。
 * 如果已经存在，则不能再次增加，应该抛出此异常，调用方需要处理此异常。
 */
public class ObjectAlreadyExistException extends ServiceException {
	private BaseHibernateObject businessObject;

	public ObjectAlreadyExistException() {
		super();
	}

	public ObjectAlreadyExistException(String message) {
		super(message);
	}

	public ObjectAlreadyExistException(String message,
			BaseHibernateObject businessObject) {
		super(message);
		this.businessObject = businessObject;
	}

	public ObjectAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectAlreadyExistException(String message, Throwable cause,
			BaseHibernateObject businessObject) {
		super(message, cause);
		this.businessObject = businessObject;
	}

	public ObjectAlreadyExistException(Throwable cause) {
		super(cause);
	}

	public BaseHibernateObject getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(BaseHibernateObject businessObject) {
		this.businessObject = businessObject;
	}
}
