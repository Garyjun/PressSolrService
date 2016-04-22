package com.brainsoon.solrservice.res.service;


import java.util.List;
import java.util.Map;

import com.brainsoon.solrservice.res.po.Book;
import com.channelsoft.appframe.service.IBaseOperateService;
/**
 * 
* @ClassName: IEventService
* @Description: 大事辑览查询
* @author brainsoon
* @date 2016年4月14日
*
 */
public interface IEventService extends IBaseOperateService{

    /**
     * 获取所有大事辑览ids
     */
    public List getEventIds();
    
    /**
     * 获取大事辑览信息
     */
    public Map getEventInfo(Long id);
    
}
