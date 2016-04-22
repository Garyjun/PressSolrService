/**
 * 
 */
package com.channelsoft.appframe.dao;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.DaoException;

/**
 * 基于JDBC的DAO的公共操作接口，对数据库表提供标准的增删改查功能
 * 
 */
public interface IBaseJdbcDao {
	
	/**
	 * 执行更新SQL语句
	 * @param sql
	 * @return    返回受影响的数据行数
	 * @throws DaoException
	 */
	public int update(String sql) throws DaoException;
	/**
	 * 执行带参数的更新SQL语句
	 * @param sql   带参数的SQL语句
	 * @param args  参数值
	 * @return
	 * @throws DaoException
	 * @author liwei   2007-7-12
	 */
	public int update(String sql,Object[] args) throws DaoException;
	/**
	 * 执行查询SQL语句，返回String值
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	public String queryForString(String sql) throws DaoException;
	/**
	 * 执行查询SQL语句，返回int值
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	public int queryForInt(String sql) throws DaoException;

	/**
	 * 执行查询SQL语句，返回long值
	 * @param sql
	 * @return
	 * @throws DaoException
	 *
	 * @author Fuwenbin
	 * @date Jul 6, 2007 10:05:16 AM 
	 */
	public long queryForLong(String sql) throws DaoException;
	
	/**
	 * 执行查询SQL语句
	 * @param sql
	 * @param objectClass
	 * @return 返回Class的对象
	 * @throws DaoException
	 *
	 * @author Fuwenbin
	 * @date Jul 6, 2007 9:27:38 AM 
	 */
	public Object queryForObject(String sql,Class objectClass) throws DaoException;
	
	/**
	 * 执行查询SQL语句
	 * @param sql
	 * @return 返回结果列表
	 * @throws DaoException
	 *
	 * @author Fuwenbin
	 * @date Jul 6, 2007 9:27:30 AM 
	 */
	public List<Map> queryForList(String sql) throws DaoException;  
	/**
	 * 执行带参数的查询SQL语句
	 * @param sql   带参数的查询SQL语句
	 * @param args  参数值
	 * @return
	 * @throws DaoException
	 */
	public List<Map> queryForList(String sql,Object[] args) throws DaoException;  
	/**
	 * 执行查询SQL语句
	 * @param sql
	 * @throws DaoException
	 *
	 * @author Fuwenbin
	 * @date Jul 6, 2007 9:35:31 AM 
	 */
	public void execute(String sql) throws DaoException;
	
	/**
	 * 行处理回调接口
	 * @param sql
	 * @param handler
	 * @throws DaoException
	 *
	 * @author Fuwenbin
	 * @date Aug 16, 2007 7:13:02 PM 
	 */
	public void query(String sql, RowCallbackHandler handler) throws DaoException;
	
	/**
     * 校验表是否存在
     * @param tableName
     * @return
     * @throws DaoException
     */
    public boolean isExistTable (String tableName) throws DaoException;
    
    /**
     * 校验数据是否存在。
     * @param tableName 需要校验的表名
     * @param whereClause 查询条件子句，不需要包含“where”关键字
     * @return
     * @throws DaoException
     *
     * @author Fuwenbin
     * @date Aug 6, 2007 3:22:12 PM 
     */
    public boolean isExistData (String tableName, String whereClause) throws DaoException;

    /**
     * 返回存在的表名
     * @param tableName
     * @return
     * @throws DaoException
     */
    public String[] getExistTables (String[] tableName) throws DaoException;
    /**
     * 得到系统日期
     * @return
     * @throws DaoException
     */
    public Timestamp getSysDate() throws DaoException;
    
    /**
     * 返回指定的sequence的下一个值
     * @param name
     * @return
     * @throws DaoException
     */
    public long getSequence (String name) throws DaoException;
    
	/**
	 * 根据sql，返回可分页的列表对象
	 * @param conditions
	 * @return
	 * @throws DaoException
	 *
	 * @author Fuwenbin
	 */
	public PageableResult queryBySql(QueryConditionList conditions) throws DaoException;
	
	public Connection getJdbcConnection() throws DaoException;
	
	/**
	 * 校验表是否存在
	 * @param databaseName
	 * @param tableName
	 * @return
	 * @throws DaoException
	 * @author donghf
	 * @date 2009-9-9
	 */	
	public boolean tableExist(String databaseName,String tableName) throws DaoException;
}
