package com.brainsoon.solr.thread;

import org.apache.log4j.Logger;

import com.brainsoon.solr.po.Resource;

/**
 * <dl>
 * <dt>DataBaseMultiThread</dt>
 * <dd>Description:数据处理多线程基类</dd>
 * <dd>Copyright: Copyright (c) 2011 青牛（北京）技术有限公司</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Apr 8, 2011</dd>
 * </dl>
 * 
 * @author 张欣
 */

public abstract class DataBaseMultiThread {
	protected final Logger logger = Logger.getLogger(getClass());
	protected TaskQueue queue;
	protected int poolSize = 5;
	protected IThreadPool<DataBaseThread> threadPool;
	protected Checker checker;

	public abstract void start();

	public abstract void stop();

	public abstract void restartDeadThread();

	public abstract String getThreadGroupName();

	public TaskQueue getTaskQueue() {
		return queue;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public IThreadPool<DataBaseThread> getThreadPool() {
		return threadPool;
	}

	public int getActiveThreadCount() {
		return threadPool.getActiveThreadCount();
	}

	public void addTask(Resource resource) {
		queue.addMessage(resource);
	}

	public Checker getChecker() {
		return checker;
	}

	public void setChecker(Checker checker) {
		this.checker = checker;
	}

	class Checker extends Thread {
		private long sleepTime = 5 * 60 * 1000;
		// private long sleepTime = 60 * 1000;
		private boolean runFlag = true;

		public Checker() {
			this.setDaemon(true);
		}

		@Override
		public void run() {
			while (runFlag) {
				try {
					int queueSize = getTaskQueue().size();
					String threadGroupName = getThreadGroupName();
					int activeThreadCount = getActiveThreadCount();

					logger.info("当前队列长度: " + queueSize + "; 当前活动线程数: "
							+ activeThreadCount + "; 线程组名: " + threadGroupName);

					if (activeThreadCount < getPoolSize()) {
						logger.error("线程池部分线程宕掉！重新启动宕掉的线程，线程组名: "
								+ threadGroupName);
						restartDeadThread();
					}

					if (queueSize >= getTaskQueue().getMaxQueueSize())
						logger.warn("队列消息堆积过多！当前队列长度: " + queueSize
								+ "; 线程组名: " + threadGroupName);

					Thread.sleep(sleepTime);
				} catch (Exception e) {

				}
			}
		}

		public boolean getRunFlag() {
			return runFlag;
		}

		public void setRunFlag(boolean runFlag) {
			this.runFlag = runFlag;
		}

		public long getSleepTime() {
			return sleepTime;
		}

		public void setSleepTime(long sleepTime) {
			this.sleepTime = sleepTime;
		}
	}
}
