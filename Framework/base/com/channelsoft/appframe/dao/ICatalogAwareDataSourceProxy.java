package com.channelsoft.appframe.dao;

import com.channelsoft.appframe.exception.ServiceException;

/**
 * <dl>
 * <dt>ICatalogAwareDataSourceProxy</dt>
 * <dd>Description:获取hibernate dao的代理接口</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jun 27, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public interface ICatalogAwareDataSourceProxy {
	/**
	 * 返回当前用户所属catalog对应的dao
	 * @return
	 * @throws ServiceException
	 */
	public IBaseDao getBaseDao()throws ServiceException;
	
	/**
	 * 根据catalogName返回对应的dao
	 * @param catalogName
	 * @return
	 * @throws ServiceException
	 */
	public IBaseDao getBaseDao(String catalogName)throws ServiceException;
	
	/**
	 * 返回当前用户所属catalog对应的jdbc dao
	 * @return
	 * @throws ServiceException
	 */
	public IBaseJdbcDao getBaseJdbcDao()throws ServiceException;
	
	/**
	 * 根据catalogName返回对应的jdbc dao
	 * @param catalogName
	 * @return
	 * @throws ServiceException
	 */
	public IBaseJdbcDao getBaseJdbcDao(String catalogName)throws ServiceException;
}
