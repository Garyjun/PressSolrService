/**
 * 
 */
package com.channelsoft.appframe.batch;

/**
 * 批量操作参数的基类，封装批量操作的公共参数
 * @author liwei
 *
 */
public class BaseBatchParameter {

	private String batchOperationObject = null;
	private String operatorId = null;
	private String loginId = null;
	private Class poClass = null;
	//added by hao on 0725
	private String batchType;
	//end
	private String locationId;//add by liweilu on 2007-1-8

	/**
	 * 批量操作对象，如果没有直接提供，则考虑用PO的类名来代替
	 * 
	 * @return
	 */
	public String getBatchOperationObject() {
		if (batchOperationObject == null) {
			if (poClass != null) {
				int index = poClass.getName().lastIndexOf(".");
				String poName = poClass.getName().substring(index + 1);
				if (poName.toUpperCase().startsWith("CFG")) {
					batchOperationObject = poName.substring(3);
				}
			}
		}
		return batchOperationObject;
	}

	public void setBatchOperationObject(String batchOperationObject) {
		this.batchOperationObject = batchOperationObject;
	}

	public Class getPoClass() {
		return poClass;
	}

	public void setPoClass(Class poClass) {
		this.poClass = poClass;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

}
