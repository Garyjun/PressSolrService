/**
 * FileName: UsbossConfigUtil.java
 */
package com.brainsoon.common.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.channelsoft.appframe.common.BaseObject;
import com.channelsoft.appframe.utils.WebAppUtils;

/**
 * <dl>
 * <dt>ScaConfigUtil</dt>
 * <dd>Description:系统参数配置工具类</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 30, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class ScaConfigUtil extends BaseObject {
	
	private static Logger logger = Logger.getLogger(ScaConfigUtil.class.getName());
	
	private static final String APP_VERSION = "APP_VERSION";
	public static final String IP_FILTER_TYPE = "IP_FILTER_TYPE";
	public static final String IP_LIST = "IP_LIST";

	public static final String DEFAULT_CATALOG = "DEFAULT_CATALOG";
	public static final String USE_MULTI_CATALOG = "USE_MULTI_CATALOG";
	public static final String DEFAULT_PASSWORD = "DEFAULT_PASSWORD";
	
	public static final String DEPLOY_PROCESS_DEFINITION = "DEPLOY_PROCESS_DEFINITION";
	public static final String PROCESS_DEFINITION_DEPLOY_MODE = "PROCESS_DEFINITION_DEPLOY_MODE";
	public static final String PROCESS_DEFINITION_LIST = "PROCESS_DEFINITION_LIST";
	
	private final static String CONFIG_FILE_NAME = WebAppUtils.getWebAppRoot() + "/webapp-config.xml";

	private Document configDocument;
	private static ScaConfigUtil instance = new ScaConfigUtil();

	public static Boolean getBoolean(String name) {
		String value = getParameter(name);
		return Boolean.valueOf(value);
	}

	public static Integer getInteger(String name, Integer defValue) {
		String value = getParameter(name);
		if (StringUtils.isEmpty(value)) {
			return defValue;
		}
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException exp) {
			instance.logger.error("配置参数[" + name + "]的值不是数字");
			return defValue;
		}
	}

	public static String getParameter(String name) {
		String xpath = "/webapp-configs/param[@name='" + name + "']/@value";
		Node node = instance.configDocument.selectSingleNode(xpath);
		if (node == null) {
			instance.logger.warn("配置参数[" + name + "]不存在");
			return "";
		}
		return node.getText();
	}

	/** 
	 * 
	 * @param name
	 * @return
	 * @author zhanglelei
	 * @date 2007-2-5 23:05:43 
	 */
	public static List<String> getParameterList(String name) {
		String xpath = "/webapp-configs/param[@name='" + name + "']/value";
		List nodes = instance.configDocument.selectNodes(xpath);
		List<String> values = new ArrayList<String>();
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			Node node = (Node) iter.next();
			values.add(node.getText());
		}
		return values;
	}

	protected ScaConfigUtil() {
		SAXReader reader = new SAXReader();
		try {
			configDocument = reader.read(CONFIG_FILE_NAME);

		} catch (DocumentException exp) {
			logger.warn(CONFIG_FILE_NAME + "文件读取错误：" + exp.getMessage());
			configDocument = DocumentHelper.createDocument();
			configDocument.addElement("webapp-configs");
		}
	}
	
	protected static Document getDocument() {
		return instance.configDocument;
	}
	
	protected static void outputWarnLog(String log) {
		instance.logger.warn(log);
	}
	
	public static String getAppVersion(){
		return getParameter(APP_VERSION);
	}
	
	
}
