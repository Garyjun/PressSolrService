package com.brainsoon.solrservice.res.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.brainsoon.solrservice.res.po.Book;
import com.brainsoon.solrservice.res.service.IArticleService;
import com.brainsoon.solrservice.res.service.IJournalService;
import com.channelsoft.appframe.service.impl.BaseServiceObject;


public class JournalService extends BaseServiceObject implements IJournalService {

	private Log logger = LogFactory.getLog(JournalService.class);

	/**
	 * 获取文章的ids
	 */
	public List getJournalIds() {
		String sql = "select bookid from xhyb_library_peri";
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return list;
		}
		return null;
	}

	@Override
	public Map getJournalInfo(Long bookId) {
		String sql = "SELECT lp.bookid,l.title,l.price,l.actprice,l.batchprice,l.magazineyear," +
					" l.magazineyearnum,l.magazinenum,l.stream,l.thumb,l.description,l.published as onlinetime " +
					" FROM xhyb_library_peri lp LEFT JOIN xhyb_library l ON lp.bookid=l.bookid "+
					" WHERE lp.issue=1 and lp.bookid = " + bookId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return (Map)list.get(0);
		}
		return null;
	}
	
	@Override
	public String getOrgIds(Long bookId) {
		String sql = "SELECT DISTINCT(organizationid) FROM xhyb_collect_list WHERE isvalid=1 AND bookid=" + bookId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			String orgIds = "";
			for(int i=0;i<list.size();i++) {
				Map item = (Map)list.get(i);
				orgIds += item.get("organizationid").toString();
				if(i!=list.size()-1) {
					orgIds += " ";
				}
			}
			return orgIds;
		}
		return null;
	}

	@Override
	public String getSpeIds(Long bookId) {
		String sql = "SELECT DISTINCT(lb.booklistid) FROM xhyb_list_book lb LEFT JOIN xhyb_list l ON lb.booklistid = l.booklistid WHERE l.status =1 AND lb.bookid=" + bookId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			String speIds = "";
			for(int i=0;i<list.size();i++) {
				Map item = (Map)list.get(i);
				speIds += item.get("booklistid").toString();
				if(i!=list.size()-1) {
					speIds += " ";
				}
			}
			return speIds;
		}
		return null;
	}

	
    
}
