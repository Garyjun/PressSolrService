package com.channelsoft.appframe.po.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <dl>
 * <dt>Condition</dt>
 * <dd>Description:查询条件对应的属性注释定义</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jan 12, 2009</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Condition {
	boolean isIgnore();
}
