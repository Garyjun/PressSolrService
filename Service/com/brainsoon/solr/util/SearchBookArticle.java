package com.brainsoon.solr.util;


/**
 * Description: <p>索引文档对象</p>
 * 
 */
public class SearchBookArticle {
	
	//正题名
	private String title;
	//作者
	private String authorname;
	//出版社
	private String pressname;
	//出版时间
	private String publishDate;
	//出版地 
	private String publishAddress;
	//摘要
	private String description;
	//主题词 
	private String subject;
	//isbn
	private String isbn;
	//专题库标识
	private String speId;
	//资源类型
	private String resType;
	 //产品标识
	private String prodId;
	 //机构标识
	private String orgId;
	 //专题库文章类别
	private String genre;
	 //价格
	private String price;
	 //上线时间
	private String onlineTime;
	 //销量
	private String salesNum;
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
	
	public SearchBookArticle(){}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthorname() {
		return authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public String getPressname() {
		return pressname;
	}

	public void setPressname(String pressname) {
		this.pressname = pressname;
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getSpeId() {
		return speId;
	}

	public void setSpeId(String speId) {
		this.speId = speId;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishAddress() {
		return publishAddress;
	}

	public void setPublishAddress(String publishAddress) {
		this.publishAddress = publishAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(String salesNum) {
		this.salesNum = salesNum;
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
              