package com.channelsoft.appframe.taglib;
import java.util.HashMap;
import java.util.Map;


public class ConstantsRepository implements IConstantsRepository {
	Map<Object, Object> map;

	/**实现Lazy Initialization线程安全
	 * Fuwenbin 2009-02-07
	 */
	private static class ConstantsRepositoryHolder {
		static ConstantsRepository instance = new ConstantsRepository();
	}
	
	public static ConstantsRepository getInstance() {
		return ConstantsRepositoryHolder.instance;
//		if(instance == null) {
//			instance = new ConstantsRepository();
//		}
//		return instance;
	}
	
	/**
	 * 
	 */
	private ConstantsRepository() {
		map = new HashMap<Object, Object>();
	}

	/* (non-Javadoc)
	 * @see com.qnuse.qframe.extensions.taglib.IConstantsRepository#register(java.lang.Class, com.qnuse.qframe.extensions.taglib.ConstantsMap)
	 */
	public void register(Class cls, ConstantsMap constantsMap) {
		map.put(cls.getName(), constantsMap);
	}

	/* (non-Javadoc)
	 * @see com.qnuse.qframe.extensions.taglib.IConstantsRepository#getConstantsMap(java.lang.String)
	 */
	public ConstantsMap getConstantsMap(String className, String typeName) {
    	try {
    		String subClassName = className+"$"+typeName;
//    		System.out.println("subClassName: "+subClassName);    		
    		ConstantsMap object = (ConstantsMap)map.get(subClassName);
    		if(object == null) {
    			// force it to register
    			Class.forName(subClassName);
    			object = (ConstantsMap)map.get(subClassName);
    		}
   			return object;
    	} catch (ClassNotFoundException ex) {
    		return null;
    	}
    	
	}
	
	public String getConstantsDesc(String className, String typeName, Object value) {
		ConstantsMap constantsMap = getConstantsMap(className, typeName);
		return (String)constantsMap.getDescByValue(value);
	}

}
