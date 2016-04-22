package com.brainsoon.solr.po;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.channelsoft.appframe.po.BaseHibernateObject;

public class SolrQueue extends BaseHibernateObject implements Serializable {

	private static final long serialVersionUID = -2384568126530869332L;
	
	 //主键
	private Long id;
	
	//资源类型
	private Long resType;
	
	//产品标识（图书bookid/文章contentid）
	private Long prodId;
	
	//机构馆标识
	private Long orgId = 0l;
	
	//机构馆操作
	private String orgAction;
	
	//专题库标识
	private Long speId;
	
	//机构馆操作
	private String speAction;
	
	//上线时间
	private String onlinetime;
	
	//价格
	private String price;
	
	//销量
	private String salesnum;
	
	//实体书销量
	private String stsalesnum;

	//索引动作(0创建索引，1删除索引)
	private String actions;
	
	//资源对象索引状态(0索引未创建,1索引已创建,2创建索引失败)
	private String status;
	
	private String bookStatus;
	
	private String bookStStatus;
	
	//创建时间
	private Date createTime;
	
	//采选类型：1.专题库、2.采选单
	private Long selectType = 0l;
	
	private Set<SolrFile> solrFiles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getResType() {
		return resType;
	}

	public void setResType(Long resType) {
		this.resType = resType;
	}
	
	public Long getProdId() {
		return prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public String getOrgAction() {
		return orgAction;
	}

	public void setOrgAction(String orgAction) {
		this.orgAction = orgAction;
	}
	
	public Long getSpeId() {
		return speId;
	}

	public void setSpeId(Long speId) {
		this.speId = speId;
	}
	
	public String getSpeAction() {
		return speAction;
	}

	public void setSpeAction(String speAction) {
		this.speAction = speAction;
	}
	
	public String getOnlinetime() {
		return onlinetime;
	}

	public void setOnlinetime(String onlinetime) {
		this.onlinetime = onlinetime;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getSalesnum() {
		return salesnum;
	}

	public void setSalesnum(String salesnum) {
		this.salesnum = salesnum;
	}
	
	public String getStsalesnum() {
		return stsalesnum;
	}

	public void setStsalesnum(String stsalesnum) {
		this.stsalesnum = stsalesnum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getBookStatus() {
		return bookStatus;
	}
	
	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}
	
	public String getBookStStatus() {
		return bookStStatus;
	}
	
	public void setBookStStatus(String bookStStatus) {
		this.bookStStatus = bookStStatus;
	}
	
	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getSelectType() {
		return selectType;
	}

	public void setSelectType(Long selectType) {
		this.selectType = selectType;
	}
	
	public Set<SolrFile> getSolrFiles() {
		if(null == solrFiles) {
			solrFiles = new HashSet<SolrFile>();
		}
		return solrFiles;
	}

	public void setSolrFiles(Set<SolrFile> solrFiles) {
		this.solrFiles = solrFiles;
	}

	@Override
	public Serializable getObjectID() {
		return getId();
	}

	@Override
	public String getObjectDescription() {
		return "全文索引队列表";
	}

	@Override
	public String getEntityDescription() {
		return "全文索引队列表";
	}	

}
