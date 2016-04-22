package com.channelsoft.appframe.exception;

/**
 * <dl>
 * <dt>PrivilegeException</dt>
 * <dd>Description:权限异常类</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Feb 7, 2007</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class PrivilegeException extends BaseRuntimeException {

	public PrivilegeException() {
		super();
	}

	public PrivilegeException(String message) {
		super(message);
	}

	public PrivilegeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrivilegeException(Throwable cause) {
		super(cause);
	}
}
