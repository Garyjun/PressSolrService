package com.channelsoft.appframe.upload;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;

import com.channelsoft.appframe.exception.ServiceException;

public class FileUploadUtils {
	protected static final Logger logger = Logger
			.getLogger(FileUploadUtils.class);

	private FileUploadManager fileUploadManager;

	private static FileUploadUtils instance = new FileUploadUtils();

	public static FileUploadUtils getInstance() {
		return instance;
	}

	private FileUploadUtils() {
		super();
		fileUploadManager = new FileUploadManager();
	}

	/**
	 * 创建上传文件的存放文件夹
	 * @param fileFolder  文件夹名称
	 */
	private void createFileFolder(String fileFolder) {
		File file = new File(fileFolder);
		if (!file.exists()) {
			if (file.mkdirs()) {
				logger.debug("\n = the mkdirs is:" + file.getAbsolutePath()
						+ "\n " + file.getPath() + "\n" + file.getName());
			} else {
				throw new ServiceException("创建文件目录失败！");
			}
		} else {
			if (file.isFile() && !file.delete()) {
				throw new ServiceException("删除文件失败！");
			}
		}
	}

	public String uploadOneFile(String name, File file, String uploadFolder)
			throws Exception {
		try {
			// 创建文件上传路径
			createFileFolder(uploadFolder);

			// 上传文件到web服务器
			return fileUploadManager.uploadOne(name, file, uploadFolder);
		} catch (Exception ex) {
			logger.error("上传文件失败!" + ex.getMessage());
			throw new Exception("上传文件失败!", ex);
		}
	}

	/**
	 * 上传文件到web服务器,多在action中调用
	 * @param files 
	 * @param uploadFolder
	 * @param fileNames 文件名称列表(不可重复)
	 * @return
	 * @throws Exception
	 */
	public UploadFileBean uploadFiles(Map<String, File> files,
			String uploadFolder) throws Exception {
		try {
			// 创建文件上传路径
			createFileFolder(uploadFolder);

			// 上传文件到web服务器
			return fileUploadManager.upload(files, null, uploadFolder);
		} catch (Exception ex) {
			logger.error("上传文件失败!" + ex.getMessage());
			throw new Exception("上传文件失败!", ex);
		}
	}
}
