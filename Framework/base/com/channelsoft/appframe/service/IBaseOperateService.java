package com.channelsoft.appframe.service;

import java.io.Serializable;
import java.util.List;

import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.po.BaseHibernateObject;

/**
 * 业务逻辑的抽象接口，包括对业务逻辑实体的维护接口（创建、变更、删除、查询）
 */
public interface IBaseOperateService extends IBaseQueryService {
	/**
	 * 创建业务逻辑实体，需要检查该数据对象在逻辑上是否存在，不允许存在逻辑上重复的数据
	 * 
	 * @param mockBusinessObject  假的业务对象，用于保存业务属性
	 * @throws ServiceException
	 */
	public BaseHibernateObject doCreateObject(BaseHibernateObject mockBusinessObject) throws ServiceException;
	/**
	 * 更新业务逻辑实体，需要检查该数据对象在物理上是否存在，如果不存在，需要抛出业务异常，提示用户
	 * @param mockBusinessObject 假的业务对象，用于保存发生变更的属性
	 * @throws ServiceException
	 */
	public void doUpdateObject(BaseHibernateObject mockBusinessObject) throws ServiceException;
	/**
	 * 物理删除业务逻辑实体，需要检查该数据对象在物理上是否存在，如果不存在，需要抛出业务异常，提示用户 
	 * @param mockBusinessObject  假的业务对象，可能只有业务对象的主键
	 * @return
	 * @throws ServiceException
	 */
	public BaseHibernateObject doDeleteObject(BaseHibernateObject mockBusinessObject)throws ServiceException;
	/**
	 * 物理批量删除业务逻辑实体，需要检查该数据对象在物理上是否存在，如果不存在，需要抛出业务异常，提示用户 
	 * @param mockBusinessObject  假的业务对象，可能只有业务对象的主键
	 * @return 成功删除的对象
	 * @throws ServiceException
	 */
	public List doDeleteObjects(Class poClass, Serializable[] oids) throws ServiceException;
	
//	/**
//	 * 检查是否有满足条件的数据
//	 * @param poClass     待检查的业务类
//	 * @param conditions  检查条件
//	 * @return            true表示存在满足条件的业务对象；false表示不存在满足条件的业务对象
//	 * @throws ServiceException  出现业务操作异常
//	 */
//	public boolean exist(Class poClass, QueryConditionList conditions);
//	/**
//	 * 检查是否存在满足主键条件的PO对象
//	 * @param poClass     待检查的业务类
//	 * @param oid         待检查的业务对象主键
//	 * @return            true表示存在满足条件的业务对象；false表示不存在满足条件的业务对象
//	 * @throws ServiceException  出现业务操作异常
//	 */
//	public boolean exist(Class poClass, Serializable oid);
//	/**
//	 * 根据查询条件，返回可分页的PO对象列表；主要供页面查询使用
//	 * @param poClass     PO的类
//	 * @param conditions  查询条件 
//	 * @return 可分页的PO对象列表
//	 * @throws ServiceException
//	 */
//	public PageableResult query(Class poClass, QueryConditionList conditions);
//	
//	/**
//	 * 根据查hql脚本查询，返回可分页的PO对象列表；主要供页面查询使用
//	 * @param conditions  封装了hql脚本及参数值的对象 
//	 * @return 可分页的PO对象列表
//	 *
//	 * @author Fuwenbin
//	 * @date Jan 13, 2009 9:37:23 AM 
//	 */
//	public PageableResult pageableQueryWithHql(QueryConditionList conditions);
//	
//	/**
//	 * 根据HQL查询语句，返回符合条件的PO对象列表；
//	 * 注意：如果能够通过PO对象关联拼写查询条件，请直接使用query(Class poClass,QueryConditionList conditions)方法；
//	 * 如果使用此方法，请注意保证HQL语法正确！
//	 * @param queryHql  符合HQL规范的查询语句
//	 * @return 符合条件的PO对象列表
//	 * @throws ServiceException
//	 */
//	public List query(String queryHql);
//	/**
//	 * 根据查询条件，检索指定的PO对象，返回的列表不限制行数；主要供业务逻辑处理中使用；
//	 * 
//	 * @param poClass    待检查的业务类
//	 * @param conditions 检查条件
//	 * @return           符合条件的PO对象列表
//	 * @throws ServiceException
//	 */
//	public List getListBy(Class poClass, QueryConditionList conditions);
//
//	/**
//	 * 根据数据库表主键，检索唯一的PO对象，如果没有符合条件的PO对象，则抛出对象不存在的异常ObjectNotFoundException
//	 * 该方法对对象提供了懒加载处理。
//	 * @param poClass  待检索的业务类
//	 * @param oid      待检索的业务对象主键
//	 * @return  如果没有符合条件的PO对象，则抛出对象不存在的异常ObjectNotFoundException
//	 * @throws ServiceException
//	 */
//	public BaseHibernateObject load(Class poClass, Serializable oid);
//	
//	/**
//	 * 根据数据库表主键，检索唯一的PO对象。
//	 * @param poClass  待检索的业务类
//	 * @param oid      待检索的业务对象主键
//	 * @return  如果没有符合条件的PO对象
//	 * @throws ServiceException
//	 */
//	public BaseHibernateObject getByPk(Class poClass, Serializable oid);
//	
//	/**
//	 * 加载指定表的所有数据；本方法谨慎使用，建议只对常量表使用！
//	 * @param poClass  待加载的业务类
//	 * @return
//	 * @throws ServiceException
//	 */
//	public List loadAll(Class poClass);
}
