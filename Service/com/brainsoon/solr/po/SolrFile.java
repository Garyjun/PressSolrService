package com.brainsoon.solr.po;

import java.io.Serializable;

import com.channelsoft.appframe.po.BaseHibernateObject;

public class SolrFile extends BaseHibernateObject implements Serializable {

	private static final long serialVersionUID = -1184568126530869332L;

	 //主键
	private Long id;
	
	 //文件路径
	private String path;
	
	 //文件对象索引状态(0创建索引成功,1创建索引失败)
	private String status;
	 //创建日志
	private String message;
	
	private SolrQueue solrQueue;
	
	
	public SolrQueue getSolrQueue() {
        return solrQueue;
    }

    public void setSolrQueue(SolrQueue solrQueue) {
        this.solrQueue = solrQueue;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Serializable getObjectID() {
		return getId();
	}

	@Override
	public String getObjectDescription() {
		return "全文索引文件详情表";
	}

	@Override
	public String getEntityDescription() {
		return "全文索引文件详情表";
	}
}
