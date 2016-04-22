package com.channelsoft.appframe.taglib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * This class is designed to put all the public variables in a class to a
 * specified scope - designed for exposing a Constants class to Tag Libraries.
 * </p>
 * 
 * <p>
 * It is designed to be used as follows:
 * 
 * <pre>
 * 
 *  &lt;tag:constants /&gt;
 *  
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * Optional values are "className" (fully qualified) and "scope".
 * </p>
 * 
 * <p>
 * <a href="BaseAction.java.html"> <i>View Source </i> </a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible </a>
 * @version $Revision$ $Date$
 * 
 * @jsp.tag name="constants" bodycontent="empty"
 *          tei-class="org.appfuse.webapp.taglib.ConstantsTei"
 */
public class ConstantsTag extends TagSupport {
	protected static final Log logger = LogFactory.getLog(ConstantsTag.class);
	private static final long serialVersionUID = 3258417209566116146L;

//	public String id = null;
//
//	/**
//	 * @return Returns the id.
//	 */
//	public String getId() {
//		return id;
//	}
//
//	/**
//	 * @param id
//	 *            The id to set.
//	 */
//	public void setId(String id) {
//		this.id = id;
//	}

	public String repository = null;

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

	/**
	 * The class to expose the variables from.
	 */

	// by liyong
	// class is required attribute
	//    public String clazz = Constants.class.getName();
	public String clazz = null;

	/**
	 * The scope to be put the variable in.
	 */
	protected String scope = null;

	/**
	 * The single variable to expose.
	 */
	protected String var = null;

	public int doStartTag() throws JspException {
		// Using reflection, get the available field names in the class

		int toScope = PageContext.PAGE_SCOPE;

		if (scope != null) {
			toScope = getScope(scope);
		}

		ConstantsMap map = ConstantsRepository.getInstance().getConstantsMap(
				repository, clazz);
 
//		printDebugInfo(map);

		pageContext.setAttribute(id, map.getConstantsSet(), toScope);

		return (SKIP_BODY);
	}

	private void printDebugInfo(ConstantsMap map) {
		if (logger.isDebugEnabled()) {
			StringBuffer content = new StringBuffer(200);
			for (Iterator iter = map.getConstantsSet().iterator(); iter
					.hasNext();) { 
				content.append(iter.next());
			}
			logger.debug(content.toString());
		}
	}

	/**
	 * @jsp.attribute
	 */
	public void setClassName(String clazz) {
		this.clazz = clazz;
	}

	public String getClassName() {
		return this.clazz;
	}

	/**
	 * @jsp.attribute
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getScope() {
		return (this.scope);
	}

	/**
	 * @jsp.attribute
	 */
	public void setVar(String var) {
		this.var = var;
	}

	public String getVar() {
		return (this.var);
	}

	/**
	 * Release all allocated resources.
	 */
	public void release() {
		super.release();
		clazz = null;
		//        scope = Constants.class.getName();
	}

	// ~========== From Struts' TagUtils class =====================

	/**
	 * Maps lowercase JSP scope names to their PageContext integer constant
	 * values.
	 */
	private static final Map<String, Object> scopes = new HashMap<String, Object>();

	/**
	 * Initialize the scope names map and the encode variable with the Java 1.4
	 * method if available.
	 */
	static {
		scopes.put("page", PageContext.PAGE_SCOPE);
		scopes.put("request", PageContext.REQUEST_SCOPE);
		scopes.put("session", PageContext.SESSION_SCOPE);
		scopes.put("application", PageContext.APPLICATION_SCOPE);
	}

	/**
	 * Converts the scope name into its corresponding PageContext constant
	 * value.
	 * 
	 * @param scopeName
	 *            Can be "page", "request", "session", or "application" in any
	 *            case.
	 * @return The constant representing the scope (ie.
	 *         PageContext.REQUEST_SCOPE).
	 * @throws JspException
	 *             if the scopeName is not a valid name.
	 */
	public int getScope(String scopeName) throws JspException {
		Integer scope = (Integer) scopes.get(scopeName.toLowerCase());

		if (scope == null) {
			throw new JspException("Scope '" + scopeName
					+ "' not a valid option");
		}

		return scope.intValue();
	}
}