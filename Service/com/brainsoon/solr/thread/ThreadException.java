package com.brainsoon.solr.thread;


public class ThreadException extends Exception {
	
	public enum ErrorLevel
	{
		WARN("警告"),
		ERROR("错误");
		
		private String desc;
		private ErrorLevel(String desc)
		{
			this.desc = desc;
		}
		public String getDesc()
		{
			return this.desc;
		}
	}
	
	private ErrorLevel level;
	
	public ThreadException(ErrorLevel level, String msg) {
		super(msg);		
		this.level = level;
	}
	
	public ThreadException(ErrorLevel level, String msg, Throwable cause) {
		super(msg,cause);		
		this.level = level;
	}
	
	public ErrorLevel getErrorLevel() {
		return this.level;
	}
	
	@Override
	public String getMessage() {
		StringBuilder msg = new StringBuilder(100);
		msg.append("ERROR_LEVEL:").append(getErrorLevel().getDesc())
			.append("  ERROR_MSG:").append(super.getMessage());
		
		return msg.toString();
	}
}
