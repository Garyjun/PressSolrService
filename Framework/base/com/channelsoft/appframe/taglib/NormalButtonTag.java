/**
 * FileName: NormalButtonTag.java
 */
package com.channelsoft.appframe.taglib;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * <dl>
 * <dt>NormalButtonTag</dt>
 * <dd>Description:普通按钮</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Mar 20, 2008</dd>
 * </dl>
 */
public class NormalButtonTag extends BaseBodyTag {
	private String caption;
	private String name;//Fuwenbin 2008-04-14
	private String img = "/images/transparent.gif";
	private String style;
	private String styleClass;
	private String onclick;
	private boolean isDefault = false;

	//Fuwenbin 20080320
	private String action;
//	private String method;
	private boolean isPrivilege = false;

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
		//Fuwenbin 20080320 用户权限校验
		if (!hasPrivilege()) {
			return EVAL_PAGE;
		}
		
		StringBuffer html = new StringBuffer();
		html.append("<button ");
		html.append(StringUtils.isNotBlank(getName()) ? "name=\""+getName()+"\"" : "");
		html.append(" onclick=\"").append(getOnclick()).append("\"");
		html.append(" isDefault=\"").append(isDefault()).append("\"");
		if (StringUtils.isNotEmpty(getStyleClass())) {
			html.append(" class=\"").append(getStyleClass()).append("\"");
		}
		if (StringUtils.isNotEmpty(getStyle())) {
			html.append(" style=\"").append(getStyle()).append("\"");
		}
		html.append(">");
		html.append("<img src='").append(getContextPath()).append(getImg())
				.append("'>");
		if(StringUtils.isNotBlank(getCaption())){
			html.append(getCaption());
		}
		html.append("</button>");
		pageWriter(html.toString());
		return EVAL_PAGE;
	}
	
	//Fuwenbin 20080320 用户权限校验
	private boolean hasPrivilege() {
		return true;
//		if (!isPrivilege()) {
//			return true;
//		}
//
//		IUserInfo userInfo = AuthorHelper.getUserInfo(getHttpServletRequest());
//		String action = getAction().lastIndexOf(".") > -1 ? getAction().substring(0,
//				getAction().lastIndexOf(".")) : getAction();
//		return userInfo.hasPrivilege(action, getMethod());
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

	public String getOnclick() {
		return onclick.replace('"', '\'');
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
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

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	public boolean isPrivilege() {
		return isPrivilege;
	}

	public void setPrivilege(boolean isPrivilege) {
		this.isPrivilege = isPrivilege;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

//	public String getMethod() {
//		return method;
//	}
//
//	public void setMethod(String method) {
//		this.method = method;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
