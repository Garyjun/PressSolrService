package com.brainsoon.solr.thread;

import java.util.List;

/**
 * <dl>
 * <dt>IThreadPool</dt>
 * <dd>Description:线程池接口</dd>
 * <dd>Copyright: Copyright (c) 2011 青牛（北京）技术有限公司</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Apr 8, 2011</dd>
 * </dl>
 * 
 * @author 张欣
 */

public interface IThreadPool<T extends DataBaseThread> {

	public abstract void addThread(T thread) throws ThreadPoolFullException;

	public abstract boolean isFull();

	public abstract int getThreadCount();

	public abstract int getActiveThreadCount();

	public abstract void removeThread(String threadName);

	public abstract T getThread(String threadName)
			throws ThreadNotFoundException;

	public abstract List<T> getDeadThread();

	public abstract List<T> getThread();

}
