package com.channelsoft.appframe.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.DaoException;
import com.channelsoft.appframe.po.BaseHibernateObject;


/**
 * DAO的公共操作接口，对数据库表提供标准的增删改查功能，
 * 
 * @author liwei
 * 
 */
public interface IBaseDao {
	/**
	 * 创建Hibernate的PO对象，并且持久化。捕获DataAccessException异常，转换为DaoException
	 * 
	 * @param obj  要持久化的PO对象
	 * @return 持久化后的PO对象，对于利用数据库提供主键生成的PO对象，可以通过返回值获得其主键OID
	 * @throws DaoException
	 */
	public BaseHibernateObject create(BaseHibernateObject obj) throws DaoException;

	/**
	 * 更新Hibernate的PO对象，捕获DataAccessException异常，转换为DaoException
	 * 
	 * @param obj
	 * @throws DaoException
	 */
	public void update(BaseHibernateObject obj) throws DaoException;

	/**
	 * 物理删除Hibernate的PO对象，捕获DataAccessException异常，转换为DaoException
	 * 
	 * @param obj
	 * @throws DaoException
	 */
	public void delete(BaseHibernateObject obj) throws DaoException;

	/**
	 * 删除符合条件的PO对象
	 * 
	 * @param poClass
	 * @param conditions
	 * @return
	 * @throws DaoException
	 */
	public int delete(Class poClass,QueryConditionList conditions) throws DaoException;
	/**
	 * 根据主键删除PO对象
	 * 
	 * @param poClass  PO类
	 * @param oid 待删除对象的主键
	 */
	public void delete(Class poClass, Serializable oid) throws DaoException;
	/**
	 * 删除符合条件的PO对象，
	 * @param deleteHql  条件删除的HQL
	 * @return  被删除的符合条件的PO对象个数
	 * @throws DaoException
	 */
//	public void delete(String deleteHql)throws DaoException;
	/**
	 * 根据查询条件，返回可分页的PO对象列表；主要供页面查询使用
	 * @param poClass     PO的类
	 * @param conditions  查询条件
	 * 
	 * @return 可分页的PO对象列表
	 * @throws DaoException
	 */
	public PageableResult query(Class poClass,
			QueryConditionList conditions) throws DaoException;
	/**
	 * 根据HQL查询语句，返回符合条件的PO对象列表；
	 * 注意：如果能够通过PO对象关联拼写查询条件，请直接使用query(Class poClass,QueryConditionList conditions)方法；
	 * 如果使用此方法，请注意保证HQL语法正确！
	 * @param queryHql  符合HQL规范的查询语句
	 * @return 符合条件的PO对象列表
	 * @throws DaoException
	 */
	public List query(String queryHql) throws DaoException;
	
	/**
	 * 带参数的hql查询,参数格式参考方法executeUpdate
	 * @param queryHql
	 * @param parameters
	 * @return
	 * @throws DaoException
	 */
	public List query(String queryHql,Map<String, Object> parameters) throws DaoException;
	
	/**
	 * 支持参数的hql批量操作,可批量执行update、delete和inserte操作。
	 * @param executeHql hql脚本，参数变量名为规则为":变量名"，如：":newName"
	 * @param parameters 参数集合，元素key为参数变量名，value为参数变量值
	 * @return 操作的记录条数
	 * @throws DaoException
	 *
	 * @author Fuwenbin
	 * @date Jan 6, 2009 5:17:03 PM 
	 */
	public int executeUpdate(String executeHql, Map<String, Object> parameters) throws DaoException;
		
	/**
	 * 加载指定表的所有数据；本方法谨慎使用，建议只对常量表使用！
	 * @param poClass
	 * @return
	 */
	public List loadAll(Class poClass);
	/**
	 * 根据查询条件，检索指定的PO对象，返回的列表不限制行数；主要供业务逻辑处理中使用；
	 * 
	 * @param poClass
	 * @param conditions
	 * @return
	 * @throws DaoException
	 */
	public List getLimitedListBy(Class poClass, QueryConditionList conditions)
			throws DaoException;
	/**
	 * 根据查询条件，检索指定的PO对象，返回的列表最多maxRow行；
	 * @param poClass
	 * @param conditions
	 * @param maxRow   最大行数
	 * @return
	 * @throws DaoException
	 * @deprecated  如果需要限制返回的数据行数,推荐使用分页查询
	 */
	public List getLimitedListBy(Class poClass, QueryConditionList conditions, int maxRow)
			throws DaoException ;
	/**
	 * 根据数据库表主键，检索唯一的PO对象
	 * @param oid
	 * @return  如果没有符合条件的PO对象，则返回null
	 * @throws DaoException
	 */
	public BaseHibernateObject getByPk(Class poClass,Serializable oid)throws DaoException;
	
	/**
	 * 获取最大ID值
	 * @param poClass PO类
	 * @param fieldName ID字段名称
	 * @return
	 *
	 * @author 李大鹏
	 */
	public String getMaxId(Class poClass, String fieldName);
	
	/**
	 * 根据数据库表主键，检索唯一的PO对象
	 * @param poClass
	 * @param oid
	 * @return  如果没有符合条件的PO对象，则抛出对象不存在的异常ObjectNotFoundException
	 * @throws DaoException
	 */
	public BaseHibernateObject load(Class poClass,Serializable oid)throws DaoException;
	/**
	 * 检查是否有满足条件的数据
	 * @param poClass
	 * @param conditions
	 * @return
	 * @throws DaoException
	 */
	public boolean exist(Class poClass,QueryConditionList conditions) throws DaoException;
	/**
	 * 检查是否没有满足条件的数据
	 * @param poClass
	 * @param conditions
	 * @return
	 * @throws DaoException
	 * @author 刘彦军
	 */
	public boolean notExist(Class poClass,QueryConditionList conditions) throws DaoException;
	/**
	 * 检查是否存在满足主键条件的PO对象
	 * @param poClass
	 * @param oid
	 * @return
	 * @throws DaoException
	 */
	public boolean exist(Class poClass,Serializable oid) throws DaoException;
	/**
	 * 检查是否满足主键条件的PO对象不存在
	 * @param poClass
	 * @param oid
	 * @return
	 * @throws DaoException
	 */
	public boolean notExist(Class poClass,Serializable oid) throws DaoException;
	/**
	 * 迫使 session 的缓存与数据库同步
	 * 当缓存中的 对象 与数据库不同步，且 想取得数据库中的数据的时候，可调用此方法
	 * 慎用！！！
	 * @param obj
	 * @throws DaoException
	 * @author: rui.zh
	 * time: 2006-7-26 上午10:29:56
	 */
	public void refresh(BaseHibernateObject obj) throws DaoException;
	
	/**
	 * 根据HQL语句，返回可分页的PO对象列表；主要供页面查询使用
	 * @param conditions
	 * 
	 * @return 可分页的PO对象列表
	 * @throws DaoException
	 */
	public PageableResult pageableQueryWithHql(QueryConditionList conditions) throws DaoException;
	
	public void saveOrUpdate(BaseHibernateObject o) ;
}
