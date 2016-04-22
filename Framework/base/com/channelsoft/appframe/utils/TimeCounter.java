
package com.channelsoft.appframe.utils;

import java.util.HashMap;
import java.util.Map;


public class TimeCounter {

	public long max = 0l;
	public long average = 0l;
	public long min = 10000000000000l;
	public long total = 0l;
	
	
	public long startTime = 0l;
	public long endTime = 0l;
	
	public Map tracedMethods = new HashMap();
	
	public TimeCounter() {
		super();
	}
	
	public void count(String message) {
		if(message == "") {
			
		}
		if(startTime == 0l) {
			startTime = System.currentTimeMillis();
			System.out.println("开始计时:"+startTime);
			return;
		}
		endTime = System.currentTimeMillis();
		long length = endTime - startTime;
		System.out.println(message+"耗时="+length+", startTime="+endTime);
		startTime = endTime;
		total += length;
		
//			TimeCounter tracedMethodCounter = new TimeCounter(); 
//			tracedMethodCounter.total = length;
//			tracedMethods.put(message, tracedMethodCounter);
		return;
	}

}
