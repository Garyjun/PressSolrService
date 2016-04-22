package com.brainsoon.solr.service;

import java.util.List;
import java.util.Map;

import com.brainsoon.solr.po.SolrQueue;
import com.channelsoft.appframe.exception.ServiceException;

/**
 * Description: <p>为Solr提供封装门面模式</p>
 * Content Desc:<p><p>
 */
public interface ISolrQueueFacede {
	
	
	/**
	 * 通过状态返回创建索引信息的集合
	 * @param status
	 * @return
	 */
	List<SolrQueue> getSolrQueueByStatus(String status);
	
	/**
	 * 通过标识更新创建索引信息的状态
	 * @param id
	 * @param status
	 * @return
	 */
	void updateSolrQueueStatus(Long id, String status);
	
	/**
	 * 更新文件创建索引日志
	 * @param fileId
	 * @param message
	 */
	void updateSolrFileStatusMsg(Long fileId, String status, String message);
	
	/**
	 * 创建索引失败状态
	 * @param id
	 */
	void createIndexFailed(Long id);
	
	/**
	 * 创建索引成功状态
	 * @param id
	 */
	void createIndexSuccess(Long id);

    /**
     * @param solrQueue
     */
	public void saveOrUpdate(SolrQueue solrQueue);
	
	
	/**
	 * 创建或删除产品索引
	 * @param bookid
	 * @param orgid
	 * @param speid
	 * @param bookinfo 图书基础信息
	 * @param actions
	 * @throws ServiceException
	 */
	public void doBookIndex(Long bookid, Long orgId, String orgAction, Long speId, String speAction, String onlinetime, String price, String salesnum, String stsalesnum, Map bookinfo,
			String actions) throws ServiceException;
	
	public void doBookArticleIndex(Long bookid, Long orgId, String orgAction, Long speId, String speAction, String onlinetime, String price, String salesnum, Map bookarticleinfo,
			String actions) throws ServiceException;
	
	public void doArticleIndex(Long articleId,
			String actions) throws ServiceException;
	
	public void doSpecialIndex(Long specialId,
			String actions, Long selectType, Long orgid) throws ServiceException;
	
	public SolrQueue doBookIndexNoSave(Long bookId, Long orgId, String orgAction, Long speId, String speAction, String onlinetime, String price, String salesnum, String stsalesnum, Map bookinfo,
			String actions) throws ServiceException;
	
}
