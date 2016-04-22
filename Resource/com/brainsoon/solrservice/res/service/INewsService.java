package com.brainsoon.solrservice.res.service;


import java.util.List;
import java.util.Map;

import com.channelsoft.appframe.service.IBaseOperateService;

/**
 * com.brainsoon.solrservice.res.service.IArticleService.java 
 * 创建者：yugang
 * 
 */
public interface INewsService extends IBaseOperateService{

	/**
	 * @param bookid
	 * @return
	 * 
	 */
	public Map getNewsinfo(Long contentId);
    
    /**
     * 获取所有文章ids
     */
    public List getNewsIds();


}
