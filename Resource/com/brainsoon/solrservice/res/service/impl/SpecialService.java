package com.brainsoon.solrservice.res.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.brainsoon.solrservice.res.service.ISpecialService;
import com.channelsoft.appframe.service.impl.BaseServiceObject;


public class SpecialService extends BaseServiceObject implements ISpecialService {

	private Log logger = LogFactory.getLog(SpecialService.class);

	@Override
	public Map getSpecialinfo(Long specialId) {
		String sql = "SELECT l.booklistid as specialid, l.title, lt.name as typeid, l.keyword as keywords, l.description, "
				+ "l.published as onlinetime,l.thumb,l.recordbegindate as era "
				+ "FROM xhyb_list l "
				+ "LEFT JOIN xhyb_list_type lt "
				+ "ON l.typeid = lt.typeid "
				+ "WHERE l.status = 1 AND l.booklistid = " + specialId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return (Map)list.get(0);
		}
		return null;
	}
	
	@Override
	public List getSpecialIds() {
		String sql = "SELECT l.booklistid as specialid " + 
				"FROM xhyb_list l " +
                "LEFT JOIN xhyb_list_type lt " +
                "ON l.typeid = lt.typeid " +
                "WHERE l.status = 1";
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return list;
		}
		return null;
	}
    
	public List getCollectBooksIds(Long collId) {
		String sql = "SELECT l.bookid FROM xhyb_collect_list l WHERE l.collect_id = " + collId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return list;
		}
		return null;
	}
	
	@Override
	public Map getCollectInfo(Long collectId) {
		String sql = "SELECT collect_id, organizationid " +
                     "FROM xhyb_collect " +
                     "WHERE collect_id = " + collectId + " LIMIT 1";
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return (Map)list.get(0);
		}
		return null;
	}
	
	@Override
	public List getCollects() {
		String sql = "SELECT collect_id as collectid, organizationid, type, special FROM xhyb_collect ";
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return list;
		}
		return null;
	}
	
	@Override
	public List getSpecialBooksIds(Long specialId) {
		String sql = "SELECT l.bookid FROM xhyb_list_book l WHERE l.booklistid = " + specialId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return list;
		}
		return null;
	}
	
	
	@Override
	public Map getAttributeByid(Long bookId) {
		String sql = "SELECT attribute FROM xhyb_library WHERE bookid = " + bookId;
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return (Map)list.get(0);
		}
		return null;
	}
}
