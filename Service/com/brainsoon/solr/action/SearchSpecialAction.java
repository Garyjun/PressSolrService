package com.brainsoon.solr.action;

import org.apache.commons.lang.StringUtils;

import com.brainsoon.common.utils.JSONConvertor;
import com.brainsoon.solr.po.SearchResource;
import com.brainsoon.solr.service.ISearchSpecialFacede;
import com.brainsoon.solr.util.PageSupport;
import com.channelsoft.appframe.utils.BeanFactoryUtil;

public class SearchSpecialAction extends SolrBaseAction {

	private static final long serialVersionUID = 9L;
	
	/**
	 * 
	* @Title: searchSpecialArticleAdvanced
	* @Description: 主题库高级搜索（进查询文章）
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void searchSpecialArticleAdvanced() throws Exception {
		collectParam();
		String keyword = params.get("keyword");
		String typeid = params.get("typeid");
		String speid = params.get("speid");
		String orgid = params.get("orgid");
		String spage = params.get("page");
		String size = params.get("size");
		String restype = params.get("restype");
		String sorts = params.get("sorts");	
		String orders = params.get("orders");
		
		if(StringUtils.isBlank(speid)) {
			speid = "-1";
		}
		
		if(StringUtils.isBlank(orgid)) {
			orgid = "-1";
		}
		
		int ipage = 1;
		if(StringUtils.isNotBlank(spage)) {
			ipage = Integer.parseInt(spage);
		}
		
		int pageSize = 10;
		if(StringUtils.isNotBlank(size)) {
			pageSize = Integer.parseInt(size);
		}
		
		int start = -1;
		if(ipage > 0)
		  start = (ipage -1) * pageSize;
		
		if (StringUtils.isBlank(restype)) {
			restype = "3";
		}
		
		SearchResource doc = new SearchResource();
		doc.setKeywords(keyword);
		doc.setSpeid(speid);
		doc.setOrgid(orgid);
		doc.setStart(start);
		doc.setRows(pageSize);
		doc.setRestype(restype);
		doc.setSorts(sorts);
		doc.setOrders(orders);
		doc.setTypeid(typeid);
		PageSupport pages = getSearchSpecialFacede().searchSpecialArticleAdvanced(doc);
		logger.debug(JSONConvertor.bean2Json(pages));
		this.outputResult(JSONConvertor.bean2Json(pages));
	}
	
	public void searchSpecialArticle() throws Exception {
		collectParam();
		String keywords = params.get("keywords");
		String typeid = params.get("typeid");
		String speid = params.get("speid");
		String orgid = params.get("orgid");
		String resType = "3";
		
		if(StringUtils.isBlank(speid)) {
			speid = "-1";
		}
		if(StringUtils.isBlank(orgid)) {
			orgid = "-1";
		}
		int page = 1;
		if(StringUtils.isNotBlank(params.get("page"))) {
			page = Integer.parseInt(params.get("page"));
		}
		int pageSize = 10;
		if(StringUtils.isNotBlank(params.get("size"))) {
			pageSize = Integer.parseInt(params.get("size"));
		}
		int start = -1;
		if(page > 0)
		  start = (page -1) * pageSize;
		
		SearchResource doc = new SearchResource();
		doc.setKeywords(keywords);
		doc.setSpeid(speid);
		doc.setOrgid(orgid);
		doc.setStart(start);
		doc.setRows(pageSize);
		doc.setRestype(resType);
		doc.setTypeid(typeid);
		PageSupport pages = getSearchSpecialFacede().searchSpecialArticle(doc);
		logger.debug(JSONConvertor.bean2Json(pages));
		this.outputResult(JSONConvertor.bean2Json(pages));
	}
	
	private ISearchSpecialFacede getSearchSpecialFacede() {
    	return (ISearchSpecialFacede)BeanFactoryUtil.getBean("searchSpecialFacede");
    }
	
}
