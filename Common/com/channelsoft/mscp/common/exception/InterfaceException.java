package com.channelsoft.mscp.common.exception;

import com.channelsoft.appframe.exception.BaseRuntimeException;



/**
 * <dl>
 * <dt>InterfaceException</dt>
 * <dd>Description:接口返回值异常</dd>
 * <dd>Copyright: Copyright (C) 2009</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 11, 2009</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class InterfaceException extends BaseRuntimeException {
	public InterfaceException() {
		super();
	}

	public InterfaceException(String message) {
		super(message);
	}

	public InterfaceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InterfaceException(Throwable cause) {
		super(cause);
	}
}
