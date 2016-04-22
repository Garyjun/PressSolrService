package com.channelsoft.appframe.po;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.channelsoft.appframe.utils.ConvertUtil;
import com.channelsoft.appframe.utils.ObjectToMessage;
import com.googlecode.jsonplugin.annotations.JSON;

/**
 * <dl>
 * <dt>BaseHibernateObject</dt>
 * <dd>Description:数据库对象映射的基类</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-10-24</dd>
 * </dl>
 * 
 * 2008-07-03 Fuwenbin 增加清除对象的方法
 */
public abstract class BaseHibernateObject implements Serializable {

	protected static final Log logger = LogFactory
			.getLog(BaseHibernateObject.class);

	/**
	 * 返回该业务实体类对象的主键值，属于动态信息，子类必须实现此方法
	 * 
	 * @return
	 */
	public abstract Serializable getObjectID();

	/**
	 * 返回该业务实体对象的描述信息，属于动态信息，子类必须实现此方法
	 * 
	 * @return
	 */
	public abstract String getObjectDescription();

	/**
	 * 返回该业务实体类的描述信息，属于静态信息，子类必须实现此方法
	 * 
	 * @return
	 */
	public abstract String getEntityDescription();

	public String buildPoLogMessage() {
		ObjectToMessage objectToMessage = new ObjectToMessage();
		String desc = objectToMessage.getObjectLogMessage(this);
		if (desc.length() > 400)
			return desc.substring(0, 400);
		return desc;
	}

	/**
	 * 清除对象的属性
	 * 
	 * @author Fuwenbin
	 * @date Jul 3, 2008 4:55:02 PM
	 */
	public void clear() {
		try {
			Object newObj = this.getClass().newInstance();
			ConvertUtil.convert(newObj, this);
		} catch (IllegalAccessException ex) {
			logger.warn(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		} catch (InstantiationException ex) {
			logger.warn(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 根据自定义对象类型获取约定的对象名称
	 * 约定对象名称为首字母小写的类名
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Dec 2, 2008 3:23:28 PM 
	 */
	@JSON(serialize=false)
	public String getObjectName(){
		return StringUtils.uncapitalize(getClassName());
	}
	
	@JSON(serialize=false)
	public String getClassName(){
		return this.getClass().getSimpleName();
	}
}
