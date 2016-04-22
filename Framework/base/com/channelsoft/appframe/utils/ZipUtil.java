package com.channelsoft.appframe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.channelsoft.appframe.exception.ServiceException;

/**
 * 
 * <dl>
 * <dt>ZipUtils</dt>
 * <dd>Description:压缩解压文件工具类，如果出现中文文件名这个类出错</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2007-5-24</dd>
 * </dl>
 */
public class ZipUtil {
	private static Log logger = LogFactory.getLog(ZipUtil.class);

	/**
	 * 转换字符编码格式
	 */
	private static String makeToGB(String str) {
		try {
			return new String(str.getBytes("8859_1"), "GB2312");
		} catch (UnsupportedEncodingException ioe) {
			return str;
		}
	}

	/**
	 * 解压zip包，可以解压中文字符的压缩文件
	 * 
	 * @param zipFileName
	 * @param outputDirectory
	 * @throws Exception
	 */
	public static void unzip(String zipFileName, String outputDirectory) {
		try {
			org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(
					zipFileName);
			java.util.Enumeration e = zipFile.getEntries();
			org.apache.tools.zip.ZipEntry zipEntry = null;
			createDirectory(outputDirectory, "");
			while (e.hasMoreElements()) {
				zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
				System.out.println("正在解压: " + zipEntry.getName());
				String name = null;
				if (zipEntry.isDirectory()) {
					name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(outputDirectory + File.separator + name);
					if (!f.mkdir()) {
						throw new ServiceException("创建文件目录失败！");
					}
				} else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');

					if (fileName.indexOf("/") != -1) {
						createDirectory(outputDirectory, fileName.substring(0,
								fileName.lastIndexOf("/")));
						fileName = fileName.substring(
								fileName.lastIndexOf("/") + 1, fileName
										.length());
					}

					File f = new File(outputDirectory + File.separator
							+ zipEntry.getName());

					if (!f.createNewFile()) {
						throw new ServiceException("创建文件失败！");
					}
					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);

					byte[] by = new byte[1024];
					int c;
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					out.close();
					in.close();
				}
			}

		} catch (ZipException e) {
			logger.error("读取zip文件时异常", e);
			throw new ServiceException("读取zip文件时异常", e);
		} catch (FileNotFoundException e) {
			logger.error("zip文件没有找到", e);
			throw new ServiceException("zip文件没有找到", e);
		} catch (IOException e) {
			logger.error("读取zip文件时IO异常", e);
			throw new ServiceException("读取zip文件时IO异常", e);
		}

	}

	private static void createDirectory(String directory, String subDirectory) {
		String dir[];
		File fl = new File(directory);
		try {
			if ("".equals(subDirectory) && !fl.exists()) {
				if (!fl.mkdir()) {
					throw new ServiceException("创建文件目录失败！");
				}
			} else if (!"".equals(subDirectory)) {
				dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; i++) {
					File subFile = new File(directory + File.separator + dir[i]);
					if (!subFile.exists() && !subFile.mkdir()) {
						throw new ServiceException("创建文件目录失败！");
					}
					directory += File.separator + dir[i];
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 解压文件
	 * 
	 * @param inputFilePath
	 * @throws ServiceException
	 */
	public static void unZip(String inputFilePath) throws ServiceException {
		ZipInputStream zipInputStream = null;
		ZipEntry zipEntry = null;
		try {

			// 检查是否是ZIP文件
			checkFileIsExist(inputFilePath);
			// 建立与目标文件的输入连接
			zipInputStream = new ZipInputStream(new FileInputStream(
					inputFilePath));

			zipEntry = zipInputStream.getNextEntry();
			String dirname = createDir(inputFilePath);
			while (zipEntry != null) {
				String zipEntryName = zipEntry.getName();
				String changeZipEntryName = makeToGB(zipEntryName);
				String filePath = dirname + File.separator;
				int lastPoint = replaceLine(changeZipEntryName).lastIndexOf(
						'\\');
				if (lastPoint != -1) {
					unZipWriteDir(filePath
							+ replaceLine(changeZipEntryName).substring(0,
									lastPoint));
				}
				if (zipEntry.isDirectory()) {
					unZipWriteDir(zipEntryName);
				} else {
					String fileName = replaceLine(changeZipEntryName);
					if (StringUtils.isNotBlank(fileName))
						unZipWriteFile(zipInputStream, filePath + zipEntryName);
				}
				zipEntry = zipInputStream.getNextEntry();
			}
		} catch (IOException i) {
			throw new ServiceException("文件流处理异常");
		} catch (IllegalArgumentException e) {
			throw new ServiceException("压缩格式异常");
		} finally {
			try {
				if (zipInputStream != null)
					zipInputStream.close();
			} catch (IOException e) {
				throw new ServiceException("文件流关闭异常");
			}
		}
	}

	private static void checkFileIsExist(String inputFilePath)
			throws ServiceException {
		org.apache.tools.zip.ZipFile zipFile = null;
		try {
			File inFile = new File(inputFilePath);
			if (!inFile.exists() || inFile.isDirectory()) {
				throw new ServiceException("压缩文件不存在");
			}
			zipFile = new org.apache.tools.zip.ZipFile(inFile);
		} catch (ZipException zipe) {
			throw new ServiceException("文件格式异常");
		} catch (IOException ioe) {
			throw new ServiceException("文件读取异常");
		} finally {
			try {
				if (zipFile != null)
					zipFile.close();
			} catch (IOException e) {
				throw new ServiceException("文件关闭异常");
			}
		}
	}

	private static String replaceLine(String str) {
		if (StringUtils.isBlank(str))
			return "";
		return str.replace('/', '\\');
	}

	private static void unZipWriteDir(String fileDir) {
		File dirs = new File(replaceLine(makeToGB(fileDir)));
		if (!dirs.mkdir()) {
			throw new ServiceException("创建文件目录失败！");
		}
	}

	private static void unZipWriteFile(ZipInputStream zipInputStream,
			String filePath) throws ServiceException {
		byte[] bytes = new byte[1024];
		int in;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filePath);
			while ((in = zipInputStream.read(bytes, 0, bytes.length)) != -1)
				fileOutputStream.write(bytes, 0, in);
		} catch (IOException ioe) {
			throw new ServiceException("文件读取异常");
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				throw new ServiceException("文件流关闭异常");
			}
		}
	}

	private static String createDir(String inputFilePath) {
		int lastPoint = inputFilePath.lastIndexOf('.');
		if (lastPoint != -1)
			inputFilePath = inputFilePath.substring(0, lastPoint);
		File newdir = new File(inputFilePath);
		if (!newdir.mkdir()) {
			throw new ServiceException("创建文件目录失败！");
		}
		return inputFilePath;
	}

	/**
	 * 压缩文件和目录
	 * 
	 * @param inputFileName
	 *            ：被压缩的目录或文件的绝对路径
	 * @param outputPath
	 *            ：压缩后文件的绝对路径
	 * @throws ServiceException
	 */
	public static void zip(String inputFileName, String outputPath)
			throws ServiceException {
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(outputPath));
			File file = new File(inputFileName);
			if (file.isDirectory())
				zipDir(file, out);
			else
				zipFile(file, out);
		} catch (IOException e) {
			throw new ServiceException("压缩文件处理异常");
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				throw new ServiceException("关闭压缩文件异常");
			}
		}
	}

	private static void zipFile(File file, ZipOutputStream out)
			throws ServiceException {
		zipWriteFile(file, out, file.getName());
	}

	private static void zipDir(File file, ZipOutputStream out)
			throws ServiceException {
		zipDir(file, out, "");
	}

	private static void zipDir(File file, ZipOutputStream out, String base)
			throws ServiceException {
		if (file.isDirectory()) {
			zipWriteDir(file, out, base);
			return;
		}
		zipWriteFile(file, out, base);
	}

	/**
	 * 压缩文件操作写目录
	 */
	private static String zipWriteDir(File file, ZipOutputStream out,
			String base) throws ServiceException {

		try {
			File[] fl = file.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			String temp = (base.length() == 0) ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zipDir(fl[i], out, temp + fl[i].getName());
			}

			return temp;
		} catch (IOException e) {
			throw new ServiceException("压缩文件处理异常");
		}
	}

	/**
	 * 压缩文件操作写文件
	 */
	private static void zipWriteFile(File file, ZipOutputStream out, String base)
			throws ServiceException {
		FileInputStream in = null;
		try {
			out.putNextEntry(new ZipEntry(base));
			in = new FileInputStream(file);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
		} catch (IOException e) {
			System.out.print(e.getMessage());
			throw new ServiceException("压缩文件处理异常");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw new ServiceException("流关闭异常");
			}
		}

	}

	/**
	 * 
	 * @param f
	 * @param out
	 * @param base
	 * @throws ServiceException
	 */
	private static void zipCN(File f, org.apache.tools.zip.ZipOutputStream out,
			String base) throws ServiceException {
		FileInputStream in = null;
		try {
			if (f.isDirectory()) {
				logger.debug("目录");
				File[] fl = f.listFiles();
				out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
				base = base.length() == 0 ? "" : base + "/";
				for (int i = 0; i < fl.length; i++) {
					zipCN(fl[i], out, base + fl[i].getName());
				}
			} else {
				logger.debug("文件");
				out.putNextEntry(new org.apache.tools.zip.ZipEntry(StringUtils
						.isBlank(base) ? f.getName() : base));
				System.out.println("1:" + out.toString());
				System.out.println("2:" + out.hashCode());
				in = new FileInputStream(f);
				int b;
				System.out.println("base:" + base);
				while ((b = in.read()) != -1) {
					out.write(b);
				}
			}
		} catch (IOException e) {
			throw new ServiceException("压缩文件出现异常：" + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException se) {
				throw new ServiceException("关闭文件出现异常：" + se.getMessage());
			}
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param inputFile
	 * @param zipFileName
	 * @throws ServiceException
	 *             注： 1、如果inputFile是目录，则将该目录下的文件以及目录统一压缩成一个文件。 例：new
	 *             File("D:\\kk") 2、如果inputFile是一个目录，则压缩后的zip文件的路径不能设置在这个目录中
	 */
	public static void zipCN(File inputFile, String zipFileName)
			throws ServiceException {

		org.apache.tools.zip.ZipOutputStream out = null;
		try {
			out = new org.apache.tools.zip.ZipOutputStream(
					new FileOutputStream(zipFileName));
		} catch (FileNotFoundException se) {
			throw new ServiceException("文件不存在：" + se.getMessage());
		}
		try {
			zipCN(inputFile, out, "");
			System.out.println("zip done");
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e1) {
				throw new ServiceException("关闭文件出现异常:" + e1.getMessage());
			}
		}
	}

	/**
	 * 删除文件夹下所有内容，包括此文件夹删除文件夹下所有内容，包括此文件夹
	 * 
	 * @param f
	 * @throws IOException
	 */
	public static void delAll(File f) throws IOException {
		if (!f.exists())// 文件夹不存在不存在
		{
			throw new IOException("指定目录不存在:" + f.getName());
		}

		boolean rslt = true;// 保存中间结果

		if (!(rslt = f.delete())) {// 先尝试直接删除

			// 若文件夹非空。枚举、递归删除里面内容
			File subs[] = f.listFiles();

			for (int i = 0; i <= subs.length - 1; i++) {

				if (subs[i].isDirectory()) {
					delAll(subs[i]);// 递归删除子文件夹内容
				}
				rslt = subs[i].delete();// 删除子文件夹本身
			}
			rslt = f.delete();// 删除此文件夹本身
		}
		if (!rslt)
			throw new IOException("无法删除:" + f.getName());
		return;
	}
}
