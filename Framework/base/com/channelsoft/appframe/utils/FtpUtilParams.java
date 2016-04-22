package com.channelsoft.appframe.utils;
/**
 * 这只是一个参数封装类，用于传递参数到FtpUtil。
 */
public class FtpUtilParams {
	private String aimFileName;
	private String origFileName;
	private String aimPath;
	private String origPath;
	private String aimHost;
	private String origHost;
	private String aimUserName;
	private String origUserName;
	private String aimPassword;
	private String origPassword;
	public void setAimFileName(String aimFileName){
		this.aimFileName = aimFileName;
	}
	public void setAimHost(String aimHost) {
		this.aimHost = aimHost;
	}
	public void setAimPath(String aimPath) {
		this.aimPath = aimPath;
	}
	public void setAimUserName(String aimUserName) {
		this.aimUserName = aimUserName;
	}
	public void setOrigFileName(String origFileName) {
		this.origFileName = origFileName;
	}
	public void setOrigHost(String origHost) {
		this.origHost = origHost;
	}
	public void setAimPassword(String aimPassword) {
		this.aimPassword = aimPassword;
	}
	public void setOrigPassword(String origPassword) {
		this.origPassword = origPassword;
	}
	public void setOrigPath(String origPath) {
		this.origPath = origPath;
	}
	public void setOrigUserName(String origUserName) {
		this.origUserName = origUserName;
	}
	public String getAimFileName() {
		return aimFileName;
	}
	public String getAimHost() {
		return aimHost;
	}
	public String getAimPassword() {
		return aimPassword;
	}
	public String getAimPath() {
		return aimPath;
	}
	public String getAimUserName() {
		return aimUserName;
	}
	public String getOrigFileName() {
		return origFileName;
	}
	public String getOrigHost() {
		return origHost;
	}
	public String getOrigPassword() {
		return origPassword;
	}
	public String getOrigPath() {
		return origPath;
	}
	public String getOrigUserName() {
		return origUserName;
	}
	
	public String toString() {
        return  "  aimFileName = "+ aimFileName
    	+"   ,origFileName ="+ origFileName
        +"   ,aimPath ="+ aimPath
        +"   ,origPath ="+ origPath
        +"   ,aimHost ="+ aimHost
        +"   ,origHost ="+ origHost
        +"   ,aimUserName ="+ aimUserName+"   ,origUserName ="+ origUserName
        +"   ,aimPassword ="+ aimPassword+"   ,origPassword ="+ origPassword;
    }
}
