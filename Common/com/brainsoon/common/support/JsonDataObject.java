package com.brainsoon.common.support;


public class JsonDataObject<T> extends SuperJSONDataObject{
	/**
	 * 对应json串中的data域的模板类
	 */
	private static final long serialVersionUID = 1L;
	private T data=null;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	/** default constructor */
	public JsonDataObject() {
	}
	
	public JsonDataObject(String status,String msg,T data) {
		super(status,msg);
		this.data=data;
	}
}
