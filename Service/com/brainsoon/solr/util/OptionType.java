package com.brainsoon.solr.util;

import java.util.HashMap;
import java.util.Map;

public class OptionType {
	
	//创建索引
	public static final String CREATE_INDEX = "createIndex";
	
	//删除索引
	public static final String DELETE_INDEX = "deleteIndex";
	
	
	
	public static final String SUCCESS = "0"; 
	public static final String PARAM_100001 = "100001";
	public static final String PARAM_200001 = "200001";
	public static final String PARAM_300001 = "300001";
	public static final String PARAM_300002 = "300002";
	public static final String PARAM_300003 = "300003";
	public static final String PARAM_300004 = "300004";
	
	private static final Map<String,Map<String,String>> data = new HashMap<String,Map<String,String>>();
	/**
	 * 跟具操作返回对应操作的操作码的描述
	 * @param opt 操作类型
	 * @param code 操作码
	 * @return
	 */
	static{
		//构造CREATE_INDEX的操作码与描述对应关系
		Map<String,String> createIndexMap = new HashMap<String,String>();
		createIndexMap.put(SUCCESS, "操作成功");
		createIndexMap.put(PARAM_100001, "接口调用失败");
		createIndexMap.put(PARAM_200001, "输入的必选参数为空");
		createIndexMap.put(PARAM_300001, "源文件不存在");
		createIndexMap.put(PARAM_300002, "生成打包文件错误");
		data.put(CREATE_INDEX, createIndexMap);
		
		//构造DELETE_INDEX的操作码与描述对应关系
		Map<String,String> deleteIndexMap = new HashMap<String,String>();
		deleteIndexMap.put(SUCCESS, "操作成功");
		deleteIndexMap.put(PARAM_100001, "接口调用失败");
		deleteIndexMap.put(PARAM_200001, "输入的必选参数为空");
		deleteIndexMap.put(PARAM_300001, "源文件不存在");
		deleteIndexMap.put(PARAM_300002, "生成打包文件错误");
		data.put(DELETE_INDEX, deleteIndexMap);

	}
	public static String getCodeDesc(String opt,String code){
		return data.get(opt).get(code);
	}
}
