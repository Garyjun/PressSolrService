package com.brainsoon.solrservice.res.po;

import java.io.Serializable;

import com.channelsoft.appframe.po.BaseHibernateObject;

public class BookWork extends BaseHibernateObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long bookid;
	private Integer ispdf;
	public Long getBookid() {
		return bookid;
	}
	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}
	public Integer getIspdf() {
		return ispdf;
	}
	public void setIspdf(Integer ispdf) {
		this.ispdf = ispdf;
	}
	
	@Override
	public String getEntityDescription() {
		return "bookwork";
	}
	@Override
	public String getObjectDescription() {
		return "bookwork【"+bookid+"】";
	}
	@Override
	public Serializable getObjectID() {
		return bookid;
	}
}
