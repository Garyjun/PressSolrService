package com.brainsoon.solr.service;

import com.brainsoon.solr.po.SolrQueue;
import com.channelsoft.appframe.exception.ServiceException;

/**
 * Description: <p>索引创建业务类</p>
 * Content Desc:<p><p>
 */
public interface ICreateIndexFacede {

	/**
	 * 创建专题库索引
	 * @param doc 查询对象
	 * @return
	 * @throws ServiceException
	 */
	public void createSpecialIndex(SolrQueue queue);

	/**
	 * 创建专题库图书索引
	 * @param doc 查询对象
	 * @return
	 * @throws ServiceException
	 */
	public void createSpecialBookIndex(SolrQueue queue);

}
