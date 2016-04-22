package com.channelsoft.appframe.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestUtils {

	public static final String BACK_URL = "BACK_URL";

	final static Log logger = LogFactory.getLog(RequestUtils.class);

	/**
	 * 返回由URL参数组成的字符串
	 * 
	 * @param request
	 * @param excludeParam
	 *            被排除的参数
	 * @return
	 */
	public static String getParameterStr(HttpServletRequest request,
			String[] excludeParam) {
		
		StringBuffer exParam = new StringBuffer();
		for(int i=0; i<excludeParam.length; i++)
		{
			exParam.append(",").append(excludeParam[i]);
		}
		if(StringUtils.isNotEmpty(exParam.toString()))
		{
			exParam.append(",");
		}
		
		StringBuffer strBuf = new StringBuffer();
		for (Iterator iter = request.getParameterMap().keySet().iterator(); iter
				.hasNext();) {
			String key = (String) iter.next();

			// 排除指定的参数
			if (StringUtils.indexOf(key, "," + key + ",") != -1) {
				continue;
			}

			String[] data = (String[]) request.getParameterMap().get(key);
			for (int i = 0; i < data.length; i++) {
				try {
					strBuf.append("&").append(key).append("=").append(
							URLEncoder.encode(data[i], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// 发生编码异常不处理
					e.printStackTrace();
				}
			}
		}
		logger.debug("url= " + strBuf.toString());
		return strBuf.toString();
	}

	public static void setBackUrl(HttpServletRequest request, String backUrl) {
		StringBuffer url = new StringBuffer(200);
		url.append(request.getContextPath()).append(backUrl);
		url.append(getParameterStr(request, new String[]{"reqCode"})); 
		request.setAttribute(BACK_URL, url.toString());
		if (logger.isDebugEnabled()) {
			logger.debug("backUrl: " + url.toString());
		} 
	}

	/**
	 * 供带回查询参数的返回按钮调用
	 * @param reqCode
	 * @param request
	 * @param mapping
	 *
	 */
//	public static void setBackUrl(String reqCode, HttpServletRequest request,ActionMapping mapping) {
//        StringBuffer backUrl = new StringBuffer(200);
//        backUrl.append(request.getContextPath())
//        .append(mapping.getPath())
//        .append(".do?reqCode=")
//        .append(reqCode)
//        .append(getParameterStr(request, new String[]{"reqCode"})); 
//        request.setAttribute(BACK_URL, backUrl.toString());
//    } 
}
