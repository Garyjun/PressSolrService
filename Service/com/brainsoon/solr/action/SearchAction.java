package com.brainsoon.solr.action;

import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.brainsoon.common.utils.JSONConvertor;
import com.brainsoon.solr.po.SearchResource;
import com.brainsoon.solr.service.ISearchFacede;
import com.brainsoon.solr.util.PageSupport;
import com.channelsoft.appframe.utils.BeanFactoryUtil;
/**
 * @author 
 *
 */
public class SearchAction extends SolrBaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log logger = LogFactory.getLog(SearchAction.class);

	//文章高级搜索
	public void searchArticleAdvanced() throws Exception {
		collectParam();
		String typeid = params.get("typeid");
		String publisheds = params.get("publisheds");
		String publishede = params.get("publishede");
		String description = params.get("description");
		String title = params.get("title");
		String author = params.get("author");
		String keywords = params.get("keywords");
		String content = params.get("content");
		int page = 1;
		if(StringUtils.isNotBlank(params.get("page"))) {
			page = Integer.parseInt(params.get("page"));
		}
		int pageSize = 10;
		if(StringUtils.isNotBlank(params.get("size"))) {
			pageSize = Integer.parseInt(params.get("size"));
		}
		String resType = "3";
		String sorts = params.get("sorts");	
		String orders = params.get("orders");
		int start = -1;
		if(page > 0)
		  start = (page -1) * pageSize;
		
		SearchResource doc = new SearchResource();
		doc.setTitle(title);
		doc.setDescription(description);
		doc.setTypeid(typeid);
		doc.setPublishTimeStart(publisheds);
		doc.setPublishTimeEnd(publishede);
		doc.setAuthor(author);
		doc.setKeywords(keywords);
		doc.setStart(start);
		doc.setRows(pageSize);
		doc.setRestype(resType);
		doc.setSorts(sorts);
		doc.setOrders(orders);
		doc.setFiles(content);
		PageSupport pages = getSearchFacede().searchArticleAdvanced(doc);
		
		logger.debug(JSONConvertor.bean2Json(pages));
		this.outputResult(JSONConvertor.bean2Json(pages));
	}
	
	/**
	 * 
	* @Title: searchNews
	* @Description: 查询资讯索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void searchNews() throws Exception {
		collectParam();
		String page = params.get("page");
		String size = params.get("size");
		//1-期刊 2-资讯 3-文章 4-专题库 5-大事辑览
		String resType = "2";
		String keywords = params.get("keywords");
		String typeid = params.get("typeid");
		if(StringUtils.isNotBlank(keywords)) {
			keywords = URLDecoder.decode(keywords);
		}
		if(StringUtils.isNotBlank(typeid)) {
			typeid = URLDecoder.decode(typeid);
		}
		
		logger.debug("[searchNews] 查询资讯 搜索词语：" + keywords+"---------");
		SearchResource search = new SearchResource();
		search.setStart((Integer.parseInt(page) - 1)*Integer.parseInt(size));
		search.setRows(Integer.parseInt(size));
		search.setKeywords(keywords);
		search.setRestype(resType);
		search.setTypeid(typeid);
		PageSupport pages = getSearchFacede().searchNews(search);
		logger.debug(JSONConvertor.bean2Json(pages));
		this.outputResult(JSONConvertor.bean2Json(pages));
	}
	
	/**
	 * 
	* @Title: searchArticle
	* @Description: 查询文章索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void searchArticle() throws Exception {
		collectParam();
		String page = params.get("page");
		String size = params.get("size");
		//String genre = params.get("genre");
		//String speId = params.get("speid");
		//1-期刊 2-资讯 3-文章 4-专题库 5-大事辑览
		String resType = "3";
		String keywords = params.get("keywords");
		String typeid = params.get("typeid");
		/*String searchType = params.get("searchtype");
		if(StringUtils.isEmpty(searchType)) {
			searchType = "all";
		}*/
		if(StringUtils.isNotBlank(keywords)) {
			keywords = URLDecoder.decode(keywords);
		}
		if(StringUtils.isNotBlank(typeid)) {
			typeid = URLDecoder.decode(typeid);
		}
		
		logger.debug("[searchArticle] 查询文章 搜索词语：" + keywords+"---------");
		SearchResource search = new SearchResource();
		search.setStart((Integer.parseInt(page) - 1)*Integer.parseInt(size));
		search.setRows(Integer.parseInt(size));
		search.setKeywords(keywords);
		search.setRestype(resType);
		search.setTypeid(typeid);
		//search.setSearchType(searchType);
		//search.setGenre(genre);
		//search.setSpeId(speId);
		PageSupport pages = getSearchFacede().searchArticle(search);
		logger.debug(JSONConvertor.bean2Json(pages));
		this.outputResult(JSONConvertor.bean2Json(pages));
	}
	
	/**
	 * 
	* @Title: searchJournal
	* @Description: 期刊索引搜索
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void searchJournal() throws Exception {
		collectParam();
		
		String page = params.get("page");
		String size = params.get("size");
		//1-期刊 2-资讯 3-文章 4-专题库 5-大事辑览
		String resType = "1";
		String keywords = params.get("keywords");
		String typeid = params.get("typeid");
		/*String speId = params.get("speid");
		String orgId = params.get("orgid"); 
		String typeId = params.get("typeid"); 
		String searchType = params.get("searchtype");
		if(StringUtils.isEmpty(searchType)) {
			searchType = "all";
		}
		*/
		
		if(StringUtils.isNotBlank(keywords)) {
			keywords = URLDecoder.decode(keywords);
		}
		if(StringUtils.isNotBlank(typeid)) {
			typeid = URLDecoder.decode(typeid);
		}
		
		logger.debug("[searchJournal] 查询期刊 搜索词语：" + keywords+"---------");
		SearchResource search = new SearchResource();
		search.setStart((Integer.parseInt(page) - 1)*Integer.parseInt(size));
		search.setRows(Integer.parseInt(size));
		search.setKeywords(keywords);
		search.setRestype(resType);
		search.setTypeid(typeid);
/*		search.setSearchType(searchType);
		search.setSpeId(speId);
		search.setOrgId(orgId);
		*/
		PageSupport pages = getSearchFacede().searchJournal(search);
		logger.debug(JSONConvertor.bean2Json(pages));
		this.outputResult(JSONConvertor.bean2Json(pages));
	}
	
	/**
	 * 
	* @Title: searchJournal
	* @Description: 大事辑览索引搜索
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void searchEvent() throws Exception {
		collectParam();
		
		String page = params.get("page");
		String size = params.get("size");
		//1-期刊 2-资讯 3-文章 4-专题库 5-大事辑览
		String resType = "5";
		String keywords = params.get("keywords");
		String typeid = params.get("typeid");
		
		if(StringUtils.isNotBlank(keywords)) {
			keywords = URLDecoder.decode(keywords);
		}
		if(StringUtils.isNotBlank(typeid)) {
			typeid = URLDecoder.decode(typeid);
		}
		
		logger.debug("[searchEvent] 查询大事辑览 搜索词语：" + keywords+"---------");
		SearchResource search = new SearchResource();
		search.setStart((Integer.parseInt(page) - 1)*Integer.parseInt(size));
		search.setRows(Integer.parseInt(size));
		search.setKeywords(keywords);
		search.setRestype(resType);
		search.setTypeid(typeid);
		PageSupport pages = getSearchFacede().searchEvent(search);
		logger.debug(JSONConvertor.bean2Json(pages));
		this.outputResult(JSONConvertor.bean2Json(pages));
	}
		
	private ISearchFacede getSearchFacede() {
    	return (ISearchFacede)BeanFactoryUtil.getBean("searchFacede");
    }
}
