package com.brainsoon.solrservice.res.po;

import java.io.Serializable;

import com.channelsoft.appframe.po.BaseHibernateObject;

public class Author_Book extends BaseHibernateObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long authorid;
	private Long bookid;
	public Long getAuthorid() {
		return authorid;
	}
	public void setauthorid(Long authorid) {
		this.authorid = authorid;
	}
	public Long getBookid() {
		return bookid;
	}
	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}
	@Override
	public String getEntityDescription() {
		return "author_bookid";
	}
	@Override
	public String getObjectDescription() {
		return "author_book【"+authorid+"】";
	}
	@Override
	public Serializable getObjectID() {
		return authorid;
	}
}
