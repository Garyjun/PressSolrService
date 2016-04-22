package com.channelsoft.appframe.taglib;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.channelsoft.appframe.utils.DateUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * <dl>
 * <dt>DateTimeTag</dt>
 * <dd>Description:日期时间标签</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 24, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class DateTimeTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	protected transient final Log logger = LogFactory.getLog(getClass());

	private String property;

	private String cssClass = "inputnotnull";

	private String clearImage = "images/lookupClear.gif";

	private String dateImage = "images/calendar.gif";

	private boolean readOnly = true;
	
	private int startYear = 0;
	private int endYear = 0;

	private static final String MONTH_MODE = "month";
	private static final String DATE_MODE = "date";
	private static final String DATETIME_MODE = "dateTime";
	private static final String TIME_MODE = "time";
	/*显示模式：
	 * month：月模式；
	 * date:日期模式，缺省；
	 * dateTime：时间模式。
	 */
	private String showMode = DATE_MODE;
	
	public String getShowMode() {
		if(!StringUtils.equalsIgnoreCase(showMode, MONTH_MODE)
			&& !StringUtils.equalsIgnoreCase(showMode, DATE_MODE)
			&& !StringUtils.equalsIgnoreCase(showMode, DATETIME_MODE)
			&& !StringUtils.equalsIgnoreCase(showMode, TIME_MODE)){
			return DATE_MODE;
		}
		
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}
	
	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public String getClearImage() {
		return clearImage;
	}

	public void setClearImage(String clearImage) {
		this.clearImage = clearImage;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String styleClass) {
		this.cssClass = styleClass;
	}

	public DateTimeTag() {
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

	public int doStartTag() throws JspException {
		//输出日期文本框
		TagUtils.getInstance().write(pageContext, buildText());

		//输出图片
		TagUtils.getInstance().write(pageContext, buildImages());
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void release() {
	}

	public String buildImages() {
		StringBuffer m_date = new StringBuffer();

		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String path = request.getContextPath();

		if (path.startsWith("/") == false) {
			path = "/" + path;
		}
		if (StringUtils.isNotEmpty(path) && !path.endsWith("/"))
			path = path + "/";

		//输出日期图片
//		m_date.append("&nbsp;<img style=\"CURSOR: hand\" alt=\"点击这里选择时间\" ");
//		StringBuffer dtImage = new StringBuffer();
//		dtImage.append(path).append(dateImage);
//		m_date.append(" src=\"").append(dtImage).append("\" align=absMiddle ");
//		m_date.append(" onClick=\"fPopCalendar(");
//		m_date.append(getIdValue()).append(",").append(getIdValue()).append(",'");
//		m_date.append(getShowMode()).append("','");
//		m_date.append(getStartYear() != 0 ? getStartYear() : "").append("','");
//		m_date.append(getEndYear() != 0 ? getEndYear() : "").append("',");
//		m_date.append("'2'");
//		m_date.append(");return false;\" >&nbsp;");
		
		//输出清除图片
		StringBuffer lc_buffer = new StringBuffer();
		lc_buffer.append(path);

		lc_buffer.append(clearImage);
		m_date.append("<img style=\"CURSOR: hand\" src=").append(
				lc_buffer.append(" alt=\"清除\""));
		m_date.append(" onclick=\"return fClearCalendar(");
		m_date.append(getIdValue()).append(");\"/>");

		return m_date.toString();
	}

	private String buildText() throws JspException{
		StringBuffer textBuf = new StringBuffer(50);
		textBuf.append("<input type=\"text\"")
			   .append(" name=\"").append(getProperty()).append("\"")
			   .append(" id=\"").append(getIdValue()).append("\"");;

		String size = "9";
		String maxLength = "10";
		if (StringUtils.equalsIgnoreCase(getShowMode(), DATETIME_MODE)) {
			size = "19";
			maxLength = "19";
		} else if (StringUtils.equalsIgnoreCase(getShowMode(), MONTH_MODE)) {
			size = "5";
			maxLength = "6";
		} else if (StringUtils.equalsIgnoreCase(getShowMode(), TIME_MODE)) {
			size = "6";
			maxLength = "8";
		}
		textBuf.append(" value=\"").append(getValue()).append("\"")
			   .append(" size=\"").append(size).append("\"")
			   .append(" maxlength=\"").append(maxLength).append("\"")
			   .append(" class=\"").append(getCssClass()).append("\"");
		if(isReadOnly()){
			textBuf.append(" readonly=\"readonly\"");
		}
		
		textBuf.append(" onClick=\"fPopCalendar(");
		textBuf.append(getIdValue()).append(",").append(getIdValue()).append(",'");
		textBuf.append(getShowMode()).append("','");
		textBuf.append(getStartYear() != 0 ? getStartYear() : "").append("','");
		textBuf.append(getEndYear() != 0 ? getEndYear() : "").append("',");
		textBuf.append("'2'");
		textBuf.append(");return false;\"");
		
		textBuf.append("/>");
		
		return textBuf.toString();
	}
	
	private String getValue() throws JspException{
		ValueStack vs = org.apache.struts2.views.jsp.TagUtils.getStack(pageContext);
		Object valueObject = vs.findValue(getProperty());
		if(valueObject == null){
			return "";
		}
		
		if(valueObject instanceof String){
			return (String)valueObject;
		}
		
		if(valueObject instanceof Date){
			if(StringUtils.equalsIgnoreCase(getShowMode(), DATETIME_MODE)){
				return DateUtil.convertDateTimeToString((Date)valueObject);
			}
			
			return DateUtil.convertDateToString((Date)valueObject);
		}
		
		throw new JspException("标签[dateTime]对应的属性"+getProperty()+"类型不合法！");
	}
	
	private String getIdValue(){
		return StringUtils.replace(getProperty(), ".", "_");
	}

	public String getDateImage() {
		return dateImage;
	}

	public void setDateImage(String dateImage) {
		this.dateImage = dateImage;
	}
}