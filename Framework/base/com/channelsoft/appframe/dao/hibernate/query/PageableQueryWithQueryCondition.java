/**
 * 
 */
package com.channelsoft.appframe.dao.hibernate.query;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.channelsoft.appframe.dao.query.Operator;
import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionItem;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.dao.query.QuerySortItem;
import com.channelsoft.appframe.exception.DaoException;
import com.channelsoft.appframe.exception.ServiceException;

/**
 * 分页查询的方法对象,根据查询条件,实现数据库分页的功能;
 * @author liwei
 *
 */
public class PageableQueryWithQueryCondition {
	protected static final Log logger = LogFactory.getLog(PageableQueryWithQueryCondition.class);
	protected HibernateTemplate hibernateTemplate;
	protected Session session;
	//下面的变量为临时变量，对于方法对象而言，这么做是可以接受的
	protected List<Object> valueList = new ArrayList<Object>();//保存查询条件的值 
	protected StringBuffer where = new StringBuffer();//保存查询条件的where部分
	public PageableQueryWithQueryCondition(HibernateTemplate hibernateTemplate,Session session) {
		if (hibernateTemplate==null) {
			throw new DaoException("PageableQueryWithQueryCondition构造失败:HibernateTemplate 为空!");
		}
		if (session==null) {
			throw new DaoException("PageableQueryWithQueryCondition构造失败:session 为空!");
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
	 * @throws ServiceException
	 */

	public PageableResult getPageableResult(QueryConditionList conditions,
			String poName) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("分页查询PO对象："+poName+"，查询条件为"+conditions);
		}
		try {  
			int size = getMaxRowCount(conditions, poName);
			PageableResult pageableResult = new PageableResult();
			pageableResult.setMaxRowCount(size);
			if (size > 0) {
				List currentPage = getCurrentPage(conditions, where.toString(), valueList.toArray(), poName,
						getAlias(poName));
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
	 * 统计满足条件的PO对象个数
	 * @param conditions
	 * @param poName
	 * @return
	 * @throws HibernateException
	 */
	public int getMaxRowCount(QueryConditionList conditions, String poName) throws DaoException {
		buildWhereClause(conditions, valueList, getAlias(poName)); 
		try {
			return getMaxRowCount(where.toString(), valueList.toArray(), poName, getAlias(poName));
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
	 * @param where
	 * @param values
	 * @param poName
	 * @param alias
	 *            别名
	 * @return
	 * @throws HibernateException
	 */
	protected List getCurrentPage(QueryConditionList conditions, String where,
			Object[] values, String poName, String alias) throws HibernateException {
		StringBuffer sql = new StringBuffer(100 + where.length());
		sql.append("from ").append(poName).append(" as ");
		sql.append(alias).append(" where 1=1 ").append(where);
		sql.append(buildOrderByClause(conditions, alias));

		logger.debug("sql: "+sql.toString());
		
		Query query = getSession().createQuery(sql.toString());
		query.setFirstResult(conditions.getStartIndex());
		query.setMaxResults(conditions.getPageSize());
		//	
		bindParameters(query, values);
		List currentPage = query.list();
		return currentPage;
	}
	/**
	 * 构建排序语句,如果没有提供排序字段,则使用本来的排序语句,否则,按照指定的单一字段进行排序
	 * 
	 * @param conditions
	 * @param alias
	 *            别名
	 * @return
	 */
	protected String buildOrderByClause(QueryConditionList conditions, String alias) {
		if (conditions==null) {
			return buildOriginalOrderByClause();
		}
		if (StringUtils.isNotEmpty(conditions.getSortProperty())) {
			StringBuffer orderBy = new StringBuffer(100);
			orderBy.append(" order by ").append(alias).append(".");
			orderBy.append(conditions.getSortProperty());
			orderBy.append(conditions.getSortModeValue());
			return orderBy.toString();
		}
		//王志明 2006-12-30 实现多排序条件
		List<QuerySortItem> list = conditions.getSortList();
		if(!list.isEmpty()){
			StringBuffer orderBy = new StringBuffer(100);
			orderBy.append(" order by ");
			for(QuerySortItem item : list){
				String fieldName = item.getFieldName();
				String sortMode = item.getSortMode();
				orderBy.append(alias).append(".").append(fieldName).append(" ");
				orderBy.append(sortMode).append(",");
			}
			String order = orderBy.toString();
			return order.substring(0,order.length() - 1);
		}
		return buildOriginalOrderByClause();
	}

	/**
	 * 在没有指定排序字段的情况下,按照这里设置的方式进行排序,缺省不排序
	 * 
	 * @return
	 */
	protected String buildOriginalOrderByClause() {
		return "";
	}

	/**
	 * 根据poName转换为别名
	 * 
	 * @param poName
	 *            poName不能为空
	 * @return poName的首字母转为小写
	 */
	protected String getAlias(String poName) {
		if (StringUtils.isEmpty(poName)) {
			throw new InvalidParameterException("根据poName转换为别名，poName不能为空");
		}

		char c = poName.charAt(0);
		if (Character.isLowerCase(c)) {
			return poName;
		}
		StringBuffer alias = new StringBuffer(poName);
		alias.setCharAt(0, Character.toLowerCase(c));
		return alias.toString();
	}
	/**
	 * 根据查询条件列表，构建查询条件语句
	 * 
	 * @param conditions
	 * @param values
	 * @param alias
	 *            别名
	 * @return
	 */
	protected void buildWhereClause(QueryConditionList conditions, List<Object> values,
			String alias) {
		where = new StringBuffer(2000);
		if (conditions==null) { 
			return;
		}
		
		for (Iterator iter = conditions.iterator(); iter.hasNext();) {
			QueryConditionItem element = (QueryConditionItem) iter.next();
			if (element.getOperator() == Operator.ISNULL
					|| element.getOperator()== Operator.ISNOTNULL) {
				where.append(" and  ").append(alias).append(".");
				where.append(element.getFieldName()).append(" ");
				where.append(element.getOperator().getOerator()).append(" ");
				continue;
			}
			
			if(element.getOperator()==Operator.IN) {
				processIn(values, alias, where, element);
				continue;
			}
			
			if (element.isEmptyValue()) {
				continue;
			}
			
			where.append(" and  ").append(alias).append(".");
			where.append(element.getFieldName()).append(" ");
			where.append(element.getOperator().getOerator()).append(" ?");
			values.add(element.getCalValue());
		} 
		return;
	}

	private void processIn(List<Object> values, String alias, StringBuffer whereClause, QueryConditionItem element) {
		//TODO 把查询对象都转为String，现在程序只能对String的值进行in查询
		String valueStr = element.getValue().toString();
		
		String[] valueArr = valueStr.split(",");
		
		//组织where语句
		whereClause.append(" and  ").append(alias).append(".");
		whereClause.append(element.getFieldName()).append(" ");
		whereClause.append(element.getOperator().getOerator());
		whereClause.append(" (");
		
		int arrLength = valueArr.length;
		
		//如果in的条件为空，就赋一个空值
		if(arrLength==0){
			whereClause.append("?");
			values.add("");//赋一个空值
		} else{
			for(int i=0;i<arrLength;i++) {
				if (i != arrLength - 1)
					whereClause.append("?,");
				else
					whereClause.append("?");
				values.add(valueArr[i]);//赋值
			}
		}
		whereClause.append(") ");
	}

	/**
	 * 利用Query对象，返回符合条件的数据行数
	 * 
	 * @param where
	 * @param values
	 * @param poName
	 *            持久层对象名称
	 * @param alias
	 *            持久层对象别名
	 * @return
	 * @throws DataAccessResourceFailureException
	 * @throws HibernateException
	 * @throws IllegalStateException
	 */
	protected int getMaxRowCount(String where, Object values[], String poName,
			String alias) throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		StringBuffer sql = new StringBuffer(100 + where.length());
		sql.append(" select count(*) from ");
		sql.append(poName).append(" as ").append(alias).append(" where 1=1 ");
		sql.append(where);

		Query query = getSession().createQuery(sql.toString());
		bindParameters(query, values);
		List ret = query.list();
		return Integer.valueOf(ret.get(0).toString());
	}
	/**
	 * 绑定所有的参数，这里使用JDBC风格的问号(?)参数进行绑定的方法
	 * 
	 * @param query
	 * @param values
	 */
	protected void bindParameters(Query query, Object[] values) {
		for (int i = 0; i < values.length; i++) {
			Object obj = values[i];
			// System.out.println(obj.getClass());
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
		} else if (Date.class.isInstance(obj)) {
//			query.setDate(i, (Date) obj);
			query.setTimestamp(i, (Date)obj);
		} else if (Integer.class.isInstance(obj)) {
			query.setInteger(i, ((Integer) obj).intValue());
		} else if (Long.class.isInstance(obj)) {
			query.setLong(i, ((Long) obj).longValue());
		} else if (Byte.class.isInstance(obj)) {
			query.setByte(i, ((Byte)obj).byteValue());
		} else if (Boolean.class.isInstance(obj)) {
			query.setBoolean(i, ((Boolean)obj).booleanValue());
		} else if (Double.class.isInstance(obj)) {
			query.setDouble(i, ((Double)obj).doubleValue());
		} else {
			throw new InvalidParameterException("参数类型不匹配");
		}
	}
	protected HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	private void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	protected Session getSession() {
		return session;
	}
	private void setSession(Session session) {
		this.session = session;
	}
}
