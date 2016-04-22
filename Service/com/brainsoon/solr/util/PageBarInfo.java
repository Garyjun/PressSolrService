package com.brainsoon.solr.util;

public class PageBarInfo {
	
	/*
	 * 该页对应的第一条记录的index
	 */
	private int startIndex;
	
	/*
	 * 页号
	 */
	private Integer pageNo;
	
	/*
	 * 是否是当前页
	 */
	private boolean isCurrentPage;
	
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public Integer getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	public boolean getIsCurrentPage() {
		return isCurrentPage;
	}
	
	public void setCurrentPage(boolean isCurrentPage) {
		this.isCurrentPage = isCurrentPage;
	}
}
