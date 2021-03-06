package com.brainsoon.solrservice.res.service;


import java.util.List;
import java.util.Map;

import com.brainsoon.solrservice.res.po.Book;
import com.channelsoft.appframe.service.IBaseOperateService;

/**
 * com.brainsoon.solrservice.res.service.IArticleService.java 
 * 创建者：yugang
 * 
 */
public interface IArticleService extends IBaseOperateService{

    /**
     * 获取所有图书ids
     */
    public List getArticleIds();
    
    /**
     * 获取图书信息
     */
    public Map getArticleInfo(Long bookId);
    
    /**
     * 获取所属期刊
     */
    public String getbelJoulBybookId(Long bookId); 
    
    /**
     * 获取机构馆id 用空格格开
     * return String 
     * */
    public String getOrgIds(Long bookId);
    
    /**
     * 获取专题库id 用空格格开
     * return String 
     * */
    public String getSpeIds(Long bookId);


}
