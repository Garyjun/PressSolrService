package com.channelsoft.appframe.batch;

import com.channelsoft.appframe.taglib.ConstantsMap;
import com.channelsoft.appframe.taglib.ConstantsRepository;

public class CfgBatchOperationTypeConstants {
	public static class BatchType {
        public static final String ADD = "1";
        public static final String ADD_DESC = "批量导入";
        public static final String DELETE = "2";
        public static final String DELETE_DESC = "批量删除";
        public static final String EXPORT = "3";
        public static final String EXPORT_DESC = "批量导出";
        public static final String UPDATE = "4";
        public static final String UPDATE_DESC = "批量更新";
        
    	private static ConstantsMap map;
    	static {
    		
    		map = new ConstantsMap();
    		map.putConstant(ADD, ADD_DESC);
    		map.putConstant(DELETE, DELETE_DESC);
    		map.putConstant(EXPORT, EXPORT_DESC);
    		map.putConstant(UPDATE, UPDATE_DESC);
    		ConstantsRepository.getInstance().register(BatchType.class, map);
    	}
    }
}
