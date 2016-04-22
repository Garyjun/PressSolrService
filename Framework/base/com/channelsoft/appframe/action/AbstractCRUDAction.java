package com.channelsoft.appframe.action;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.appframe.dao.query.PageableInfo;
import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.po.BaseHibernateObject;
import com.channelsoft.appframe.service.IBaseOperateService;
import com.opensymphony.xwork2.Preparable;

/**
 * <dl>
 * <dt>AbstractCRUDAction</dt>
 * <dd>Description:提供增、删、改、查的action基类</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jul 2, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public abstract class AbstractCRUDAction extends BaseAction implements
		Preparable {
	protected final static String GOTO_PAGE_ACTION_PREFIX = "goto";
	protected final static String GOTO_RESULT = "goto";
	protected final static String REDIRECT_RESULT = "redirect";
	protected final static String JSON_RESULT = "json";
	protected final static String RESPONSE_RESULT = "responseResult";

	public final static String METHOD_SEPARATOR = "-";
//	protected final static String GOTO_ACTION = "goto";
//	protected final static String GOTO_LIST_ACTION = "gotolist";
//	protected final static String GOTO_LOOKUP_ACTION = "gotolookup";
//	protected final static String GOTO_RELATION_LIST_ACTION = "gotorelationlist";
//	protected final static String ADD_ACTION = "add";
//	protected final static String UPDATE_ACTION = "update";
//	protected final static String DELETE_ACTION = "delete";
	
	protected final static int RESPONSE_SUCCESS = 1;
	protected final static int RESPONSE_FAILUER = 0;

	private IBaseOperateService baseService;
	private PageableInfo pageAbleInfo = new PageableInfo();
	private String[] toDelete;
	
	private int responseResult = RESPONSE_FAILUER;
	private String responseMessage = "";

	/**
	 * 目标页面跳转参数。参数值可以是以下形式： 
	 * 1.带路径的jsp url，自动跳转到指定jsp；
	 * 2.jsp文件的名称（不包含扩展名），跳转时自动查找当前租户包下对应对象jsp目录下的指定文件；
	 * 3.带".action"的url，自动跳转到该action; 4.关键字"json"，返回json结果。
	 */
	private String destination;

	/**
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public final void prepare() throws Exception {		
		Serializable objId = getObjectId(); 
		if (objId == null && (getCurrentObj() != null 
				&& getCurrentObj().getObjectID() != null)) {
			objId = getCurrentObj().getObjectID();
		}
		
		try {
			if (objId != null) {
				BaseHibernateObject realObject = getBaseService().getByPk(
						getEntityClass(), objId);
				if (realObject != null) {
					setCurrentObj(realObject);
				}
			} 
		} catch (ServiceException e) {
			logger.warn("装载对象[" + getEntityClass().getSimpleName() + "]不存在。");
		}
		
		prepareExt();
	}

	/**
	 * 缺省的跳转方法
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		return SUCCESS;
	}

	/**
	 * 该方法提供通用的页面跳转处理。可以处理一下几种action模式：
	 * 1.goto-{objectName}-{tenant}.action?destination=destinationpage,直接跳转到目标页面destinationpage；
	 * 2.goto{actionExt}-{objectName}-{tenant}.action?destination=destinationpage,该模式下会自动检查并调要
	 * 子类扩展的方法，方法名约定为goto{actionExt}(),并且如果destination参数不为空则跳转到该目标页面。如果goto{actionExt}
	 * 的destination参数为空，则会自动跳转到目标页面{objectName}{actionExt}.jsp。
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author Fuwenbin
	 * @date Nov 10, 2008 9:26:19 AM
	 */
//	public String gotoPage() throws Exception {
//		if (!isSimpleGoto(getActionName())) {
//			String methodName = StringUtils.substringBefore(getActionName(),
//					METHOD_SEPARATOR);
//			try {
//				Method method = this.getClass().getMethod(
//						StringUtils.isBlank(methodName) ? getActionName() : methodName);
//				method.invoke(this);
//			} catch (NoSuchMethodException e) {
//				logger.debug("gotoPage:未找到需要执行的方法。");
//			} catch (SecurityException e) {
//				logger.debug("gotoPage:未找到需要执行的方法。");
//			}
//
//			if (StringUtils.isBlank(destination)) {
//				setDefaultDestination();
//			}
//		}
//
//		if (StringUtils.isBlank(destination)) {
//			throw new ServiceException("跳转页面失败：跳转页面未被赋值。");
//		}
//
//		return gotoResult();
//	}

	/**
	 * 创建对象
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author Fuwenbin
	 * @date Nov 10, 2008 9:26:19 AM
	 */
	public final String addObj() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("进入 AbstractCRUDAction.add()");
		}

		try {
			if (getCurrentObj() == null) {
				addError("创建对象失败：所创建对象为空！");
				setAddInputDestination();
				return gotoInputResult();
			}

			setDefaultInfoForAdd();

			add();

			addMsg("创建" + getCurrentObj().getEntityDescription() + "成功！");

			// 设置缺省的返回结果
			if (StringUtils.isBlank(destination)) {
				setDestination(getDefaultListAction());
			}

			return gotoResult();
		} catch (ServiceException se) {
			addError(se, "创建" + getCurrentObj().getEntityDescription() + "失败：");
			setAddInputDestination();
			
			return gotoInputResult();
		}
	}

	/**
	 * 修改对象
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author Fuwenbin
	 * @date Nov 10, 2008 9:26:19 AM
	 */
	public final String updateObj() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("进入 AbstractCRUDAction.update()");
		}

		try {
			if (getCurrentObj() == null) {
				addError("修改对象失败：修改对象为空！");
				setUpdateInputDestination();
				return gotoInputResult();
			}

			setDefaultInfoForUpdate();

			update();

			addMsg("修改" + getCurrentObj().getEntityDescription() + "成功！");

			// 设置缺省的返回结果
			if (StringUtils.isBlank(destination)) {
				setDestination(getDefaultListAction());
			}

			return gotoResult();
		} catch (ServiceException se) {
			addError(se, "修改" + getCurrentObj().getEntityDescription() + "失败：");
			setUpdateInputDestination();
			return gotoInputResult();
		}
	}

	/**
	 * 删除对象
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author Fuwenbin
	 * @date Nov 10, 2008 9:26:19 AM
	 */
	public final String deleteObj() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("进入 AbstractCRUDAction.deleteObj()");
		}
		if (toDelete == null || toDelete.length == 0) {
			setResponseMessage("未删除任何对象！");
		}

		try {
			delete(convertIdType(toDelete));

			// 设置缺省的返回结果
//			if (StringUtils.isBlank(destination)) {
//				setDestination(DELETE_RESULT);
//			}
			setResponseResult(RESPONSE_SUCCESS);
		} catch (ServiceException e) {
			logger.debug(e.getMessage());
			addError(e, "删除数据异常");
			
			setResponseMessage(e.getMessage());
			setResponseResult(RESPONSE_FAILUER);
		} catch (Exception exp) {
			logger.debug(exp.getMessage());
			addError(exp, "删除数据异常");
			
			setResponseMessage(exp.getMessage());
			setResponseResult(RESPONSE_FAILUER);
		}

		return gotoResult();
	}

	/**
	 * 查询业务对象
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author Fuwenbin
	 * @date Nov 10, 2008 9:26:19 AM
	 */
	public final String queryObj() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("进入 AbstractCRUDAction.queryObj()");
		}

		QueryConditionList conditionList = new QueryConditionList();
		setDefaultQueryCondition(conditionList);

		buildQueryCondition(conditionList);

		try {
			if (StringUtils.isNotBlank(getPageAbleInfo().getSort())) {
				conditionList.setSortProperty(getPageAbleInfo().getSort());
				if (StringUtils.equals(getPageAbleInfo().getDir(), "asc")) {
					conditionList.setSortMode(QueryConditionList.SORT_MODE_ASC);
				} else {
					conditionList.setSortMode(QueryConditionList.SORT_MODE_DESC);
				}
			}

			conditionList.setStartIndex(getPageAbleInfo().getStartIndex());
			conditionList.setPageSize(getPageAbleInfo().getPageSize());

			PageableResult pageableResult = query(conditionList);
			getPageAbleInfo().setTotalRecords(pageableResult.getMaxRowCount());
			getPageAbleInfo().setRecords(pageableResult.getCurrentPage());
		} catch (ServiceException e) {
			logger.debug(e.getMessage());
			addError(e, "查询列表数据异常");

			e.printStackTrace();
		} catch (Exception exp) {
			logger.debug(exp.getMessage());
			addError(exp, "查询列表数据异常");

			exp.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("离开 AbstractCRUDAction.queryObj()");
		}

		return SUCCESS;
	}

	/**
	 * 查看详细信息
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author Fuwenbin
	 * @date Nov 10, 2008 9:26:19 AM
	 */
	public final String detail() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("进入 AbstractCRUDAction.detail()");
		}

		populateForDetail();

		// 设置缺省的返回结果
		if (StringUtils.isBlank(destination)) {
			if (getCurrentObj() != null) {
				setDefaultDestination();
			} else {
				throw new ServiceException("查看的数据不存在！");
			}
		}

		return gotoResult();
	}

	/**
	 * 通过在action名中指定需要执行的方法名，调用该通用方法去执行子类的扩展实现。
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author Fuwenbin
	 * @date Dec 12, 2008 10:10:05 AM
	 */
	public final String executeMethod() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("进入 AbstractCRUDAction.detail()");
		}

		String methodName = StringUtils.substringBetween(getActionName(),
				"execute", METHOD_SEPARATOR);
		methodName = methodName.substring(0, 1).toLowerCase()
				+ methodName.substring(1);
		try {
			Method method = this.getClass().getMethod(methodName);
			return (String) method.invoke(this);
		} catch (NoSuchMethodException e) {
			String error = "executeMethod:未找到需要执行的方法。";
			logger.debug(error);
			throw new ServiceException(error);
		} catch (Exception e) {
			String error = buildError(e, "executeMethod执行失败：");
			logger.debug(error);
			throw new ServiceException(error);
		}
	}

	protected abstract Class getEntityClass();

	protected abstract Serializable getObjectId();

	public abstract BaseHibernateObject getCurrentObj();

	protected abstract void setCurrentObj(BaseHibernateObject obj);

	protected void add() {
		getBaseService().doCreateObject(getCurrentObj());
	}

	protected void setAddInputDestination() {
	}

	protected void setDefaultInfoForAdd() {
	}

	protected void prepareExt() {
	}
	
	protected void update() {
		getBaseService().doUpdateObject(getCurrentObj());
	}

	protected void setUpdateInputDestination() {
	}

	protected void setDefaultInfoForUpdate() {
	}
	
	protected Serializable[] convertIdType(String[] objs) {
		Long[] objIds = new Long[objs.length];
		for (int i = 0; i < objs.length; i++) {
			objIds[i] = Long.parseLong(objs[i]);
		}
		
		return objIds;
	}

	protected void delete(Serializable[] objs) {
		getBaseService().doDeleteObjects(getEntityClass(), objs);
	}

	protected PageableResult query(QueryConditionList conditionList) {
		return getBaseService().query(getEntityClass(), conditionList);
	}

	/**
	 * 设置缺省的查询条件，如： 1.是自定义对象，只能查询删除标记为未删除的记录； 2.设置关联列表数据查询的外键查询条件。
	 * 
	 * @param conditionList
	 * 
	 * @author Fuwenbin
	 * @date Dec 2, 2008 3:25:53 PM
	 */
	protected void setDefaultQueryCondition(QueryConditionList conditionList) {
	}

	/**
	 * 组装缺省的查询条件对象
	 * 
	 * @param conditionList
	 * 
	 * @author Fuwenbin
	 * @date Dec 17, 2008 11:45:46 AM
	 */
	protected void buildQueryCondition(QueryConditionList conditionList) {
	}
	
	protected void populateForDetail() {
	};

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String[] getToDelete() {
		return toDelete;
	}

	public void setToDelete(String[] toDelete) {
		this.toDelete = toDelete;
	}
	
	public int getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(int responseResult) {
		this.responseResult = responseResult;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	protected IBaseOperateService getBaseService() {
		return baseService;
	}

	public void setBaseService(IBaseOperateService baseService) {
		this.baseService = baseService;
	}

	protected PageableInfo getPageAbleInfo() {
		return pageAbleInfo;
	}

	public void setPageAbleInfo(PageableInfo pageAbleInfo) {
		this.pageAbleInfo = pageAbleInfo;
	}
	
//	private boolean isSimpleGoto(String actionName) {
//		return StringUtils.equals("goto", StringUtils.substringBefore(
//				actionName, METHOD_SEPARATOR));
//	}

	private void setDefaultDestination() {
		setDestination("");
//		String optName = "";
//		if (getActionName().startsWith("goto")) {
//			optName = StringUtils.substringBetween(getActionName(), "goto",
//					METHOD_SEPARATOR);
//		} else {
//			String actionNameHead = StringUtils.substringBefore(
//					getActionName(), METHOD_SEPARATOR);
//			optName = actionNameHead.substring(0, 1).toUpperCase()
//					+ actionNameHead.substring(1);
//		}
//		
//		StringBuffer destBuf = new StringBuffer(getObjectName());
//		destBuf.append(optName);
//
//		setDestination(destBuf.toString());
	}

	/**
	 * 获取缺省的列表页面action
	 * 
	 * @return
	 * 
	 * @author Fuwenbin
	 * @date Dec 12, 2008 2:19:56 PM
	 */
	protected final String getDefaultListAction() {
		StringBuffer defListAction = new StringBuffer(GOTO_PAGE_ACTION_PREFIX);
		defListAction.append("List");

		return defListAction.append(".action").toString();
	}

	protected final String gotoResult() {
		if (StringUtils.isBlank(getDestination())) {
			setDefaultDestination();
		}
		
		if (destination.indexOf(".action") != -1) {
			return REDIRECT_RESULT;
		}

		if (destination.indexOf("/") != -1 || destination.indexOf("\\") != -1) {
			return GOTO_RESULT;
		}

		if (StringUtils.equals(destination, JSON_RESULT)) {
			return JSON_RESULT;
		}
		
		if (StringUtils.equals(destination, RESPONSE_RESULT)) {
			return RESPONSE_RESULT;
		}
		
		if (StringUtils.isNotBlank(destination)) {
			return destination;
		}
		
//		String destExt = findDestinationExt();
//		if (StringUtils.isNotBlank(destExt)) {
//			setDestination(destExt);
//			return GOTO_RESULT;
//		}

		return SUCCESS;
	}

	protected final String gotoInputResult() {
		if (StringUtils.isBlank(getDestination())) {
			setDefaultDestination();
		}
		
		return INPUT;
	}
}
