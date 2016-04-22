package com.channelsoft.appframe.exception.utils;

import com.channelsoft.appframe.exception.ServiceException;


/**
 * <dl>
 * <dt>BeansException</dt>
 * <dd>Description:Bean对象操作异常</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Dec 26, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class BeansException extends ServiceException {
	public BeansException() {
		super();
	}

	public BeansException(String message) {
		super(message);
	}

	public BeansException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeansException(Throwable cause) {
		super(cause);
	}
}
