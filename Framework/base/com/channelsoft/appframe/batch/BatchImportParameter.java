/**
 * 
 */
package com.channelsoft.appframe.batch;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 批量操作的参数封装类，
 * 
 * @author liwei
 * 
 */
public class BatchImportParameter extends BaseBatchParameter{ 
	
	/*
	 * 批量导入操作的文件夹名，这个是完整的目录名称
	 */
	private String batchImportErrFileFullDirName = null; 
	
	/*
	 * 批量导入操作的文件名
	 */
	private String batchImportFileName = null;
	
	/*
	 * 批量导入操作的文件输入流
	 */
	private InputStream batchImportInputStream = null;

	/*
	 * 保存批量导入时需要的公共参数,例如： batchId，PO，城市区号字冠配置中的AreaCode
	 */
	private Map<String, Object> commonPara=null;
	
	public String getBatchImportFileName() {
		return batchImportFileName;
	}

	public void setBatchImportFileName(String batchImportFileName) {
		this.batchImportFileName = batchImportFileName;
	}

	public InputStream getBatchImportInputStream() {
		return batchImportInputStream;
	}

	public void setBatchImportInputStream(InputStream batchImportInputStream) {
		this.batchImportInputStream = batchImportInputStream;
	}

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

	/**
	 * 错误文件命名规则：上传源文件的添加一个“.err”后缀
	 * @return 错误文件名称
	 */
	public String getBatchImportErrFileName() {
		return getBatchImportFileName() + ".err";
	}
 
	public String getBatchImportErrFileFullDirName() {
		return batchImportErrFileFullDirName;
	}

	public void setBatchImportErrFileFullDirName(String batchImportErrFileFullDirName) {
		this.batchImportErrFileFullDirName = batchImportErrFileFullDirName;
	} 
}
