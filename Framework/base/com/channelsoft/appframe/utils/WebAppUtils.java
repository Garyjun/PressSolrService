package com.channelsoft.appframe.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.web.util.WebUtils;

/**
 * <dl>
 * <dt>WebUtils</dt>
 * <dd>Description:web应用工具</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Nov 6, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public abstract class WebAppUtils {
	/**
	 * Web app root key parameter at the servlet context level
	 * (i.e. a context-param in <code>web.xml</code>): "webAppRootKey".
	 */
	private static final String WEB_APP_ROOT_KEY = "app.root";
	
	/**
	 * 获取web应用的根目录
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Nov 6, 2008 11:49:16 AM 
	 */
	public static String getWebAppRoot() {
		String webAppRoot = System.getProperty(WEB_APP_ROOT_KEY);
		System.out.println("获取根目录：==============" + webAppRoot);
		return StringUtils.isBlank(webAppRoot) ? System
				.getProperty(WebUtils.DEFAULT_WEB_APP_ROOT_KEY) : webAppRoot;
	}
	
	/**
	 * 获取web应用的WEB-INF目录
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Nov 6, 2008 12:33:21 PM 
	 */
	public static String getWebAppWebInfo() {
		String webAppRoot = getWebAppRoot();
		if (StringUtils.isBlank(webAppRoot)) {
			return null;
		}
		
		return webAppRoot + "WEB-INF" + SystemUtils.FILE_SEPARATOR;
	}
	
	/**
	 * 获取web应用的classes目录
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Nov 6, 2008 12:33:23 PM 
	 */
	public static String getWebAppClassesPath() {
		String webInfo = getWebAppWebInfo();
		if (StringUtils.isBlank(webInfo)) {
			return null;
		}
		
		return webInfo + "classes" + SystemUtils.FILE_SEPARATOR;
	}
	
	/**
	 * 获取web应用的临时目录
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Nov 6, 2008 1:16:30 PM 
	 */
	public static String getTempDir() {
		String webInfo = getWebAppWebInfo();
		if (StringUtils.isBlank(webInfo)) {
			return null;
		}
		
		return webInfo + "tmp" + SystemUtils.FILE_SEPARATOR;
	}
}
