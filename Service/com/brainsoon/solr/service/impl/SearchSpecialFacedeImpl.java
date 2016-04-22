package com.brainsoon.solr.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;

import com.brainsoon.solr.po.Resource;
import com.brainsoon.solr.po.SearchResource;
import com.brainsoon.solr.service.ISearchSpecialFacede;
import com.brainsoon.solr.util.DateUtil;
import com.brainsoon.solr.util.PageSupport;
import com.brainsoon.solr.util.SearchSpecial;
import com.brainsoon.solr.util.SolrClient;
import com.brainsoon.solrservice.res.service.ISpecialService;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.utils.BeanFactoryUtil;

public class SearchSpecialFacedeImpl implements ISearchSpecialFacede {

	private static final Logger logger = Logger.getLogger(SearchSpecialFacedeImpl.class);
	
	private SolrServer solrServerSpecial;
	private SolrClient solrClient;
	
	public PageSupport searchSpecial(SearchSpecial doc) throws ServiceException{
		String queryStr = buildSpecialCondition(doc);
		String filterQueryStr = buildSpecialFilterCondition(doc);
		String searchType = doc.getSearchType();
		String sorts = doc.getSorts();
		String orders = doc.getOrders();
		if(StringUtils.isBlank(sorts)) {
			sorts = "onlinetime";
			orders = "desc";
		}
		logger.info("查询条件：" + queryStr );		
		return solrClient.querySpecial(filterQueryStr, queryStr, Resource.class, doc.getStart(), doc.getRows(), sorts, orders, searchType, this.solrServerSpecial);
	}
	
	public PageSupport searchSpecialAdvanced(SearchSpecial doc) throws ServiceException{		
		String queryStr = buildSPACondition(doc);
		String filterQueryStr = buildSPAFilterCondition(doc);
		String searchType = doc.getSearchType();
		String sorts = doc.getSorts();
		String orders = doc.getOrders();
		if(StringUtils.isBlank(sorts)) {
			sorts = "onlinetime";
			orders = "desc";
		}
		logger.info("查询条件：" + queryStr );
		return solrClient.querySpecialAdvanced(queryStr, Resource.class, doc.getStart(), doc.getRows(), sorts, orders, searchType, this.solrServerSpecial, filterQueryStr);
	}
	
	public PageSupport searchSpecialBook(SearchSpecial doc) throws ServiceException{
		String queryStr = buildSpecialBookCondition(doc);
		String searchType = doc.getSearchType();
		String sorts = doc.getSorts();
		String orders = doc.getOrders();
		logger.info("查询条件：" + queryStr );		
		return solrClient.querySpecialBook(doc.getKeywords(), queryStr, Resource.class, doc.getStart(), doc.getRows(), this.solrServerSpecial);
	}
	
	public PageSupport searchSpecialBookContent(SearchSpecial doc) throws ServiceException{
		String queryStr = buildSPBCQueryCondition(doc);
		String filterQueryStr = buildSPBCFilterQueryCondition(doc);
		String searchType = doc.getSearchType();
		String sorts = doc.getSorts();
		String orders = doc.getOrders();
		if(StringUtils.isBlank(sorts)) {
			sorts = "pubdate";
			orders = "desc";
		}
		logger.info("查询条件：" + queryStr );		
		return solrClient.querySpecialBookContent(filterQueryStr, queryStr, Resource.class, doc.getStart(), doc.getRows(), sorts, orders, searchType, this.solrServerSpecial);
	}
	
	private String buildSpecialCondition(SearchSpecial doc) {
		String queryStr = "";
    	String keywords = doc.getKeywords();
    	if(StringUtils.isNotBlank(keywords)){    		
    		queryStr += keywords; 
    	}
    	if(StringUtils.isBlank(queryStr)) {
			queryStr = "*:*";
		}
		logger.debug("queryStr===================" + queryStr);
    	return queryStr;
	}
	
	private String buildSpecialFilterCondition(SearchSpecial doc) {
		String queryStr = "";
    	String typeid = doc.getTypeid();
    	if(StringUtils.isNotBlank(typeid)){
    		queryStr += "typeid:" + typeid + " AND ";
    	}
		String resType = doc.getResType();
    	if(StringUtils.isNotBlank(resType)){
    		queryStr += "restype:" + resType + " AND ";
    	}
    	String orgid = doc.getOrgid();
    	if(StringUtils.isNotBlank(orgid)){    		
    		queryStr += "orgid:" + orgid;
    	}
		int lastIndex = StringUtils.lastIndexOf(queryStr, " AND ");
		if(lastIndex == queryStr.length() - 5) {
			queryStr = queryStr.substring(0, lastIndex);
		}
		logger.debug("filterQueryStr===================" + queryStr);
    	return queryStr;
	}
	
	private String buildSPACondition(SearchSpecial doc) {    	
    	String queryStr = "";
    	
		if( doc==null ) return null;
    	
    	String title = doc.getTitle();
    	if(StringUtils.isNotBlank(title)){
    		queryStr += "title:" + title + " AND ";
    	}
    	
    	String description = doc.getDescription();
    	if(StringUtils.isNotBlank(description)){
    		queryStr += "description:" + description + " AND ";
    	}

    	String tags = doc.getKeywords();
    	if(StringUtils.isNotBlank(tags)){
    		queryStr += "tags:" + tags + " AND ";
    	}

		int lastIndex = StringUtils.lastIndexOf(queryStr, " AND ");
		if(lastIndex == queryStr.length() - 5) {
			queryStr = queryStr.substring(0, lastIndex);
		}

		if(StringUtils.isBlank(queryStr)) {
			queryStr = "*:*";
		}
		
		return queryStr;
	}
	
	private String buildSPAFilterCondition(SearchSpecial doc) {    	
    	String queryStr = "";
    	
		if( doc==null ) return null;
		String typeid = doc.getTypeid();
    	if(StringUtils.isNotBlank(typeid)){
    		queryStr += "typeid:" + typeid + " AND ";
    	}

    	String onlineTimeStart = doc.getOnlineTimeStart();
    	String onlineTimeEnd = doc.getOnlineTimeEnd();
		if(StringUtils.isNotBlank(onlineTimeStart) || StringUtils.isNotBlank(onlineTimeEnd)) {
			if(StringUtils.isNotBlank(onlineTimeStart)) {
				long timeStart;
				try {
					timeStart = DateUtil.parseTime(onlineTimeStart, "yyyy-MM-dd").getTime();
					queryStr += "onlinetime:[" + timeStart + " TO ";
				} catch (ParseException e) {
					queryStr += "onlinetime:[* TO "; 
				}
				
			} else {
				queryStr += "onlinetime:[* TO "; 
			}
			if(StringUtils.isNotBlank(onlineTimeEnd)) {
				long timeEnd;
				try {
					timeEnd = DateUtil.parseTime(onlineTimeEnd, "yyyy-MM-dd").getTime();
					queryStr += timeEnd + "] AND ";
				} catch (ParseException e) {
					queryStr += "*] AND "; 
				}				
			} else {
				queryStr += "*] AND "; 
			}
		}
		String era = doc.getEra();
    	if(StringUtils.isNotBlank(era)){
    		queryStr += "era:" + era + " AND ";
    	}
    	String orgid = doc.getOrgid();
    	if(StringUtils.isNotBlank(orgid)){
    		queryStr += "orgid:" + orgid + " AND ";
    	}
		//资源类型
		String resType = doc.getResType();
    	if(StringUtils.isNotBlank(resType)){
    		queryStr += "restype:" + resType + " AND ";
    	}

		int lastIndex = StringUtils.lastIndexOf(queryStr, " AND ");
		if(lastIndex == queryStr.length() - 5) {
			queryStr = queryStr.substring(0, lastIndex);
		}

		return queryStr;
	}
	
	private String buildSpecialBookCondition(SearchSpecial doc) {
		String queryStr = "";
    	String keywords = doc.getKeywords();
    	logger.debug("keywords=====================" + keywords);
    	if(StringUtils.isNotBlank(keywords)){    		
    		List<String> ks = SolrClient.getAnalysis(keywords, solrServerSpecial);
    		String kquery = StringUtils.join(ks, " OR ");
			queryStr += "(";
			queryStr += kquery;
    		queryStr += ") AND ";
    	}
    	String typeid = doc.getTypeid();
    	if(StringUtils.isNotBlank(typeid)){
        	String typeidStr = "";
        	String[] typeids = typeid.split(",");
    		for(String tid : typeids) {
    			typeidStr += "typeid:" + tid + " OR ";			
    		}
    		if(StringUtils.isNotBlank(typeidStr)) {
   				queryStr +=  " (" + typeidStr.substring(0, typeidStr.length() - 4) + ")  AND ";
    		}
    	}
		String resType = doc.getResType();
    	if(StringUtils.isNotBlank(resType)){
    		queryStr += "restype:" + resType + " AND ";
    	}
    	String speid = doc.getSpeid();
    	if(StringUtils.isNotBlank(speid) && !speid.equals("-1")){
    		queryStr += "speid:" + speid + " AND ";
    	}
    	String orgid = doc.getOrgid();
    	if(StringUtils.isNotBlank(orgid)){
    		queryStr += "orgid:" + orgid;
    	}
		int lastIndex = StringUtils.lastIndexOf(queryStr, " AND ");
		if(lastIndex == queryStr.length() - 5) {
			queryStr = queryStr.substring(0, lastIndex);
		}
    	if(StringUtils.isBlank(queryStr)) {
			queryStr = "*:*";
		}
		logger.debug("queryStr===================" + queryStr);
    	return queryStr;
	}
	
	private String buildSPBCQueryCondition(SearchSpecial doc) {
		String queryStr = "";
    	String keywords = doc.getKeywords();
    	if(StringUtils.isNotBlank(keywords)){    		
//    		List<String> ks = SolrClient.getAnalysis(keywords, solrServerSpecial);
//    		String kquery = StringUtils.join(ks, " OR ");
//			queryStr += kquery;
    		queryStr += keywords;    		
    	}

    	if(StringUtils.isBlank(queryStr)) {
			queryStr = "*:*";
		}    	
		logger.debug("queryStr===================" + queryStr);
    	return queryStr;
	}
	
	private String buildSPBCFilterQueryCondition(SearchSpecial doc) {
    	String filterQueryStr = "";
    	String typeid = doc.getTypeid();
    	if(StringUtils.isNotBlank(typeid)){
        	String typeidStr = "";
        	String[] typeids = typeid.split(",");
    		for(String tid : typeids) {
    			typeidStr += "typeid:" + tid + " OR ";			
    		}
    		if(StringUtils.isNotBlank(typeidStr)) {
    			filterQueryStr +=  " (" + typeidStr.substring(0, typeidStr.length() - 4) + ")  AND ";
    		}
    	}
		String resType = doc.getResType();
    	if(StringUtils.isNotBlank(resType)){
    		filterQueryStr += "restype:" + resType + " AND ";
    	}
    	String speid = doc.getSpeid();
    	if(StringUtils.isNotBlank(speid) && !speid.equals("-1")){
    		filterQueryStr += "speid:" + speid + " AND ";
    	}
    	String orgid = doc.getOrgid();
    	if(StringUtils.isNotBlank(orgid)){
    		filterQueryStr += "orgid:" + orgid;
    	}
		int lastIndex = StringUtils.lastIndexOf(filterQueryStr, " AND ");
		if(lastIndex == filterQueryStr.length() - 5) {
			filterQueryStr = filterQueryStr.substring(0, lastIndex);
		}
		
		return filterQueryStr;
	}
	
	
	
	
	
	public void deleteSpecialIndex(String spid, String orgid)  throws ServiceException{
		if(StringUtils.isBlank(orgid))
			orgid = "0";
		List sBookInfos = getSpecialService().getSpecialBooksIds(Long.parseLong(spid));			
		Iterator itr = sBookInfos.iterator();
		while (itr.hasNext()) {
			Map bookInfo = (Map) itr.next();
			String bookid = bookInfo.get("bookid").toString();	
			bookid = "1_" + bookid + "_" + orgid + "_" + spid;
			deleteIndex(bookid);
		}
		spid = "4_" + spid + "_" + orgid;
		deleteIndex(spid);
	}
	
	public void deleteSpecialBookIndex(String[] bookids, String spid, String orgid)  throws ServiceException{
		if(bookids == null || bookids.length == 0)
			return;
		for(String bookid : bookids) {
			String bookidFull="";
			Map bookinfo = getSpecialService().getAttributeByid(Long.valueOf(bookid));
			if(bookinfo!=null && bookinfo.get("attribute")!=null && !"".equals(bookinfo.get("attribute").toString()) ) {
				boolean attribute = (Boolean)bookinfo.get("attribute");
				if (attribute) {//期刊
					bookidFull = "1_" + bookid + "_" + orgid + "_" + spid;
				}else {
					bookidFull = "3_" + bookid + "_" + orgid + "_" + spid;
				}
			}
			deleteIndex(bookidFull);
		}
	}
	
	public PageSupport searchSpecialArticleAdvanced(SearchResource doc) throws ServiceException{		
		String queryStr = buildSPBACondition(doc);
		String filterQueryStr = buildSPBAFilterCondition(doc);
		String sorts = doc.getSorts();
		String orders = doc.getOrders();
		if(StringUtils.isBlank(sorts)) {
			sorts = "published";
			orders = "desc";
		}
		logger.info("专题库文章高级查询查询条件：" + queryStr );
		return solrClient.querySpecialBookAdvanced(queryStr, Resource.class, doc.getStart(), doc.getRows(), sorts, orders, this.solrServerSpecial, filterQueryStr);
	}
	
	private String buildSPBACondition(SearchResource doc) {    	
    	String queryStr = "";
    	
		if( doc==null ) return null;
    	
    	String title = doc.getTitle(); 
    	if(StringUtils.isNotBlank(title)){
    		queryStr += "title:" + title + " AND ";
    	}
    	
    	String content = doc.getFiles();
    	if(StringUtils.isNotBlank(content)){
    		queryStr += "files:" + content + " AND ";
    	}
    	
    	String description = doc.getDescription();
    	if(StringUtils.isNotBlank(description)){
    		queryStr += "description:" + description + " AND ";
    	}

    	String author = doc.getAuthor(); 
    	if(StringUtils.isNotBlank(author)){
    		queryStr += "author:" + author + " AND ";
    	}
    	String keywords = doc.getKeywords();
    	if(StringUtils.isNotBlank(keywords)){
    		queryStr += "keywords:" + keywords + " AND ";
    	}

		int lastIndex = StringUtils.lastIndexOf(queryStr, " AND ");
		if(lastIndex == queryStr.length() - 5) {
			queryStr = queryStr.substring(0, lastIndex);
		}

		if(StringUtils.isBlank(queryStr)) {
			queryStr = "*:*";
		}
		
		return queryStr;
	}
	
	private String buildSPBAFilterCondition(SearchResource doc) {    	
    	String queryStr = "";
    	
		if( doc==null ) return null;
		String typeid = doc.getTypeid();
    	if(StringUtils.isNotBlank(typeid)){
        	String typeidStr = "";
        	String[] typeids = typeid.split(",");
    		for(String tid : typeids) {
    			typeidStr += "typeid:" + tid + " OR ";			
    		}
    		if(StringUtils.isNotBlank(typeidStr)) {
   				queryStr +=  " (" + typeidStr.substring(0, typeidStr.length() - 4) + ") AND ";
    		}        	
    	}
    	
    	String publishTimeStart = doc.getPublishTimeStart();
    	String publishTimeEnd = doc.getPublishTimeEnd();
		if(StringUtils.isNotBlank(publishTimeStart) || StringUtils.isNotBlank(publishTimeEnd)) {
			if(StringUtils.isNotBlank(publishTimeStart)) {
				long timeStart;
				try {
					timeStart = DateUtil.parseTime(publishTimeStart, "yyyy-MM-dd").getTime();
					queryStr += "onlinetime:[" + timeStart + " TO ";
				} catch (ParseException e) {
					queryStr += "onlinetime:[* TO "; 
				}
				
			} else {
				queryStr += "onlinetime:[* TO "; 
			}
			if(StringUtils.isNotBlank(publishTimeEnd)) {
				long timeEnd;
				try {
					timeEnd = DateUtil.parseTime(publishTimeEnd, "yyyy-MM-dd").getTime();
					queryStr += timeEnd + "] AND ";
				} catch (ParseException e) {
					queryStr += "*] AND "; 
				}				
			} else {
				queryStr += "*] AND "; 
			}
		}
		
    	String speid = doc.getSpeid();
    	if(StringUtils.isNotBlank(speid) && !speid.equals("-1")){
    		queryStr += "speid:" + speid + " AND ";
    	}
    	String orgid = doc.getOrgid();
    	if(StringUtils.isNotBlank(orgid) && !orgid.equals("-1")){
    		queryStr += "orgid:" + orgid + " AND ";
    	}
		//资源类型
		String resType = doc.getRestype();
    	if(StringUtils.isNotBlank(resType)){
    		queryStr += "restype:" + resType + " AND ";
    	}

		int lastIndex = StringUtils.lastIndexOf(queryStr, " AND ");
		if(lastIndex == queryStr.length() - 5) {
			queryStr = queryStr.substring(0, lastIndex);
		}

		return queryStr;
	}
	

	public PageSupport searchSpecialArticle(SearchResource doc) throws ServiceException{		
		String queryStr = convertSearchResourceToSolrQuery(doc);
		String filterQueryStr = convertSearchResourceToSolrFilterQuery(doc);
		logger.info("专题库文章全文查询条件：" + queryStr );
		if( queryStr==null ) {
			return new PageSupport();
		}
		return solrClient.querySpecialBook(queryStr, filterQueryStr, Resource.class, doc.getStart(), doc.getRows(), this.solrServerSpecial);
	}
	
	private String convertSearchResourceToSolrQuery(SearchResource doc) {
		if( doc==null ) return null;
		List<String> props = new ArrayList<String>();
		
		String keyWords = doc.getKeywords();
		logger.info("专题库文章全文查询条件keyWords：" + keyWords );
		if(StringUtils.isNotBlank(keyWords)) {
			props.add(doc.getKeywords());
		}
		
		StringBuffer query = new StringBuffer();		
		for( int i=0;i<props.size();i++ ){
			query.append(props.get(i));
			if( i!=props.size()-1 ) query.append(" OR ");
		}		
		
		logger.info("专题库文章全文查询条件query：" + query.toString() );
		return query.toString();
	}
	
	private String convertSearchResourceToSolrFilterQuery(SearchResource doc) {
		if( doc==null ) return null;
		
		StringBuffer query = new StringBuffer();
		
		//资源类型
		String resType = doc.getRestype();
		//分类标识
		String typeid = doc.getTypeid();
		//专题库id
		String speid = doc.getSpeid();
		//架构id
		String orgid = doc.getOrgid();
		
		if(StringUtils.isNotEmpty(resType)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" restype:" + resType);
			} else {
				query.append(" AND restype:" + resType);
			}
		}
		
		if(StringUtils.isNotEmpty(typeid)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" typeid:" + typeid);
			} else {
				query.append(" AND typeid:" + typeid);
			}
		}
		
		String publishTimeStart = doc.getPublishTimeStart();
    	String publishTimeEnd = doc.getPublishTimeEnd();
		if(StringUtils.isNotBlank(publishTimeStart) || StringUtils.isNotBlank(publishTimeEnd)) {
			if(StringUtils.isNotBlank(publishTimeStart)) {
				long timeStart;
				try {
					timeStart = DateUtil.parseTime(publishTimeStart, "yyyy-MM-dd").getTime();
					query.append(" AND onlinetime:[" + timeStart + " TO ");
				} catch (ParseException e) {
					query.append(" AND onlinetime:[* TO "); 
				}
				
			} else {
				query.append("onlinetime:[* TO "); 
			}
			if(StringUtils.isNotBlank(publishTimeEnd)) {
				long timeEnd;
				try {
					timeEnd = DateUtil.parseTime(publishTimeEnd, "yyyy-MM-dd").getTime();
					query.append(timeEnd + "] ");
				} catch (ParseException e) {
					query.append("*] "); 
				}				
			} else {
				query.append( "*] "); 
			}
		}
		
    	if(StringUtils.isNotBlank(speid) && !speid.equals("-1")){
    		if(StringUtils.isEmpty(query.toString())) {
				query.append(" speid:" + speid);
			} else {
				query.append(" AND speid:" + speid);
			}
    	}
    	
    	if(StringUtils.isNotBlank(orgid) && !orgid.equals("-1")){
    		if(StringUtils.isEmpty(query.toString())) {
				query.append(" orgid:" + orgid);
			} else {
				query.append(" AND orgid:" + orgid);
			}
    	}
		return query.toString();
	}
	public void deleteIndex(String uuid)  throws ServiceException{
		this.solrClient.deleteById(uuid,"uuid", this.solrServerSpecial);
	}
	
	public void setSolrServerSpecial(SolrServer solrServerSpecial) {
		this.solrServerSpecial = solrServerSpecial;
	}

	public SolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(SolrClient solrClient) {
		this.solrClient = solrClient;
	}
	
	private ISpecialService getSpecialService() {
		return (ISpecialService) BeanFactoryUtil.getBean("specialService");
	}
}
