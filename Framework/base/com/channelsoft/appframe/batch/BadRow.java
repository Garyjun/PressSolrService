package com.channelsoft.appframe.batch;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhengwei
 *     张乐雷 20060715
 *     1， 增加isExistErrMessage判断函数
 * 记录错误行的信息
 */
public class BadRow {
    
    /**
     * 错误行所在位置（从0开始）
     */
    private int rowNum;
    
    /**
     * 错误的原因（空行，字段不全，SQLException，不合乎业务需求）
     */
    private String errMessage;
    
    /**
     * 行数据
     */
    private String row;

    /**
     * 
     */
    public BadRow() {
        super();
    }
    
    public boolean isExistErrMessage(){
    	return StringUtils.isNotEmpty(errMessage);
    }

    /**
     * @return Returns the errMessage.
     */
    public String getErrMessage() {
        return errMessage;
    }
    /**
     * @param errMessage The errMessage to set.
     */
    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
    /**
     * @return Returns the row.
     */
    public String getRow() {
        return row;
    }
    /**
     * @param row The row to set.
     */
    public void setRow(String row) {
        this.row = row;
    }
    /**
     * @return Returns the rowNum.
     */
    public int getRowNum() {
        return rowNum;
    }
    /**
     * @param rowNum The rowNum to set.
     */
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
    public String toString() {
    	return getRowNum()+":"+getRow()+":"+getErrMessage();
    }
}
