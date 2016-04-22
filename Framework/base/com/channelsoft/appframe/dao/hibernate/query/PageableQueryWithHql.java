
package com.channelsoft.appframe.dao.hibernate.query;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.DaoException;

/**
 * <dl>
 * <dt>PageableQueryWithHql</dt>
 * <dd>Description:覆盖qframe jar包中的相同类，解决
 * 如select distinct时，getMaxRowCount()错误的问题</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Apr 3, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class PageableQueryWithHql{
	protected static final Log logger = LogFactory.getLog(PageableQueryWithHql.class);
	private HibernateTemplate hibernateTemplate;
	private Session session;
	public PageableQueryWithHql(HibernateTemplate hibernateTemplate,Session session) {
		if (hibernateTemplate==null) {
			throw new DaoException("PageableQueryWithHql构造失败:HibernateTemplate 为空!");
		}
		if (session==null) {
			throw new DaoException("PageableQueryWithHql构造失败:session 为空!");
		}
		setHibernateTemplate(hibernateTemplate);
		setSession(session);
	}
	/**
	 * 返回可分页的列表
	 * 
	 * @param conditions
	 *            查询条件
	 * @param poName
	 *            持久层对象的类名，不需要带包名
	 * @return 符合条件的数据行数，以及当前页的数据列表
	 * @throws DaoException
	 */

	public PageableResult getPageableResult(QueryConditionList conditions) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("分页查询PO对象，查询条件为："+conditions);
		}
		try { 
			int size = getMaxRowCount(conditions);
			PageableResult pageableResult = new PageableResult();
			pageableResult.setMaxRowCount(size);
			if (size > 0) {
				List currentPage = getCurrentPage(conditions);
				pageableResult.setCurrentPage(currentPage);
			}
			return pageableResult;
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
	 * 查询当前页的数据,附带排序功能
	 * 
	 * @param conditions
	 * @return
	 * @throws HibernateException
	 */
	protected List getCurrentPage(QueryConditionList conditions) throws HibernateException { 
		Query query = getSession().createQuery(conditions.getQueryScript());
		query.setFirstResult(conditions.getStartIndex());
		query.setMaxResults(conditions.getPageSize()); 
		bindParameters(query, conditions.getQueryScriptParams());
		
		return query.list();
	}
	
	/**
	 * 绑定所有的参数，这里使用JDBC风格的问号(?)参数进行绑定的方法
	 * 
	 * @param query
	 * @param values
	 */
	protected void bindParameters(Query query, List<Object> params) {
		if(params == null || params.size() == 0){
			return;
		}
		
		Object[] values = params.toArray();
		for (int i = 0; i < values.length; i++) {
			Object obj = values[i];
			// 不同于JDBC，Hibernate对参数从0开始计数。
			bindParameter(query, i, obj);
		}
	}
	
	/**
	 * 绑定指定的参数 目前，只支持对String、Date、Integer类型的参数进行处理
	 * 
	 * @param query
	 * @param i
	 * @param obj
	 */
	protected void bindParameter(Query query, int i, Object obj) {
		if (String.class.isInstance(obj)) {
			query.setString(i, (String) obj);
			return;
		}
		if (Date.class.isInstance(obj)) {
//			query.setDate(i, (Date) obj);
			query.setTimestamp(i, (Date)obj);
			return;
		}
		if (Integer.class.isInstance(obj)) {
			query.setInteger(i, ((Integer) obj).intValue());
			return;
		}
		if (Long.class.isInstance(obj)) {
			query.setLong(i, ((Long) obj).longValue());
			return;
		}
		if (Byte.class.isInstance(obj)) {
			query.setByte(i, ((Byte)obj).byteValue());
			return;
		}
		if (Boolean.class.isInstance(obj)) {
			query.setBoolean(i, ((Boolean)obj).booleanValue());
			return;
		}
		if (BigDecimal.class.isInstance(obj)){
			query.setBigDecimal(i, (BigDecimal)obj);
			return;
		}
		throw new InvalidParameterException("参数类型不匹配");
	}

	/**
	 * 利用Query对象，返回符合条件的数据行数
	 * @param conditions 
	 * 
	 * @return
	 * @throws DataAccessResourceFailureException
	 * @throws HibernateException
	 * @throws IllegalStateException
	 */
	protected int getMaxRowCount(QueryConditionList conditions) throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		Query query = null;
		if(StringUtils.isBlank(conditions.getQueryResultCountHql())){
			String hql = conditions.getQueryScript();
			int startIndex = hql.indexOf("from");
			String str = hql.substring(startIndex);		
			StringBuffer sql = new StringBuffer(30 + conditions.getQueryScript().length());
			sql.append(" select count(*) ").append(str); 
			query = getSession().createQuery(sql.toString()); 
		}else{
			query = getSession().createQuery(conditions.getQueryResultCountHql()); 
		}
		bindParameters(query, conditions.getQueryScriptParams());
		List ret = query.list();
		return Integer.valueOf(ret.get(0).toString());
	}
	
	protected HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	private void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	private Session getSession() {
		return session;
	}
	private void setSession(Session session) {
		this.session = session;
	}
}
