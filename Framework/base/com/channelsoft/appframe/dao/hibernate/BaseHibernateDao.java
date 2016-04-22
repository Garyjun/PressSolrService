package com.channelsoft.appframe.dao.hibernate;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.channelsoft.appframe.dao.IBaseDao;
import com.channelsoft.appframe.dao.hibernate.query.PageableQueryWithHql;
import com.channelsoft.appframe.dao.hibernate.query.PageableQueryWithQueryCondition;
import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.DaoException;
import com.channelsoft.appframe.po.BaseHibernateObject;



/**
 * 基于Hibernate的DAO的基类,提供基础方法，包括：
 * 增加、修改、删除一个PO对象；
 * 分页查询功能，允许按一个指定字段排序，采用QBC，而不是HQL方式实现分页查询；
 * 注意：必须注入参数sessionFactory
 * @author 李 炜 </a>
 */
public class BaseHibernateDao extends HibernateDaoSupport implements IBaseDao{
	// 查询结果返回的最大行数的缺省值
	private final static int MAX_ROW = 1000;

	// 查询结果返回的最大行数，可以在SPRING中进行配置
	private int maxRow = MAX_ROW;

	protected final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 创建Hibernate的PO对象，并且持久化。捕获DataAccessException异常，转换为DaoException
	 * 
	 * @param obj  要持久化的PO对象
	 * @return 持久化后的PO对象，对于利用数据库提供主键生成的PO对象，可以通过返回值获得其主键OID
	 * @throws DaoException
	 */
	public BaseHibernateObject create(BaseHibernateObject obj) throws DaoException {
		save(obj);
		return obj;
	}
	/**
	 * 根据查询条件，返回可分页的列表对象
	 * @param poClass     PO的类
	 * @param conditions  查询条件
	 * 
	 * @return
	 * @throws DaoException
	 */
	public PageableResult query(Class poClass, QueryConditionList conditions) throws DaoException {
		PageableQueryWithQueryCondition pageableQueryWithQueryCondition = new  PageableQueryWithQueryCondition(getHibernateTemplate(),getSession());
		return pageableQueryWithQueryCondition.getPageableResult(conditions, getClassName(poClass));
	}
	/**
	 * 根据HQL查询语句，返回符合条件的PO对象列表；
	 * 注意：如果能够通过PO对象关联拼写查询条件，请直接使用query(Class poClass,QueryConditionList conditions)方法；
	 * 如果使用此方法，请注意保证HQL语法正确！
	 * @param queryHql  符合HQL规范的查询语句
	 * @return 符合条件的PO对象列表
	 * @throws DaoException
	 */
	public List query(String queryHql) throws DaoException{
		try {
			Query query = getSession().createQuery(queryHql);
			List currentPage = query.list();
			return currentPage;
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		}
	}
	
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
	public int executeUpdate(String executeHql, Map<String, Object> parameters) throws DaoException{
		try {
			Query query = getSession().createQuery(executeHql);
			
			if (parameters != null && !parameters.isEmpty()) {
				for (Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
					Map.Entry<String, Object> parameter = (Map.Entry<String, Object>) iterator.next();
					if (parameter.getValue() instanceof Collection){
						query.setParameterList(parameter.getKey(), (Collection) parameter.getValue());
					}else{
						query.setParameter(parameter.getKey(), parameter.getValue());
					}
				}
			}
			
			return query.executeUpdate();
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 带参数的hql查询,参数格式参考方法executeUpdate
	 * @param hql
	 * @param parameters
	 * @return
	 * @throws DaoException
	 */
	public List query(String hql,Map<String, Object> parameters) throws DaoException{
		try {
			Query query = getSession().createQuery(hql);
			
			if (parameters != null && !parameters.isEmpty()) {
				for (Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
					Map.Entry<String, Object> parameter = (Map.Entry<String, Object>) iterator.next();
					if (parameter.getValue() instanceof Collection){
						query.setParameterList(parameter.getKey(), (Collection) parameter.getValue());
					}else{
						query.setParameter(parameter.getKey(), parameter.getValue());
					}
				}
			}
			
			return query.list();
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 根据指定的PO的类，返回类名，需要去掉包名
	 * @param poClass
	 * @return
	 */
	protected String getClassName(Class poClass){
		if (poClass==null) {
			throw new InvalidParameterException("必须指定PO的类");
		}
		
		String className=poClass.getName();
		int index=className.lastIndexOf(".");
		return className.substring(index+1);
	}
	public BaseHibernateObject getByPk(Class poClass, Serializable oid) throws DaoException {
		return getObject(poClass, oid);
	}
	
	
	/**
	 * @see com.channelsoft.appframe.dao.IBaseDao#getMaxId(java.lang.Class, java.lang.String)
	 */
	public String getMaxId(Class poClass, String fieldName)
	{
		logger.debug("Runing BaseDao.queryMaxId()....");
		
		StringBuffer hql = new StringBuffer(100);
		String poName = poClass.getSimpleName();
		hql.append("select max(").append(poName).append(".").append(fieldName).append(") from ")
						.append(poName).append(" ").append(poName);
		
		try
		{
			Query query = getSession().createQuery(hql.toString());
			List result = query.list();
			String maxId = null;
			if (result.get(0) != null) {
				maxId = String.valueOf(result.get(0));
			}
			return maxId;
		}
		catch(DataAccessResourceFailureException exp)
		{
			logger.debug("get objcets count error.");
			throw new DaoException(buildError("get objcets count error.", exp));
		}
		catch(HibernateException exp)
		{
			logger.debug("get objcets count error.");
			throw new DaoException(buildError("get objcets count error.", exp));
		}
		catch(IllegalStateException exp)
		{
			logger.debug("get objcets count error.");
			throw new DaoException(buildError("get objcets count error.", exp));
		}
	}
	/**
	 * 根据数据库表主键，检索唯一的PO对象
	 * @param poClass
	 * @param oid
	 * @return  如果没有符合条件的PO对象，则抛出对象不存在的异常ObjectNotFoundException
	 * @throws DaoException
	 */
	public BaseHibernateObject load(Class poClass,Serializable oid)throws DaoException{
		if (logger.isDebugEnabled()) {
			logger.debug("检索主键为"+oid+"的PO对象"+poClass.getName());
		}
		try {
			return (BaseHibernateObject) getHibernateTemplate().load(poClass, oid);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 生成异常描述信息
	 * 
	 * @param msg
	 * @param e
	 * @return
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
	 * 保存PO对象
	 * 
	 * @param o
	 */
	public void saveOrUpdate(BaseHibernateObject o) {
		try {
			getHibernateTemplate().saveOrUpdate(o);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} 
	}

	/**
	 * 根据数据库表主键，检索唯一的PO对象
	 * @param oid
	 * @return  如果没有符合条件的PO对象，则返回null
	 * @throws DaoException
	 */
	protected BaseHibernateObject getObject(Class poClass, Serializable oid) {
		if (logger.isDebugEnabled()) {
			logger.debug("检索唯一的PO对象："+poClass.getName()+"[oid="+oid+"]");
		}
		try {
			return (BaseHibernateObject)getHibernateTemplate().get(poClass, oid);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} 
	}

	/**
	 * 查询PO的所有数据
	 * 
	 * @param poClass
	 *            PO类
	 * @return
	 */
	public List loadAll(Class poClass) {
		return getHibernateTemplate().loadAll(poClass);
	}
	/**
	 * 根据HQL返回PO对象列表
	 * 
	 * @param hql
	 * @return
	 */
	protected List getObjectList(String hql) {
		try {
            return getHibernateTemplate().find(hql);
        } catch (DataAccessException e) {
            logger.warn(e.getMessage());
            throw new DaoException(e.getMessage(), e);
        } catch (RuntimeException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} 
	}
	/**
	 * 根据主键删除PO对象
	 * 
	 * @param poClass  PO类
	 * @param oid 待删除对象的主键
	 */
	public void delete(Class poClass, Serializable oid) throws DaoException{
		if (logger.isDebugEnabled()) {
			logger.debug("根据主键["+oid+"]删除PO对象"+poClass.getName());
		}
		try {
			getHibernateTemplate().delete(getObject(poClass, oid));
        } catch (DataAccessException e) {
            logger.warn(e.getMessage());
            throw new DaoException(e.getMessage(), e);
        } catch (RuntimeException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} 
	}

	/**
	 * 删除Hibernate的PO对象，捕获DataAccessException异常，转换为DaoException
	 * 
	 * @param obj
	 */
	public void delete(BaseHibernateObject obj) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("删除Hibernate的PO对象："+obj.getObjectDescription());
		}
		try {
			getHibernateTemplate().delete(obj);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} 
	}

	/**
	 * 删除符合条件的PO对象，删除语句的条件比较难处理，所以采用先查询出符合条件的PO对象列表，再遍历删除的策略。
	 * 如果调用者能够自己拼写出删除的HQL，则推荐使用重载的接口delete(String deleteHql)
	 * @param poClass
	 * @param conditions
	 * @return    被删除的符合条件的PO对象个数
	 * @throws DaoException
	 */
	public int delete(Class poClass,QueryConditionList conditions) throws DaoException{
		if (logger.isDebugEnabled()) {
			logger.debug("删除符合条件的PO对象："+poClass.getName()+",条件为："+conditions);
		} 
		List poList = getLimitedListBy(poClass, conditions);
		for (Iterator iter = poList.iterator(); iter.hasNext();) {
			BaseHibernateObject element = (BaseHibernateObject) iter.next();
			delete(element);
		}
		return poList.size();
		 
	}
	/**
	 * 删除符合条件的PO对象，
	 * @param deleteHql  条件删除的HQL
	 * @return  被删除的符合条件的PO对象个数
	 * @throws DaoException
	 */
	//hibernat3已经不支持getHibernateTemplate().delete(deleteHql)该函数
//	public void delete(String deleteHql)throws DaoException{
//		if (logger.isDebugEnabled()) {
//			logger.debug("删除符合条件的PO对象,HQL="+deleteHql);
//		}
//		try {
//			getHibernateTemplate().delete(deleteHql);
//		} catch (DataAccessException e) {
//			logger.warn(e.getMessage());
//			throw new DaoException(e.getMessage(), e);
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//			throw new DaoException(e.getMessage(), e);
//		} 
//	}
	/**
	 * 更新Hibernate的PO对象，捕获DataAccessException异常，转换为DaoException
	 * 
	 * @param obj
	 */
	public void update(BaseHibernateObject obj) {
		if (logger.isDebugEnabled()) {
			logger.debug("更新Hibernate的PO对象："+obj.getObjectDescription());
		}
		try {
			getHibernateTemplate().update(obj);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} 
	}
	/**
	 * 保存Hibernate的PO对象，捕获DataAccessException异常，转换为DaoException
	 * 
	 * @param obj
	 */
	protected void save(BaseHibernateObject obj) throws DaoException{
		if (logger.isDebugEnabled()) {
			logger.debug("保存Hibernate的PO对象："+obj.getObjectDescription());
		}
		try {
			getHibernateTemplate().save(obj);
		}  catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}  catch (RuntimeException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} catch(Exception e){
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} catch(Throwable e){
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		}
	}
	/**
	 * 把PO对象从持久化状态变为游离状态，脱离HIBERNATE管理
	 * 
	 * @param obj
	 */
	protected void evict(BaseHibernateObject obj) {
		getHibernateTemplate().evict(obj);
	}

	/**
	 * 创建条件查询对象Criteria,并且限制返回数据行的最大数目为参数maxRow TODO QBC有问题，条件关联的时候，会出现重复的列，导致
	 * select * 会报异常： 未明确定义列 暂时改为不限制行数，如果需要限制行数，直接调用createCriteria(Class clss,
	 * int maxRow)
	 * 
	 * @param clss
	 * @return
	 * @throws DaoException
	 */
	private Criteria createCriteria(Class clss) throws DaoException {
		try {
			Criteria criteria = getSession().createCriteria(clss);
			return criteria;
		} catch (DataAccessResourceFailureException e) {
			throw new DaoException(e);
		} catch (HibernateException e) {
			throw new DaoException(e);
		} catch (IllegalStateException e) {
			throw new DaoException(e);
		}

		// return createCriteria(clss,getMaxRow());
	}

	/**
	 * 创建条件查询对象Criteria,并且限制返回数据行的最大数目
	 * 
	 * @param clss
	 * @param maxRow
	 * @return
	 * @throws DaoException
	 */
	private Criteria createCriteria(Class clss, int maxRow) throws DaoException {
		try {
			Criteria criteria = getSession().createCriteria(clss);
			criteria.setMaxResults(maxRow);
			return criteria;
		} catch (DataAccessResourceFailureException e) {
			throw new DaoException(e);
		} catch (HibernateException e) {
			throw new DaoException(e);
		} catch (IllegalStateException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * 根据查询条件，检索指定的PO对象，返回的列表最多maxRow行
	 * @param poClass
	 * @param conditions
	 * @param maxRow   最大行数
	 * @return
	 * @throws DaoException
	 */
	public List getLimitedListBy(Class poClass, QueryConditionList conditions, int maxRow)
			throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("对"+poClass.getName()+"进行条件查询，条件为["+conditions+"],最大数据行数为"+maxRow);	
		}	
		try {
			Criteria criteria = createCriteria(poClass, maxRow);
			if (conditions!=null) {
				conditions.build(criteria);
			}	
			buildOrder(conditions, criteria);
			return criteria.list();
		} catch (DataAccessException e) {
			throw new DaoException(e.getMessage(), e);
		} catch (HibernateException e) {
			throw new DaoException(e.getMessage(), e);
		}
	}

	protected void buildOrder(QueryConditionList conditions, Criteria criteria) {
		if (conditions==null) {
			return;
		}
		if (StringUtils.isNotEmpty(conditions.getSortProperty())) {
			Order order;
			if (conditions.isSortByAsc()) {
				order = Order.asc(conditions.getSortProperty());
			} else {
				order = Order.desc(conditions.getSortProperty());
			}
			criteria.addOrder(order);
		}
	}

	protected int getMaxRow() {
		return maxRow;
	}

	/**
	 * 设置最大行数，目前，限制返回行数，可能导致异常，在Hibernate3中可能会解决此问题
	 * 
	 * @param maxRow
	 */
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

	/**
	 * 检查是否有满足条件的数据
	 * @param poClass
	 * @param conditions
	 * @return
	 * @throws DaoException
	 */
	public boolean exist(Class poClass,QueryConditionList conditions) throws DaoException{
		if (logger.isDebugEnabled()) {
			logger.debug("检查Hibernate的PO对象："+poClass+"是否存在"+",条件为："+conditions);
		}
		PageableQueryWithQueryCondition pageableQueryWithQueryCondition = new  PageableQueryWithQueryCondition(getHibernateTemplate(),getSession());
		int size = pageableQueryWithQueryCondition.getMaxRowCount(conditions, getClassName(poClass));
		if (logger.isDebugEnabled()) {
			logger.debug("满足条件的PO数量为" + size);
		}
		return size!=0; 
	}
	
	/**
	 * 检查是否没有满足条件的数据
	 * @param poClass
	 * @param conditions
	 * @return
	 * @throws DaoException
	 * @author 刘彦军
	 */
	public boolean notExist(Class poClass,QueryConditionList conditions) throws DaoException{
		if (logger.isDebugEnabled()) {
			logger.debug("检查Hibernate的PO对象："+poClass+"是否不存在"+",条件为："+conditions);
		}
		PageableQueryWithQueryCondition pageableQueryWithQueryCondition = new  PageableQueryWithQueryCondition(getHibernateTemplate(),getSession());
		int size = pageableQueryWithQueryCondition.getMaxRowCount(conditions, getClassName(poClass));
		if (logger.isDebugEnabled()) {
			logger.debug("满足条件的PO数量为" + size);
		}
		return size==0; 
	}
	
	/**
	 * 检查是否存在满足主键条件的PO对象
	 * @param poClass
	 * @param oid
	 * @return
	 * @throws DaoException
	 */
	public boolean exist(Class poClass, Serializable oid) throws DaoException { 
		if (logger.isDebugEnabled()) {
			logger.debug("检查主键为["+oid+"]的PO对象："+poClass+"是否存在");
		}
		return getByPk(poClass, oid)!=null;
	}
	/**
	 * 检查是否满足主键条件的PO对象不存在
	 * @param poClass
	 * @param oid
	 * @return
	 * @throws DaoException
	 */
	public boolean notExist(Class poClass, Serializable oid) throws DaoException { 
		if (logger.isDebugEnabled()) {
			logger.debug("检查主键为["+oid+"]的PO对象："+poClass+"是否不存在");
		}
		return getByPk(poClass, oid)==null;
	}
	public PageableResult getPageableResult(QueryConditionList conditions,
			String poName) throws DaoException { 
		PageableQueryWithQueryCondition pageableQueryWithQueryCondition = new  PageableQueryWithQueryCondition(getHibernateTemplate(),getSession());
		return pageableQueryWithQueryCondition.getPageableResult(conditions, poName);
	}
	/**
	 * 根据查询条件，检索指定的PO对象，返回的列表不限制行数
	 * 
	 * @param poClass
	 * @param conditions
	 * @return
	 * @throws DaoException
	 */
	public List getLimitedListBy(Class poClass, QueryConditionList conditions)
			throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("对"+poClass.getName()+"进行不限制行数的条件查询，条件为:"+conditions);
		}
		try {
			Criteria criteria = createCriteria(poClass);
			if (conditions!=null) {
				conditions.build(criteria);
			}			
			buildOrder(conditions, criteria);
			return criteria.list();
		} catch (DataAccessException e) {
			throw new DaoException(e.getMessage(), e);
		} catch (HibernateException e) {
			throw new DaoException(e.getMessage(), e);
		}
	}
	/**
	 * 迫使 session 的缓存与数据库同步
	 * 当缓存中的 对象 与数据库不同步，且 想取得数据库中的数据的时候，可调用此方法
	 * 慎用！！！
	 * @param obj
	 * @throws DaoException
	 * @author: rui.zh
	 * time: 2006-7-26 上午10:29:56
	 */
	public void refresh(BaseHibernateObject obj) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("使session缓存中的对象 " + obj.getClass().getName()
					+ " 与数据库同步");
		}
		try {
			getHibernateTemplate().refresh(obj);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		} 
	}
	
	/**
	 * 根据HQL语句，返回可分页的PO对象列表；主要供页面查询使用
	 * @param HQL语句
	 * 
	 * @return 可分页的PO对象列表
	 * @throws DaoException
	 */
	public PageableResult pageableQueryWithHql(QueryConditionList conditions) throws DaoException{
		if (logger.isDebugEnabled()) {
			logger.debug("根据HQL语句，返回可分页的列表对象");
		}
		PageableQueryWithHql pageableQueryWithHql = new PageableQueryWithHql(getHibernateTemplate(),getSession());
		return pageableQueryWithHql.getPageableResult(conditions);
	}
}