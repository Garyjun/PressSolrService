package com.channelsoft.appframe.dao.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;

/**
 * <dl>
 * <dt>QueryConditionList</dt>
 * <dd>Description:覆盖qframe jar包中的相同类，解决
 * 如select distinct时，getMaxRowCount()错误的问题</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Apr 3, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class QueryConditionList {
	protected transient final Log logger = LogFactory.getLog(getClass());
	// 逆序
	public static final int SORT_MODE_DESC = 0;

	// 正序
	public static final int SORT_MODE_ASC = 1;

	private int sortMode = SORT_MODE_DESC;

	private List<QueryConditionItem> conditionList = new ArrayList<QueryConditionItem>();
	private List<QuerySortItem> sortList = new ArrayList<QuerySortItem>();

	// 分页使用的参数，表示当前页的开始行号
	private int startIndex = 0;
	
	// 分页使用的参数，表示每页最大行数,缺省20行
	private int pageSize = 20;

	// 排序字段
	private String sortProperty = null;
	
	/**
	 * 允许用户直接拼写完整的HQL或者SQL语句，并且实现数据库分页功能
	 */
	private String queryScript=null;
	
	/**
	 * 临时的查询语句的参数，与tempQueryHql一起使用
	 */
	private List<Object> queryScriptParams = new ArrayList<Object>();
	
	/**
	 * 使用hql查询时，计算查询结果总数的hql语句
	 */
	private String queryResultCountHql=null;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void addCondition(QueryConditionItem condition) {
		conditionList.add(condition);
	}
	
	public void addSort(QuerySortItem sort){
		sortList.add(sort);
	}

	public void build(Criteria rootCriteria) throws HibernateException {
		Map<Object, Object> appendedCriteria = new HashMap<Object, Object>();
		for (Iterator iter = iterator(); iter.hasNext();) {
			QueryConditionItem item = (QueryConditionItem) iter.next();
			item.build(rootCriteria, appendedCriteria);
		}
	}

	/**
	 * 
	 */
	public QueryConditionList() {
		super();
	}

	public Iterator iterator() {
		return conditionList.iterator();
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	//add by Fuwenbin 2006-12-11 基于sql分页查询的开始结束索引.
	public int getStartIndexForSql() {
		return startIndex + 1;
	}
	
	public int getEndIndexForSql() {
		return startIndex + pageSize + 1;
	}
	//end

	public String getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}

	public int getSortMode() {
		return sortMode;
	}

	public String getSortModeValue() {
		if (SORT_MODE_ASC == getSortMode()) {
			return " asc ";
		}
		return " desc ";
	}

	public void setSortMode(int sortValue) {
		if (logger.isDebugEnabled()) {
			logger.debug("设置排序模式="+sortValue);	
		} 
		this.sortMode = sortValue;
	}

	public int getNextSortMode() {
		if (logger.isDebugEnabled()) {
			logger.debug("排序模式由"+getSortMode()+"变为"+(isSortByAsc()?SORT_MODE_DESC:SORT_MODE_ASC));
		}
		return isSortByAsc()?SORT_MODE_DESC:SORT_MODE_ASC;
	}
	/**
	 * 判断是否按正序进行排序
	 * @return
	 */
	public boolean isSortByAsc(){
		return SORT_MODE_ASC == getSortMode();
	}
	
	public String toString(){
		StringBuffer content = new StringBuffer(200);
		content.append("当前页的开始行号=").append(getStartIndex());
		content.append(",每页最大行数=").append(getPageSize());
		content.append(",查询条件[");
		for (Iterator iter = iterator(); iter.hasNext();) {			 
			QueryConditionItem item = (QueryConditionItem) iter.next();
			content.append(item).append("|");			
		}
		content.append("]");
		return content.toString();
	}

	public String getQueryScript() {
		return queryScript;
	}

	public void setQueryScript(String queryScript) {
		this.queryScript = queryScript;
	}

	public String getQueryResultCountHql() {
		return queryResultCountHql;
	}

	public void setQueryResultCountHql(String queryResultCountHql) {
		this.queryResultCountHql = queryResultCountHql;
	}

	public List<QueryConditionItem> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<QueryConditionItem> conditionList) {
		this.conditionList = conditionList;
	}

	public List<QuerySortItem> getSortList() {
		return sortList;
	}

	public void setSortList(List<QuerySortItem> sortList) {
		this.sortList = sortList;
	}

	public List<Object> getQueryScriptParams() {
		return queryScriptParams;
	}

	public void addQueryScriptParams(Object param){
		queryScriptParams.add(param);
	}
}