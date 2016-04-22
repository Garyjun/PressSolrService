package com.brainsoon.solrservice.res.service;


import java.util.List;
import java.util.Map;

import com.channelsoft.appframe.service.IBaseOperateService;

/**
 * com.brainsoon.solrservice.res.service.IArticleService.java 
 * 创建者：yugang
 * 
 */
public interface ISpecialService extends IBaseOperateService{

	/**
	 * @param bookid
	 * @return
	 * 
	 */
	public Map getSpecialinfo(Long specialId);
    
    /**
     * 获取所有文章ids
     */
    public List getSpecialIds();

    public List getSpecialBooksIds(Long specialId);
    
    public Map getAttributeByid(Long bookId);
    
    public List getCollectBooksIds(Long collId);
    
    public Map getCollectInfo(Long collectId);
    
    public List getCollects();
}
