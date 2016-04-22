/**
 * FileName: ResetButtonTag.java
 */
package com.channelsoft.appframe.taglib;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * <dl>
 * <dt>ResetButtonTag</dt>
 * <dd>Description:表单重置按钮</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 24, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class ResetButtonTag extends BaseBodyTag {
	private String caption;
	private String img = "/images/transparent.gif";
	private String style;
	private String styleClass;

	/**
	 * @see com.channelsoft.appframe.taglib.BaseBodyTag#doStartAppFrameTag()
	 */
	@Override
	public int doStartAppFrameTag() throws JspException {
		return EVAL_PAGE;
	}

	/**
	 * @see com.channelsoft.appframe.taglib.BaseBodyTag#doEndAppFrameTag()
	 */
	@Override
	public int doEndAppFrameTag() throws JspException {
		StringBuffer html = new StringBuffer();
		html.append("<button onclick=\"this.form.reset()\"");
		if (StringUtils.isNotEmpty(getStyleClass())) {
			html.append(" class=\"").append(getStyleClass()).append("\"");
		}
		if (StringUtils.isNotEmpty(getStyle())) {
			html.append(" style=\"").append(getStyle()).append("\"");
		}
		html.append(">");
		html.append("<img src='").append(getContextPath()).append(getImg())
				.append("'>");
		html.append(getCaption());
		html.append("</button>");
		pageWriter(html.toString());
		return EVAL_PAGE;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
