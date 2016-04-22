package com.channelsoft.appframe.dao.query;

/**
 * 
 * <dl>
 * <dt>QuerySortItem</dt>
 * <dd>Description:排序条件</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-12-30</dd>
 * </dl>
 * 
 * @author 王志明
 */
public class QuerySortItem {

	private String fieldName;
	
	private String sortMode;
	
	public QuerySortItem(String fieldName, Integer sortMode) {
		super();
		setFieldName(fieldName);
		setSortMode(sortMode);
	}

	public String getSortMode() {
		return sortMode;
	}

	public void setSortMode(Integer sortMode) {
		if(sortMode.equals(QueryConditionList.SORT_MODE_DESC)){
			this.sortMode = "desc";
		}else{
			this.sortMode = "asc";
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
