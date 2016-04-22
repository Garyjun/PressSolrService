package com.brainsoon.solr.thread;

import org.apache.log4j.Logger;

import com.brainsoon.solr.po.Resource;

/**
 * <dl>
 * <dt>DataBaseThread</dt>
 * <dd>Description:数据处理单线程基类</dd>
 * <dd>Copyright: Copyright (c) 2011 青牛（北京）技术有限公司</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Apr 8, 2011</dd>
 * </dl>
 * 
 * @author 张欣
 */

public abstract class DataBaseThread extends Thread {
	protected final Logger logger = Logger.getLogger(getClass());

	private boolean isRunnable = true;

	protected TaskQueue queue;

	@Override
	public void run() {
		Resource resource = null;
		while (isRunnable()) {
			try {
				resource = queue.getMessage(3000);
				logger.debug("DataBaseThread 当前执行任务id：" + resource.getUuid() +"任务类型(0创建索引 1删除索引)："+resource.getActions());
				if (resource == null)
					continue;

				try {
					processTask(resource);

				} catch (ThreadException e) {
					logger.error("线程[" + getThreadName() + "]任务处理异常："
							+ e.getMessage());
				} catch (Throwable e) {
					logger.error("线程[" + getThreadName() + "]执行异常!", e);
				}

			} catch (InterruptedException e) {
				// logger.error("线程[" + getThreadName() +"]执行中断：" +
				// e.getMessage());
			}
		}
	}

	public abstract String getThreadName();

	public void setTaskQueue(TaskQueue queue) {
		this.queue = queue;
	}

	public void startThread() {
		isRunnable = true;
	}

	public void stopThread() {
		isRunnable = false;
	}

	public boolean isRunnable() {
		return isRunnable;
	}

	protected abstract void processTask(Resource resource)
			throws ThreadException;

}
