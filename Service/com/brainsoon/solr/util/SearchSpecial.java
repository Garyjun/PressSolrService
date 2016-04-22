package com.brainsoon.solr.util;


/**
 * Description: <p>索引文档对象</p>
 * 
 */
public class SearchSpecial {	
	//正题名
	private String title;
	//摘要
	private String description;
	//资源类型
	private String resType;
	 //产品标识
	private String prodId;
	 //上线开始时间
	private String onlineTimeStart;
	 //上线结束时间
	private String onlineTimeEnd;
	
	private String publishTimeStart;
	private String publishTimeEnd;
	
	private String isbn;
	
	private String authorName;
	
	 //查询类型 : 1：主题库，2:图书
	private String searchType;
	//关键字
	private String keywords;
	//高级查询的语句
	private String typeid;
	
	private String era;
	
	private String speid;
	
	private String orgid;
	
	private String content;
	//排序字段
	private String sorts;
	private String orders;
	//将初始偏移量指定到结果集中。可用于对结果进行分页。默认值为 0。
	private int start;
	//返回文档的最大数目。默认值为 10。
	private int rows;
	
	public SearchSpecial(){}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	
	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}
	
	public String getOnlineTimeStart() {
		return onlineTimeStart;
	}

	public void setOnlineTimeStart(String onlineTimeStart) {
		this.onlineTimeStart = onlineTimeStart;
	}
	
	public String getOnlineTimeEnd() {
		return onlineTimeEnd;
	}

	public void setOnlineTimeEnd(String onlineTimeEnd) {
		this.onlineTimeEnd = onlineTimeEnd;
	}
	
	public void setPublishTimeStart(String publishTimeStart) {
		this.publishTimeStart = publishTimeStart;
	}	
	
	public String getPublishTimeStart() {
		return publishTimeStart;
	}
	
	public String getPublishTimeEnd() {
		return publishTimeEnd;
	}

	public void setPublishTimeEnd(String publishTimeEnd) {
		this.publishTimeEnd = publishTimeEnd;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	public String getAuthorName() {
		return authorName;
	}
	
	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	public String getKeywords() {
		return keywords;
	}
	
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	
	public String getEra() {
		return era;
	}

	public void setEra(String era) {
		this.era = era;
	}
	
	public String getSpeid() {
		return speid;
	}

	public void setSpeid(String speid) {
		this.speid = speid;
	}
	
	public String getOrgid() {
		return orgid;
	}
	
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public String getSorts() {
		return sorts;
	}

	public void setSorts(String sorts) {
		this.sorts = sorts;
	}

	public String getOrders() {
		return orders;
	}
	
	public void setOrders(String orders) {
		this.orders = orders;
	}
	
}
              