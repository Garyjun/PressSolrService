/**
 * FileName: BaseObject.java
 */
package com.channelsoft.appframe.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <dl>
 * <dt>BaseObject</dt>
 * <dd>Description:基类对象</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-10-20</dd>
 * </dl>
 */
public class BaseObject
{
	protected Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 统计操作耗时
	 * 
	 * @param message
	 * @param startTime
	 */
	protected void logTime(String message, long startTime) {
		if (logger.isInfoEnabled()) {
			long timeCost = System.currentTimeMillis() - startTime;
			logger.info("操作耗时统计--" + message + (timeCost) + "毫秒");
		}
	}
}
