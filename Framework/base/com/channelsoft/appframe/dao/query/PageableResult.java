package com.channelsoft.appframe.dao.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支持分页的结果集
 * 
 * @author liwei
 * 
 */
public class PageableResult {
	//统计结果
	private Map<String, Object> statistics = new HashMap<String, Object>();
	// 满足查询条件的数据行数
	private int maxRowCount = -1;

	// 满足查询条件的当前页的数据
	private List currentPage = new ArrayList();

	public List getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(List currentPage) {
		this.currentPage = currentPage;
	}

	public int getMaxRowCount() {
		return maxRowCount;
	}

	public void setMaxRowCount(int maxRowCount) {
		this.maxRowCount = maxRowCount;
	}

	/**
	 * 根据主键返回统计值
	 * 
	 * @param stasticsKey
	 * @return
	 */
	public Object getStatisticValue(String stasticsKey) {
		return statistics.get(stasticsKey);
	}

	public void setStatisticValue(String statisticKey,Object stasticValue) {
		statistics.put(statisticKey, stasticValue);
	}
}
