package com.brainsoon.solr.util;


/**
 * Description: <p>索引文档对象</p>
 * 
 */
public class SearchNews {
	
	//正题名
	private String title;
	//摘要
	private String description;
	//资源类型
	private String resType;
	 //产品标识
	private String prodId;
	 //上线时间
	private String onlineTime;
	 //查询类型 all,title,author,publishing
	private String searchType;
	//关键字
	private String keywords;
	//排序字段
	private String sortField;
	//排序方式 ASC ,DESC
	private String sortWay;
	//高级查询的语句
	private String queryStat;
	
	//将初始偏移量指定到结果集中。可用于对结果进行分页。默认值为 0。
	private int start;
	//返回文档的最大数目。默认值为 10。
	private int rows;
	
	public SearchNews(){}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getKeywords() {
		return keywords;
	}
	
	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
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

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortWay() {
		return sortWay;
	}

	public void setSortWay(String sortWay) {
		this.sortWay = sortWay;
	}

	public String getQueryStat() {
		return queryStat;
	}

	public void setQueryStat(String queryStat) {
		this.queryStat = queryStat;
	}

}
              