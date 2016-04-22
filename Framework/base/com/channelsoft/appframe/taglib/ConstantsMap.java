package com.channelsoft.appframe.taglib;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * <dl>
 * <dt>ConstantsMap</dt>
 * <dd>Description:常量显示标签</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-10-23</dd>
 * </dl>
 * 
 * @author 李大鹏
 */
public class ConstantsMap {

	private LinkedHashMap<Object, Object> map;
	public ConstantsMap() {
		map = new LinkedHashMap<Object, Object>();
	}
	
	
	public void putConstant(Object value, Object desc) {
		map.put(value, desc);
	}
	
	public Object getDescByValue(Object value) {
		return map.get(value);
	}

	public Set getValueSet() {
		return map.keySet();
	}

	public Collection getDescSet() {
		return map.values();
	}
	
	public Set getConstantsSet() {
		return map.entrySet();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}
}
