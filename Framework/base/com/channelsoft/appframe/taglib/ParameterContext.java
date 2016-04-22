/**
 * FileName: ParameterContext.java
 */
package com.channelsoft.appframe.taglib;

import com.channelsoft.appframe.common.BaseObject;

/**
 * <dl>
 * <dt>ParameterContext</dt>
 * <dd>Description:参数对象</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-11-3</dd>
 * </dl>
 * 
 * @author 李大鹏
 */
class ParameterContext extends BaseObject
{
	private String name;
	private String value;
	
	ParameterContext(String name, String value)
	{
		this.name = name;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
