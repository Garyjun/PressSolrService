package com.brainsoon.solrservice.res.service;


import java.util.List;
import java.util.Map;

import com.brainsoon.solrservice.res.po.Book;
import com.channelsoft.appframe.service.IBaseOperateService;

/**
 * com.brainsoon.solrservice.res.service.IBookService.java 
 * 创建者：yugang
 * 
 */
public interface IBookArticleService extends IBaseOperateService{
	
    /**
     * @param oresBaseMap
     * @return
     */
    public boolean isExsitBook(Map<String, String> oresBaseMap);
    
    /**
     * 获取所有图书ids
     */
    public List getBookIds();
    
    /**
     * 获取图书属性
     * @param bookid
	 * @return
     */
    public Book getBookinfo(Long bookId);
    
    /**
     * 获取图书信息
     */
    public Map getBookArticle(Long bookId);
    
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
