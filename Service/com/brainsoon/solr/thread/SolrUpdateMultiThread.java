package com.brainsoon.solr.thread;

import java.util.List;

/**
 * 
* @ClassName: SolrUpdateMultiThread
* @Description: solr创建索引多线程
* @author huangjun
* @date 2016年4月6日
*
 */

public class SolrUpdateMultiThread extends DataBaseMultiThread {
	
	public SolrUpdateMultiThread(int poolSize) {
		this.poolSize = poolSize;		
		this.queue = new TaskQueue();
		this.threadPool = new ThreadPool<DataBaseThread>(getThreadGroupName()
				, getPoolSize(), queue);		
	}	
	
	public void start() {
		for (int i = 0; i < getPoolSize(); i++) {
			SolrUpdateThread resourceUpdateThread = null;
			try {
				resourceUpdateThread = new SolrUpdateThread(i, getThreadGroupName());							
				threadPool.addThread(resourceUpdateThread);	
				resourceUpdateThread.start();
			} catch (ThreadPoolFullException e) {				
				logger.error("线程池已满！无法插入线程!" +
						"线程名[" + resourceUpdateThread.getThreadName() + "]" );				
			}
		}
		
		this.checker = new Checker();
		checker.start();
	}	
	
	public void stop() {
		getChecker().setRunFlag(false);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		List<DataBaseThread> threads = threadPool.getThread();
		for(DataBaseThread thread : threads) {
			thread.stopThread();
		}
	}	
	
	public void restartDeadThread() {	
		List<DataBaseThread> deadThreads = getThreadPool().getDeadThread();
		for (DataBaseThread deadThread : deadThreads) {
			 deadThread.start();
		}
	}
	
	public String getThreadGroupName() {
		return "pool-solrUpdate";
	}
}
