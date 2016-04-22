
package com.channelsoft.appframe.dao.jdbc.query;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.channelsoft.appframe.dao.query.PageableResult;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.DaoException;
/**
 * <dl>
 * <dt>PageableQueryWithSql</dt>
 * <dd>Description:分页查询的方法对象,根据SQL查询语句,实现数据库分页的功能</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Dec 8, 2006</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class PageableQueryWithSql{
	protected static final Log logger = LogFactory.getLog(PageableQueryWithSql.class);
	private JdbcTemplate jdbcTemplate;
	public PageableQueryWithSql(JdbcTemplate jdbcTemplate) {
		if (jdbcTemplate==null) {
			throw new DaoException("PageableQueryWithSql:JdbcTemplate 为空!");
		}
		setJdbcTemplate(jdbcTemplate);
	}

	/**
	 * 返回可分页的列表
	 * @param conditions 查询条件
	 * @return 符合条件的数据行数，以及当前页的数据列表
	 * @throws DaoException
	 */
	public PageableResult getPageableResult(QueryConditionList conditions) throws DaoException {
		if (logger.isDebugEnabled()) {
			logger.debug("查询SQL为："+conditions.getQueryScript());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		}
	}

	/**
	 * 查询当前页的数据
	 * @param conditions
	 * @return 
	 */
	protected List getCurrentPage(QueryConditionList conditions) { 
		try { 
			String sql = prepareSql(conditions);
			
			if (logger.isDebugEnabled()) {
				logger.debug("查询SQL为："+sql);
			}
			
			return getJdbcTemplate().queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		}
	}

	/**
	 * 返回符合条件的数据行数
	 * @param conditions
	 * @return
	 */
	protected int getMaxRowCount(QueryConditionList conditions) {
		try{
			StringBuffer countSql = new StringBuffer(30 + conditions.getQueryScript().length());
			//oracle
//			countSql.append(" select count(*) from (").
//					append(conditions.getQueryScript()).append(")");
			
			//mysql
			countSql.append(" select count(*) from (").append(
					conditions.getQueryScript()).append(") tb");
			
			return getJdbcTemplate().queryForInt(countSql.toString()); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	private String prepareSql(QueryConditionList conditions){
		StringBuffer sqlBuf = new StringBuffer(50 + conditions.getQueryScript().length());
		//oracle
//		sqlBuf.append("select rownum,tt.* from")
//			  .append(" (select rownum rid,t.* from (") 
//			  .append(conditions.getQueryScript())
//              .append(") t) tt where rid >=").append(conditions.getStartIndexForSql()) 
//              .append(" and rownum < ").append(conditions.getEndIndexForSql());
		
		//mysql limit开始是从0开始
		sqlBuf.append("select t.* from (")
			  .append(conditions.getQueryScript()).append(") t limit ")
			  .append(conditions.getStartIndexForSql()-1).append(",")
			  .append(conditions.getEndIndexForSql() - conditions.getStartIndexForSql());
		
		return sqlBuf.toString();
	}
	
	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	private void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
