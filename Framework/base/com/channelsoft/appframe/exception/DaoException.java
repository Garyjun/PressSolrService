package com.channelsoft.appframe.exception;


/**
 * DAO操作的异常
 * 
 * @author 李炜
 */
public class DaoException extends BaseRuntimeException
{
	public DaoException()
	{
		super();
	}

	public DaoException(String message)
	{
		super(message);
	}

	public DaoException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DaoException(Throwable cause)
	{
		super(cause);
	}
}
