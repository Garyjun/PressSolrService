package com.channelsoft.appframe.upload;

import java.util.ArrayList;

/**
 * <p>Title: USP</p>
 * <p>Description: 维护文件上传信息</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 北京青牛软件技术有限责任公司</p>
 * @author 李炜
 * @version 1.0
 */

public class UploadFileBean {
  /*上传文件保存的目录*/
  private String uploadFileDirName = null;

  /*上传文件名称列表，申请业务的时候，保存客户端文件路径；浏览的时候，保存服务器端文件路径*/
  private java.util.List uploadFileNames;
  public UploadFileBean() {
  }

  public String getUploadFileDirName() {
    return uploadFileDirName;
  }

  public void setUploadFileDirName(String uploadFileDirName) {
    this.uploadFileDirName = uploadFileDirName;
  }

  public java.util.List getUploadFileNames() {
    if (uploadFileNames == null) {
      uploadFileNames = new ArrayList();
    }
    return uploadFileNames;
  }

  public void setUploadFileNames(java.util.List uploadFileNames) {
    this.uploadFileNames = uploadFileNames;
  }

  /**
   * 方法说明：判断文件是否已经上传
   * 输入参数：
   * 输出参数：
   * 作    者：李  炜
   */
  public boolean isFileUploaded(String fileName) {
    return getUploadFileNames().contains(fileName);
  }

  /**
   * 方法说明：删除一个文件名
   * 输入参数：
   * 输出参数：
   * 作    者：李  炜
   */
  public void removeFileName(String fileName) {
    getUploadFileNames().remove(fileName);
  }

  /**
   * 方法说明：增加一个文件名
   * 输入参数：
   * 输出参数：
   * 作    者：李  炜
   */
  public void addFileName(String fileName) {
    getUploadFileNames().add(fileName);
  }

}
