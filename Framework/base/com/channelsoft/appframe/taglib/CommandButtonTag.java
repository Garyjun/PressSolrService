/**
 * FileName: CommandButtonTag.java
 */
package com.channelsoft.appframe.taglib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * <dl>
 * <dt>CommandButtonag</dt>
 * <dd>Description:命令响应按钮</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Mar 20, 2008</dd>
 * </dl>
 */
public class CommandButtonTag extends BaseBodyTag implements IParameter {
	private String action;
//	private String method;
	private String js = "";
	private String styleClass;
	private String style;
	private String img = "/images/transparent.gif";
	private String caption = "";
	private String alt = "";
	private boolean isDefault = false;
	private boolean isSubmit = true;

	//Fuwenbin 20080320
	private boolean isPrivilege = false;

	private List<ParameterContext> parameters;

	public void addParameter(ParameterContext param) {
		parameters.add(param);
	}

	/**
	 * @see com.channelsoft.appframe.taglib.BaseBodyTag#doStartAppFrameTag()
	 */
	@Override
	public int doStartAppFrameTag() throws JspException {
		parameters = new ArrayList<ParameterContext>();
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

		StringBuffer html = new StringBuffer(200);
		html.append("<button");
		if (StringUtils.isNotEmpty(getId())) {
			html.append(" name=\"").append(getId()).append("\"");
		}

		// 设置响应命令
		html.append(" onclick=\"AppFrame.doCommand('").append(getAction())
				.append("'");
//		html.append(", '").append(getMethod()).append("'");升级到struts2不需要再传method参数 Fuwenbin
		if (isSubmit()) {
			html.append(", this.form");
		} else {
			html.append(", null");
		}
		html.append(", '").append(getJs()).append("', '");
		StringBuilder strParam = new StringBuilder(50);
		for (Iterator iter = parameters.iterator(); iter.hasNext();) {
			ParameterContext param = (ParameterContext) iter.next();
			strParam.append("&").append(param.getName()).append("=").append(
					param.getValue());
		}
		if (StringUtils.isNotEmpty(strParam.toString())) {
			html.append(strParam.substring(1));
		}
		html.append("')\"");

		// 设置其他属性
		if (StringUtils.isNotEmpty(getStyleClass())) {
			html.append(" class='").append(getStyleClass()).append("'");
		}
		if (StringUtils.isNotEmpty(getStyle())) {
			html.append(" style='").append(getStyle()).append("'");
		}
		html.append(" isDefault='").append(isDefault()).append("'");
		html.append(">");
		html.append("<img src='").append(getContextPath()).append(getImg())
				.append("'");
		if (StringUtils.isNotEmpty(getAlt())) {
			html.append(" alt='").append(getAlt()).append("'");
		}
		html.append(">");
		html.append(getCaption());
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
//		return userInfo.hasPrivilege(getAction().substring(0,
//				getAction().lastIndexOf(".")), getMethod());
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

//	public String getMethod() {
//		return method;
//	}
//
//	public void setMethod(String method) {
//		this.method = method;
//	}

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

	public boolean isSubmit() {
		return isSubmit;
	}

	public void setSubmit(boolean isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getJs() {
		return js;
	}

	public void setJs(String confrim) {
		this.js = confrim.replace("'", "\\'").replace("\"", "\\\"");
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public boolean isPrivilege() {
		return isPrivilege;
	}

	public void setPrivilege(boolean isPrivilege) {
		this.isPrivilege = isPrivilege;
	}
}
