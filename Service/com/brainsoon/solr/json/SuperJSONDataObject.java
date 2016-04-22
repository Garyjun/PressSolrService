package com.brainsoon.solr.json;

public class SuperJSONDataObject implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private String status = "";
	private String msg = "";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
