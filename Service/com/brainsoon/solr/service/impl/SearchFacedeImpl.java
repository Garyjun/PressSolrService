package com.brainsoon.solr.service.impl;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;

import com.brainsoon.solr.po.Resource;
import com.brainsoon.solr.po.SearchResource;
import com.brainsoon.solr.service.ICreateIndexFacede;
import com.brainsoon.solr.service.ISearchFacede;
import com.brainsoon.solr.service.ISolrQueueFacede;
import com.brainsoon.solr.util.DateUtil;
import com.brainsoon.solr.util.PageSupport;
import com.brainsoon.solr.util.SearchArticle;
import com.brainsoon.solr.util.SearchBookArticle;
import com.brainsoon.solr.util.SearchDocument;
import com.brainsoon.solr.util.SearchNews;
import com.brainsoon.solr.util.SolrClient;
import com.brainsoon.solrservice.res.service.IBookService;
import com.brainsoon.solrservice.res.service.ISpecialService;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.utils.BeanFactoryUtil;

/**
 * Description: <p></p>
 * Content Desc:<p><p>
 */
public class SearchFacedeImpl implements ISearchFacede {
	
	private static final Logger logger = Logger.getLogger(SearchFacedeImpl.class);

	private SolrServer solrServer;
	private SolrServer solrServerSpecial;
	private SolrClient solrClient;
	
	public PageSupport searchDocument(SearchDocument doc) throws ServiceException{
		String query = this.convertTaxDocumentToSolrQuery(doc);
		String filterQuery = convertTaxDocumentToSolrFilterQuery(doc);
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.query(filterQuery, doc, query, Resource.class, doc.getStart(), doc.getRows(), this.solrServer);
	}
	
	public PageSupport searchOneDocument(String uuid, SearchDocument doc) throws ServiceException{
		String query = "uuid:"+uuid;
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.query(null, doc, query, Resource.class, doc.getStart(), doc.getRows(), this.solrServer);
	}
	
	public PageSupport searchBookArticle(SearchBookArticle doc) throws ServiceException{
		String query = this.convertTaxDocumentToSolrQuery(doc);
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.query(doc, query, Resource.class, doc.getStart(), doc.getRows(), this.solrServer);
	}
	
	public PageSupport searchOneBookArticle(String uuid, SearchBookArticle doc) throws ServiceException{
		String query = "uuid:"+uuid;
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.query(doc, query, Resource.class, doc.getStart(), doc.getRows(), this.solrServer);
	}
	
	public PageSupport searchArticle(SearchArticle doc) throws ServiceException{
		String query = this.convertTaxDocumentToSolrQuery(doc);
		String filterQuery = convertTaxDocumentToSolrFilterQuery(doc);
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.query(filterQuery, doc, query, Resource.class, doc.getStart(), doc.getRows(), this.solrServer);
	}
	
	public PageSupport searchArticleAdvanced(SearchResource doc) throws ServiceException{
		String queryStr = buildBookAdvancedCondition(doc);
		String filterQueryStr = buildBookAdvancedFilterCondition(doc);

		String searchType = doc.getSearchType();
		String sorts = doc.getSorts();
		String orders = doc.getOrders();
		if(StringUtils.isBlank(sorts)) {
			sorts = "pubdate";
			orders = "desc";
		}
		logger.info("查询条件：" + queryStr );
		return solrClient.queryAdvanced(queryStr, Resource.class, doc.getStart(), doc.getRows(), sorts, orders, searchType, this.solrServer, filterQueryStr);
	}
	
	public PageSupport searchBookArticleAdvancedDocument(SearchBookArticle doc) throws ServiceException{
		String query = doc.getQueryStat();
		//资源类型
		String resType = doc.getResType();
		if(StringUtils.isNotEmpty(resType)) {
			if(StringUtils.isEmpty(query)) {
				query = " restype:" + resType;
			} else {
				query = "(" + query + ") AND restype:" + resType;
			}
		}
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.queryAdvanced(doc, Resource.class, doc.getStart(), doc.getRows(), this.solrServer);
	}
	
	public String searchAutoComplete(String keywords) throws ServiceException{
		
		String params =  new String();
		if(StringUtils.isEmpty(keywords)) {
			return "[]";
		}
		
		// query(SolrParams params, Class<T> cls, int start, int rows, SolrServer server) 
		return solrClient.query2(keywords, 1, 100, this.solrServer);
	}
	
	public void addIndex(Resource resource,File file) throws ServiceException {
		try{
			solrClient.commitRichDocument(resource, file, this.solrServer);
		}catch(Exception ex){
			throw new ServiceException(ex);
		}
	}
	
	public void addIndex(Resource resource) throws ServiceException {
		try{
			solrClient.commitDocument(resource, this.solrServer);
		}catch(Exception ex){
			throw new ServiceException(ex);
		}
	}

	public void addSpecialBookIndex(Resource resource,File file) throws ServiceException {
		try{
			solrClient.commitRichDocument(resource, file, this.solrServerSpecial);
		}catch(Exception ex){
			throw new ServiceException(ex);
		}
		
	}
	
	public void addSpecialBookIndex(Resource resource) throws ServiceException {
		try{
			solrClient.commitDocument(resource, this.solrServerSpecial);
		}catch(Exception ex){
			throw new ServiceException(ex);
		}
	}
	
	public void addSpecialIndex(Resource resource) throws ServiceException {
		try{
			solrClient.commitDocument(resource, this.solrServerSpecial);
		}catch(Exception ex){
			throw new ServiceException(ex);
		}
	}
		
	public void deleteIndex(Resource resource) throws ServiceException {
		this.solrClient.deleteById(resource.getUuid(),"uuid", this.solrServer);
	}
	
	public void deleteIndex(String uuid)  throws ServiceException{
		this.solrClient.deleteById(uuid,"uuid", this.solrServer);
	}

	public void deleteIndex(String[] uuids)  throws ServiceException{
		this.solrClient.deleteById(uuids,"uuid", this.solrServer);
	}
	
	public void deleteJournalIndex(String[] uuids)  throws ServiceException{		
		for(String uuid : uuids) {
			uuid = "1_" + uuid;
			deleteIndex(uuid);
		}
	}
	
	public void deleteArticleIndex(String[] uuids)  throws ServiceException{		
		for(String uuid : uuids) {
			uuid = "3_" + uuid;
			deleteIndex(uuid);
		}
	}
	
	public void deleteNewsIndex(String[] uuids)  throws ServiceException{		
		for(String uuid : uuids) {
			uuid = "2_" + uuid;
			deleteIndex(uuid);
		}
	}
	
	public void deleteEventIndex(String[] uuids)  throws ServiceException{		
		for(String uuid : uuids) {
			uuid = "5_" + uuid;
			deleteIndex(uuid);
		}
	}
	
	public void deleteByQuery(String key, String value)  throws ServiceException{
		this.solrClient.deleteByQuery(key,value, this.solrServer);
	}
	public void deleteSpecialByQuery(String key, String value)  throws ServiceException{
		this.solrClient.deleteByQuery(key,value, this.solrServerSpecial);
	}
	
	public void deleteIndxes(List resources) throws ServiceException {
		if( resources!=null && resources.size()>0){
			String[] uuids = new String[resources.size()];
			for(int i=0;i<resources.size();i++){
				uuids[i] = ((Resource)resources.get(i)).getUuid().toString();
			}
			this.deleteIndex(uuids);
		}
	}

	public void updateIndex(Resource resource) throws ServiceException {
		//just use addIndex method for rich document operation
	}


	public void optimizeSearchEngin() throws ServiceException {
		this.solrClient.optimize(solrServer);
	}

	public String pingSearchEngin() throws ServiceException {
		return this.solrClient.ping(solrServer);
	}
	
	public boolean isCanUseSolrServer() throws ServiceException {
		return this.solrClient.isCanUseSolrServer(solrServer);
	}
	
	public void cleanSolrIndex() throws ServiceException {
		this.solrClient.deleteAll(this.solrServer);
	}

	/**
	 * 把文档对象转换为Solr的查询参数
	 * @param doc
	 * @return
	 */
	private String convertTaxDocumentToSolrQuery(SearchDocument doc){
		
		if( doc==null ) return null;
		
		List<String> props = new ArrayList<String>();
		 //搜索类型
		String searchType = doc.getSearchType();
		
		String keyWords = doc.getKeywords();
		
		if(StringUtils.isNotBlank(keyWords)) {
			if (StringUtils.equals("all", searchType)) {
				props.add(doc.getKeywords());
			} else if (StringUtils.equals("authorname", searchType)) {
				props.add("authorname:" + doc.getKeywords());				
			} else if (StringUtils.equals("title", searchType)) {				
				props.add("title:" + doc.getKeywords());								
			} else if (StringUtils.equals("description", searchType)) {				
				props.add("description:" + doc.getKeywords());								
			} else {
				logger.error("搜索类型出错type:" + searchType);
			}
		}
		
		StringBuffer query = new StringBuffer();		
		for( int i=0;i<props.size();i++ ){
			query.append(props.get(i));
			if( i!=props.size()-1 ) query.append(" OR ");
		}		
		
		logger.error("图书搜索条件:" + query.toString());
		return query.toString();
	}
	
	private String convertTaxDocumentToSolrFilterQuery(SearchDocument doc){
		
		if( doc==null ) return null;
		
		List<String> props = new ArrayList<String>();

		//资源类型
		String resType = doc.getResType();
		 //机构标识
		String orgId = doc.getOrgId();
		 //专题库
		String speId = doc.getSpeId();
		//图书类别
		String typeId = doc.getTypeId();
		
		StringBuffer query = new StringBuffer();
		
		if(StringUtils.isNotEmpty(resType)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" restype:" + resType);
			} else {
				query.append(" AND restype:" + resType);
			}
		}
		
		if(StringUtils.isNotEmpty(speId)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" speid:" + speId);
			} else {
				query.append(" AND speid:" + speId);
			}
		}
		
		if(StringUtils.isNotEmpty(orgId)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" orgid:" + orgId);
			} else {
				query.append(" AND orgid:" + orgId);
			}
		}
		
		if(StringUtils.isNotEmpty(typeId)) {
			if(typeId.equals("-1")) {
				if(StringUtils.isEmpty(query.toString())) {
					query.append(" -typeid:[* TO *]");
				} else {
					query.append(" AND -typeid:[* TO *]");
				}
			} else {		    	
		    	if(StringUtils.isNotBlank(typeId)){
		        	String typeidStr = "";
		        	String[] typeids = typeId.split(",");
		    		for(String tid : typeids) {
		    			typeidStr += "typeid:" + tid + " OR ";			
		    		}
		    		String queryStr = "";
		    		if(StringUtils.isNotBlank(typeidStr)) {
		   				queryStr +=  " AND (" + typeidStr.substring(0, typeidStr.length() - 4) + ")";
		    		}
		    		query.append(queryStr);
		    	}
			}
		}
		logger.error("图书搜索过滤接口:" + query.toString());
		return query.toString();
	}
	
	/**
	 * 把文档对象转换为Solr的查询参数
	 * @param doc
	 * @return
	 */
	private String convertTaxDocumentToSolrQuery(SearchBookArticle doc){
		
		if( doc==null ) return null;
		
		List<String> props = new ArrayList<String>();
		 //搜索类型
		String searchType = doc.getSearchType();
		//资源类型
		String resType = doc.getResType();
		 //专题库文章类别
		String genre = doc.getGenre();
		 //机构标识
		String orgId = doc.getOrgId();
		 //专题库
		String speId = doc.getSpeId();
		
		if(StringUtils.isNotEmpty(doc.getKeywords())) {
			if (StringUtils.equals("all", searchType)) {
				props.add("title:" + doc.getKeywords());
				props.add("authorname:" + doc.getKeywords());
				props.add("pressname:" + doc.getKeywords());
				props.add("description:" + doc.getKeywords());
				props.add("subject:" + doc.getKeywords());
				props.add("era:" + doc.getKeywords());
				props.add("text:" + doc.getKeywords());
			} else if (StringUtils.equals("files", searchType)) {
				props.add("files:" + doc.getKeywords());
			} else if (StringUtils.equals("authorname", searchType)) {
				props.add("authorname:" + doc.getKeywords());
			} else if (StringUtils.equals("title", searchType)) {
				props.add("title:" + doc.getKeywords());
			} else if (StringUtils.equals("pressname", searchType)) {
				props.add("pressname:" + doc.getKeywords());
			} else if (StringUtils.equals("description", searchType)) {
				props.add("description:" + doc.getKeywords());
			} else if (StringUtils.equals("subject", searchType)) {
				props.add("subject:" + doc.getKeywords());
			} else if (StringUtils.equals("era", searchType)) {
				props.add("era:" + doc.getKeywords());
			} else {
				logger.error("搜索类型出错type:" + searchType);
			}
		}
		
		StringBuffer query = new StringBuffer();
		query.append("(");
		for( int i=0;i<props.size();i++ ){
			query.append(props.get(i));
			if( i!=props.size()-1 ) query.append(" OR ");
		}
		query.append(")");
		
		if(StringUtils.isNotEmpty(resType)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" restype:" + resType);
			} else {
				query.append(" AND restype:" + resType);
			}
		}
		
		if(StringUtils.isNotEmpty(genre)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" genre:" + genre);
			} else {
				query.append(" AND genre:" + genre);
			}
		}
		
		if(StringUtils.isNotEmpty(speId)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" speid:" + speId);
			} else {
				query.append(" AND speid:" + speId);
			}
		}
		
		if(StringUtils.isNotEmpty(orgId)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" orgid:" + orgId);
			} else {
				query.append(" AND orgid:" + orgId);
			}
		}
		
		return query.toString();
	}
	
	private String convertTaxDocumentToSolrQuery(SearchArticle doc){
		
		if( doc==null ) return null;
		
		List<String> props = new ArrayList<String>();
		
		String keyWords = doc.getKeywords();
		
		if(StringUtils.isNotBlank(keyWords)) {
			props.add(doc.getKeywords());
		}
		
		StringBuffer query = new StringBuffer();		
		for( int i=0;i<props.size();i++ ){
			query.append(props.get(i));
			if( i!=props.size()-1 ) query.append(" OR ");
		}		
		
		logger.error("资讯搜索条件:" + query.toString());
		return query.toString();
	}
	
	private String convertTaxDocumentToSolrFilterQuery(SearchArticle doc){
		
		if( doc==null ) return null;
		
		//资源类型
		String resType = doc.getResType();
		
		StringBuffer query = new StringBuffer();
		
		if(StringUtils.isNotEmpty(resType)) {
			if(StringUtils.isEmpty(query.toString())) {
				query.append(" restype:" + resType);
			} else {
				query.append(" AND restype:" + resType);
			}
		}
		
		logger.error("图书搜索过滤接口:" + query.toString());
		return query.toString();
	}
	
	private String buildBookAdvancedCondition(SearchResource doc) {	
    	String queryStr = "";
    	
		if( doc==null ) return null;
    	
    	String title = doc.getTitle(); 
    	if(StringUtils.isNotBlank(title)){
    		queryStr += "title:" + title + " AND ";
    	}
    	
    	String files = doc.getFiles();
    	if(StringUtils.isNotBlank(files)){
    		queryStr += "files:" + files + " AND ";
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
	
	private String buildBookAdvancedFilterCondition(SearchResource doc) {    	
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
	
	/**
	 * 文章索引搜素
	 */
	public PageSupport searchArticle(SearchResource search) throws ServiceException{
		String query = this.convertSearchResourceToSolrQuery(search);//全文检索
		String filterQuery = convertSearchResourceToSolrFilterQuery(search);//普通字段检索
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.query(filterQuery, search, query, Resource.class, search.getStart(), search.getRows(), this.solrServer);
	}
	
	/**
	 * 资讯索引搜素
	 */
	public PageSupport searchNews(SearchResource doc) throws ServiceException{
		String query = this.convertSearchResourceToSolrQuery(doc);//全文检索
		String filterQuery = convertSearchResourceToSolrFilterQuery(doc);//普通字段检索
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.query(filterQuery, doc, query, Resource.class, doc.getStart(), doc.getRows(), this.solrServer);
	}
	
	/**
	 * 期刊索引搜素
	 */
	public PageSupport searchJournal(SearchResource doc) throws ServiceException{
		String query = this.convertSearchResourceToSolrQuery(doc);//全文检索
		String filterQuery = convertSearchResourceToSolrFilterQuery(doc);//普通字段检索
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.query(filterQuery, doc, query, Resource.class, doc.getStart(), doc.getRows(), this.solrServer);
	}
	
	/**
	 * 大事辑览索引搜素
	 */
	public PageSupport searchEvent(SearchResource doc) throws ServiceException{
		String query = this.convertSearchResourceToSolrQuery(doc);//全文检索
		String filterQuery = convertSearchResourceToSolrFilterQuery(doc);//普通字段检索
		logger.info("查询条件：" + query );
		if( query==null ) {
			return new PageSupport();
		}
		return solrClient.query(filterQuery, doc, query, Resource.class, doc.getStart(), doc.getRows(), this.solrServer);
	}
	
	private String convertSearchResourceToSolrFilterQuery(SearchResource doc) {
		if( doc==null ) return null;
		
		StringBuffer query = new StringBuffer();
		
		//资源类型
		String resType = doc.getRestype();
		//分类标识
		String typeid = doc.getTypeid();
		
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
		
		logger.error("图书搜索过滤接口:" + query.toString());
		return query.toString();
	}

	private String convertSearchResourceToSolrQuery(SearchResource doc) {
		if( doc==null ) return null;
		List<String> props = new ArrayList<String>();
		
		String keyWords = doc.getKeywords();
		if(StringUtils.isNotBlank(keyWords)) {
			props.add(doc.getKeywords());
		}
		
		StringBuffer query = new StringBuffer();		
		for( int i=0;i<props.size();i++ ){
			query.append(props.get(i));
			if( i!=props.size()-1 ) query.append(" OR ");
		}		
		
		logger.error("资讯搜索条件:" + query.toString());
		return query.toString();
	}

	public void setSolrServerSpecial(SolrServer solrServerSpecial) {
		this.solrServerSpecial = solrServerSpecial;
	}

	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	public SolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(SolrClient solrClient) {
		this.solrClient = solrClient;
	}
	
	public ISpecialService getSpecialService() {
		return (ISpecialService)BeanFactoryUtil.getBean("specialService");
	}
	
	public IBookService getBookService() {
		return (IBookService)BeanFactoryUtil.getBean("bookService");
	}
	
	private ISolrQueueFacede getSolrQueueFacede() {
    	return (ISolrQueueFacede)BeanFactoryUtil.getBean("solrQueueFacede");
    }
	
	public ICreateIndexFacede getCreateIndexService() {
		return (ICreateIndexFacede)BeanFactoryUtil.getBean("createIndexService");
	}
}
