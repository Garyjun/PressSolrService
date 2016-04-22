package com.brainsoon.solr.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * <dl>
 * <dt>TaskInfo</dt>
 * <dd>Description:任务信息</dd>
 * <dd>Copyright: Copyright (c) 2011 青牛（北京）技术有限公司</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Apr 8, 2011</dd>
 * </dl>
 * 
 * @author 张欣
 */

public class TaskInfo
{
	private String taskId;	
	
	private int domainType = -1;
	
	List<String> resources = new ArrayList<String>();
	
	private int opType = 0;
	
	public String getTaskId()
	{
		return this.taskId;
	}
	
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	
	public int getDomainType()
	{
		return this.domainType;
	}
	
	public void setDomainType(int domainType)
	{
		this.domainType = domainType;
	}
	
	public List<String> getResources()
	{
		return resources;
	}
	
	public void setResources(List<String> resources)
	{
		this.resources = resources;
	}
	
	public int getOpType()
	{
		return this.opType;
	}
	
	public void setOpType(int opType)
	{
		this.opType = opType;
	}
	
	public void addResource(String resource) {
		this.resources.add(resource);        
	}

}
