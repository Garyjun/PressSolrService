/**
 * FileName: ParameterTag.java
 */
package com.channelsoft.appframe.taglib;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * <dl>
 * <dt>ParameterTag</dt>
 * <dd>Description:参数标签</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 24, 2008</dd>
 * </dl>
 * 
 */
public class ParameterTag extends BaseBodyTag {
	private String paramName;
	private String beanName;
	private String property;
	private String value;
	private String scope;

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
		if (IParameter.class.isAssignableFrom(getParent().getClass())) {
			if (StringUtils.isNotEmpty(getBeanName())
					&& StringUtils.isNotEmpty(getProperty())) {
				Object object = TagUtils.getInstance().lookup(getPageContext(),
						getBeanName(), getProperty(), getScope());
				setValue(String.valueOf(object));
			}
			ParameterContext param = new ParameterContext(getParamName(),
					getValue());
			((IParameter) getParent()).addParameter(param);
		}
		return EVAL_PAGE;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String name) {
		this.paramName = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

}
