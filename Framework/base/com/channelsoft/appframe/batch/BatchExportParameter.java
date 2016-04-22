/**
 * 
 */
package com.channelsoft.appframe.batch;

import java.util.HashMap;
import java.util.Map;

import com.channelsoft.appframe.dao.query.QueryConditionList;

/**
 * 批量导出、批量删除操作的参数
 * 
 * @author liwei
 * @author zhanglelei  
 *    1. 增加批量导出、批量删除时，所需要的一些公共参数。例如：文件目录名称、文件名、存放公共参数的Map
 */
public class BatchExportParameter extends BaseBatchParameter {
	
	/*
	 * 批量导出操作的文件夹名，这个是完整的目录名称
	 */
	private String batchExportFullDirName = null; 
	
	/*
	 * 批量导出操作的文件名
	 */
	private String batchExportFileName = null;
	
	/*
	 * 保存批量导出时需要的公共参数,例如： batchId，PO，城市区号字冠配置中的AreaCode
	 */
	private Map<String, Object> commonPara=null;
	
	/*
	 * 查询条件
	 */
	private QueryConditionList queryCondition;
	
	/**
	 * 查询公共参数的值
	 * 
	 * @param key
	 * @return
	 */
	public Object getCommonPara(String key) {
		return getCommonPara().get(key);		
	}

	public void setCommonPara(String key, Object value) {
		getCommonPara().put(key, value);
	}
	private Map<String, Object> getCommonPara() {
		if (commonPara==null) {
			commonPara = new HashMap<String, Object>();	
		}
		return commonPara;
	} 


	public QueryConditionList getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(QueryConditionList queryCondition) {
		this.queryCondition = queryCondition;
	}

	public String getBatchExportFileName() {
		return batchExportFileName;
	}

	public void setBatchExportFileName(String batchExportFileName) {
		this.batchExportFileName = batchExportFileName;
	}

	public String getBatchExportFullDirName() {
		return batchExportFullDirName;
	}

	public void setBatchExportFullDirName(String batchExportFullDirName) {
		this.batchExportFullDirName = batchExportFullDirName;
	}
}
