package com.brainsoon.solr.thread;

import com.brainsoon.solr.po.Resource;

public class ThreadManager {
	private static int poolSize = 2;

	private static SolrUpdateMultiThread solrUpdateMultiThread = null;

	static {
		solrUpdateMultiThread = new SolrUpdateMultiThread(getPoolSize());
	}

	public static void startThread() {
		solrUpdateMultiThread.start();
	}

	public static int getPoolSize() {
		return poolSize;
	}

	public static void addTask(Resource resource) {
		solrUpdateMultiThread.addTask(resource);
	}

	public static void stopThread() {
		solrUpdateMultiThread.stop();
	}
}
