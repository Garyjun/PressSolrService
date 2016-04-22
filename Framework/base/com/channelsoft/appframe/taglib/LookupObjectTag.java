/**
 * FileName: LookupObjectTag.java
 */
package com.channelsoft.appframe.taglib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * <dl>
 * <dt>LookupObjectTag</dt>
 * <dd>Description:对象查询标签</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-11-3</dd>
 * </dl>
 * 
 * @author 李大鹏
 */
public class LookupObjectTag extends BaseBodyTag implements IParameter
{
	private String action;
	private String actionMethod;
	private String img;
	private String alt;
	private String style;
	private String styleClass;
	private String jsName;
	private String width = "600";
	private String height = "400";
	
	private List<ParameterContext> parameters;
	/**
	 * @see com.channelsoft.appframe.taglib.BaseBodyTag#doStartAppFrameTag()
	 */
	@Override
	public int doStartAppFrameTag() throws JspException
	{
		parameters  = new ArrayList<ParameterContext>();
		return EVAL_PAGE;
	}

	/**
	 * @see com.channelsoft.appframe.taglib.BaseBodyTag#doEndAppFrameTag()
	 */
	@Override
	public int doEndAppFrameTag() throws JspException
	{
		/*
		 * <button class="image"
		 * onclick="AppFrame.Util.openLookup('/system/cfgLocationAction.do', '_lookup',
		 * onLookupLocation, '300', '300', 'lookupMode=0&locationId=0')"><html:img
		 * page="/images/lookup.gif" alt="查询"/></button>
		 */
		StringBuffer html = new StringBuffer(200);
		html.append("<button onclick=\"AppFrame.Util.openLookup(");
		html.append("'").append(getAction()).append("', '").append(getActionMethod()).append("'");
		html.append(", ").append(getJsName()).append(", '").append(getWidth()).append("'");
		html.append(", '").append(getHeight()).append("'");
		if(parameters.isEmpty())
		{
			html.append(", ''");
		}else{
			StringBuffer strParam = new StringBuffer(100);
			for(Iterator iter = parameters.iterator(); iter.hasNext(); )
			{
				ParameterContext param = (ParameterContext)iter.next();
				strParam.append("&").append(param.getName()).append("=").append(param.getValue());
			}
			
			html.append(", '").append(strParam.substring(1)).append("'");
		}
		html.append(")\"");
		
		if(StringUtils.isNotEmpty(getStyle()))
		{
			html.append(" style=\"").append(getStyle()).append("\"");
		}
		if(StringUtils.isNotEmpty(getStyleClass()))
		{
			html.append(" class=\"").append(getStyleClass()).append("\"");
		}
		
		html.append(">");
		html.append("<img src=\"").append(getContextPath()).append(getImg()).append("\"");
		if(StringUtils.isNotEmpty(getAlt()))
		{
			html.append(" alt=\"").append(getAlt()).append("\"");
		}
		html.append(">");
		html.append("</button>");
		
		pageWriter(html.toString());
		return EVAL_PAGE;
	}

	/**
	 * @see com.channelsoft.appframe.taglib.IParameter#addParameter(java.lang.Object)
	 */
	public void addParameter(ParameterContext param)
	{
		parameters.add(param);
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getActionMethod()
	{
		return actionMethod;
	}

	public void setActionMethod(String actionMethod)
	{
		this.actionMethod = actionMethod;
	}

	public String getAlt()
	{
		return alt;
	}

	public void setAlt(String alt)
	{
		this.alt = alt;
	}

	public String getHeight()
	{
		return height;
	}

	public void setHeight(String height)
	{
		this.height = height;
	}

	public String getImg()
	{
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getJsName()
	{
		return jsName;
	}

	public void setJsName(String onclick)
	{
		this.jsName = onclick;
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

	public String getWidth()
	{
		return width;
	}

	public void setWidth(String width)
	{
		this.width = width;
	}

}
