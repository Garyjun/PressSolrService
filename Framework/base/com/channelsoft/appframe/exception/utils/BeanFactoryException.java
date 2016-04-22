/**
 * FileName: BeanFactoryException.java
 */
package com.channelsoft.appframe.exception.utils;

import com.channelsoft.appframe.exception.ServiceException;

/**
 * <dl>
 * <dt>BeanFactoryException</dt>
 * <dd>Description:Spring Bean工厂类异常</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-10-20</dd>
 * </dl>
 */
public class BeanFactoryException extends ServiceException {
	public BeanFactoryException(String message) {
		super(message);
	}

	public BeanFactoryException(String message, Throwable cause) {
		super(message, cause);
	}

}
