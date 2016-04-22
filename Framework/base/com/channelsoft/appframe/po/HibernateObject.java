package com.channelsoft.appframe.po;

import java.io.Serializable;
/**
 * <dl>
 * <dt>HibernateObject</dt>
 * <dd>Description:数据库对象映射,需要实现获取该对象的主键名称</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-11-23</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public abstract class HibernateObject extends BaseHibernateObject implements Serializable{
	/**
	 * 返回该业务实体类的主键名称，属于静态信息，子类必须实现此方法
	 * @return
	 */
	public abstract String getObjectIDName();
}
