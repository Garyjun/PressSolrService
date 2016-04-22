package com.channelsoft.appframe.batch;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量退订的结果类
 * 
 * @author rui.zhang
 */
public class BatchCancelResult {
	// 总条数
	private int total;

	// 成功执行的orderRelation列表
	private List successList = new ArrayList();

	// 失败执行的orderRelation列表
	private List badList = new ArrayList(); 

	// 内存数据库是否同步成功的标示 true 成功 false 失败 
	private boolean mdbRefreshFlag = true;

	// 插入批量退订日志对象 是否成功的标示 true 成功 false 失败
	private boolean addLogFlag = true;

	public List getBadList() {
		return badList;
	}

	public void setBadList(List badList) {
		this.badList = badList;
	}

	public int getBadTotal() {
		return getBadList().size();
	}
 

	public List getSuccessList() {
		return successList;
	}

	public void setSuccessList(List successList) {
		this.successList = successList;
	}

	public int getSuccessTotal() {
		return getSuccessList().size();
	} 

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean isMdbRefreshFlag() {
		return mdbRefreshFlag;
	}

	public void setMdbRefreshFlag(boolean mdbRefreshLag) {
		this.mdbRefreshFlag = mdbRefreshLag;
	}

	public boolean isAddLogFlag() {
		return addLogFlag;
	}

	public void setAddLogFlag(boolean addLogFlag) {
		this.addLogFlag = addLogFlag;
	}

}
