package com.brainsoon.solr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileHandler {

	protected final static Log logger = LogFactory.getLog(FileHandler.class);

	/**
	 * 读取目录文件
	 * 
	 * @param dirname
	 *            目录名称
	 * @return list集合
	 */
	public static List<String> getFiles(String dirname) throws Exception {
		List<String> file_names = null;
		File dir = new File(dirname);
		if (dir.exists()) {
			file_names = new ArrayList<String>();
			File[] files = dir.listFiles();
			// 排序
			Arrays.sort(files, new CompratorByLastModified());
			for (int i = 0; i < files.length; i++) {

				if ("CVS".equals(files[i].getName()))
					continue;
				// 获取当文件最后修改时间
				// String creatime=format("yyyy-MM-dd HH:mm:ss",new
				// Date(files[i].lastModified()));
				if (files[i].isHidden()) {// 判断是隐藏文件
					file_names.add(files[i].getName());
				} else if (files[i].isDirectory()) {// 判断是目录
					file_names.add(files[i].getName());
				} else {// 普通文件
					file_names.add(files[i].getName());
				}
			}
		} else {
			logger.info("该目录不存在！");
		}
		return file_names;
	}

	/**
	 * 获取最大值
	 * 
	 * @param initValue
	 *            初始值
	 * @param replaceString
	 *            替代字符串
	 * @param filenames
	 *            文件名列表
	 * @return
	 */
	public static int getMax(int initValue, String replaceString,
			String endString, List<String> filenames) {
		int max = initValue;
		List<Integer> list = new ArrayList<Integer>();
		if (filenames != null && filenames.size() > 0) {
			for (String filename : filenames) {
				String temp = filename.replace(replaceString, "");
				temp = temp.replace(endString, "");
				list.add(Integer.parseInt(temp));
			}
		}

		Collections.sort(list);
		max = list.get(list.size() - 1);
		return max;
	}

	/**
	 * 读取文件
	 * 
	 * @param fileName
	 */
	public static String readFileByChars(String fileName) {
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		Reader reader = null;
		try {
			// 以字符为单位读取文件内容，一次读一个字节
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream("---" + file),
					"UTF-8");
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				// 对于windows下，\r\n这两个字符在一起时，表示一个换行。
				// 但如果这两个字符分开显示时，会换两次行。
				// 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
				if (((char) tempchar) != '\r') {
					sb.append((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info(sb.toString());
		return sb.toString();
	}

	public static boolean writeFile(String path, String xml) {
		boolean b = true;
		logger.info("xml：" + xml);
		try {
			File file = new File(path);
			// 判断文件是否
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream(path), "UTF-8");
			out.write(xml);
			out.flush();
			out.close();

		} catch (Exception e) {
			b = false;
			logger.error(e);
		}
		return b;
	}

	/**
	 * 进行文件排序时间
	 * 
	 * @author 谈情
	 */
	private static class CompratorByLastModified implements Comparator<File> {

		public int compare(File f1, File f2) {
			long diff = f1.lastModified() - f2.lastModified();
			if (diff > 0)
				return 1;
			else if (diff == 0)
				return 0;
			else
				return -1;
		}

		public boolean equals(Object obj) {
			return true;
		}
	}

	/**
	 * 格式化时间
	 * 
	 * @param format
	 *            格式化显示样式
	 * @param date
	 *            时间
	 * @return
	 */
	private static String format(String format, Date date) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.format(date);
	}
}
