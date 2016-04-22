package com.brainsoon.solr.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.AnalysisPhase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.TokenInfo;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;

import com.brainsoon.solr.po.Resource;
import com.brainsoon.solr.po.SearchResource;
import com.channelsoft.appframe.utils.DateUtil;

/**
 * Description: <p>与Solr搜索服务器通讯Client</p>
 * Content Desc:<p>提供搜索服务：query,update,delete,optimize<p>
 * 
 */
public class SolrClient {
	
	private static final Logger logger = Logger.getLogger(SolrClient.class);
	public static final String RICH_HANDLE_URL = "/update/extract";
	
	public SolrClient(){}
	
	/**
	 * 利用solr的CommonParams.Q查询
	 * @param keyword 查询字符串
	 * @param cls 查询的类
	 * @param start 查询起始记录
	 * @param rows 查询条数
	 * @param server
	 * @return PaginationSupport<T>
	 */
	public static <T>PageSupport<T> query(String filterQueryStr, SearchDocument doc, String queryStr, Class<T> cls, int start, int rows, SolrServer server) {
		
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		if(StringUtils.isNotBlank(filterQueryStr))
			query.addFilterQuery(filterQueryStr);
		query.setHighlight(true);
		query.setParam("hl.fl", "title,authorname,tags,description,files");
		query.setParam("defType", "edismax");
		query.setParam("qf", "title^1000000000 authorname^100000000 tags^10000000 description^1000000 files^0.001");
		query.setFacet(true);
		query.addFacetField("typeid");
		query.setStart(start);
		query.setRows(rows);

		query.setParam("fl", "uuid,title,authorname,pressname," +
				"pubdate,publishdate,publishaddress,description,tags," +
				"subject,isbn,restype," +
				"prodid,orgid,speid," +
				"price,onlinetime,salesnum,stsalesnum,typeid,typename,resfileid,filetype,era,genre,listprice,pbooks_price,status,ststatus");
		
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
			
		} catch (SolrServerException e) {
			logger.error(e);
			e.printStackTrace();
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
       
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();

		List<FacetField> Facets = response.getFacetFields();
		
		/*
		for(Map.Entry<String,Map<String,List<String>>> entry: highLightings.entrySet()) {  
			Map<String,List<String>> mapValue = entry.getValue(); 
			
		} 
		*/
		
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
		SolrClient.parseSolrHighlighting(highLightings, pageSupport);

		//解析分类
		SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
		//return new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	/**
	 * 利用solr的CommonParams.Q查询
	 * @param keyword 查询字符串
	 * @param cls 查询的类
	 * @param start 查询起始记录
	 * @param rows 查询条数
	 * @param server
	 * @return PaginationSupport<T>
	 */
	public static <T>PageSupport<T> query(SearchBookArticle doc, String queryStr, Class<T> cls, int start, int rows, SolrServer server) {
		
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		query.setStart(start);
		query.setRows(rows);
		query.setFacet(true);
		query.addFacetField("typeid");
		setQuerySort(query, doc);
		
//		query.setParam("hl", true);
//		query.setParam("hl.fl", "files");
		query.setParam("fl", "uuid,title,authorname,pressname," +
				"pubdate,publishdate,publishaddress,description,tags," +
				"subject,isbn,restype," +
				"prodid,orgid,speid," +
				"price,onlinetime,salesnum,typeid,typename,resfileid,filetype,era,genre");
		
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
		} catch (SolrServerException e) {
			logger.error(e);
			e.printStackTrace();
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = new SolrDocumentList();
		int totalCount = 0;

		sdl = response.getResults();
		totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();

       
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();

		List<FacetField> Facets = response.getFacetFields();
		
		/*
		for(Map.Entry<String,Map<String,List<String>>> entry: highLightings.entrySet()) {  
			Map<String,List<String>> mapValue = entry.getValue(); 
			
		} 
		*/
		
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
		SolrClient.parseSolrHighlighting(highLightings, pageSupport);
		//解析分类
		//SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
		//return new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	public static <T>PageSupport<T> query(String filterQueryStr, SearchArticle doc, String queryStr, Class<T> cls, int start, int rows, SolrServer server) {
		
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		if(StringUtils.isNotBlank(filterQueryStr))
			query.addFilterQuery(filterQueryStr);
		query.setHighlight(true);
		query.setParam("hl.fl", "title,description,files");
		query.setParam("defType", "edismax");
		query.setParam("qf", "title^1000000000 description^1000000 files^0.001");
		query.setFacet(true);
		query.addFacetField("typeid");
		query.setStart(start);
		query.setRows(rows);
		
		query.setParam("fl", "uuid,title,description,restype,prodid,onlinetime,typeid,typename,filetype,genre,era");
		
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
			
		} catch (SolrServerException e) {
			logger.error(e);
			e.printStackTrace();
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();
		
		List<FacetField> Facets = response.getFacetFields();
		
		/*
		for(Map.Entry<String,Map<String,List<String>>> entry: highLightings.entrySet()) {  
			Map<String,List<String>> mapValue = entry.getValue(); 
			
		} 
		*/
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
		SolrClient.parseSolrHighlighting(highLightings, pageSupport);

		//解析分类
		SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
		//return new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	/**
	 * 利用solr的CommonParams.Q查询
	 * @param keyword 查询字符串
	 * @param cls 查询的类
	 * @param start 查询起始记录
	 * @param rows 查询条数
	 * @param server
	 * @return PaginationSupport<T>
	 */
	public static <T>PageSupport<T> queryAdvanced(String queryStr, Class<T> cls, int start, int rows, String sorts, String orders, String searchType, SolrServer server, String filterQueryStr) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		if(StringUtils.isNotBlank(filterQueryStr))
			query.addFilterQuery(filterQueryStr);
	
		query.setParam("fl", "uuid,prodid,restype,orgid,speid," +
				"title,author,editor,published,description,keywords," +
				"stream,price,batchprice,actprice,onlinetime,typeid," +
				"sourceid,thumb,url,belongjournal,magazineyear," +
				"magazineyearnum,magazinenum");
		query.setHighlight(true);
		query.setParam("hl.fl", "title,author,keywords,description,files");
		query.setParam("defType", "edismax");
		query.setParam("qf", "title^1000000000 author^100000000 keywords^10000000 description^1000000 files^0.001");
		query.setFacet(true);
		query.addFacetField("typeid");
		
		if(start > -1) {
			query.setParam("start", String.valueOf(start));
			query.setParam("rows", String.valueOf(rows));		
		}
		
		logger.debug(query);
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
			
		} catch (SolrServerException e) {
			logger.error(e);
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();
		List<FacetField> Facets = response.getFacetFields();
		
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
		SolrClient.parseSolrHighlighting(highLightings, pageSupport);
		//解析分类
		SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
		//return new PageSupport<T>(SolrEntfityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	public static <T>PageSupport<T> queryAdvanced(SearchBookArticle doc, Class<T> cls, int start, int rows, SolrServer server) {
		
		SolrQuery query = new SolrQuery();
		query.setQuery(doc.getQueryStat());
		query.setStart(start);
		query.setFacet(true);
		query.addFacetField("typeid");
		setQuerySort(query, doc);
		query.setRows(rows);
		
		query.setParam("hl", true);
		query.setParam("hl.fl", "files");
		query.setParam("fl", "uuid,title,authorname,pressname," +
				"pubdate,publishdate,publishaddress,description,tags," +
				"subject,isbn,restype," +
				"prodid,orgid,speid," +
				"price,onlinetime,salesnum,typeid,typename,resfileid,filetype,era,genre");
		
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
			
		} catch (SolrServerException e) {
			logger.error(e);
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();
		List<FacetField> Facets = response.getFacetFields();
		
		/*
		for(Map.Entry<String,Map<String,List<String>>> entry: highLightings.entrySet()) {  
			Map<String,List<String>> mapValue = entry.getValue(); 
			
		} 
		*/
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
		SolrClient.parseSolrHighlighting(highLightings, pageSupport);
		//解析分类
		SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
		//return new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	public static <T>PageSupport<T> querySpecial(String filterQueryStr, String queryStr, Class<T> cls, int start, int rows, String sorts, String orders, String searchType, SolrServer server) {
		
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		
		if(StringUtils.isNotBlank(filterQueryStr))
			query.addFilterQuery(filterQueryStr);
	
		query.setParam("fl", "uuid,title,description,restype,prodid,onlinetime,tags,era,typeid,typename,tags,orgid,genre");
		query.setHighlight(true);
		query.setParam("hl.fl", "title,tags,description");
		query.setParam("defType", "edismax");
		query.setParam("qf", "title^1000000000 tags^100 description^1");
		query.setFacet(true);
		query.addFacetField("typeid");

		
//		if(StringUtils.isNotBlank(sorts) && StringUtils.isNotBlank(orders)) {			
//			String[] sortArr = sorts.split(",");
//			String[] orderArr = orders.split(",");
//			if(sortArr.length == orderArr.length) {
//				for(int i = 0; i < sortArr.length; i++) {
//					ORDER order = ORDER.desc;
//					if(orderArr[i].equals("asc")) {
//						order = ORDER.asc;
//					}
//					query.addSort(sortArr[i], order);					
//				}
//			}
//		}
		
		if(start > -1) {
			query.setParam("start", String.valueOf(start));
			query.setParam("rows", String.valueOf(rows));		
		}
		logger.debug(query);
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
			
		} catch (SolrServerException e) {
			logger.error(e);
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();
		List<FacetField> Facets = response.getFacetFields();
		
		/*
		for(Map.Entry<String,Map<String,List<String>>> entry: highLightings.entrySet()) {  
			Map<String,List<String>> mapValue = entry.getValue(); 
			
		} 
		*/
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);

//		SolrClient.parseSolrHighlighting(highLightings, pageSupport);
		//解析分类
		SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
		//return new PageSupport<T>(SolrEntfityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	public static <T>PageSupport<T> querySpecialAdvanced(String queryStr, Class<T> cls, int start, int rows, String sorts, String orders, String searchType, SolrServer server, String filterQueryStr) {
		
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		if(StringUtils.isNotBlank(filterQueryStr))
			query.addFilterQuery(filterQueryStr);
		
		query.setParam("fl", "uuid,title,description,restype,prodid,onlinetime,tags,era,typeid,typename,tags,orgid,genre");
		query.setHighlight(true);
		query.setParam("hl.fl", "title,tags,description");
		query.setParam("defType", "edismax");
		query.setParam("qf", "title^1000 tags^100 description^1");
		query.setFacet(true);
		query.addFacetField("typeid");
//		if(StringUtils.isNotBlank(sorts) && StringUtils.isNotBlank(orders)) {
//			//sorts onlinetime orders desc
//			String[] sortArr = sorts.split(",");
//			String[] orderArr = orders.split(",");
//			if(sortArr.length == orderArr.length) {
//				for(int i = 0; i < sortArr.length; i++) {
//					ORDER order = ORDER.desc;
//					if(orderArr[i].equals("asc")) {
//						order = ORDER.asc;
//					}
//					query.addSort(sortArr[i], order);					
//				}
//			}
//		}
		if(start > -1) {
			query.setParam("start", String.valueOf(start));
			query.setParam("rows", String.valueOf(rows));		
		}
		logger.debug(query);
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
			
		} catch (SolrServerException e) {
			logger.error(e);
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();
		List<FacetField> Facets = response.getFacetFields();
		
		/*
		for(Map.Entry<String,Map<String,List<String>>> entry: highLightings.entrySet()) {  
			Map<String,List<String>> mapValue = entry.getValue(); 
			
		} 
		*/
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
//		SolrClient.parseSolrHighlighting(highLightings, pageSupport);
		//解析分类
		SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
		//return new PageSupport<T>(SolrEntfityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	public static <T>PageSupport<T> querySpecialBook(String queryStr, String filterQueryStr, Class<T> cls, int start, int rows, SolrServer server) {
		
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		if(StringUtils.isNotBlank(filterQueryStr))
			query.addFilterQuery(filterQueryStr);
		query.setParam("fl", "uuid,prodid,restype,orgid,speid," +
				"title,author,editor,published,description,keywords," +
				"stream,price,batchprice,actprice,onlinetime,typeid," +
				"sourceid,thumb,url,belongjournal,magazineyear," +
				"magazineyearnum,magazinenum");
		query.setHighlight(true);
		query.setParam("hl.fl", "title,author,keywords,description,files");
		query.setParam("defType", "edismax");
		query.setParam("qf", "title^1000000000 author^100000000 keywords^10000000 description^1000000 files^0.001");
		query.setFacet(true);
		query.addFacetField("typeid");
		
//		if(StringUtils.isNotBlank(sorts) && StringUtils.isNotBlank(orders)) {			
//			String[] sortArr = sorts.split(",");
//			String[] orderArr = orders.split(",");
//			if(sortArr.length == orderArr.length) {
//				for(int i = 0; i < sortArr.length; i++) {
//					ORDER order = ORDER.desc;
//					if(orderArr[i].equals("asc")) {
//						order = ORDER.asc;
//					}
//					query.addSort(sortArr[i], order);					
//				}
//			}
//		}
		
		if(start > -1) {
			query.setParam("start", String.valueOf(start));
			query.setParam("rows", String.valueOf(rows));		
		}
		logger.debug(query);
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
			
		} catch (SolrServerException e) {
			logger.error(e);
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();
		List<FacetField> Facets = response.getFacetFields();		
		
//		for(Map.Entry<String,Map<String,List<String>>> entry: highLightings.entrySet()) {  
//			Map<String,List<String>> mapValue = entry.getValue(); 			
//		} 
		
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
		SolrClient.parseSolrHighlighting(highLightings, pageSupport);
		//解析分类
		SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
		//return new PageSupport<T>(SolrEntfityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	public static <T>PageSupport<T> querySpecialBookContent(String filterQueryStr, String queryStr, Class<T> cls, int start, int rows, String sorts, String orders, String searchType, SolrServer server) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		if(StringUtils.isNotBlank(filterQueryStr))
			query.addFilterQuery(filterQueryStr);
		query.setParam("fl", "uuid,title,authorname,pressname," +
				"pubdate,publishdate,publishaddress,description,tags," +
				"subject,isbn,restype," +
				"prodid,orgid,speid," +
				"price,typeid,typename,resfileid,filetype,genre,listprice,pbooks_price");
		query.setHighlight(true);
		query.setParam("hl.fl", "title,authorname,tags,description,files");
		query.setParam("defType", "edismax");
		query.setParam("qf", "title^1000000000 authorname^100000000 tags^10000000 description^1000000 files^0.001");
		query.setFacet(true);
		query.addFacetField("typeid");
		
		if(start > -1) {
			query.setParam("start", String.valueOf(start));
			query.setParam("rows", String.valueOf(rows));		
		}
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
			
		} catch (SolrServerException e) {
			logger.error(e);
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();
		List<FacetField> Facets = response.getFacetFields();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
		SolrClient.parseSolrHighlighting(highLightings, pageSupport);
		SolrClient.parseSolrFacets(Facets, pageSupport);
		
		return pageSupport;
	}
	
	public static <T>PageSupport<T> querySpecialBookAdvanced(String queryStr, Class<T> cls, int start, int rows, String sorts, String orders, SolrServer server, String filterQueryStr) {
		
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		if(StringUtils.isNotBlank(filterQueryStr))
			query.addFilterQuery(filterQueryStr);
	
		query.setParam("fl", "uuid,prodid,restype,orgid,speid," +
				"title,author,editor,published,description,keywords," +
				"stream,price,batchprice,actprice,onlinetime,typeid," +
				"sourceid,thumb,url,belongjournal,magazineyear," +
				"magazineyearnum,magazinenum");
		query.setHighlight(true);
		query.setParam("hl.fl", "title,author,keywords,description,files");
		query.setParam("defType", "edismax");
		query.setParam("qf", "title^1000000000 author^100000000 keywords^10000000 description^1000000 files^0.001");
		query.setFacet(true);
		query.addFacetField("typeid");
		
//		if(StringUtils.isNotBlank(sorts) && StringUtils.isNotBlank(orders)) {			
//			String[] sortArr = sorts.split(",");
//			String[] orderArr = orders.split(",");
//			if(sortArr.length == orderArr.length) {
//				for(int i = 0; i < sortArr.length; i++) {
//					ORDER order = ORDER.desc;
//					if(orderArr[i].equals("asc")) {
//						order = ORDER.asc;
//					}
//					query.addSort(sortArr[i], order);					
//				}
//			}
//		}
		if(start > -1) {
			query.setParam("start", String.valueOf(start));
			query.setParam("rows", String.valueOf(rows));		
		}
		logger.debug(query);
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
			
		} catch (SolrServerException e) {
			logger.error(e);
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();
		List<FacetField> Facets = response.getFacetFields();
		
		/*
		for(Map.Entry<String,Map<String,List<String>>> entry: highLightings.entrySet()) {  
			Map<String,List<String>> mapValue = entry.getValue(); 
			
		} 
		*/
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
		SolrClient.parseSolrHighlighting(highLightings, pageSupport);
		//解析分类
		SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
		//return new PageSupport<T>(SolrEntfityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	/**
	 * 设置查询的排序方式
	 * @param query
	 * @param doc
	 */
	private static void  setQuerySort(SolrQuery query, SearchDocument doc) {
		String sortField = doc.getSortField();
		String sortWay   = doc.getSortWay();
		SolrQuery.ORDER order =null;
		
		if(StringUtils.equals("desc", sortWay.toLowerCase())) {
			order = SolrQuery.ORDER.desc;
		} else if(StringUtils.equals("asc", sortWay.toLowerCase())) {
			order = SolrQuery.ORDER.asc;
		}
		
		if(StringUtils.isNotEmpty(sortField) && order != null) {
			query.setSort(sortField.toLowerCase(), order);
		}
	}
	
	private static void  setQuerySort(SolrQuery query, SearchBookArticle doc) {
		String sortField = doc.getSortField();
		String sortWay   = doc.getSortWay();
		SolrQuery.ORDER order =null;
		
		if(StringUtils.equals("desc", sortWay.toLowerCase())) {
			order = SolrQuery.ORDER.desc;
		} else if(StringUtils.equals("asc", sortWay.toLowerCase())) {
			order = SolrQuery.ORDER.asc;
		}
		
		if(StringUtils.isNotEmpty(sortField) && order != null) {
			query.setSort(sortField.toLowerCase(), order);
		}
	}
	
	private static void  setQuerySort(SolrQuery query, SearchArticle doc) {
		String sortField = doc.getSortField();
		String sortWay   = doc.getSortWay();
		SolrQuery.ORDER order =null;
		
		if(StringUtils.equals("desc", sortWay.toLowerCase())) {
			order = SolrQuery.ORDER.desc;
		} else if(StringUtils.equals("asc", sortWay.toLowerCase())) {
			order = SolrQuery.ORDER.asc;
		}
		
		if(StringUtils.isNotEmpty(sortField) && order != null) {
			query.setSort(sortField.toLowerCase(), order);
		}
	}
	
//	private static void parseHighlighting(String keyword,PageSupport pageSupport) {
//		if(StringUtils.isBlank(keyword)) {
//			return;
//		}
//		if( pageSupport==null || pageSupport.getTotalCount()==0) return;
//		List<Resource> records = pageSupport.getItems();
//		Resource doc = null;
//		for(int i=0;i<records.size();i++){
//			doc = (Resource)records.get(i);
//			String title = doc.getTitle();
//			if(StringUtils.isNotBlank(title)) {
//				if(title.indexOf(keyword) >= 0) {
//					title = title.replaceAll(keyword, "<em>" + keyword + "</em>");
//					doc.setTitle(title);
//				}
//			}
//			String authorname = doc.getAuthorname();
//			if(StringUtils.isNotBlank(authorname)) {				
//				if(authorname.indexOf(keyword) >= 0) {
//					authorname = authorname.replaceAll(keyword, "<em>" + keyword + "</em>");
//					doc.setAuthorname(authorname);
//				}
//			}			
//			String description = doc.getDescription();
//			if(StringUtils.isNotBlank(description)) {
//				if(description.indexOf(keyword) >= 0) {
//					description = SolrUtil.Html2Text(description);
//					description = SolrUtil.getWeiBoStr(description, 160);
//					logger.debug("description=====================" + description);					
//					if(!description.endsWith(".")) {
//						description = description + ".....";
//					}
//					int idx = description.indexOf(".....");
//					description.length();
//					description = description.replaceAll(keyword, "<em>" + keyword + "</em>");
//					doc.setDescription(description);
//				}	
//			}
//			
//		}
//		orderRecords(records);
//		
//		pageSupport.setItems(records);
//	}	
	
	private static void orderRecords(List<Resource> records) {
		Collections.sort(records, new Comparator<Resource>(){
			public int compare(Resource d1, Resource d2) {
				Integer score1 = 0;
				String title = d1.getTitle();
				if(StringUtils.isNotBlank(title)) {
					if(title.indexOf("<em>") >= 0) {
						score1 = score1 + 10;
					}
				}
				String authorname = d1.getAuthor();
				if(StringUtils.isNotBlank(authorname)) {
					if(authorname.indexOf("<em>") >= 0) {
						score1 = score1 + 5;
					}
				}
				String description = d1.getDescription();
				if(StringUtils.isNotBlank(description)) {
					if(description.indexOf("<em>") >= 0) {
						score1 = score1 + 1;
					}
				}
				Integer score2 = 0;
				String title2 = d2.getTitle();
				if(StringUtils.isNotBlank(title2)) {
					if(title2.indexOf("<em>") >= 0) {
						score2 = score2 + 10;
					}
				}
				String authorname2 = d2.getAuthor();
				if(StringUtils.isNotBlank(authorname2)) {
					if(authorname2.indexOf("<em>") >= 0) {
						score2 = score2 + 5;
					}
				}
				String description2 = d2.getDescription();
				if(StringUtils.isNotBlank(description2)) {
					if(description2.indexOf("<em>") >= 0) {
						score2 = score2 + 1;
					}
				}
				int score = score2 - score1;
				return score;
			}
		});
	}
	/**
	 * 处理Solr搜索引擎返回的高亮显示内容
	 * @param highLightings
	 * @throws Exception
	 */
	private static void parseSolrHighlighting(Map<String,Map<String,List<String>>> highLightings,PageSupport pageSupport) {
		if( highLightings==null ) return;
		if( pageSupport==null || pageSupport.getTotalCount()==0) return;
		List records = pageSupport.getItems();
		Resource doc = null;
		Map entry = null;
		String highLighting = null;
		for(int i=0;i<records.size();i++){
			doc = (Resource)records.get(i);
			if( highLightings.containsKey(doc.getUuid()) ){
				//处理高亮显示
				entry = (Map)highLightings.get(doc.getUuid());
				List titles = (List)entry.get("title");
				if(titles != null && titles.size() > 0) {
					doc.setTitle((String)titles.get(0));
				}
				List authors = (List)entry.get("author");	
				if(authors != null && authors.size() > 0) {
					doc.setAuthor((String)authors.get(0));
				}
				List tags = (List)entry.get("keywords");
				if(tags != null && tags.size() > 0) {
					doc.setKeywords((String)tags.get(0));
				}
				List descriptions = (List)entry.get("description");
				if(descriptions != null && descriptions.size() > 0) {
					doc.setDescription((String)descriptions.get(0));
				}
				List contents = (List)entry.get("files");
				if(contents != null && contents.size() > 0) {
					doc.setFiles((String)contents.get(0));
					if(descriptions == null || descriptions.size() == 0) {
						doc.setDescription((String)contents.get(0));
					}
				}				
			}
		}
		pageSupport.setItems(records);
	}
	
	private static void parseSolrFacets(List<FacetField> facets,PageSupport pageSupport) {
		if( facets==null ) return;
		if( pageSupport==null || pageSupport.getTotalCount()==0) return;
		List<Category> categorys = new LinkedList<Category>();
		for(FacetField facetField : facets) {
			Category category = new Category();
			category.setName(facetField.getName());
			Map<String,Long> values = new HashMap<String,Long>();
			for(Count count:facetField.getValues()) {
				if(count.getCount()>0){
					values.put(count.getName(), count.getCount());
				}
			}
			category.setFields(values);
			categorys.add(category);
		}
		pageSupport.setFacets(categorys);
	}
	
	
	public String query2(String keyword, int start, int rows, SolrServer server) {
		
		
		//select?spellcheck.build=true&rows=0&q=*:*&facet=true&facet.field=suggestion&facet.mincount=1&facet.prefix=hear
		SolrQuery query = new SolrQuery();
		query.setQueryType("suggests");
		query.set("q", "*:*");
		
		SolrServer solrs = null;
		// SolrServer　solr　=　null;　
		 try {
			 solrs  = new 	HttpSolrServer("http://localhost:8088/solr");
			 QueryResponse response = solrs.query(query);
			 
			  
			 
		 }catch(Exception ex) {
			 logger.error(ex);
		 }


//		SolrQuery query = new SolrQuery();
//		
//		query.setParam("spellcheck.build", true);
//		query.setParam("facet", true);
//		
//		query.setParam("facet.field", "suggestion");
//		query.setParam("fl", "suggestion");
//		query.setParam("facet.mincount", "1");
//		query.setParam("facet.prefix", "hear");
//		query.setParam("q", keyword);
//		QueryResponse response = null;
//		try {
//			response = server.query(query);
//			
//		} catch (SolrServerException e) {
//			logger.error(e);
//			return null;
//		}catch (Exception e) {
//			logger.error(e);
//			return null;
//		}
//		
//		List<JosnObj> jsonList = new ArrayList<JosnObj>();
//		SolrDocumentList docList = response.getResults();
//		 for(SolrDocument solrDoc : docList)
//	        {
//			 List<String> docNames = (List<String>)solrDoc.get("suggestion");
//			  for(int i=0;i<docNames.size();i++) {
//				  if(docNames.get(i).length()>20) 
//					  continue;
//				  JosnObj obj = new JosnObj();
//				  obj.setName("name");
//				  obj.setTo(docNames.get(i));
//				  jsonList.add(obj);
//			  }
//	        }
//		 
		// return JSONArray.fromObject(jsonList).toString();
		 return "";
	}
	
	/**
	 *  利用solr的SolrParams查询
	 * @param params 查询参数Map
	 * @param cls 查询的类
	 * @param start 查询起始记录
	 * @param rows 查询条数
	 * @param server
	 * @return PaginationSupport<T>
	 */
	public static <T>PageSupport<T> query(SolrParams params, Class<T> cls, int start, int rows, SolrServer server) {
		QueryResponse response = null;
		
		try {
			response = server.query(params);
		} catch (SolrServerException e) {
			logger.error(e);
			return null;
		}
		SolrDocumentList sdl = response.getResults();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		return new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
	}
	
	
	/**
	 * 提交富文档是
	 * @param doc
	 * @param file
	 * @param server
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public void commitRichDocument(Resource resource ,File file, SolrServer server) throws IOException, SolrServerException{
		ContentStreamUpdateRequest up = new ContentStreamUpdateRequest(RICH_HANDLE_URL);
		//设置提交参数 
		if( file!=null && file.exists()) 
			up.addFile(file, null);
		up.setParam("literal.uuid",resource.getUuid());
		if( resource.getProdid()!=null ) up.setParam("literal.prodid", resource.getProdid());
		if( resource.getRestype()!=null ) up.setParam("literal.restype", resource.getRestype());
		if( resource.getSpeid()!=null ) up.setParam("literal.speid", resource.getSpeid());
		if( resource.getOrgid()!=null ) up.setParam("literal.orgid", resource.getOrgid());
		if( resource.getTitle()!=null ) up.setParam("literal.title", resource.getTitle());
		if( resource.getAuthor()!=null ) up.setParam("literal.author", resource.getAuthor());
		if( resource.getEditor()!=null ) up.setParam("literal.editor", resource.getEditor());
		if( resource.getPublished()!=null ) up.setParam("literal.published", resource.getPublished());
		String description = resource.getDescription();
 		if( description!=null ) {
 			description = SolrUtil.Html2Text(description);
			up.setParam("literal.description", description);
 		}
		if( resource.getKeywords()!=null ) up.setParam("literal.keywords", resource.getKeywords());
		if( resource.getStream()!=null ) up.setParam("literal.stream", resource.getStream());
		if( resource.getPrice()!=null ) up.setParam("literal.price", resource.getPrice());
		if( resource.getBatchprice()!=null ) up.setParam("literal.batchprice", resource.getBatchprice());
		if( resource.getActprice()!=null ) up.setParam("literal.actprice", resource.getActprice());
		if( resource.getOnlinetime()!=null ) up.setParam("literal.onlinetime", resource.getOnlinetime());
		if( resource.getTypeid()!=null ) up.setParam("literal.typeid", resource.getTypeid());
		if( resource.getSourceid()!=null ) up.setParam("literal.sourceid", resource.getSourceid());
		if( resource.getThumb()!=null ) up.setParam("literal.thumb", resource.getThumb());
		if( resource.getUrl()!=null ) up.setParam("literal.url", resource.getUrl());
		if( resource.getBelongjournal()!=null ) up.setParam("literal.belongjournal", resource.getBelongjournal());
		if( resource.getMagazineyear()!=null ) up.setParam("literal.magazineyear", resource.getMagazineyear());
		if( resource.getMagazineyearnum()!=null ) up.setParam("literal.magazineyearnum", resource.getMagazineyearnum());	
		if( resource.getMagazinenum()!=null ) up.setParam("literal.magazinenum", resource.getMagazinenum());
		
		up.setParam("literal.indextime",DateUtil.convertDateTimeToString(new Date()));
		up.setParam("fmap.content", "files");
		up.setParam("boost.title", "100");
		up.setParam("boost.author", "50");
		up.setParam("boost.description", "0.5");
		up.setParam("boost.files", "0.01");
		up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);  
		server.request(up);
	}
	
	public void commitDocument(Resource resource , SolrServer server) throws IOException, SolrServerException{
		SolrInputDocument doc = new SolrInputDocument();  
		doc.addField("uuid", resource.getUuid());  
		if( resource.getProdid()!=null ) doc.addField("prodid", resource.getProdid());
		if( resource.getRestype()!=null ) doc.addField("restype", resource.getRestype()); 
		if( resource.getSpeid()!=null ) doc.addField("speid", resource.getSpeid());
		if( resource.getOrgid()!=null ) doc.addField("orgid", resource.getOrgid());
        if( resource.getTitle()!=null ) doc.addField("title", resource.getTitle(),1000f);
        if( resource.getAuthor()!=null ) doc.addField("author", resource.getAuthor(),10f);
		if( resource.getEditor()!=null ) doc.addField("editor", resource.getEditor());
		if( resource.getPublished()!=null ) doc.addField("published", resource.getPublished());
		String description = resource.getDescription();
		if( description != null ) {
 			description = SolrUtil.Html2Text(description);
 			doc.addField("description", description, 1f);
 		}        
        if( resource.getKeywords()!=null ) doc.addField("keywords", resource.getKeywords(),100f);
        if( resource.getStream()!=null ) doc.addField("stream", resource.getStream());
		if( resource.getPrice()!=null ) doc.addField("price", resource.getPrice());
		if( resource.getBatchprice()!=null ) doc.addField("batchprice", resource.getBatchprice());
		if( resource.getActprice()!=null ) doc.addField("actprice", resource.getActprice());
		if( resource.getOnlinetime()!=null ) doc.addField("onlinetime", resource.getOnlinetime());
		if( resource.getTypeid()!=null ) doc.addField("typeid", resource.getTypeid());
		if( resource.getSourceid()!=null ) doc.addField("sourceid", resource.getSourceid());
		if( resource.getThumb()!=null ) doc.addField("thumb", resource.getThumb());
		if( resource.getUrl()!=null ) doc.addField("url", resource.getUrl());
		if( resource.getBelongjournal()!=null ) doc.addField("belongjournal", resource.getBelongjournal());
		if( resource.getMagazineyear()!=null ) doc.addField("magazineyear", resource.getMagazineyear());		
		if( resource.getMagazineyearnum()!=null ) doc.addField("magazineyearnum", resource.getMagazineyearnum());	
		if( resource.getMagazinenum()!=null ) doc.addField("magazinenum", resource.getMagazinenum());	
		
		if( resource.getFiles()!=null ) doc.addField("files", resource.getFiles());	
		
		doc.addField("indextime", DateUtil.convertDateTimeToString(new Date()));
		
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();  
        docs.add(doc);  
        server.add(docs);  
        UpdateRequest req = new UpdateRequest();  
        req.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);  
        req.add(docs);  
        req.process(server);  
	}
	
	/**
	 * 提交数据
	 * @param obj
	 * @param server
	 */
	public void commitObject(Object obj, SolrServer server) {
		try {
			server.add(SolrEntityConvert.entity2SolrInputDocument(obj));
			server.commit(false, false);
			logger.info("成功提交 1 个元素到SOLR SYSTEM中");
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * 提交数据采用批量提交
	 * @param objectList
	 * @param server
	 */
	public <T> void commitList(List<T> objectList, SolrServer server) {
		try {
			server.add(SolrEntityConvert.entityList2SolrInputDocument(objectList));
			server.commit(false, false);
			logger.info("成功提交 " + objectList.size() + " 个元素到SOLR SYSTEM中");
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * 提交数据采用批量提交
	 * 注意：该Map元素必须不大于1024或者去更改solrconfig.xml中的参数，参数如下：
	 * &ltmaxBooleanClauses&gt1024&lt/maxBooleanClauses&gt
	 * 
	 * @param objMap key 为实体类的ID，value为即将要更新的object，该object的id键值不能为空
	 * @param idName
	 * @param server
	 */
	public void update(Map<Object, Object> objMap, String idName, SolrServer server) {
		if (objMap != null && objMap.size() > 0 && StringUtils.isNotBlank(idName)) {
			SolrQuery query = new SolrQuery();
			Set<Object> objSet = objMap.keySet();
			int i = 0;
			StringBuffer q = new StringBuffer();
			for (Object o : objSet) {
				if (i == 0) {
					q.append(idName + ":" + o.toString());
					i ++;
				} else {
					q.append(" OR " + idName + ":" + o.toString());
				}
			}
			query.setQuery(q.toString());
			query.setStart(0);
			query.setRows(objMap.size());
			QueryResponse qr = null;
			try {
				qr = server.query(query);
				SolrDocumentList sdl = qr.getResults();
				if (sdl.size() > 0) {
					UpdateRequest updateRequest = new UpdateRequest();
					updateRequest.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);
					updateRequest.add(SolrEntityConvert.solrDocumentList2SolrInputDocumentList(sdl, idName, objMap));
					updateRequest.process(server);
					
					logger.info("从SOLR SYSTEM中更新 " + objMap.size() + " 个元素");
				}
			} catch (SolrServerException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	/**
	 * 更新数据
	 * 
	 * @param obj
	 * @param idName
	 * @param server
	 */
	public void update(Object obj, String idName, SolrServer server) {
		if (obj != null && StringUtils.isNotBlank(idName)) {
			Class<?> cls = obj.getClass();
			try {
				Method method = cls.getMethod(SolrEntityConvert.getMethodName(idName, "get"));
				Object o = method.invoke(obj);
				
				if (o != null) {// 主键不能为空
					SolrQuery query = new SolrQuery();
					query.setQuery(idName + ":" + o.toString());
					query.setStart(0);
					query.setRows(1);
					QueryResponse qr = server.query(query);
					SolrDocument sd = qr.getResults().get(0);
					
					UpdateRequest updateRequest = new UpdateRequest();
					updateRequest.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);
					updateRequest.add(SolrEntityConvert.solrDocument2SolrInputDocument(sd, obj));
					updateRequest.process(server);
					
					logger.info("从SOLR SYSTEM中更新 1 个元素， id:" + o.toString());
				}
			} catch (SecurityException e) {
				logger.error(e);
			} catch (NoSuchMethodException e) {
				logger.error(e);
			} catch (IllegalArgumentException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			} catch (InvocationTargetException e) {
				logger.error(e);
			} catch (SolrServerException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	/**
	 * 删除给出的一个对象 
	 * @param obj
	 * @param idName
	 * @param server
	 */
	public void deleteByExample(Object obj, String idName, SolrServer server) {
		Class<?> cls = obj.getClass();
		try {
			Method method = cls.getMethod(SolrEntityConvert.getMethodName(idName, "get"));
			Object o = method.invoke(obj);
			if (o != null) {
				deleteById(method.invoke(obj), idName, server);
			}
		} catch (SecurityException e) {
			logger.error(e);
		} catch (NoSuchMethodException e) {
			logger.error(e);
		} catch (IllegalArgumentException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 删除数据
	 * @param id     Id值
	 * @param idName Id名称
	 * @param server
	 */
	public void deleteById(Object id, String idName, SolrServer server) {
		try {
			server.deleteById(id.toString());
			server.commit(false, false);
			logger.info("从SOLR SYSTEM中删除 1 个元素， id:" + id.toString());
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 批量删除数据
	 * 注意：该数组元素必须不大于1024或者去更改solrconfig.xml中的参数，参数如下：
	 * &lgmaxBooleanClauses&gt1024&lg/maxBooleanClauses&gt
	 * 
	 * @param idArrays Id数组
	 * @param idName   Id名称
	 * @param server
	 */
	public void deleteById(Object[] idArrays, String idName, SolrServer server) {
		if (idArrays.length > 0) {
			try {
				StringBuffer query = new StringBuffer(idName + ":" + idArrays[0]);
				for (int i = 1; i < idArrays.length; i++) {
					if (idArrays[i] != null) {
						query.append(" OR " + idName + ":" + idArrays[i].toString());
					}
				}
				server.deleteByQuery(query.toString());
				server.commit(false, false);
				logger.info("从SOLR SYSTEM中删除 " + idArrays.length + " 个元素");
			} catch (SolrServerException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	public void deleteByQuery(String key, String value, SolrServer server) {
		try {
			server.deleteByQuery(key+":"+value);
			server.commit(false, false);
			logger.info("从SOLR SYSTEM中删除"+key+":"+value);
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 删除所有元素
	 * 
	 * @param server
	 */
	public void deleteAll(SolrServer server) {
		try {
			server.deleteByQuery("*:*");
			server.commit(false, false);
			logger.info("从SOLR SYSTEM中删除所有元素");
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
    public static List<String> getAnalysis(String sentence, SolrServer server) {
        FieldAnalysisRequest request = new FieldAnalysisRequest(
                "/analysis/field");
        request.addFieldName("title");
        request.setFieldValue("");        
        request.setQuery(sentence);
       
        FieldAnalysisResponse response = null;
        try {
            response = request.process(server);
        } catch (Exception e) {
        	logger.error("获取查询语句的分词时遇到错误", e);
        }

        List<String> results = new ArrayList<String>();
        Iterator<AnalysisPhase> it = response.getFieldNameAnalysis("title")
                .getQueryPhases().iterator();
        while(it.hasNext()) {
          AnalysisPhase pharse = (AnalysisPhase)it.next();
          List<TokenInfo> list = pharse.getTokens();
          for (TokenInfo info : list) {
              results.add(info.getText());
          }
          break;
        }
        
        return results;
    }
	
    public static List<String> getAnalysisByType(String sentence, String fieldType, SolrServer server) {
        FieldAnalysisRequest request = new FieldAnalysisRequest(
                "/analysis/field");
        request.addFieldType(fieldType);  
        request.addFieldName("files");
        request.setFieldValue(sentence);
//        request.setQuery(sentence);
       
        FieldAnalysisResponse response = null;
        try {
            response = request.process(server);
        } catch (Exception e) {
        	logger.error("获取查询语句的分词时遇到错误", e);
        }

        List<String> results = new ArrayList<String>();
        Iterator it = response.getFieldTypeAnalysis(fieldType).getIndexPhases().iterator();
        while(it.hasNext()) {
          AnalysisPhase pharse = (AnalysisPhase)it.next();
          List<TokenInfo> list = pharse.getTokens();
          for (TokenInfo info : list) {
              results.add(info.getText());
          }

        }
        
        return results;
    }
    
	private static void parseHighlighting(String keyword,PageSupport pageSupport) {
		if(StringUtils.isBlank(keyword)) {
			return;
		}
		if( pageSupport==null || pageSupport.getTotalCount()==0) return;
		List<Resource> records = pageSupport.getItems();
		Resource doc = null;
		for(int i=0;i<records.size();i++){
			doc = (Resource)records.get(i);
			String title = doc.getTitle();
			if(StringUtils.isNotBlank(title)) {
				if(title.indexOf(keyword) >= 0) {
					title = title.replaceAll(keyword, "<em>" + keyword + "</em>");
					doc.setTitle(title);
				}
			}
			String authorname = doc.getAuthor();
			if(StringUtils.isNotBlank(authorname)) {				
				if(authorname.indexOf(keyword) >= 0) {
					authorname = authorname.replaceAll(keyword, "<em>" + keyword + "</em>");
					doc.setAuthor(authorname);
				}
			}			
			String description = doc.getDescription();
			if(StringUtils.isNotBlank(description)) {
				if(description.indexOf(keyword) >= 0) {
					description = SolrUtil.Html2Text(description);
					description = SolrUtil.getWeiBoStr(description, 160);
					logger.debug("description=====================" + description);					
					if(!description.endsWith(".")) {
						description = description + ".....";
					}
					int idx = description.indexOf(".....");
					description.length();
					description = description.replaceAll(keyword, "<em>" + keyword + "</em>");
					doc.setDescription(description);
				}	
			}
			
		}
		orderRecords(records);
		
		pageSupport.setItems(records);
	}
    
	/**
	 * 与SOLR SYSTEM PING, 主要是检测solr是否down掉
	 * 
	 * @param server
	 * @return String
	 */
	public String ping(SolrServer server) {
		try {
			logger.info("与SOLR SYSTEM PING成功");
			return server.ping().getResponse().toString();
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * 与SOLR SYSTEM PING, 主要是检测solr是否down掉
	 * 
	 * @param server
	 * @return String
	 */
	public boolean isCanUseSolrServer(SolrServer server) {
		boolean isCanUse = false;
		try {
			String status = (String)server.ping().getResponse().get("status");
			if("OK".equals(status)) isCanUse = true;
			logger.info("与SOLR SYSTEM PING成功！");
		} catch (SolrServerException e) {
			logger.info("与SOLR SYSTEM PING失败！");
		} catch (IOException e) {
			logger.info("与SOLR SYSTEM PING失败！");
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.info("与SOLR SYSTEM PING失败！");
		}
		return isCanUse;
	}
	
	/**
	 * 优化solr数据存储结构
	 * 
	 * @param server
	 */
	public void optimize(SolrServer server) {
		try {
			logger.info("正在优化 SOLR SYSTEM ... ...");
			long now = System.currentTimeMillis();
			server.optimize(true, false);
			server.optimize(false, false);
			logger.info("优化 SOLR SYSTEM 完毕， 花费时间：" + (System.currentTimeMillis() - now) / 1000 + "秒");
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public static void main(String[] args) {
		HttpSolrServer solrServer = new HttpSolrServer("http://10.130.18.23:8983/solr/core0");
		List<String> dic = SolrClient.getAnalysisByType("煤炭工业出版社大家觉得怎么样", "index", solrServer);
		System.out.println(dic.toString());
	}

	public static <T>PageSupport<T> query(String filterQueryStr, SearchResource doc, String queryStr,Class<T> cls, int start, int rows, SolrServer server) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		if(StringUtils.isNotBlank(filterQueryStr))
			query.addFilterQuery(filterQueryStr);
		query.setHighlight(true);
		query.setParam("hl.fl", "title,author,keywords,description,files");
		query.setParam("defType", "edismax");
		query.setParam("qf", "title^1000000000 author^100000000 keywords^10000000 description^1000000 files^0.001");
		query.setFacet(true);
		query.addFacetField("typeid");
		query.setParam("fl", "uuid,prodid,restype,orgid,speid," +
				"title,author,editor,published,description,keywords," +
				"stream,price,batchprice,actprice,onlinetime,typeid," +
				"sourceid,thumb,url,belongjournal,magazineyear," +
				"magazineyearnum,magazinenum");
		if(start > -1) {
			query.setParam("start", String.valueOf(start));
			query.setParam("rows", String.valueOf(rows));	
		}
		
		QueryResponse response = null;
		try {
			response = server.query(query,SolrRequest.METHOD.POST);
		} catch (SolrServerException e) {
			logger.error(e);
			e.printStackTrace();
			return null;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		
		SolrDocumentList sdl = response.getResults();
		Map<String,Map<String,List<String>>> highLightings = response.getHighlighting();
		List<FacetField> Facets = response.getFacetFields();
		int totalCount = Long.valueOf(response.getResults().getNumFound()).intValue();
		PageSupport<T> pageSupport = new PageSupport<T>(SolrEntityConvert.solrDocument2Entity(sdl, cls), totalCount, start, rows);
		SolrClient.parseSolrHighlighting(highLightings, pageSupport);

		//解析分类
		SolrClient.parseSolrFacets(Facets, pageSupport);
		return pageSupport;
	}
}
