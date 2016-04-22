package com.channelsoft.appframe.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.channelsoft.appframe.exception.BaseRuntimeException;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.utils.BeanFactoryUtil;
import com.channelsoft.appframe.utils.DateConverter;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;



/**
 * <dl>
 * <dt>BaseAction</dt>
 * <dd>Description:实现action公共方法的封装</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 20, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public abstract class BaseAction extends ActionSupport {
	private static Long defaultLong = null;
	static {
		ConvertUtils.register(new DateConverter(), Date.class);
		ConvertUtils.register(new DateConverter(), String.class);
		ConvertUtils.register(new LongConverter(defaultLong), Long.class);
		ConvertUtils.register(new IntegerConverter(defaultLong), Integer.class);
	}
	
	protected transient final Log logger = LogFactory.getLog(getClass());

	protected ActionContext getContext() {
		/*ServletActionContext is subclasses ActionContext which provides access to 
		 things like the action name, value stack, etc. 
		 This class adds access to web objects like servlet parameters, request attributes and 
		 things like the HTTP session*/
		return ServletActionContext.getContext();
		
		//return ActionContext.getContext();
	}
	
	protected Map getSession() {
		return getContext().getSession();
	}
	
	protected Map getApplication() {
		return getContext().getApplication();
	}
	
	public String getActionName() {
		return getContext().getName();
	}
	
	public String getNamespace() {
		return ServletActionContext.getActionMapping().getNamespace();
	}
	
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	
	/**
	 * 根据参数name获取bean实例
	 * 
	 * @param name
	 * @return
	 * @throws ServiceException
	 *             如果没有找到业务处理对象
	 */
	protected Object getBean(String name) {
		return BeanFactoryUtil.getBean(name); 
	}
	
	/**
	 * 保存错误信息
	 * @param e
	 * @param msg
	 */
	protected void addError(Exception e, String msg) {
		StringBuffer error = new StringBuffer();
		if (StringUtils.isNotBlank(msg)) {
			error.append(msg);
		}
		if (e != null) {
			error.append(e.getMessage());
		}
		logger.debug("错误：" + error.toString(), e);
		addActionError("错误：" + error.toString());
	}

	protected void addError(String msg) {
		addError(new BaseRuntimeException(""), msg);
	}
	
	protected void addError(Exception e) {
		if (e instanceof org.springframework.transaction.CannotCreateTransactionException) {
			addError("数据库连接故障！请联系系统管理员。");
			return;
		}
		addError(new BaseRuntimeException(""), e.getMessage());
	}

	/**
	 * 保存提示信息
	 * @param msg
	 */
	protected void addMsg(String msg) {
		addActionMessage("信息：" + msg);
	}
	
	/**
	 * 生成异常描述信息
	 * 
	 * @param e
	 * @param msg
	 * @return
	 */
	protected String buildError(Exception e, String msg) {
		StringBuffer error = new StringBuffer();
		error.append(msg).append(":");
		if (e != null) {
			error.append(e.getMessage());
		}
		return error.toString();
	}
	
	private final static String ACTION_MESSAGE_KEY = "action_message_key";
	private boolean hasMsg;
	public void appendMsg(String msg) {
		if(StringUtils.isNotBlank(msg)){
			getSession().put(ACTION_MESSAGE_KEY, msg);
		}
	}
	
	protected void initActionMsg(){
		String msg = (String)getSession().get(ACTION_MESSAGE_KEY);
		if(StringUtils.isNotBlank(msg)){
			addMsg(msg);
		}
	}
	
	protected void cleanActionMsg(){
		getSession().remove(ACTION_MESSAGE_KEY);
		clearErrorsAndMessages();
	}

	public boolean hasMsg() {
		return hasMsg;
	}

	public void setHasMsg(boolean hasMsg) {
		this.hasMsg = hasMsg;
	}
	
	/**
	 * 输出结果字符串
	 * @param result
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Mar 1, 2010 11:28:47 AM 
	 */
//	protected String outputResult(String result) {
//		try {
//			getResponse().setContentType("Content-type:html/text;charset=utf-8");
////			getResponse().getOutputStream().print(result);
//			getResponse().getOutputStream().print(new String(result.getBytes(), "ISO-8859-1"));
//		} catch (IOException e) {
//			String errInfo = "接口输出数据失败：";
//			logger.error(errInfo, e);
//			throw new ServiceException(errInfo , e);
//		}
//		
//		return null;
//	}
	public String outputResult(String result) {  
		getResponse().setCharacterEncoding("utf-8"); 
		getResponse().setContentType("text/plain;charset=utf-8");
	    PrintWriter pw=null;  
	    try {  
	        pw = getResponse().getWriter();  
	        pw.write(result);  
	    } catch (IOException e) {  
	    	String errInfo = "接口输出数据失败：";
	    	logger.error(errInfo, e);
	    	throw new ServiceException(errInfo , e);
	    }
        pw.flush();  
        pw.close();
        return null;
	}
}
