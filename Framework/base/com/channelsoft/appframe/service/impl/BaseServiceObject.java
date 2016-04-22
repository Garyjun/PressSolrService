package com.channelsoft.appframe.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.channelsoft.appframe.common.BaseObject;
import com.channelsoft.appframe.dao.IBaseDao;
import com.channelsoft.appframe.dao.IBaseJdbcDao;
import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.DaoException;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.exception.dao.ObjectNotFoundException;
import com.channelsoft.appframe.exception.service.ObjectAlreadyExistException;
import com.channelsoft.appframe.po.BaseHibernateObject;
import com.channelsoft.appframe.service.IBaseOperateService;
import com.channelsoft.appframe.service.IBaseQueryService;
import com.channelsoft.appframe.utils.BeanFactoryUtil;
import com.channelsoft.appframe.utils.ConvertUtil;

/**
 * Service层的基类
 * 
 * @author liwei
 * 
 * 增加支持多catalog;注入baseQueryService。
 * @author Fuwenbin 2008-06-27
 */
public class BaseServiceObject extends BaseObject implements
		IBaseOperateService {
	private IBaseDao baseDao;
	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	protected IBaseDao getBaseDao() {
		if(baseDao == null) {
			baseDao = (IBaseDao)BeanFactoryUtil.getBean("baseDao");
		}
		
		return baseDao;
	}
	
	private IBaseJdbcDao baseJdbcDao;
	public void setBaseJdbcDao(IBaseJdbcDao baseJdbcDao) {
		this.baseJdbcDao = baseJdbcDao;
	}
	
	protected IBaseJdbcDao getBaseJdbcDao() {
		if(baseJdbcDao == null) {
			baseJdbcDao = (IBaseJdbcDao)BeanFactoryUtil.getBean("baseJdbcDao");
		}
		
		return baseJdbcDao;
	}
	
	private IBaseQueryService baseQueryService;
	private IBaseQueryService getBaseQueryService() {
		return baseQueryService;
	}

	public void setBaseQueryService(IBaseQueryService baseQueryService) {
		this.baseQueryService = baseQueryService;
	}

	/**
	 * 生成异常描述信息
	 */
	protected String buildError(String msg, Exception e) {
		StringBuffer error = new StringBuffer(500);
		error.append(msg).append(":");
		if (e != null) {
			error.append(e.getMessage());
		}
		return error.toString();
	}

	/**
	 * @see com.qnuse.usboss.permission.service.IBaseService#addBHObject(com.qnuse.qframe.base.po.BaseHibernateObject)
	 */
	public BaseHibernateObject doCreateObject(
			BaseHibernateObject mockBusinessObject) {
		logger.debug("进入 doCreateObject():"
				+ mockBusinessObject.getEntityDescription());
		try {
			checkDuplicate(mockBusinessObject);
			processBeforCreate(mockBusinessObject);
			populateForCreate(mockBusinessObject);
			BaseHibernateObject obj = getBaseDao().create(mockBusinessObject);
			logger.debug("离开 doCreateObject()"
					+ mockBusinessObject.getEntityDescription());
			processAfterCreate(mockBusinessObject);
			return obj;
		} catch (DaoException e) {
			String error = buildError("创建"
					+ mockBusinessObject.getEntityDescription() + "业务对象异常", e);
			logger.warn(error);
			throw new ServiceException(error, e);
		}
	}

	/**
	 * 检查该数据对象在物理上和逻辑上是否存在，这里只提供根据数据库表的OID来查存的功能， 子类根据需要，可以覆盖此方法，提供基于业务逻辑的查存
	 * 
	 * @param businessObject
	 * @throws ObjectAlreadyExistException
	 *             表示业务对象已经存在
	 */
	protected void checkDuplicate(BaseHibernateObject businessObject) {
		if (businessObject.getObjectID() != null
				&& getBaseDao().exist(businessObject.getClass(),
						businessObject.getObjectID())) {
			throw new ObjectAlreadyExistException(businessObject
					.getEntityDescription()
					+ "[" + businessObject.getObjectID() + "]已经存在: ",
					businessObject);
		}
		checkLogicDuplicate(businessObject);
	}

	/**
	 * 检查该数据对象在逻辑上是否存在，子类需要在这里检查逻辑重复的数据
	 * 
	 * @param businessObject
	 * @throws ObjectAlreadyExistException
	 */
	protected void checkLogicDuplicate(BaseHibernateObject businessObject) {
	}

	/**
	 * 检查指定的业务对象是否存在
	 * 
	 * @param mockBusinessObject
	 * 
	 * @throws ObjectNotFoundException
	 *             表示业务对象不存在
	 */
	protected BaseHibernateObject checkExist(
			BaseHibernateObject mockBusinessObject) {
		BaseHibernateObject realBusinessObject = getBaseDao()
				.getByPk(mockBusinessObject.getClass(),
						mockBusinessObject.getObjectID());
		if (realBusinessObject == null) {
			throw new ObjectNotFoundException(mockBusinessObject
					.getEntityDescription()
					+ "[" + mockBusinessObject.getObjectID() + "]不存在: ");
		}
		return realBusinessObject;
	}

	/**
	 * 物理删除业务逻辑实体，需要检查该数据对象在逻辑上是否存在，如果不存在，需要抛出业务异常，提示用户
	 * 
	 * @param mockBusinessObject
	 *            假的业务对象，可能只有业务对象的主键
	 * @return
	 * @throws ServiceException
	 */
	public BaseHibernateObject doDeleteObject(
			BaseHibernateObject mockBusinessObject) {
		logger.debug("进入 doDeleteObject():"
				+ mockBusinessObject.getEntityDescription());
		try {
			BaseHibernateObject realBusinessObject = checkExist(mockBusinessObject);
			processBeforDelete(mockBusinessObject);
			processDelete(realBusinessObject);
			// 20061205 liwei:
			// 把真实的po属性全部复制到假po中，以防止ａｃｔｉｏｎ中没有使用返回的真实po拼写界面描述信息而产生的ｎｕｌｌ
			ConvertUtil.convert(realBusinessObject, mockBusinessObject);
			processAfterDelete(mockBusinessObject);
			logger.debug("离开 doDeleteObject()"
					+ mockBusinessObject.getEntityDescription());
			return realBusinessObject;
		} catch (DaoException e) {
			String error = buildError("删除"
					+ mockBusinessObject.getEntityDescription() + "["
					+ mockBusinessObject.getObjectID() + "]异常", e);
			logger.warn(error);
			throw new ServiceException(error, e);
		}
	}
	
	public List doDeleteObjects(Class poClass, Serializable[] oids) {
		List ret = new ArrayList();
        if (logger.isDebugEnabled()) {
			logger.debug("进入 doDeleteObjects()");
        } 
        if (oids == null || oids.length == 0) {
        	return ret;
        }
        
        int count = 0;
        for (int i = 0, j = oids.length; i < j; i++) {
    		BaseHibernateObject realBusinessObject = getBaseDao().getByPk(
					poClass, oids[i]);
    		doDeleteObject(realBusinessObject);
    		ret.add(realBusinessObject);
            count++;
        }
        
        if (logger.isDebugEnabled()) {
        	logger.debug("共删除" + count + "个对象。");
        }
        
        return ret;
	}
	
	/**
	 * 执行删除数据，缺省的是物理删除；如果子类需要做逻辑删除，需要覆盖此方法
	 * 
	 * @param realBusinessObject
	 */
	protected void processDelete(BaseHibernateObject realBusinessObject) {
		getBaseDao().delete(realBusinessObject);
	}

	/*
	 * @see 修改一个PO对象
	 * @see com.qnuse.usboss.permission.service.IBaseService#updateBHObject(com.qnuse.qframe.base.po.BaseHibernateObject)
	 */
	public void doUpdateObject(BaseHibernateObject mockBusinessObject) {
		logger.debug("进入 doUpdateObject():"
				+ mockBusinessObject.getEntityDescription());
		try {
			BaseHibernateObject realBusinessObject = checkExist(mockBusinessObject);
			processBeforUpdate(mockBusinessObject);
			populateForUpdate(mockBusinessObject, realBusinessObject);
			getBaseDao().update(realBusinessObject);
			ConvertUtil.convert(realBusinessObject, mockBusinessObject);
			processAfterUpdate(mockBusinessObject);
		} catch (DaoException e) {
			e.printStackTrace();
			String error = buildError("修改"
					+ mockBusinessObject.getEntityDescription() + "["
					+ mockBusinessObject.getObjectID() + "]异常", e);
			logger.warn(error);
			throw new ServiceException(error, e);
		}
		logger.debug("离开 doUpdateObject()"
				+ mockBusinessObject.getEntityDescription());
	}

	/**
	 * 在创建数据之前，进行预处理，缺省的，不做特殊处理，子类根据需要覆盖此方法
	 * 
	 * @param mockBusinessObject
	 * @throws ServiceException
	 */
	protected void processBeforCreate(BaseHibernateObject mockBusinessObject) {

	}

	/**
	 * 在创建数据之后，进行后处理，缺省的，不做特殊处理，子类根据需要覆盖此方法
	 * 
	 * @param mockBusinessObject
	 * @throws ServiceException
	 */
	protected void processAfterCreate(BaseHibernateObject mockBusinessObject) {

	}

	/**
	 * 在删除数据之前，进行预处理，缺省的，不做特殊处理，子类根据需要覆盖此方法
	 * 
	 * @param mockBusinessObject
	 * @throws ServiceException
	 */
	protected void processBeforDelete(BaseHibernateObject mockBusinessObject) {

	}

	/**
	 * 在删除数据之后，进行后处理，缺省的，不做特殊处理，子类根据需要覆盖此方法
	 * 
	 * @param mockBusinessObject
	 * @throws ServiceException
	 */
	protected void processAfterDelete(BaseHibernateObject mockBusinessObject) {

	}

	/**
	 * 在更新数据之前，进行预处理，缺省的，不做特殊处理，子类根据需要覆盖此方法
	 * 
	 * @param mockBusinessObject
	 * @throws ServiceException
	 */
	protected void processBeforUpdate(BaseHibernateObject mockBusinessObject) {

	}

	/**
	 * 在更新数据之后，进行后处理，缺省的，不做特殊处理，子类根据需要覆盖此方法
	 * 
	 * @param mockBusinessObject
	 * @throws ServiceException
	 */
	protected void processAfterUpdate(BaseHibernateObject mockBusinessObject) {

	}

	/**
	 * 修改业务对象时，需要通过此方法把发生变更的属性复制到真正的业务对象中，并且保存真正的业务对象，而不能保存假的业务对象
	 * 子类需要覆盖此方法，只复制允许修改的属性。
	 * 
	 * @param mockBusinessObject
	 *            假的业务对象，用于保存发生变更的属性
	 * @param realBusinessObject
	 * @return
	 */
	protected BaseHibernateObject populateForUpdate(
			BaseHibernateObject mockBusinessObject,
			BaseHibernateObject realBusinessObject) {
		ConvertUtil.convert(mockBusinessObject, realBusinessObject);
		return realBusinessObject;
	}

	/**
	 * 创建业务对象时，需要通过此方法对引用的业务对象进行组装; 缺省的,不做组装.
	 * 子类根据需要,决定是否覆盖此方法；如果需要做po组装，请注意检查被引用的po是否存在！
	 * 
	 * @param mockBusinessObject
	 *            组装前的业务对象
	 * @return 组装后的业务对象
	 */
	protected BaseHibernateObject populateForCreate(
			BaseHibernateObject mockBusinessObject) {
		return mockBusinessObject;
	}
	
	/**
	 * @see com.channelsoft.appframe.service.IBaseOperateService#exist(java.lang.Class, com.channelsoft.appframe.dao.query.QueryConditionList)
	 */
	public boolean exist(Class poClass, QueryConditionList conditions)
			throws ServiceException { 
		return getBaseQueryService().exist(poClass, conditions);
	}

	/**
	 * @see com.channelsoft.appframe.service.IBaseOperateService#exist(java.lang.Class, java.io.Serializable)
	 */
	public boolean exist(Class poClass, Serializable oid)
			throws ServiceException { 
		return getBaseQueryService().exist(poClass, oid);
	}

	/**
	 * @see com.channelsoft.appframe.service.IBaseOperateService#query(java.lang.Class, com.channelsoft.appframe.dao.query.QueryConditionList)
	 */
	public PageableResult query(Class poClass, QueryConditionList conditions)
			throws ServiceException { 
		return getBaseQueryService().query(poClass, conditions);
	}
	
	/**
	 * @see com.channelsoft.appframe.service.IBaseOperateService#pageableQueryWithHql(com.channelsoft.appframe.dao.query.QueryConditionList)
	 */
	public PageableResult pageableQueryWithHql(QueryConditionList conditions)
			throws ServiceException {
		return getBaseQueryService().pageableQueryWithHql(conditions);
	}

	/**
	 * @see com.channelsoft.appframe.service.IBaseOperateService#query(java.lang.String)
	 */
	public List query(String queryHql) throws ServiceException { 
		return getBaseQueryService().query(queryHql);
	}

	/**
	 * @see com.channelsoft.appframe.service.IBaseOperateService#getListBy(java.lang.Class, com.channelsoft.appframe.dao.query.QueryConditionList)
	 */
	public List getListBy(Class poClass, QueryConditionList conditions)
			throws ServiceException { 
		return getBaseQueryService().getListBy(poClass, conditions);
	}

	/**
	 * @see com.channelsoft.appframe.service.IBaseOperateService#load(java.lang.Class, java.io.Serializable)
	 */
	public BaseHibernateObject load(Class poClass, Serializable oid)
			throws ServiceException { 
		return getBaseQueryService().load(poClass, oid);
	}
	
	/**
	 * @see com.channelsoft.appframe.service.IBaseOperateService#getByPk(java.lang.Class, java.io.Serializable)
	 */
	public BaseHibernateObject getByPk(Class poClass, Serializable oid)
			throws ServiceException {
		return getBaseQueryService().getByPk(poClass, oid);
	}

	/**
	 * @see com.channelsoft.appframe.service.IBaseOperateService#loadAll(java.lang.Class)
	 */
	public List loadAll(Class poClass) throws ServiceException { 
		return getBaseQueryService().loadAll(poClass);
	}
}
