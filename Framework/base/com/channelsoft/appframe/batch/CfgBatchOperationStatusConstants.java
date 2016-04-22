package com.channelsoft.appframe.batch;

import com.channelsoft.appframe.taglib.ConstantsMap;
import com.channelsoft.appframe.taglib.ConstantsRepository;

public class CfgBatchOperationStatusConstants {
	public static class Status {
        public static final String SUCCESS = "1";
        public static final String SUCCESS_DESC = "成功";
        public static final String FAIL = "2";
        public static final String FAIL_DESC = "失败";
        
    	private static ConstantsMap map;
    	static {
    		
    		map = new ConstantsMap();
    		map.putConstant(SUCCESS, SUCCESS_DESC);
    		map.putConstant(FAIL, FAIL_DESC);
    		ConstantsRepository.getInstance().register(Status.class, map);
    	}
    }
}
