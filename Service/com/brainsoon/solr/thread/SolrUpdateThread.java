package com.brainsoon.solr.thread;

import jxl.read.biff.File;

import com.brainsoon.solr.po.Resource;
import com.brainsoon.solr.po.SolrQueue;
import com.brainsoon.solr.service.ISearchFacede;
import com.channelsoft.appframe.utils.BeanFactoryUtil;

/**
 * 
* @ClassName: SolrUpdateThread
* @Description: solr创建索引单线程
* @author huangjun
* @date 2016年4月6日
*
 */

public class SolrUpdateThread extends DataBaseThread {
	private int threadId;
	private String threadGroupName;
	
	public SolrUpdateThread(int threadId, String threadGroupName) {
		this.threadId = threadId;
		this.threadGroupName = threadGroupName;		
	}

	public String getThreadName() {
		return getThreadGroupName() + "-" + "thread" + "-" + getThreadId();
	}
	
	public int getThreadId() {
		return threadId;
	}
	
	public String getThreadGroupName() {
		return threadGroupName;
	}
	
	protected void processTask(Resource resource) throws ThreadException {	
		if (resource != null ) {
			String prodid = resource.getProdid();
			String restype = resource.getRestype();
			String orgid = resource.getOrgid();
			String speid = resource.getSpeid();
			String actions = resource.getActions();
			if("0".equals(actions)) {//创建索引
				
				if (resource.getFile()!=null && resource.getFile().exists()) {
					getSearchFacede().addIndex(resource, resource.getFile());
					resource.getFile().delete();
					logger.info("[processTask] 索引创建完成"+resource.getUuid());
					logger.info("[processTask] 删除文件："+resource.getFile());
				}else {
					getSearchFacede().addIndex(resource);
					logger.info("[processTask] 索引创建完成"+resource.getUuid());
				}
				
			} else if("1".equals(actions)) {//删除索引
				String uuid = restype + "_" + prodid;
				getSearchFacede().deleteIndex(uuid);
				
			} else if("2".equals(actions)) {//创建专题库索引
				
				if (resource.getFile()!=null && resource.getFile().exists()) {
					getSearchFacede().addSpecialBookIndex(resource, resource.getFile());
					resource.getFile().delete();
					logger.info("[processTask] 索引创建完成"+resource.getUuid());
					logger.info("[processTask] 删除文件："+resource.getFile());
				}else {
					getSearchFacede().addSpecialBookIndex(resource);
					logger.info("[processTask] 索引创建完成"+resource.getUuid());
				}
				
			} else {
				logger.debug("索引动作信息：{产品Id：" + prodid + " 类型："+restype+" 动作："+actions+"}无该索引动作状态！");
			}
		}
	}
	
	
	public ISearchFacede getSearchFacede() {
		return (ISearchFacede)BeanFactoryUtil.getBean("searchFacede");
	}
	

}
