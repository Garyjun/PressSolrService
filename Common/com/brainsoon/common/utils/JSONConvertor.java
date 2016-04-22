package com.brainsoon.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * JSON转换器
 */
public class JSONConvertor {
	
    // 将String转换成JSON        
    public static String string2Json(String key, String value) {   
    	
        JSONObject object = new JSONObject();        
        object.put(key, value);        
        return object.toString();        
    }        
       
    // 将List转换成JSON        
    public static String list2Json(List list) {        
    	
        JSONArray jsonArray = JSONArray.fromObject(list);        
        return jsonArray.toString();        
    }        
        
    // 将JSON转换成List<T>             
    public static <T> List<T> json2List(String json, Class<T> objectClass) {        
    	
        JSONArray jsonArray = JSONArray.fromObject(json);    
        return JSONArray.toList(jsonArray, objectClass);       
    }              

    // 将Map转换成JSON        
    public static String map2Json(Map map) {   
    	
        JSONObject jsonObject = JSONObject.fromObject(map);        
        return jsonObject.toString();        
    }        
        
    // 将JSON转换成Map      
    public static HashMap<String, Object> json2Map(String json) { 
    	
        JSONObject jsonObject = JSONObject.fromObject(json);        
       
        HashMap<String, Object> data = new HashMap<String, Object>();
        Iterator it = jsonObject.keys();
        while (it.hasNext())
        {
            String key = String.valueOf(it.next());
            Object value = jsonObject.get(key);
            data.put(key, value);
        }

        return data;

    }        

    // 将POJO转换成JSON        
    public static String bean2Json(Object object) {   
    	
        JSONObject jsonObject = JSONObject.fromObject(object);        
        return jsonObject.toString();        
    }        

    // 将JSON转换成POJO对象       
    public static Object json2Bean(String json, Class beanClz) {  
    	
        return JSONObject.toBean(JSONObject.fromObject(json), beanClz);
    }
    
    // 将JSON转换成POJO对象       
    public static Object json2Bean(String json, Class beanClz, String childListName, Class childClass) {  
    	
		Map<String, Class> m = new HashMap<String, Class>();
        m.put(childListName, childClass);
        
        return JSONObject.toBean(JSONObject.fromObject(json), beanClz, m);
    }
    
 // 将JSON转换成POJO对象       
    public static Object json2Bean(String json, Class beanClz, Map<String, Class> m) {  
        return JSONObject.toBean(JSONObject.fromObject(json), beanClz, m);
    } 
        
    // 获取JSON的键对值       
    public static String json2String(String json, String key) {       
    	
        JSONObject jsonObject = JSONObject.fromObject(json);        
        return jsonObject.get(key).toString();        
    }        

}
