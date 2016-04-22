package com.channelsoft.appframe.dao.jdbc;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.channelsoft.appframe.dao.IBaseJdbcDao;
import com.channelsoft.appframe.dao.jdbc.query.PageableQueryWithSql;
import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.DaoException;



/**
 * <dl>
 * <dt>SPMS</dt>
 * <dd>Description:基于JDBC的DAO基类,对于需要批量处理,或者涉及到大数据量的操作,请使用此基类。
 * 注意：必须注入参数jdbcTemplate</dd>
 * <dd>Copyright: Copyright (C) 2004</dd>
 * <dd>Company: 北京青牛软件技术有限责任公司</dd>
 * <dd>CreateDate: 2005-8-4</dd>
 * </dl>
 */
public class BaseJdbcDao extends JdbcDaoSupport implements IBaseJdbcDao{
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * 
	 */
	public BaseJdbcDao() {
		super();
	}

	public int update(String sql) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行更新SQL语句：" + sql);
		}
		try {
			return getJdbcTemplate().update(sql);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}
	public int update(String sql,Object[] args) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行带参数的更新SQL语句：[" + sql+"]");
		}
		try {
			return getJdbcTemplate().update(sql,args);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}
	public int queryForInt(String sql) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，返回int值，SQL=[" + sql + "]");
		}
		try {
			return getJdbcTemplate().queryForInt(sql);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}

	public long queryForLong(String sql) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，返回long值，SQL=[" + sql + "]");
		}
		try {
			return getJdbcTemplate().queryForLong(sql);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}

	public Object queryForObject(String sql, Class objClass)
			throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，返回Class的对象，SQL=[" + sql + "]");
		}
		try {
			return getJdbcTemplate().queryForObject(sql, objClass);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map> queryForList(String sql) throws DaoException { 
		try {
			List<Map> list = getJdbcTemplate().queryForList(sql);
			if (logger.isDebugEnabled()) {
				if(list.size() > 0){
					logger.debug("执行查询SQL语句，SQL=[" + sql + "]");
					logger.debug("返回结果列表:" + list.size());
				}
			}
			return list;
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<Map> queryForList(String sql, Object[] args)
			throws DaoException {
		try {
			List<Map> list = getJdbcTemplate().queryForList(sql,args);
			if (logger.isDebugEnabled()) {
				if(list.size() > 0){
					logger.debug("执行带参数的查询SQL语句，SQL=[" + sql + "]");
					logger.debug("返回结果列表:" + list.size());
				}
			}
			return list;
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}

	public void execute(String sql) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行SQL语句，SQL=[" + sql + "]");
		}
		try {
			getJdbcTemplate().execute(sql);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}
	/**
	 * 统计操作耗时
	 * 
	 * @param message
	 * @param startTime
	 */
	protected void logTime(String message, long startTime) {
		if (logger.isInfoEnabled()) {
			long timeCost = System.currentTimeMillis() - startTime;
			logger.info("操作耗时统计--" + message + (timeCost) + "毫秒");
		}
	}

	public String queryForString(String sql) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，返回String值，SQL=[" + sql + "]");
		}
		try {
			return (String)getJdbcTemplate().queryForObject(sql, String.class);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * @see com.channelsoft.appframe.dao.IBaseJdbcDao#query(java.lang.String, org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String sql, RowCallbackHandler handler) throws DaoException{
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，并逐条处理结果数据，SQL=[" + sql + "]");
		}
		try {
			getJdbcTemplate().query(sql, handler);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}
	}
	
    public boolean isExistTable(String tableName) throws DaoException { 
		StringBuffer sql = new StringBuffer(200);
		sql.append("select 1 from tab where UPPER(tname)=UPPER('");
		sql.append(tableName).append("')");
		 
		try {
			List list = getJdbcTemplate().queryForList(sql.toString());
			return (list != null && !list.isEmpty());
		} catch (DataAccessException e) {
			throw new DaoException(e);
		} 
    }
    
    /**
     * @see com.channelsoft.appframe.dao.ICommonDao#dataExist(java.lang.String, java.lang.String)
     */
    public boolean isExistData (String tableName, String whereClause) throws DaoException{
    	if(StringUtils.isBlank(tableName)){
    		throw new IllegalArgumentException("数据判存失败：表名参数不合法！");
    	}
    	
		StringBuffer sql = new StringBuffer(200);
		
		//DBMS=ORACLE
		//sql.append("select 1 from ").append(tableName).append(" where rownum = 1 ");
		//DBMS=MySql
		sql.append("select 1 from ").append(tableName).append(" where 1 = 1 ");
		
		if(StringUtils.isNotBlank(whereClause)){
			sql.append(" and (").append(whereClause).append(")");
		}
		
		//DBMS=MySql
		sql.append(" limit 1 ");
		
		logger.debug("查询SQL语句：" + sql);
		
		try {
			return getJdbcTemplate().queryForList(sql.toString()).size()==1; 
		} catch (DataAccessException e) {
			throw new DaoException(e);
		} 
    }
    
    public String[] getExistTables(String[] tableName) throws DaoException { 
		StringBuffer sql = new StringBuffer(200);
		sql.append("select TNAME from tab where TNAME between '");
		sql.append(tableName[0]).append("' and '");
		sql.append(tableName[1]).append("'");
		 
		try {
			List list = getJdbcTemplate().queryForList(sql.toString()); 
			if (list == null || list.isEmpty()) {
				return new String[0];
			}
			String[] ret = new String[list.size()];
			int i=0;
	        for (Iterator iter = list.iterator(); iter.hasNext();i++) {
	            Map item = (Map) iter.next();
	            ret[i] = (String) item.get("TNAME"); 
	        }
			return ret;
		} catch (DataAccessException e) {
			throw new DaoException(e);
		} 
    }
    
    /* (non-Javadoc)
     * @see com.qnuse.usboss.dao.ICommonDao#getSysDate()
     */
    public Timestamp getSysDate() throws DaoException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.qnuse.usboss.dao.ICommonDao#getSequence(java.lang.String)
     */
    public long getSequence(String seqName) throws DaoException {
        StringBuffer sql = new StringBuffer(100);
        sql.append("select ").append(seqName).append(".nextval from dual");
        return getJdbcTemplate().queryForInt(sql.toString());
    }
    
	/**
	 * 根据sql，返回可分页的列表对象
	 * @param conditions
	 * @return
	 * @throws DaoException
	 *
	 * @author Fuwenbin
	 */
	public PageableResult queryBySql(QueryConditionList conditions) throws DaoException{
		PageableQueryWithSql pageableQueryWithSql = new PageableQueryWithSql(getJdbcTemplate());
		return pageableQueryWithSql.getPageableResult(conditions);
	}
	
	public Connection getJdbcConnection() throws DaoException{
		try {
			return super.getConnection();
		} catch (CannotGetJdbcConnectionException e) {
			throw new DaoException(e);
		}
	}
	
	//mysql中查询某数据库中是否存在指定的表
	/**
	 * 校验表是否存在
	 * @param databaseName
	 * @param tableName
	 * @return
	 * @throws DaoException
	 * @author donghf
	 * @date 2009-9-9
	 */	
	public boolean tableExist(String databaseName,String tableName) throws DaoException {
		StringBuilder sql = new StringBuilder();
		sql.append("select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='")
		   .append(databaseName).append("' and TABLE_NAME='")
		   .append(tableName).append("'");
		
		try {
			List list = getJdbcTemplate().queryForList(sql.toString());
			return (list != null && !list.isEmpty());
		} catch (DataAccessException e) {
			throw new DaoException(e);
		} 
	}
}
