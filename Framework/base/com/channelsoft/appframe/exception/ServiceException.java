package com.channelsoft.appframe.exception;



/**
 * 业务处理对象的异常
 * 
 * @author 李炜
 */
public class ServiceException extends BaseRuntimeException
{

	public ServiceException()
	{
		super();
	}

	public ServiceException(String message)
	{
		super(message);
	}

	public ServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ServiceException(Throwable cause)
	{
		super(cause);
	}
}
