package com.brainsoon.solr.thread;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.brainsoon.solr.po.Resource;

/**
 * <dl>
 * <dt>TaskQueue</dt>
 * <dd>Description:任务队列</dd>
 * <dd>Copyright: Copyright (c) 2011 青牛（北京）技术有限公司</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Apr 8, 2011</dd>
 * </dl>
 * 
 * @author 张欣
 */

public class TaskQueue {
	private Queue<Resource> queue;
	private int maxQueueSize = 10000;

	private static class TaskQueueHolder {
		private static TaskQueue instance = buildSingleton();

		private static TaskQueue buildSingleton() {
			return new TaskQueue();
		}
	}

	public static TaskQueue getInst() {
		return TaskQueueHolder.instance;
	}

	public TaskQueue() {
		this.queue = new LinkedBlockingQueue<Resource>(maxQueueSize);

	}

	public void addMessage(Resource resource) {
		queue.offer(resource);
	}

	public Resource getMessage(int timeoutInMillionSeconds)
			throws InterruptedException {
		if (queue.isEmpty()) {
			Thread.sleep(timeoutInMillionSeconds);
		}
		if (queue.isEmpty()) {
			throw new InterruptedException("wait timeout");
		}
		return queue.poll();
	}

	public int size() {
		return queue.size();
	}

	public void setMaxQueueSize(int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
	}

	public int getMaxQueueSize() {
		return maxQueueSize;
	}
}
