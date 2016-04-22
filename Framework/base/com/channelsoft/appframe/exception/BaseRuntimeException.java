/**
 * FileName: BaseRuntimeException.java
 */
package com.channelsoft.appframe.exception;

/**
 * <dl>
 * <dt>BaseRuntimeException</dt>
 * <dd>Description:运行时异常类</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-10-20</dd>
 * </dl>
 * 
 * @author 李大鹏
 */
public class BaseRuntimeException extends RuntimeException
{
	public BaseRuntimeException()
	{
		super();
	}
	
	public BaseRuntimeException(String message)
	{
		super(message);
	}
	
	public BaseRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public BaseRuntimeException(Throwable cause)
	{
		super(cause);
	}
}
