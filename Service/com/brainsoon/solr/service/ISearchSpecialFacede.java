package com.brainsoon.solr.service;

import com.brainsoon.solr.po.SearchResource;
import com.brainsoon.solr.util.PageSupport;
import com.brainsoon.solr.util.SearchSpecial;
import com.channelsoft.appframe.exception.ServiceException;

public interface ISearchSpecialFacede {

	public PageSupport searchSpecial(SearchSpecial doc) throws ServiceException;
	
	public PageSupport searchSpecialAdvanced(SearchSpecial doc) throws ServiceException;
	
	public PageSupport searchSpecialBookContent(SearchSpecial doc) throws ServiceException;
	
	public void deleteSpecialIndex(String spid, String orgid)  throws ServiceException;
	
	public void deleteSpecialBookIndex(String[] bookids, String spid, String orgid)  throws ServiceException;
	
	public PageSupport searchSpecialArticleAdvanced(SearchResource doc) throws ServiceException;
	
	public PageSupport searchSpecialArticle(SearchResource doc) throws ServiceException;
}
