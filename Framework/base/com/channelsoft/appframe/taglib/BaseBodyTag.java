/**
 * FileName: BaseBodyTag.java
 */
package com.channelsoft.appframe.taglib;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <dl>
 * <dt>BaseBodyTag</dt>
 * <dd>Description:AppFrame标签基类</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 24, 2008</dd>
 * </dl>
 */
public abstract class BaseBodyTag extends BodyTagSupport {
	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		return doEndAppFrameTag();
	}

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		return doStartAppFrameTag();
	}

	public abstract int doStartAppFrameTag() throws JspException;

	public abstract int doEndAppFrameTag() throws JspException;

	protected void pageWriter(String str) {
		JspWriter out = getPageContext().getOut();
		try {
			out.println(str);
		} catch (IOException exp) {
			logger.error("Jsp output error.", exp);
		}
	}

	protected PageContext getPageContext() {
		return pageContext;
	}

	protected ServletRequest getServletRequest() {
		return getPageContext().getRequest();
	}

	protected ServletResponse getServletResponse() {
		return getPageContext().getResponse();
	}

	protected HttpServletRequest getHttpServletRequest() {
		return (HttpServletRequest) getServletRequest();
	}

	protected HttpServletResponse getHttpServletResponse() {
		return (HttpServletResponse) getServletResponse();
	}

	protected String getContextPath() {
		return getHttpServletRequest().getContextPath();
	}

}
