package com.channelsoft.appframe.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;	
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

import com.channelsoft.appframe.exception.ServiceException;

import ftp.FtpBean;
import ftp.FtpException;
import ftp.FtpListResult;
import ftp.FtpObserver;

// import org.apache.commons.net.ftp.FTPClient;

/**
 * 文件FTP下载上传工具类
 * 
 * @author 王志明
 */
public class FtpUtil implements FtpObserver {
	private final static Log logger = LogFactory.getLog(FtpUtil.class);
	private FtpUtilParams ftpUtilParams;

	private FtpBean ftp;
	long num_of_bytes_read = 0;
	long num_of_bytes_write = 0;

	public FtpUtil() {
		ftp = new FtpBean();
	}

	/**
	 * 从远程服务器上下载某个目录下的所有文件，下载完后把远程服务器上的这些文件删除掉
	 * @param host		
	 * @param userName
	 * @param password
	 * @param localDir 
	 * @param remoteDir
	 * @param desc TODO
	 * @return 
	 * @return
	 * @throws ServiceException
	 *
	 * @author 李巍璐
	 */
	public Vector<String> downloadFileFromDir(String host, String userName,
			String password, String localDir, String remoteDir, String desc)
			throws ServiceException {
		logger.debug(desc + "--host:" + host);
		logger.debug(desc + "--userName:" + userName);
		logger.debug(desc + "--password:" + password);
		logger.debug(desc + "--localDir:" + localDir);
		logger.debug(desc + "--remoteDir:" + remoteDir);
		try {
			ftp.ftpConnect(host, userName, password);
		} catch (FtpException e) {
			throw new ServiceException("连接ftp服务器失败,请确认ftp连接参数是否设置正确");
		} catch (IOException e) {
			throw new ServiceException("连接ftp服务器失败," + e.getLocalizedMessage());
		}

		try {
			ftp.setDirectory(remoteDir);

			FtpListResult ftplrs1 = ftp.getDirectoryContent();

			Vector<String> fileNames = new Vector<String>();
			File localDirPath = new File(localDir);
			if (!localDirPath.exists() && !localDirPath.mkdirs()) {
				throw new ServiceException("创建文件目录失败！");
			}
			// 下载文件到临时目录
			while (ftplrs1.next()) {
				int type = ftplrs1.getType();
				if (type == FtpListResult.FILE) {
					String fn = ftplrs1.getName();
					logger.debug(desc + "--fn:" + fn);
					//下载文件
					ftp.getBinaryFile(fn, localDir + File.separator + fn, this);

					fileNames.add(localDir + File.separator + fn);

					//删除文件
					ftp.fileDelete(fn);
				}
			}
			return fileNames;
		} catch (FtpException e) {
			logger.error("目前系统还没有生成相关数据,请稍后下载!", e);
			throw new ServiceException("目前系统还没有生成相关数据,请稍后下载!");
		} catch (IOException e) {
			logger.error("web服务器上没有要下载的文件", e);
			throw new ServiceException("web服务器上没有要下载的文件");
		} catch (Exception e) {
			logger.error("目前系统还没有生成相关数据,请稍后下载!", e);
			throw new ServiceException("目前系统还没有生成相关数据,请稍后下载!");
		}
	}

	/**
	 * 获得Ftp服务器当前目录下有多少符合过滤条件的文件
	 * 
	 * @param path
	 * @param host
	 * @param username
	 * @param password
	 * @param mustFilter
	 *            必须检查的文件扩展名
	 * @param filters
	 *            可选的文件扩展名数组 例如：{"exe","txt"}
	 * @return
	 */
	public long getFilter2FtpFileCount(String path, String host,
			String username, String password, String mustFilter,
			String[] filters) throws ServiceException {
		try {
			ftp.ftpConnect(host, username, password);
		} catch (FtpException e) {
			throw new ServiceException("连接ftp服务器失败,请确认ftp连接参数是否设置正确");
		} catch (IOException e) {
			throw new ServiceException("连接ftp服务器失败," + e.getLocalizedMessage());
		}

		try {
			ftp.setDirectory(path);

			FtpListResult ftplrs = ftp.getDirectoryContent();
			boolean isExist = false;
			String[] mFilter = { mustFilter };
			// 先判断是否有必须要上传的文件类型
			while (ftplrs.next()) {
				int type = ftplrs.getType();
				if (type == FtpListResult.FILE) {

					String file = ftplrs.getName();
					int len = file.lastIndexOf(".");
					// 文件有可能没有文件扩展名，即没有.,例如linux下的文件，滤掉不处理
					if (len > 0) {
						String tmpFile = file.substring(0, len);
						// 对于用我们的FTP控件上传的文件，自动加了版本号，如.001,需要滤掉
						if (tmpFile.lastIndexOf(".") > 0) {
							file = tmpFile;
						}
					}

					String[] splitFile = file.split("\\.");
					String extName = splitFile[splitFile.length - 1];

					if (isContainedFile(extName, mFilter)) {
						isExist = true;
						break;
					}

				}
			}
			if (!isExist) {
				throw new Exception();
			}

			long i = 0, j = 0;
			ftplrs = ftp.getDirectoryContent();
			// 判断符合过滤条件的扩展名
			while (ftplrs.next()) {
				int type = ftplrs.getType();
				if (type == FtpListResult.FILE) {
					i++;
					String file = ftplrs.getName();
					int len = file.lastIndexOf(".");
					// 文件有可能没有文件扩展名，即没有.,例如linux下的文件，滤掉不处理
					if (len > 0) {
						String tmpFile = file.substring(0, len);
						// 对于用我们的FTP控件上传的文件，自动加了版本号，如.001,需要滤掉
						if (tmpFile.lastIndexOf(".") > 0) {
							file = tmpFile;
						}
					}
					logger.debug("第" + i + "个文件是:" + file);

					String[] splitFile = file.split("\\.");
					String extName = splitFile[splitFile.length - 1];
					logger.debug("第" + i + "个文件后缀是:" + extName);

					if (isContainedFile(extName, filters)) {
						j++;
					}

				}
			}

			logger.debug("文件数量：" + i);
			logger.debug("符合过滤条件的文件数量：" + j);
			return j;
		} catch (FtpException ex) {
			throw new ServiceException("Ftp服务器上没有相应目录");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("目前系统还没有上传相关数据!");
		}
	}

	/**
	 * 获得Ftp服务器当前目录下有多少符合过滤条件的文件
	 * 
	 * @param path
	 * @param host
	 * @param username
	 * @param password
	 * @param filters
	 *            文件扩展名数组 例如：{"exe","txt"}
	 * @return
	 */
	public long getFilterFtpFileCount(String path, String host,
			String username, String password, String[] filters)
			throws ServiceException {
		try {
			ftp.ftpConnect(host, username, password);
		} catch (FtpException e) {
			throw new ServiceException("连接ftp服务器失败,请确认ftp连接参数是否设置正确");
		} catch (IOException e) {
			throw new ServiceException("连接ftp服务器失败," + e.getLocalizedMessage());
		}

		try {
			ftp.setDirectory(path);

			FtpListResult ftplrs = ftp.getDirectoryContent();
			long i = 0, j = 0;
			while (ftplrs.next()) {
				int type = ftplrs.getType();
				if (type == FtpListResult.FILE) {
					i++;
					String file = ftplrs.getName();
					int len = file.lastIndexOf(".");
					// 文件有可能没有文件扩展名，即没有.,例如linux下的文件，滤掉不处理
					if (len > 0) {
						String tmpFile = file.substring(0, len);
						// 对于用我们的FTP控件上传的文件，自动加了版本号，如.001,需要滤掉
						if (tmpFile.lastIndexOf(".") > 0) {
							file = tmpFile;
						}
					}
					logger.debug("第" + i + "个文件是:" + file);

					String[] splitFile = file.split("\\.");
					String extName = splitFile[splitFile.length - 1];
					logger.debug("第" + i + "个文件后缀是:" + extName);

					if (isContainedFile(extName, filters)) {
						j++;
					}

				}
			}
			logger.debug("文件数量：" + i);
			logger.debug("符合过滤条件的文件数量：" + j);
			return j;
		} catch (FtpException ex) {
			throw new ServiceException("Ftp服务器上没有相应目录");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("目前系统还没有上传相关数据!");
		}
	}

	/**
	 * 专门用于 业务申请->测试审核 阶段，有时间重构
	 * 
	 * @param path
	 * @param host
	 * @param username
	 * @param password
	 * @param filters
	 * @return
	 */
	public long getFilterFtpFileForTestCheck(String path, String host,
			String username, String password, String[] filters)
			throws ServiceException {
		try {
			ftp.ftpConnect(host, username, password);
		} catch (FtpException e) {
			throw new ServiceException("连接ftp服务器失败,请确认ftp连接参数是否设置正确");
		} catch (IOException e) {
			throw new ServiceException("连接ftp服务器失败," + e.getLocalizedMessage());
		}

		try {
			ftp.setDirectory(path);

			FtpListResult ftplrs = ftp.getDirectoryContent();
			boolean isExistVsdFile = false;
			boolean isExistUsmlFile = false;
			long i = 0;
			while (ftplrs.next()) {
				int type = ftplrs.getType();
				if (type == FtpListResult.FILE) {
					i++;
					String file = ftplrs.getName();
					int len = file.lastIndexOf(".");
					// 文件有可能没有文件扩展名，即没有.,例如linux下的文件，滤掉不处理
					if (len > 0) {
						String tmpFile = file.substring(0, len);
						// 对于用我们的FTP控件上传的文件，自动加了版本号，如.001,需要滤掉
						if (tmpFile.lastIndexOf(".") > 0) {
							file = tmpFile;
						}
					}
					logger.debug("第" + i + "个文件是:" + file);

					String[] splitFile = file.split("\\.");
					String extName = splitFile[splitFile.length - 1];
					logger.debug("第" + i + "个文件后缀是:" + extName);

					if (isContainedFile(extName, filters)) {
						if (extName.equalsIgnoreCase("vsd")) {
							isExistVsdFile = true;
						}
						if (extName.equalsIgnoreCase("usml")) {
							isExistUsmlFile = true;
						}
					}

				}
			}
			if (!isExistVsdFile) {
				throw new ServiceException("业务需要至少一个visio文件");
			}
			if (!isExistUsmlFile) {
				throw new ServiceException("业务需要至少一个usml文件");
			}
			logger.debug("文件数量：" + i);
			return i;
		} catch (FtpException ex) {
			throw new ServiceException("Ftp服务器上没有相应目录");
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * 判断文件的扩展名是否在要求的filter中
	 * 
	 * @param extName
	 * @param filters
	 *            文件扩展名数组 例如：{"exe","txt"}
	 * @return
	 */
	private boolean isContainedFile(String extName, String[] filters) {
		boolean ret = false;
		// 如果没有，则不过滤
		if (filters == null || filters.length < 1) {
			ret = true;
			return ret;
		}
		for (int i = 0; i < filters.length; i++) {
			if (extName.equalsIgnoreCase(filters[i])) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * 获得Ftp服务器当前目录下的所有文件，如果没有符合过滤条件的，则异常退出
	 * 
	 * @param path
	 * @param host
	 * @param username
	 * @param password
	 * @param filters
	 *            文件扩展名 "txt"
	 * @return
	 */
	public long getFtpFileCount(String path, String host, String username,
			String password, String filter) throws ServiceException {
		try {
			ftp.ftpConnect(host, username, password);
		} catch (FtpException e) {
			throw new ServiceException("连接ftp服务器失败,请确认ftp连接参数是否设置正确");
		} catch (IOException e) {
			throw new ServiceException("连接ftp服务器失败," + e.getLocalizedMessage());
		}

		try {
			ftp.setDirectory(path);

			String[] filters = { filter };
			boolean isExist = false;

			FtpListResult ftplrs = ftp.getDirectoryContent();
			long i = 0;
			while (ftplrs.next()) {
				int type = ftplrs.getType();
				if (type == FtpListResult.FILE) {
					i++;
					String file = ftplrs.getName();
					int len = file.lastIndexOf(".");
					// 文件有可能没有文件扩展名，即没有.,例如linux下的文件，滤掉不处理
					if (len > 0) {
						String tmpFile = file.substring(0, len);
						// 对于用我们的FTP控件上传的文件，自动加了版本号，如.001,需要滤掉
						if (tmpFile.lastIndexOf(".") > 0) {
							file = tmpFile;
						}
					}
					logger.debug("第" + i + "个文件是:" + file);

					String[] splitFile = file.split("\\.");
					String extName = splitFile[splitFile.length - 1];
					logger.debug("第" + i + "个文件后缀是:" + extName);

					if (isContainedFile(extName, filters)) {
						isExist = true;
					}

				}
			}
			if (!isExist) {
				throw new Exception();
			}
			logger.debug("文件数量：" + i);
			return i;
		} catch (FtpException ex) {
			throw new ServiceException("Ftp服务器上没有相应目录");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("目前系统还没有上传相关数据!");
		}
	}

	public void deleteUsbossFtpFiles(String path, String host, String username,
			String password) throws ServiceException {
		try {
			ftp.ftpConnect(host, username, password);
		} catch (FtpException e) {
			throw new ServiceException("连接ftp服务器失败,请确认ftp连接参数是否设置正确");
		} catch (IOException e) {
			throw new ServiceException("连接ftp服务器失败," + e.getLocalizedMessage());
		}

		try {
			ftp.setDirectory(path);
			FtpListResult ftplrs = ftp.getDirectoryContent();
			while (ftplrs.next()) {
				int type = ftplrs.getType();
				String dirName = "";
				if (type == FtpListResult.DIRECTORY) {
					dirName = ftplrs.getName();
					logger.debug("目录名:" + dirName);
				}
				String p = path + dirName;

				logger.debug("删除:" + p + " 下的所有文件");
				deleteFtpFiles(p, host, username, password);

			}
		} catch (FtpException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 删除Ftp服务器当前目录下的所有文件
	 * 
	 * @param path
	 * @param host
	 * @param username
	 * @param password
	 * @throws ServiceException
	 */
	public void deleteFtpFiles(String path, String host, String username,
			String password) throws ServiceException {
		try {
			ftp.ftpConnect(host, username, password);
		} catch (FtpException e) {
			throw new ServiceException("连接ftp服务器失败,请确认ftp连接参数是否设置正确");
		} catch (IOException e) {
			throw new ServiceException("连接ftp服务器失败," + e.getLocalizedMessage());
		}

		try {
			ftp.setDirectory(path);
			FtpListResult ftplrs = ftp.getDirectoryContent();
			while (ftplrs.next()) {
				int type = ftplrs.getType();
				if (type == FtpListResult.FILE) {
					String filename = ftplrs.getName();
					ftp.fileDelete(filename);
				}
			}
		} catch (FtpException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 从ftp服务器目录下载文件到Web服务器的一个临时目录，生成一个zip文件,然后删除该临时目录
	 * 注意：
	 *    1、如果文件名为空，则下载指定目录下的所有文件。
	 * 	  2、如果文件不为空，且在指定的文件目录下存在，则下载这个文件。
	 * 
	 * 限制：
	 *    1、在HP的Unix下，通过ftp文件下载存在问题。在linux下的ftp下载没有问题。 
	 * 
	 * @param localeBaseDir 本地下载根目录
	 * @param path  Ftp登录后的相对路径
	 * @param fileName Ftp上文件名
	 * @param host Ftp主机名
	 * @param username Ftp用户名
	 * @param password Ftp密码
	 * @return 文件名
	 * @throws ServiceException
	 * @author 王志明 张乐雷
	 * @date 2007-6-21 上午09:19:32 
	 */
	@Deprecated
	public String downloadFromDir(String localeBaseDir, String path,
			String fileName, String host, String username, String password)
			throws ServiceException {
		try {
			ftp.ftpConnect(host, username, password);
		} catch (FtpException e) {
			throw new ServiceException("连接ftp服务器失败,请确认ftp连接参数是否设置正确");
		} catch (IOException e) {
			throw new ServiceException("连接ftp服务器失败," + e.getLocalizedMessage());
		}

		try {
			ftp.setDirectory(path);

			FtpListResult ftplrs1 = ftp.getDirectoryContent();

			// 文件下载目录
			String downlaodToTmpDir = saveDownloadDir(localeBaseDir, path);
			// 下载文件到下载目录
			while (ftplrs1.next()) {
				int type = ftplrs1.getType();
				if (type == FtpListResult.FILE) {
					String fn = ftplrs1.getName();
					if (fileName != null) {
						if (fn.equals(fileName)) {
							ftp.getBinaryFile(fn, downlaodToTmpDir
									+ File.separator + fn, this);
						}
					} else {
						ftp.getBinaryFile(fn, downlaodToTmpDir + File.separator
								+ fn, this);
					}

				}
			}
			String zipFileName = zipFile(localeBaseDir, downlaodToTmpDir);
			deleteFromDir(new File(downlaodToTmpDir));
			return zipFileName;
		} catch (FtpException e) {
			e.printStackTrace();
			throw new ServiceException("目前系统还没有生成相关数据,请稍后下载!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("web服务器上没有要下载的文件");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("目前系统还没有生成相关数据,请稍后下载!");
		}
	}

	/**
	 * 删除目录中的所有文件
	 * 
	 * @param localeDir
	 */
	protected void deleteFromDir(File localeDir) {
		File[] fileList = localeDir.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.exists() && !file.delete()) {
				throw new ServiceException("删除文件失败！");
			}
		}
		if(!localeDir.delete()){
			throw new ServiceException("删除文件目录失败！");
		}
	}

	/**
	 * 压缩目录下的所有文件
	 * 
	 * @param zipFileName
	 * @param localeDir
	 * @throws Exception
	 */
	protected String zipFile(String localeDir, String downloadDir)
			throws IOException {
		String zipDir = saveZipDir(localeDir);
		String zipFileName = String.valueOf(System.currentTimeMillis())
				+ ".zip";

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipDir
				+ File.separator + zipFileName));
		try {
			zip(out, downloadDir);
		} finally {
			out.close();
		}
		return zipFileName;
	}

	/**
	 * 保存zip文件的目录
	 * 
	 * @param localeDir
	 *            本地下载文件的根目录
	 * @return
	 */
	protected String saveZipDir(String localeDir) {
		String zipDir = localeDir + File.separator + "zip";
		File dir = new File(zipDir);
		if (!dir.exists() && !dir.mkdir()) {
			throw new ServiceException("创建文件目录失败！");
		}
		return zipDir;
	}

	/**
	 * 保存下载文件的目录
	 * @param localeDir	本地下载文件的根目录
	 * @param path
	 * @return
	 *
	 * @author 王志明
	 * CreateDate: 2006-12-30
	 */
	protected String saveDownloadDir(String localeDir, String path) {
		String relativeDir = String.valueOf(System.currentTimeMillis());
		String downloadDir = localeDir + File.separator + relativeDir;
		File dir = new File(downloadDir);
		if (!dir.exists() && !dir.mkdir()) {
			throw new ServiceException("创建文件目录失败！");
		}
		return downloadDir;
	}

	/**
	 * 生成zip文件
	 * 
	 * @param out
	 * @param dir
	 * @throws IOException
	 */
	protected void zip(ZipOutputStream out, String dir) throws IOException {
		File[] fileList = new File(dir).listFiles();

		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];

			out.putNextEntry(new ZipEntry(file.getName()));
			FileInputStream in = new FileInputStream(file);
			try {
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
			} finally {
				in.close();
			}
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param localeFile
	 *            本地文件名
	 * @param remoteFile
	 *            远程文件名
	 * @param path
	 *            Ftp登录后的相对路径
	 * @param host
	 *            主机名
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 0 成功 1 失败
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void downloadFile(String localeFile, String remoteFile,
			String path, String host, String username, String password)
			throws ServiceException {
		FtpClient client = null;
		RandomAccessFile getFile = null;
		TelnetInputStream fget = null;
		DataInputStream puts = null;
		try {
			client = getConnect(path, host, username, password);

			int ch;
			File file = new File(localeFile);

			getFile = new RandomAccessFile(file, "rw");
			getFile.seek(0);

			fget = client.get(remoteFile);

			puts = new DataInputStream(fget);
			while ((ch = puts.read()) >= 0) {
				getFile.write(ch);
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			throw new ServiceException("请确认文件是否存在！");
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new ServiceException("文件读取异常", ex);
		} finally {

			try {
				if (fget != null)
					fget.close();
				if (getFile != null)
					getFile.close();
				if (client != null)
					client.closeServer();
				if (puts != null)
					puts.close();
			} catch (IOException e) {
				throw new ServiceException("流关闭异常", e);
			}

		}
	}

	/**
	 * 把一个端服务器上的文件FTP到另一个服务器.
	 * 服务器可以都是远端服务器
	 * @author @author 胡海波
	 * 2007-4-18
	 */
	public int ftpFileFromOneAddressToOtherAddress(FtpUtilParams transParam) {
		FtpClient origClient = null;
		FtpClient aimClient = null;
		TelnetInputStream fget = null;
		TelnetOutputStream out = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("ftpParams ==" + transParam);
			}
			origClient = getConnect(transParam.getOrigPath(), transParam
					.getOrigHost(), transParam.getOrigUserName(), transParam
					.getOrigPassword());
			if (origClient == null) {
				return 1;
			}
			aimClient = getConnect(transParam.getAimPath(), transParam
					.getAimHost(), transParam.getAimUserName(), transParam
					.getAimPassword());

			if (aimClient == null) {
				return 1;
			}
			File file = new File(transParam.getAimFileName());
			fget = origClient.get(transParam.getOrigFileName());
			out = aimClient.put(file.getName());
			int c = 0;
			while ((c = fget.read()) != -1) {
				out.write(c);
			}

			return 1;
		} catch (FileNotFoundException ex) {
			throw new ServiceException("请确认文件是否存在！");
		} catch (IOException exp) {
			throw new ServiceException("文件读取异常", exp);
		} finally {
			try {
				if (out != null)
					out.close();
				if (fget != null)
					fget.close();
				if (aimClient != null)
					aimClient.closeServer();
				if (origClient != null)
					origClient.closeServer();
			} catch (IOException e) {
				throw new ServiceException("流关闭异常", e);
			}

		}
	}

	/**
	 * FTP文件到指定的服务器上。（注意：如果是在广域网上传输，此方法会有问题，不能上传。）
	 * 
	 * @param fileName
	 *            上传文件名
	 * @param path
	 *            Ftp登录后的相对路径
	 * @param host
	 *            主机名
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 0 成功 1 失败
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static int uploadFile(String fileName, String path, String host,
			String username, String password) throws ServiceException {
		FtpClient client = getConnect(path, host, username, password);
		FileInputStream in = null;
		TelnetOutputStream out = null;
		if (client == null) {
			return 1;
		}
		try {
			File file = new File(fileName);
			in = new FileInputStream(file);
			out = client.put(file.getName());
			int c = 0;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			return 0;
		} catch (FileNotFoundException ex) {
			logger.debug("没有要上传的文件，请确认！");
			return 1;
		} catch (IOException exp) {
			throw new ServiceException("文件读取异常", exp);
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				if (client != null)
					client.closeServer();
			} catch (IOException e) {
				throw new ServiceException("流关闭异常", e);
			}

		}

	}

	/**
	 * @param path
	 *            Ftp登录后的相对路径
	 * @param host
	 *            主机名
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws IOException
	 */
	private static FtpClient getConnect(String path, String host,
			String username, String password) throws ServiceException {
		try {
			FtpClient client = new FtpClient(host);
			client.login(username, password);
			client.binary();
			client.cd(path);
			return client;
		} catch (IOException ex) {
			throw new ServiceException("连接ftp服务器失败,请确认ftp连接参数是否设置正确");
		}
	}

	// Implemented for FtpObserver interface.
	// To monitor download progress.
	public void byteRead(int bytes) {
		// num_of_bytes_read += bytes;
		// System.out.println(num_of_bytes_read + " of bytes read already.");
	}

	// Needed to implements by FtpObserver interface.
	public void byteWrite(int bytes) {
		// num_of_bytes_write += bytes;
		// System.out.println(num_of_bytes_write + " of bytes read already.");
	}

	public FtpUtilParams getFtpUtilParams() {
		return ftpUtilParams;
	}

	public void setFtpUtilParams(FtpUtilParams ftpUtilParams) {
		this.ftpUtilParams = ftpUtilParams;
	}
}