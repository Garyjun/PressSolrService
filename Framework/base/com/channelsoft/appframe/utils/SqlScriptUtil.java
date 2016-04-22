package com.channelsoft.appframe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.appframe.dao.IBaseJdbcDao;
import com.channelsoft.appframe.exception.ServiceException;

/**
 * <dl>
 * <dt>SqlScriptUtil</dt>
 * <dd>Description:sql脚本工具类</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jul 30, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class SqlScriptUtil {
	/**脚本支持注释符号定义*/
	private final static String[] excludeScriptHead = {"/*","*","*/","--"};
	
	/**
	 * 逐行执行脚本文件中的脚本
	 * @param fileName
	 * @param jdbcDao
	 */
	public static void executeScriptFile(String fileName,IBaseJdbcDao jdbcDao){
		File scriptFile = new File(fileName);
		executeScriptFile(scriptFile,jdbcDao);
	}
	
	/**
	 * 逐行执行脚本文件中的脚本
	 * @param file
	 * @param jdbcDao
	 */
	public static void executeScriptFile(File file,IBaseJdbcDao jdbcDao){
		if(!file.exists() || !file.isFile()){
			throw new ServiceException("脚本文件不存！");
//			throw new FileNotFoundException();
		}	
		
		BufferedReader br = null;
        try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String script = br.readLine();
			while(script != null){
				if(isExcludeScript(script)){
					script = br.readLine();
					continue;
				}
				
				if(StringUtils.indexOf(script,";") >= 0){
					script = StringUtils.substring(script, 0,script.length() - 1);
				}
				jdbcDao.execute(script);
				
				script = br.readLine();
			}
		} catch (IOException e) {
			throw new ServiceException("读取脚本文件异常！",e);
		} finally {
			try {
				br.close();//close()中已经对br进行了判空
			} catch (IOException e) {
				throw new ServiceException("关闭脚本文件异常！",e);
			}
		}
	}
	
	private static boolean isExcludeScript(String script){
		if(StringUtils.isBlank(script)){
			return true;
		}
		
		return StringUtils.indexOfAny(script, excludeScriptHead) >= 0;
	}
}
