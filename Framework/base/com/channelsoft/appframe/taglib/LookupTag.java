package com.channelsoft.appframe.taglib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.channelsoft.appframe.utils.RandomUtils;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * <dl>
 * <dt>LookupTag</dt>
 * <dd>Description:数据选择控件，显示选择按钮、清除按钮和文本框控件。</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 24, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class LookupTag extends BaseBodyTag implements IParameter {
	public static final long serialVersionUID = 1L;
	protected transient final Log logger = LogFactory.getLog(getClass());
	private String cssClass = "inputnormal";
	private String width = "700";
	private String height = "500";
	private String value;
	private String backImage = "/images/lookup.gif";
	private String clearImage = "/images/lookupClear.gif";
	private String property = "";
	private String size = "";
	private String maxLength = "";
	private String action = "/lookupAction.do";
	private String hidden = "";
	private String callBackJS = "";
	private String callParamJS = "";
	private String callClearJS = "";
	private String callPerJS = "";
	private String returnValues = "";

	private static final String HIDDEN_FIELD = "0";
	private static final String TEXT_FIELD = "1";

	private boolean isDisplayName = true;
	private boolean isReadOnly = true;

	private List<ParameterContext> parameters;

	/**
	 * @see com.channelsoft.qframe.taglib.IParameter#addParameter(com.channelsoft.qframe.taglib.ParameterContext)
	 */
	public void addParameter(ParameterContext param) {
		if (param == null)
			return;

		parameters.add(param);
	}

	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return Returns the size.
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            The size to set.
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return Returns the property.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return Returns the backImage.
	 */
	public String getBackImage() {
		return backImage;
	}

	/**
	 * @param backImage
	 *            The backImage to set.
	 */
	public void setBackImage(String backImage) {
		if (StringUtils.isEmpty(backImage))
			return;

		if (backImage.startsWith("/"))
			this.backImage = backImage;
		else
			this.backImage = "/" + backImage;
	}

	public String getClearImage() {
		return clearImage;
	}

	public void setClearImage(String clearImage) {
		if (StringUtils.isEmpty(clearImage))
			return;

		if (clearImage.startsWith("/"))
			this.clearImage = clearImage;
		else
			this.clearImage = "/" + clearImage;
	}

	/**
	 * @return Returns the height.
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            The height to set.
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @return Returns the styleClass.
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param styleClass
	 *            The styleClass to set.
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return Returns the width.
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	@Override
	public String getId() {
		if (StringUtils.isEmpty(super.getId())) {
			super.setId(String.valueOf(RandomUtils.random(10)));
		}
		return super.getId();
	}

	public int doStartAppFrameTag() throws JspException {
		parameters = new ArrayList<ParameterContext>();
		return EVAL_PAGE;
	}

	public int doEndAppFrameTag() throws JspException {
		List<TextField> list = buildCtrl();
		for (int index = 0; index < list.size(); index++) {
			TextField element = list.get(index);
//			if (index > 0 && TEXT_FIELD.equals(element.type)) {
//				TagUtils.getInstance().write(pageContext, "-");
//			}
			
			element.write();
		}

		TagUtils.getInstance().write(pageContext, buildButton());
		super.setId("");
		return EVAL_PAGE;
	}

	/**
	 * 根据property/hidden属性生成控件
	 */
	private List<TextField> buildCtrl() throws JspException {
		List<TextField> fieldList = new ArrayList<TextField>();

		List<String> propertyList = getPropertyList();
		List<String> sizeList = getSizeList();
		List<String> maxLengthList = getMaxLengthList();
		List<String> hiddenList = getHiddenList();

		if (propertyList.size() != sizeList.size()) {
			throw new JspException("property与size属性不匹配");
		}

		if (!maxLengthList.isEmpty()
				&& maxLengthList.size() != propertyList.size()) {
			throw new JspException("property与maxlength属性不匹配");
		}

		for (int proIndex = 0; proIndex < propertyList.size(); proIndex++) {
			String pro = propertyList.get(proIndex);

			TextField field = new TextField();
			field.setId(getIdName(pro));
			field.setName(pro);
			field.setValue(getFieldValue(pro));
			field.setSize(sizeList.get(proIndex));

			if (hiddenList.contains(pro)) {
				field.setType(HIDDEN_FIELD);
			} else {
				field.setType(TEXT_FIELD);

				if (StringUtils.isNotBlank(getCssClass())) {
					field.setStyleClass(getCssClass());
				}
				if (!maxLengthList.isEmpty()) {
					field.setMaxlength(maxLengthList.get(proIndex));
				}
			}

			fieldList.add(field);
		}

		return fieldList;
	}

	private String getIdName(String protpertyName) {
		return StringUtils.replace(protpertyName, ".", "_");
	}

	private String getFieldValue(String protpertyName) {
		ValueStack vs = org.apache.struts2.views.jsp.TagUtils
				.getStack(pageContext);
		Object valueObject = vs.findValue(protpertyName);
		if (valueObject == null) {
			return "";
		}

		return valueObject.toString();
	}

	private String buildButton() {
		StringBuilder htmlButton = new StringBuilder(200);
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String context = request.getContextPath();

		StringBuilder proQuery = new StringBuilder(200);
		/* 拼接参数到URL */
		for (Iterator iter = parameters.iterator(); iter.hasNext();) {
			ParameterContext param = (ParameterContext) iter.next();
			proQuery.append("&").append(param.getName()).append("=").append(
					param.getValue());
		}

		/*
		 * <button
		 * onclick="AppFrame.Util.openLookup('/system/cfgLocationAction.do',
		 * '_lookup', onLookupLocation, '300', '300',
		 * 'lookupMode=0&locationId=0')" class="image"><img
		 * src="/usboss40/images/lookup.gif" alt="查询"></button>
		 */
		htmlButton.append("<button onClick=\"AppFrame.Util.openLookup('").append(
				getAction()).append("', ")
				.append("onCallBackJS_").append(getId()).append(", '").append(
						getWidth()).append("', '").append(getHeight()).append(
						"', '").append(proQuery.toString()).append(
						"', onCallParamJS_").append(getId()).append(
						", onCallPerJS_").append(getId())
						.append(", '").append(getIdName(getPropertyList().get(0))).append(
						"')\" class=\"image\"").append("><img src=\"").append(
						context).append(getBackImage()).append("\" alt=\"")
				.append(getValue()).append("\"></button>");
		htmlButton.append("<button onClick=\"onCallClearJS_").append(getId())
				.append("()\"").append(" class=\"image\"><img src=\"").append(
						context).append(getClearImage()).append(
						"\" alt=\"清除\"></button>");

		htmlButton.append("<script language='javascript'>\n");
		/* Lookup打开之前执行的回调函数 */
		htmlButton.append("function onCallPerJS_").append(getId()).append(
				"(){\n");
		if (StringUtils.isNotEmpty(getCallPerJS())) {
			htmlButton.append("\treturn ").append(getCallPerJS()).append(
					"();\n");
		} else {
			htmlButton.append("\treturn true;\n");
		}
		htmlButton.append("}\n");

		/* Lookup回调响应函数 */
		htmlButton.append("function onCallBackJS_").append(getId()).append(
				"(record){\n");
		if (StringUtils.isNotEmpty(getCallBackJS())) {
			htmlButton.append("\t").append(getCallBackJS()).append(
					"(record);\n");
		} else {
			List<String> returnValueList = getReturnValueList();
			List<String> proList = getPropertyList();
			for (int index = 0; index < returnValueList.size(); index++) {
				htmlButton.append("\t$('").append(proList.get(index)).append(
						"').value=record[\"").append(
						returnValueList.get(index)).append("\"];\n");
			}
		}
		htmlButton.append("}\n");

		/* 脚本参数响应函数 */
		htmlButton.append("function onCallParamJS_").append(getId()).append(
				"(){\n");
		if (StringUtils.isNotEmpty(getCallParamJS())) {
			htmlButton.append("\treturn ").append(getCallParamJS()).append(
					"();\n");
		} else {
			htmlButton.append("\treturn new Array();\n");
		}
		htmlButton.append("}\n");

		/* 清除选择数据函数 */
		htmlButton.append("function onCallClearJS_").append(getId()).append(
				"(){\n");
		if (StringUtils.isNotEmpty(getCallClearJS())) {
			htmlButton.append("\t").append(getCallClearJS()).append("();");
		} else {
			List<String> proList = getPropertyList();
			for (String pro : proList) {
				htmlButton.append("\t$('").append(pro).append("').value='';\n");
			}
		}
		htmlButton.append("}\n");
		htmlButton.append("</script>");
		return htmlButton.toString();
	}

	/**
	 * @return
	 */
	public boolean isDisplayName() {
		return isDisplayName;
	}

	/**
	 * @param isDisplayName
	 */
	public void setDisplayName(boolean isDisplayName) {
		// 如果不给值，则取默认值为true，否则"true"以外的其他值返回false,此处转换没必要做且websphere上会出错,因此更改参数类型为boolean
		// if(StringUtils.isNotEmpty(isDisplayName)) {
		this.isDisplayName = isDisplayName;
		// }
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	/**
	 * @return Returns the maxLength.
	 */
	public String getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength
	 *            The maxLength to set.
	 */
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getCallBackJS() {
		return callBackJS;
	}

	public void setCallBackJS(String callBackJS) {
		this.callBackJS = callBackJS;
	}

	public String getCallParamJS() {
		return callParamJS;
	}

	public void setCallParamJS(String callParamJS) {
		this.callParamJS = callParamJS;
	}

	public String getHidden() {
		return hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public String getReturnValues() {
		if (StringUtils.isEmpty(returnValues)) {
			StringBuilder strReturn = new StringBuilder(100);
			List proList = getPropertyList();
			for (int index = 0; index < proList.size(); index++) {
				strReturn.append(",").append(proList.get(index));
			}
			if (StringUtils.isNotEmpty(strReturn.toString())) {
				return strReturn.substring(1);
			}
		}
		return returnValues;
	}

	public void setReturnValues(String returnValue) {
		this.returnValues = returnValue;
	}

	private List<String> getHiddenList() {
		List<String> hiddenList = buildStringToList(getHidden());
		List<String> proList = getPropertyList();
		if (!proList.isEmpty() && proList.size() > 1
				&& !hiddenList.contains(proList.get(1))) {
			if (!isDisplayName()) {
				hiddenList.add(proList.get(1));
			} else {
				hiddenList.add(proList.get(0));
			}
		}
		
		return hiddenList;
	}

	private List<String> getPropertyList() {
		return buildStringToList(getProperty());
	}

	private List<String> getSizeList() {
		return buildStringToList(getSize());
	}

	private List<String> getMaxLengthList() {
		return buildStringToList(getMaxLength());
	}

	private List<String> getReturnValueList() {
		return buildStringToList(getReturnValues());
	}

	private List<String> buildStringToList(String strParam) {
		List<String> strList = new ArrayList<String>();

		if (StringUtils.isNotBlank(strParam)) {
			String[] strs = strParam.split(",");
			for (String str : strs) {
				strList.add(str);
			}
		}
		return strList;
	}

	public String getCallClearJS() {
		return callClearJS;
	}

	public void setCallClearJS(String callClearJS) {
		this.callClearJS = callClearJS;
	}

	public String getCallPerJS() {
		return callPerJS;
	}

	public void setCallPerJS(String callPerJS) {
		this.callPerJS = callPerJS;
	}

	class TextField {
		private String id;
		private String name;
		private String type;
		private String maxlength;
		private String size;
		private String value;
		private String styleClass;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMaxlength() {
			return maxlength;
		}

		public void setMaxlength(String maxlength) {
			this.maxlength = maxlength;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getStyleClass() {
			return styleClass;
		}

		public void setStyleClass(String styleClass) {
			this.styleClass = styleClass;
		}
		
		public void write()throws JspException{
			StringBuffer buf = new StringBuffer(50);
			
			if(StringUtils.equals(this.type, HIDDEN_FIELD)){
				buf.append("<input type=\"hidden\" name=\"")
				   .append(this.getName()).append("\"")
				   .append(" id=\"").append(this.getId()).append("\"")
				   .append(" value=\"").append(this.getValue()).append("\">");
				
				TagUtils.getInstance().write(pageContext, buf.toString());
				return;
			}
			
			buf.append("<input type=\"text\" name=\"")
			   .append(this.getName()).append("\"")
			   .append(" id=\"").append(this.getId()).append("\"")
			   .append(" size=\"").append(this.getSize()).append("\"")
			   .append(" value=\"").append(this.getValue()).append("\"");
			if(isReadOnly()){
				buf.append(" readonly=\"readonly\"");
			}
			if(StringUtils.isNotBlank(this.getMaxlength())){
				buf.append(" maxlength=\"").append(this.getMaxlength()).append("\"");
			}
			if(StringUtils.isNotBlank(this.getStyleClass())){
				buf.append(" class=\"").append(this.getStyleClass()).append("\"");
			}
			
			buf.append(">");
			
			TagUtils.getInstance().write(pageContext, buf.toString());
		}
	}
}