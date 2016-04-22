package com.brainsoon.solr.service;

import java.io.File;
import java.util.List;

import com.brainsoon.solr.po.Resource;
import com.brainsoon.solr.po.SearchResource;
import com.brainsoon.solr.util.PageSupport;
import com.brainsoon.solr.util.SearchArticle;
import com.brainsoon.solr.util.SearchBookArticle;
import com.brainsoon.solr.util.SearchDocument;
import com.brainsoon.solr.util.SearchNews;
import com.channelsoft.appframe.exception.ServiceException;

/**
 * Description: <p>为Solr提供封装门面模式</p>
 * Content Desc:<p><p>
 */
public interface ISearchFacede {

	
	/**
	 * 搜索文档服务器
	 * @param doc 查询对象
	 * @return
	 * @throws ServiceException
	 */
	public PageSupport searchDocument(SearchDocument doc) throws ServiceException;
	
	public PageSupport searchOneDocument(String uuid, SearchDocument doc) throws ServiceException;
	
	//文章高级搜索
	public PageSupport searchArticleAdvanced(SearchResource doc) throws ServiceException;
	
	/**
	 * 搜索文档服务器
	 * @param doc 查询对象
	 * @return
	 * @throws ServiceException
	 */
	public PageSupport searchBookArticle(SearchBookArticle doc) throws ServiceException;
	
	public PageSupport searchOneBookArticle(String uuid, SearchBookArticle doc) throws ServiceException;
	
	/**
	 * 专题库文章高级搜索文档服务器
	 * @param doc 查询对象
	 * @return
	 * @throws ServiceException
	 */
	public PageSupport searchBookArticleAdvancedDocument(SearchBookArticle doc) throws ServiceException;
	
	/**
	 * 搜索文档服务器
	 * @param doc 查询对象
	 * @return
	 * @throws ServiceException
	 */
	public String searchAutoComplete(String keywords) throws ServiceException;
	
	/**
	 * 增加索引
	 * @param resource
	 * @param file
	 * @throws ServiceException
	 */
	public void addIndex(Resource resource,File file) throws ServiceException;
	
	/**
	 * 增加索引
	 * @param resource
	 * @throws ServiceException
	 */
	public void addIndex(Resource resource) throws ServiceException;	
	
	public void addSpecialIndex(Resource resource) throws ServiceException;
	
	public void addSpecialBookIndex(Resource resource,File file) throws ServiceException;
	
	public void addSpecialBookIndex(Resource resource) throws ServiceException;
	
	/**
	 * 更新索引
	 * @param resource
	 * @throws ServiceException
	 */
	public void updateIndex(Resource resource) throws ServiceException;
	
	/**
	 * 删除索引
	 * @param resource
	 * @throws ServiceException
	 */
	public void deleteIndex(Resource resource) throws ServiceException;
	
	/**
	 * 批量删除索引
	 * @param uuids
	 * @throws ServiceException
	 */
	public void deleteIndex(String[] uuids)  throws ServiceException;
	
	/**
	 * 删除某个字段下的索引
	 * @param key
	 * @param value
	 * @author yugang
	 * */
	public void deleteByQuery(String key, String value) throws ServiceException;
	
	/**
	 * 批量删除索引
	 * @param uuid
	 * @throws ServiceException
	 */
	public void deleteIndex(String uuid)  throws ServiceException;
	
	/**
	 * 批量删除索引
	 * @param resource
	 * @throws ServiceException
	 */
	public void deleteIndxes(List resources) throws ServiceException;
	
	/**
	 * 查看搜索服务器是否可用
	 * @return
	 * @throws ServiceException
	 */
	public String pingSearchEngin() throws ServiceException;
	
	/**
	 * 查看搜索服务器是否可用
	 * @return
	 * @throws ServiceException
	 */
	public boolean isCanUseSolrServer() throws ServiceException;
	
	/**
	 * 优化搜索服务器索引
	 * @throws ServiceException
	 */
	public void optimizeSearchEngin() throws ServiceException;
	
	/**
	 * 清除Solr搜索引擎所有索引
	 * @throws ServiceException
	 */
	public void cleanSolrIndex() throws ServiceException;
	
	public void deleteJournalIndex(String[] uuids)  throws ServiceException;
	
	public void deleteArticleIndex(String[] uuids)  throws ServiceException;

	public void deleteNewsIndex(String[] uuids);

	public void deleteEventIndex(String[] jeventidsArr);
	
	public PageSupport searchNews(SearchResource search);

	public PageSupport searchJournal(SearchResource search);
	
	public PageSupport searchArticle(SearchResource search) throws ServiceException;
	
	public PageSupport searchEvent(SearchResource search) throws ServiceException;

}
