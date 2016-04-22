/**
 * 
 */
package com.channelsoft.appframe.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.channelsoft.appframe.dao.IBaseDao;
import com.channelsoft.appframe.dao.IBaseJdbcDao;
import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.DaoException;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.exception.service.ParameterErrorException;
import com.channelsoft.appframe.po.BaseHibernateObject;
import com.channelsoft.appframe.service.IBaseQueryService;
import com.channelsoft.appframe.utils.BeanFactoryUtil;

/**
 * 数据库查询接口的SERVICE层封装，简单的代理IBaseDao的接口调用
 * @author liwei
 *
 * 增加支持多catalog
 * @author Fuwenbin 2008-06-30
 */
public class BaseQueryService implements IBaseQueryService {
	protected final Log logger = LogFactory.getLog(getClass());

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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qnuse.qframe.base.service.IBaseQueryService#exist(java.lang.Class,
	 *      com.qnuse.qframe.base.query.QueryConditionList)
	 */
	public boolean exist(Class poClass, QueryConditionList conditions)
			throws ServiceException { 
		try {
			return getBaseDao().exist(poClass, conditions);
		} catch (DaoException e) { 
			logger.warn(buildError("检查符合条件的业务对象["+getPoName(poClass)+"]是否存在，异常", e));
			throw new ServiceException(e.getMessage(),e);
		}
	}

	/* (non-Javadoc)
	 * @see com.qnuse.qframe.base.service.IBaseQueryService#exioist(java.lang.Class, java.io.Serializable)
	 */
	public boolean exist(Class poClass, Serializable oid)
			throws ServiceException { 
		try{
			return getBaseDao().exist(poClass, oid);
		} catch (DaoException e) { 
			logger.warn(buildError("检查业务对象["+getPoName(poClass)+",oid="+oid+"]是否存在，异常", e));
			throw new ServiceException(e.getMessage(),e);
		}
	}

	/* (non-Javadoc)
	 * @see com.qnuse.qframe.base.service.IBaseQueryService#query(java.lang.Class, com.qnuse.qframe.base.query.QueryConditionList)
	 */
	public PageableResult query(Class poClass, QueryConditionList conditions)
			throws ServiceException { 
		try{
			return getBaseDao().query(poClass, conditions);
		} catch (DaoException e) { 
			logger.warn(buildError("分页查询符合条件的业务对象["+getPoName(poClass)+"]，异常", e));
			throw new ServiceException(e.getMessage(),e);
		}
	}
	
	/**
	 * @see com.channelsoft.appframe.service.IBaseQueryService#pageableQueryWithHql(com.channelsoft.appframe.dao.query.QueryConditionList)
	 */
	public PageableResult pageableQueryWithHql(QueryConditionList conditions)
			throws ServiceException {
		try{
			return getBaseDao().pageableQueryWithHql(conditions);
		} catch (DaoException e) { 
			logger.warn(buildError("分页查询业务对象异常", e));
			throw new ServiceException(e.getMessage(),e);
		}
	}

	/* (non-Javadoc)
	 * @see com.qnuse.qframe.base.service.IBaseQueryService#query(java.lang.String)
	 */
	public List query(String queryHql) throws ServiceException { 
		try{
			return  getBaseDao().query(queryHql);
		} catch (DaoException e) { 
			logger.warn(buildError("查询符合条件的业务对象[hql="+queryHql+"]，异常", e));
			throw new ServiceException(e.getMessage(),e);
		}
	}

	/* (non-Javadoc)
	 * @see com.qnuse.qframe.base.service.IBaseQueryService#getListBy(java.lang.Class, com.qnuse.qframe.base.query.QueryConditionList)
	 */
	public List getListBy(Class poClass, QueryConditionList conditions)
			throws ServiceException { 
		try{
			return  getBaseDao().getLimitedListBy(poClass, conditions);
		} catch (DaoException e) { 
			logger.warn(buildError("查询符合条件的业务对象["+getPoName(poClass)+"]列表，异常", e));
			throw new ServiceException(e.getMessage(),e);
		}
	}

	/* (non-Javadoc)
	 * @see com.qnuse.qframe.base.service.IBaseQueryService#load(java.lang.Class, java.io.Serializable)
	 */
	public BaseHibernateObject load(Class poClass, Serializable oid)
			throws ServiceException { 
		try{
			return  getBaseDao().load(poClass, oid);
		} catch (DaoException e) { 
			logger.warn(buildError("加载符合条件的业务对象["+getPoName(poClass)+"，oid="+oid+"]，异常", e));
			throw new ServiceException(e.getMessage(),e);
		}
	}
	
	/**
	 * @see com.channelsoft.appframe.service.IBaseQueryService#getByPk(java.lang.Class, java.io.Serializable)
	 */
	public BaseHibernateObject getByPk(Class poClass, Serializable oid)
			throws ServiceException {
		try {
			return getBaseDao().getByPk(poClass, oid);
		} catch (DaoException e) {
			logger.warn(buildError("加载符合条件的业务对象[" + getPoName(poClass)
					+ "，oid=" + oid + "]，异常", e));
			throw new ServiceException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see com.qnuse.qframe.base.service.IBaseQueryService#loadAll(java.lang.Class)
	 */
	public List loadAll(Class poClass) throws ServiceException { 
		try{
			return  getBaseDao().loadAll(poClass);
		} catch (DaoException e) { 
			logger.warn(buildError("加载业务对象["+getPoName(poClass)+"]，异常", e));
			throw new ServiceException(e.getMessage(),e);
		}
	}

	protected String buildError(String msg,Exception e) {
        StringBuffer error = new StringBuffer(500);
        error.append(msg).append(":");
        if (e!=null) {
            error.append(e.getMessage());    
        }        
        return error.toString();
    }
	protected String getPoName(Class poClass) throws ParameterErrorException{
		if (poClass==null) {
			throw new ParameterErrorException("您没有指定需要查询的PO类");
		}
		return poClass.getSimpleName();
	}
}
