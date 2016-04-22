/**
 * FileName: SearchBarTag.java
 */
package com.channelsoft.appframe.taglib;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * <dl>
 * <dt>SearchBarTag</dt>
 * <dd>Description:搜索条标签</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 25, 2008</dd>
 * </dl>
 * 
 */
public class SearchBarTag extends BaseBodyTag
{
	private String caption;
	private String style;
	private String styleClass;
	private String onclick;
	private boolean isDisplay=false;
	
	/**
	 * @see com.channelsoft.appframe.taglib.BaseBodyTag#doStartAppFrameTag()
	 */
	@Override
	public int doStartAppFrameTag() throws JspException
	{
		StringBuffer html = new StringBuffer(200);
		html.append("<fieldset");
		if(StringUtils.isNotEmpty(getStyleClass()))
		{
			html.append(" class='").append(getStyleClass()).append("'");
		}
		if(StringUtils.isNotEmpty(getStyle()))
		{
			html.append(" style='").append(getStyle()).append("'");
		}
		html.append(">");
		html.append("<legend");
		html.append(" id='").append(getId()).append("'");
		if(StringUtils.isNotEmpty(getOnclick()))
		{
			html.append(" onclick=\"").append(getOnclick()).append("\"");
		}
		html.append(">").append(getCaption());
		html.append("<img id=\"").append(getId()).append("_img").append("\"");
		if(isDisplay())
		{
			html.append(" src=\"").append(getContextPath()).append("/images/closeField.gif").append("\"");
		}else{
			html.append(" src=\"").append(getContextPath()).append("/images/openField.gif").append("\"");
		}
		html.append(">");
		html.append("</legend>");
		html.append("<div><div id=\"").append(getId()).append("_div").append("\"");
		if(isDisplay())
		{
			html.append(" style=\"display:block\"");
		}else{
			html.append(" style=\"display:none\"");
		}
		html.append(">");
		
		pageWriter(html.toString());
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * @see com.channelsoft.appframe.taglib.BaseBodyTag#doEndAppFrameTag()
	 */
	@Override
	public int doEndAppFrameTag() throws JspException
	{
		StringBuffer html = new StringBuffer(200);
		html.append("</div></div>");
		html.append("</fieldset><br><br>");
		
		pageWriter(html.toString());
		return EVAL_PAGE;
	}

	public String getCaption()
	{
		return caption;
	}

	public void setCaption(String caption)
	{
		this.caption = caption;
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

	public boolean isDisplay()
	{
		return isDisplay;
	}

	public void setDisplay(boolean isDisplay)
	{
		this.isDisplay = isDisplay;
	}

	public String getOnclick()
	{
		if(StringUtils.isNotEmpty(onclick))
		{
			return onclick.replace('\"', '\'');
		}
		return "";
	}

	public void setOnclick(String onclick)
	{
		this.onclick = onclick;
	}

}
