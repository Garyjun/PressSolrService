package com.channelsoft.appframe.taglib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * <dl>
 * <dt>TagUtils</dt>
 * <dd>Description:自定义标签的工具类</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 24, 2008</dd>
 * </dl>
 * 
 */
public class TagUtils {
	
	/**
     * The Singleton instance.
     */
    private static final TagUtils instance = new TagUtils();
    
    /**
     * Maps lowercase JSP scope names to their PageContext integer constant
     * values.
     */
    private static final Map<String, Integer> scopes = new HashMap<String, Integer>();

    /**
     * Initialize the scope names map.
     */
    static {
        scopes.put("page", PageContext.PAGE_SCOPE);
        scopes.put("request", PageContext.REQUEST_SCOPE);
        scopes.put("session", PageContext.SESSION_SCOPE);
        scopes.put("application", PageContext.APPLICATION_SCOPE);
    }

    /**
     * Constructor for TagUtils.
     */
    protected TagUtils() {
        super();
    }

    /**
     * Returns the Singleton instance of TagUtils.
     */
    public static TagUtils getInstance() {
        return instance;
    }
    
    /**
     * Converts the scope name into its corresponding PageContext constant value.
     * @param scopeName Can be "page", "request", "session", or "application" in any
     * case.
     * @return The constant representing the scope (ie. PageContext.REQUEST_SCOPE).
     * @throws JspException if the scopeName is not a valid name.
     */
    public int getScope(String scopeName) throws JspException {
        Integer scope =  scopes.get(scopeName.toLowerCase());

        if (scope == null) {
            throw new JspException("读取参数错误！");
        }

        return scope.intValue();
    }
    
    /**
     * Locate and return the specified property of the specified bean, from
     * an optionally specified scope, in the specified page context.  If an
     * exception is thrown, it will have already been saved via a call to
     * <code>saveException()</code>.
     *
     * @param pageContext Page context to be searched
     * @param name Name of the bean to be retrieved
     * @param property Name of the property to be retrieved, or
     *  <code>null</code> to retrieve the bean itself
     * @param scope Scope to be searched (page, request, session, application)
     *  or <code>null</code> to use <code>findAttribute()</code> instead
     * @return property of specified JavaBean
     *
     * @exception JspException if an invalid scope name
     *  is requested
     * @exception JspException if the specified bean is not found
     * @exception JspException if accessing this property causes an
     *  IllegalAccessException, IllegalArgumentException,
     *  InvocationTargetException, or NoSuchMethodException
     */
    public Object lookup(
            PageContext pageContext,
            String name,
            String property,
            String scope)
            throws JspException {

        // Look up the requested bean, and return if requested
        Object bean = lookup(pageContext, name, scope);
        if (bean == null) {
        	throw new JspException("查找的bean不存在！");
        }

        if (property == null) {
            return bean;
        }

        // Locate and return the specified property
        try {
            return PropertyUtils.getProperty(bean, property);

        } catch (IllegalAccessException e) {
            throw new JspException("读取参数错误！");

        } catch (InvocationTargetException e) {
        	throw new JspException("读取参数错误！");

        } catch (NoSuchMethodException e) {
        	throw new JspException("读取参数错误！");
        }

    }

    
    /**
     * Locate and return the specified bean, from an optionally specified
     * scope, in the specified page context.  If no such bean is found,
     * return <code>null</code> instead.  If an exception is thrown, it will
     * have already been saved via a call to <code>saveException()</code>.
     *
     * @param pageContext Page context to be searched
     * @param name Name of the bean to be retrieved
     * @param scopeName Scope to be searched (page, request, session, application)
     *  or <code>null</code> to use <code>findAttribute()</code> instead
     * @return JavaBean in the specified page context
     * @exception JspException if an invalid scope name
     *  is requested
     */
    public Object lookup(PageContext pageContext, String name, String scopeName)
            throws JspException {

        if (scopeName == null) {
            return pageContext.findAttribute(name);
        }

        try {
            return pageContext.getAttribute(name, instance.getScope(scopeName));

        } catch (JspException e) {
            throw e;
        }
    }
    
    /**
     * Write the specified text as the response to the writer associated with
     * this page.  <strong>WARNING</strong> - If you are writing body content
     * from the <code>doAfterBody()</code> method of a custom tag class that
     * implements <code>BodyTag</code>, you should be calling
     * <code>writePrevious()</code> instead.
     *
     * @param pageContext The PageContext object for this page
     * @param text The text to be written
     *
     * @exception JspException if an input/output error occurs (already saved)
     */
    public void write(PageContext pageContext, String text)
            throws JspException {

        JspWriter writer = pageContext.getOut();

        try {
            writer.print(text);

        } catch (IOException e) {
            TagUtils.getInstance().saveException(pageContext, e);
            throw new JspException("IO错误！");
        }

    }
    
    /**
     * Save the specified exception as a request attribute for later use.
     *
     * @param pageContext The PageContext for the current page
     * @param exception The exception to be saved
     */
    public void saveException(PageContext pageContext, Throwable exception) {

        pageContext.setAttribute(
                "org.apache.struts.action.EXCEPTION",
                exception,
                PageContext.REQUEST_SCOPE);
    }
}






