package com.brainsoon.solrservice.res.po;

import java.io.Serializable;

import com.channelsoft.appframe.po.BaseHibernateObject;

public class Author extends BaseHibernateObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long authorid;
	private String name;
	public Long getAuthorid() {
		return authorid;
	}
	public void setauthorid(Long authorid) {
		this.authorid = authorid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getEntityDescription() {
		return "author";
	}
	@Override
	public String getObjectDescription() {
		return "author【"+authorid+"】";
	}
	@Override
	public Serializable getObjectID() {
		return authorid;
	}
}
