package com.brainsoon.solrservice.res.po;

import java.io.Serializable;

import com.channelsoft.appframe.po.BaseHibernateObject;

public class Book extends BaseHibernateObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long bookid;
	private String title;
	private String price;
	private Integer attribute;
	//pdf地址
	private String stream;
	public Long getBookid() {
		return bookid;
	}
	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getAttribute() {
		return attribute;
	}
	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	@Override
	public String getEntityDescription() {
		return "book";
	}
	@Override
	public String getObjectDescription() {
		return "book【"+bookid+"】";
	}
	@Override
	public Serializable getObjectID() {
		return bookid;
	}
}
