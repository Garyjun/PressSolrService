package com.brainsoon.solr.po;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class SearchResource implements Serializable {
	
	private static final long serialVersionUID = -4426979592036954312L;

	//文档UUID KEY值(资源类型+资源标识)
	private String uuid;
	//产品标识
	private String prodid;
	//资源类型(1-图书或2-文章)
	private String restype;
	//机构标识
	private String orgid;
	//专题库标识
	private String speid;
	
	//正题名
	private String title;
	//作者
	private String author;
	//编辑
	private String editor;
	//出版时间
	private String published;
	//摘要
	private String description;
	//关键字
	private String keywords;
	//目录
	private String stream;
	//价格
	private String price;
	//初始化价格
	private String batchprice;
	//折扣价格
	private String actprice;
	//上线时间
	private String onlinetime;
	//分类标识
	private String typeid;
	//内容
	private String files;
	//index文件
	private File file;
	//来源
	private String sourceid;
	//缩略图
	private String thumb;
	//url
	private String url;
	//所属期刊
	private String belongjournal;
	//年代
	private String magazineyear;
	//期数
	private String magazineyearnum;
	//总期数 
	private String magazinenum;
	//索引创建时间
	private Date indextime;
	//0创建索引 1删除索引
	private String actions;
	
	private Long selectType = 0l;
	private Long _version_;
	
	//查询类型 all,title,author,publishing
	private String searchType;
	//排序字段
	private String sorts;
	//排序方式 ASC ,DESC
	private String orders;
	//高级查询的语句
	private String queryStat;
	
	//将初始偏移量指定到结果集中。可用于对结果进行分页。默认值为 0。
	private int start;
	//返回文档的最大数目。默认值为 10。
	private int rows;
	//发布开始时间
	private String publishTimeStart;
	//发布结束时间
	private String publishTimeEnd;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getProdid() {
		return prodid;
	}
	public void setProdid(String prodid) {
		this.prodid = prodid;
	}
	public String getRestype() {
		return restype;
	}
	public void setRestype(String restype) {
		this.restype = restype;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getSpeid() {
		return speid;
	}
	public void setSpeid(String speid) {
		this.speid = speid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getBatchprice() {
		return batchprice;
	}
	public void setBatchprice(String batchprice) {
		this.batchprice = batchprice;
	}
	public String getActprice() {
		return actprice;
	}
	public void setActprice(String actprice) {
		this.actprice = actprice;
	}
	public String getOnlinetime() {
		return onlinetime;
	}
	public void setOnlinetime(String onlinetime) {
		this.onlinetime = onlinetime;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getSourceid() {
		return sourceid;
	}
	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBelongjournal() {
		return belongjournal;
	}
	public void setBelongjournal(String belongjournal) {
		this.belongjournal = belongjournal;
	}
	public String getMagazineyear() {
		return magazineyear;
	}
	public void setMagazineyear(String magazineyear) {
		this.magazineyear = magazineyear;
	}
	public String getMagazineyearnum() {
		return magazineyearnum;
	}
	public void setMagazineyearnum(String magazineyearnum) {
		this.magazineyearnum = magazineyearnum;
	}
	public String getMagazinenum() {
		return magazinenum;
	}
	public void setMagazinenum(String magazinenum) {
		this.magazinenum = magazinenum;
	}
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	public Long getSelectType() {
		return selectType;
	}
	public void setSelectType(Long selectType) {
		this.selectType = selectType;
	}
	public Long get_version_() {
		return _version_;
	}
	public void set_version_(Long _version_) {
		this._version_ = _version_;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public Date getIndextime() {
		return indextime;
	}
	public void setIndextime(Date indextime) {
		this.indextime = indextime;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
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
	public String getQueryStat() {
		return queryStat;
	}
	public void setQueryStat(String queryStat) {
		this.queryStat = queryStat;
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
	public String getPublishTimeStart() {
		return publishTimeStart;
	}
	public void setPublishTimeStart(String publishTimeStart) {
		this.publishTimeStart = publishTimeStart;
	}
	public String getPublishTimeEnd() {
		return publishTimeEnd;
	}
	public void setPublishTimeEnd(String publishTimeEnd) {
		this.publishTimeEnd = publishTimeEnd;
	}
	
}
