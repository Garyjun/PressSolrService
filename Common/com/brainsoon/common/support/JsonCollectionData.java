/**
 * 
 */
package com.brainsoon.common.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @ClassName: SysMetaData 
 * @Description:  集合类- JSON化数据列表对象
 * @author tanghui 
 * @date 2013-3-22 下午6:17:05 
 *
 */
public class JsonCollectionData implements Serializable{

	
    private static final long serialVersionUID = 1L;
    
    private Map<String,String> strMap;
    private List<String> strList;
    private Set<String> strSet;
    
    
	public JsonCollectionData() {
		super();
	}


	public Map<String, String> getStrMap() {
		return strMap;
	}


	public void setStrMap(Map<String, String> strMap) {
		this.strMap = strMap;
	}


	public List<String> getStrList() {
		return strList;
	}


	public void setStrList(List<String> strList) {
		this.strList = strList;
	}


	public Set<String> getStrSet() {
		return strSet;
	}


	public void setStrSet(Set<String> strSet) {
		this.strSet = strSet;
	}


	public JsonCollectionData(Map<String, String> strMap, List<String> strList,
			Set<String> strSet) {
		super();
		this.strMap = strMap;
		this.strList = strList;
		this.strSet = strSet;
	}


	public JsonCollectionData(Map<String, String> strMap) {
		super();
		this.strMap = strMap;
	}


	public JsonCollectionData(List<String> strList) {
		super();
		this.strList = strList;
	}


	public JsonCollectionData(Set<String> strSet) {
		super();
		this.strSet = strSet;
	}


	

	
    
}
