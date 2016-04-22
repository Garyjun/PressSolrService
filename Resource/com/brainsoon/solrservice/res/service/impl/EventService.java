package com.brainsoon.solrservice.res.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.brainsoon.solrservice.res.po.Book;
import com.brainsoon.solrservice.res.service.IArticleService;
import com.brainsoon.solrservice.res.service.IEventService;
import com.brainsoon.solrservice.res.service.IJournalService;
import com.channelsoft.appframe.service.impl.BaseServiceObject;
/**
 * 
* @ClassName: IEventService
* @Description: 大事辑览查询
* @author brainsoon
* @date 2016年4月14日
*
 */
public class EventService extends BaseServiceObject implements IEventService {

	private Log logger = LogFactory.getLog(EventService.class);

	/**
	 * 获取大事辑览的ids
	 */
	public List getEventIds() {
		String sql = "select id from xhyb_jilan";
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return list;
		}
		return null;
	}

	/**
	 * 获取大事辑览信息
	 */
	public Map getEventInfo(Long id) {
		String sql = "SELECT j.id,l.title as belongjournal,j.title,jt.name as typeid,j.text,j.pressdate as published" +
					" FROM xhyb_jilan j LEFT JOIN xhyb_library l ON j.belong=l.bookid "+
					" LEFT JOIN xhyb_jilan_type jt ON j.classifyid=jt.typeid "+
					" WHERE j.id = " + id;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return (Map)list.get(0);
		}
		return null;
	}
}
