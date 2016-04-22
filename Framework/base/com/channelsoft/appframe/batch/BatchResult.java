package com.channelsoft.appframe.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.SystemUtils;


/**
 * @author zhengwei
 * @author 张乐雷 
 *   1. 批处理结果类。对批量导入、批量导出、批量删除提供了工具方法。
 *   2. 增加一些工具方法。通过它们可以方便的访问和操作批量的处理情况。
 */
public class BatchResult {
	/*
	 * 批量导入、批量删除、批量导出，都有自己唯一批次号
	 */
	private String batchId;
    /**
     * 批处理数据的总行数
     */
    private int totalRows;
    
    /**
     * 错误行列表，保存所有的错误行数据对象。
     */
    private List<BadRow> badRows = new ArrayList<BadRow>();
    
    /**
     * 批量导出数据
     */
    private String batchExportData;
    
    public BatchResult() {
        super();
    }
    
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

    public void addBadRow (BadRow br) {
    	if (br==null||!br.isExistErrMessage()) {
			return;
		}
    	getBadRows().add(br);
    }
    
    /**
     * @return Returns the badRows.
     */
    public List<BadRow> getBadRows() {
        return badRows;
    }
    
     /**
     * @return
     */
    public boolean isExistBadRows(){
    	return !getBadRows().isEmpty();
    }
    
    /**
     * @return
     */
    public boolean isNotExistBadRows(){
    	return getBadRows().isEmpty();
    }
    
    /**
     * @param badRows The badRows to set.
     */
    public void setBadRows(List<BadRow> badRows) {
        this.badRows = badRows;
    }
    /**
     * @return Returns the totalRows.
     */
    public int getTotalRows() {
        return totalRows;
    }
    /**
     * @param totalRows The totalRows to set.
     */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }
    /**
     * 返回成功操作的纪录行数
     * @return
     */
    public int getSuccessRows(){
    	return getTotalRows() - getBadRows().size();
    }
    
    /**
     * 返回失败的数据的行的个数
     * @return
     */
    public int getFailRows(){
    	if(getBadRows().isEmpty()){
    		return 0;
    	}
    	return getBadRows().size();
    }
    
    /**
     * 检查批量导入文件中的数据是否全部的导入成功
     * @return 
     */
    public boolean isBatchImportSuccess(){
    	return getFailRows() == 0;
    }
    
	/**
	 * 检查批量导入文件中的数据是否全部的导入失败
	 * @return
	 */
	public boolean isBatchImportFail(){
		return getFailRows() == getTotalRows();
	}
	
	/**
	 * 检查批量导入文件中的数据是否有部分数据成功导入到DB
	 * @return
	 */
	public boolean isBatchImportPartSuccess(){
		return  getSuccessRows() > 0 && getSuccessRows() < getTotalRows();
	}
	
	/**
     * 返回操作统计信息和错误行的数据信息
     * 错误行的格式：行号：行数据: 行错误描述信息
     *  这些信息可以被保存到文件中。
     * @param badRows
     * @return
     */
    public String getErrResult(){
    	StringBuffer result = new StringBuffer();
    	result.append("批量导入操作的批次号为:").append(getBatchId());
		result.append(" 批处理数据的总行数为:").append(getTotalRows());
		result.append(" 成功的记录行数为:").append(getSuccessRows());
    	if(!getBadRows().isEmpty()){
    		result.append(" 失败的记录数为:").append(getFailRows())
    			  .append(SystemUtils.LINE_SEPARATOR);
    		Iterator iterator = getBadRows().iterator();
        	for(int i = 0; iterator.hasNext(); i++ ){
        		BadRow badRow = (BadRow)iterator.next();
        		result.append(badRow.toString())
        		.append(SystemUtils.LINE_SEPARATOR);
        	}
    	}
    	return result.toString();
    }
    
    /**
	 * 返回批量操作的统计信息, 不包括相关错误行，及错误行的描述信息
	 * @return
	 */
	public String getStatisticResult() {
		StringBuffer result = new StringBuffer();
		result.append("批量导入操作的批次号为:").append(getBatchId());
		result.append(" 批处理数据的总行数为:").append(getTotalRows());
		result.append(" 成功操作的纪录行数为:").append(getSuccessRows());
		if (!getBadRows().isEmpty()) {
			result.append(" 不成功的记录数为:").append(getBadRows().size());
		} 
		return result.toString();
	}

	public String getBatchExportData() {
		return batchExportData;
	}

	public void setBatchExportData(String batchExportData) {
		this.batchExportData = batchExportData;
	}
}
