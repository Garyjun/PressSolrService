package com.brainsoon.solrservice.res.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.brainsoon.solrservice.res.service.INewsService;
import com.channelsoft.appframe.service.impl.BaseServiceObject;


public class NewsService extends BaseServiceObject implements INewsService {

	private Log logger = LogFactory.getLog(NewsService.class);

	@Override
	public Map getNewsinfo(Long contentId) {
		String sql = "SELECT a.contentid, c.title, a.description, c.tags as keywords, a.author, a.editor, a.content, g.name as typeid, " +
				 	"c.published as onlinetime, c.url, c.thumb, s.name as sourceid " +
                    "FROM xhyb_article a " +
                    "LEFT JOIN xhyb_content c ON a.contentid = c.contentid " +
                    "LEFT JOIN xhyb_category g ON c.catid = g.catid " +
                    "LEFT JOIN xhyb_source s ON  c.sourceid = s.sourceid " +
                    "WHERE c.status = 6 AND a.contentid = " + contentId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return (Map)list.get(0);
		}
		return null;
	}
	
	@Override
	public List getNewsIds() {
		String sql = "SELECT a.contentid " + 
				  	 "FROM xhyb_article a " +
				  	 "LEFT JOIN xhyb_content c " +
				  	 "ON a.contentid = c.contentid " +
                     "WHERE c.status = 6";
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return list;
		}
		return null;
	}
    
}
