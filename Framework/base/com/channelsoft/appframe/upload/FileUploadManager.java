package com.channelsoft.appframe.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

import com.channelsoft.appframe.exception.ServiceException;

/**
 * <p>Title: USBOSS</p>
 * <p>Description: 文件上传管理器</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 北京青牛软件技术有限责任公司</p>
 * @author 李炜
 * @version 1.0
 */

public class FileUploadManager {
	public final static Logger logger = Logger.getLogger("FileUploadManager");

	public FileUploadManager() {
	}

	/**
	 * 方法说明：根据上传的文件名，确定保存的文件名
	 * 输入参数：
	 * 输出参数：
	 * 作    者：李  炜
	 */
	private String getSavedFileName(String dirName, String uploadFileName) {
		
		uploadFileName = uploadFileName.substring(uploadFileName
				.lastIndexOf(SystemUtils.FILE_SEPARATOR) + 1);
		

		return dirName + uploadFileName;
	}

	/**
	 * 方法说明：修改目录名称
	 * 输入参数：
	 * 输出参数：
	 * 作    者：李  炜
	 */
	public boolean commitUploadFile(String srcDirName, String processID) {
		int index = srcDirName.lastIndexOf(SystemUtils.FILE_SEPARATOR, srcDirName.length() - 2);
		String prefix = srcDirName.substring(0, index + 1);
		logger.debug("修改后的目录名称＝" + prefix + processID);
		return renameTempDir(srcDirName, prefix + processID);
	}

	/**
	 * 方法说明：修改目录名称
	 * 输入参数：
	 * 输出参数：
	 * 作    者：李  炜
	 */
	private boolean renameTempDir(String srcDirName, String destDirName) {
		File srcDir = new File(srcDirName);
		if (srcDir.isDirectory()) {
			File destDir = new File(destDirName);
			if (destDir.exists()) {
				logger.warn("目标目录" + destDirName + "已经存在");
				return false;
			}
			if(srcDir.renameTo(destDir)){
				logger.debug("修改目录名称成功:源目录" + srcDirName + " - 目标目录" + destDirName);
			}else{
				throw new ServiceException("修改目录名称失败！");
			}
			return true;
		}
		logger.warn("修改目录名称失败:源目录" + srcDirName + "不是合法的目录");
		return false;
	}

	private String createDir(String baseDirName, String subDirName)
			throws Exception {
		String tempDirName = baseDirName + subDirName + SystemUtils.FILE_SEPARATOR;
		File dir = new File(tempDirName);
		boolean dirExist = true;
		if (!dir.exists()) {
			dirExist = dir.mkdirs();
		}
		if (!dirExist) {
			throw new Exception("创建临时目录失败：" + tempDirName);
		}
		return tempDirName;
	}

	

	

	/**
	 * 方法说明：删除上传文件
	 * 输入参数：
	 * 输出参数：上传文件保存的临时目录名称及文件列表信息（客户端文件路径）
	 * 作    者：卢伟
	 */
	public UploadFileBean deleteUploadFile(String fileName,
			UploadFileBean uploadFileBean) throws Exception {
		uploadFileBean.removeFileName(fileName);
		deleteFile(uploadFileBean.getUploadFileDirName() + fileName);
		return uploadFileBean;
	}

	/**
	 * 方法说明：删除一个上传文件
	 * 输入参数：
	 * 输出参数：
	 * 作    者：卢伟
	 */
	private void deleteFile(String path) {
		logger.debug("删除文件" + path);
		File f = new File(path);
		try {
			f.delete();
		} catch (Exception e) {
		}
	}

	

	/**
	 * 方法说明：读取上传文件列表
	 * 输入参数：
	 * 输出参数：保存服务器端文件路径
	 * 作    者：李  炜
	 */
	public String[] getUploadedFileList(String baseDirName, String processID) {
		String prefix = baseDirName + processID;
		File dir = new File(prefix);
		if (dir.exists()) {
			if (dir.isDirectory()) {
				String fileList[] = dir.list();
				for (int i = 0; i < fileList.length; i++) {
					fileList[i] = prefix + SystemUtils.FILE_SEPARATOR + fileList[i];
				}
				return fileList;
			}
		}
		logger.warn("指定的工作流没有对应的上传文件目录：processID=" + processID);
		return null;
	}
	
	public String uploadOne(String name,File file, String baseDirName) throws Exception {
		UploadFileBean uploadFileBean = new UploadFileBean();
		
		uploadFileBean.setUploadFileDirName(createDir(baseDirName, ""));
	    String abosultePath=uploadFile(name,file, uploadFileBean);
		uploadFileBean.addFileName(abosultePath);
		
		return abosultePath;
	
	}
	
	
	private String uploadFile(String fileName,File file, UploadFileBean uploadFileBean)
			throws Exception {
		try {
			long fileSize = file.length();
			if (fileSize != 0) {
				String localFilePath = getSavedFileName(uploadFileBean
						.getUploadFileDirName(), fileName); // 存储到本地的文件路径
				FileOutputStream fos = new FileOutputStream(localFilePath);
				InputStream is = new FileInputStream(file);
				try {
					byte buffer[] = new byte[1024];
					for (int bytesRead = 0; (bytesRead = is.read(buffer)) != -1;) {
						fos.write(buffer, 0, bytesRead);
					}
				} finally {
					fos.close();
					is.close();
				}
				logger.debug(file.getName() + "上传到" + localFilePath);
				file.delete();
				return file.getAbsolutePath();
			}
			return null;
		} catch (FileNotFoundException ex) {
			logger.debug("上传的文件不存在:" + ex.getMessage());
			throw new Exception("上传的文件不存在", ex);
		} catch (IOException ex) {
			logger.debug("文件上传异常:" + ex.getMessage());
			throw new Exception("文件上传异常", ex);
		}
	}

	/**
	 * 方法说明：上传文件，首先保存到临时目录中，最终提交后，再修改目录名称
	 * 输入参数：String baseDirName 根目录名称；
	 * 
	 * 输出参数：上传文件保存的临时目录名称及文件列表信息（客户端文件路径）
	 * 作    者：李  炜
	 */
	public UploadFileBean upload(Map<String, File> files, UploadFileBean uploadFileBean,
			String baseDirName) throws Exception {
		if (uploadFileBean == null) {
			uploadFileBean = new UploadFileBean();
		}
		uploadFileBean.setUploadFileDirName(createDir(baseDirName, ""));
		Iterator<String> it=files.keySet().iterator();
		while(it.hasNext()) {
			String key=it.next();
			String abosultePath=uploadFile(key,files.get(key), uploadFileBean);
			uploadFileBean.addFileName(abosultePath);
		}
		
		return uploadFileBean;
	}
	
	/**
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 * 
	 * @author tangxj
	 * @date Jul 30, 2009 2:57:53 PM 
	 */
	public boolean upload(File srcFile, String destFileName){
		return upload(srcFile, new File(destFileName));
	}
	
	public boolean upload(String srcFileName, String destFileName){
		return upload(new File(srcFileName), new File(destFileName));
	}
	
	public boolean upload(File srcFile, File destFile){
		try {
			FileInputStream fis = new FileInputStream(srcFile);
			this.createDir(destFile.getParent(), "");
			FileOutputStream fos = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int readBytes = 0;
			
			while((readBytes = fis.read(buffer, 0, 1024)) != -1){
				fos.write(buffer, 0, readBytes);
			}
			fos.flush();
			fos.close();
			fis.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteUploadFile(String fileName){
		File file = new File(fileName);
		if(!file.exists()){
			return true;
		}
		return file.delete();
	}
	
	public boolean checkExist(String fileName){
		File file = new File(fileName);
		return file.exists();
	}
}
