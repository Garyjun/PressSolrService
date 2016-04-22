/**
 * FileName: BaseException.java
 */
package com.channelsoft.appframe.exception;

/**
 * <dl>
 * <dt>BaseException</dt>
 * <dd>Description:异常类</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-10-20</dd>
 * </dl>
 * 
 * @author 李大鹏
 */
public class BaseException extends Exception
{
	public BaseException()
	{
		super();
	}
	
	public BaseException(String message)
	{
		super(message);
	}
	
	public BaseException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public BaseException(Throwable cause)
	{
		super(cause);
	}
}
