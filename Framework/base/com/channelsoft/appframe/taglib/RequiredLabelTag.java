/**
 * FileName: RequiredLabelTag.java
 */
package com.channelsoft.appframe.taglib;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * <dl>
 * <dt>RequiredLabelTag</dt>
 * <dd>Description:必填项标签</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-11-1</dd>
 * </dl>
 * 
 * @author 李大鹏
 */
public class RequiredLabelTag extends BaseBodyTag
{
	private String style;
	private String styleClass;
	
	/**
	 * @see com.channelsoft.appframe.taglib.BaseBodyTag#doStartAppFrameTag()
	 */
	@Override
	public int doStartAppFrameTag() throws JspException
	{
		return EVAL_PAGE;
	}

	/**
	 * @see com.channelsoft.appframe.taglib.BaseBodyTag#doEndAppFrameTag()
	 */
	@Override
	public int doEndAppFrameTag() throws JspException
	{
		StringBuffer html = new StringBuffer(200);
		html.append("<span");
		if(StringUtils.isNotEmpty(getStyleClass()))
		{
			html.append(" class=\"").append(getStyleClass()).append("\"");
		}
		if(StringUtils.isNotEmpty(getStyle()))
		{
			html.append(" style=\"").append(getStyle()).append("\"");
		}
		html.append(">*</span>");
		
		pageWriter(html.toString());
		return EVAL_PAGE;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public String getStyleClass()
	{
		return styleClass;
	}

	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}

}
