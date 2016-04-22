package com.channelsoft.appframe.dao.query;

import java.util.ArrayList;
import java.util.List;

/**
 * <dl>
 * <dt>PageableInfo</dt>
 * <dd>Description:分页信息包装类</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Aug 6, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class PageableInfo {
	private Integer totalRecords;
	private Integer startIndex = 0;
	private Integer pageSize = 10;
	private String sort;
	private String dir;
	private List<Object> records = new ArrayList<Object>();
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public List<Object> getRecords() {
		return records;
	}
	public void setRecords(List<Object> records) {
		this.records = records;
	}
}
