/**
 * 
 */
package com.channelsoft.appframe.utils;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.appframe.common.BaseObject;

/**
 * @author liwei
 * 
 */
public class RequestParameterHelper extends BaseObject {
	private static RequestParameterHelper instance = null;

	public static RequestParameterHelper getInstance() {
		if (instance == null) {
			instance = new RequestParameterHelper();
		}
		return instance;
	}

	/**
	 * 取得用户提交的int参数值
	 * 
	 * @param request
	 * @param parameterName
	 * @param defValue
	 * @return
	 */
	public int getIntParameterValue(HttpServletRequest request,
			String parameterName, int defValue) {
		String value = request.getParameter(parameterName);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defValue;
		}
	}


	/**
	 * 取得用户提交的Integer参数值
	 * 
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public Integer getIntegerParameterValue(HttpServletRequest request,
			String parameterName) {
		String stringValue = request.getParameter(parameterName);
		logger.info(parameterName + "=" + stringValue);
		try {
			return Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
