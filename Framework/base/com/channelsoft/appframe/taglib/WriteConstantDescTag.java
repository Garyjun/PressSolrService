package com.channelsoft.appframe.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

public class WriteConstantDescTag extends TagSupport {
	public static final long serialVersionUID = 1L;
	private transient final Logger logger = Logger
			.getLogger(WriteConstantDescTag.class);

	private String repository = null;

	private String className = null;

	private String name = null;

	private String property = null;

	private Object value = null;

	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            The className to set.
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return Returns the repository.
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 *            The repository to set.
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	// ------------------------------ Public Methods
	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		String output = null;
		
		
		if ((name!=null || property != null) && value != null) {
			throw new JspException(
					"<app:writeConstantDesc> taglib accept either name/property or value, not both.");
		}
		
		if ( name == null && property == null && value == null) {
			throw new JspException(
					"<app:writeConstantDesc> taglib require either of name/property/value is non-empty string.");
		}		

		// name和property都有可能没有值！
		if (value == null) {
			if (name == null) {
//				setName(Constants.BEAN_KEY);
				throw new JspException(
				"<app:writeConstantDesc> taglib accept either name/property or value, not both.");

			}
			Object obj = TagUtils.getInstance().lookup(pageContext, name, property, null);
			setValue(obj);
		}


		try {
			IConstantsRepository rep = ConstantsRepository.getInstance();
			ConstantsMap constantsMap = rep.getConstantsMap(repository,	className);
			output = (String) constantsMap.getDescByValue(value);
			if(output == null) {
				output = "";
			}
//			System.out.println("output: " + output);
			pageContext.getOut().println(output);
		} catch (IOException e) {
			logger.error(e);
			throw new JspException(e.getMessage());
		}

		setValue(null);
		return EVAL_PAGE;
	}

	public void release() {
		super.release();
		//    scope = null;
		name = null;
		property = null;
		value = null;
	}
}