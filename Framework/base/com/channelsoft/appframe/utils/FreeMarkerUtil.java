package com.channelsoft.appframe.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.channelsoft.appframe.common.BaseObject;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.exception.utils.BeanFactoryException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * <dl>
 * <dt>FreeMarkerUtil</dt>
 * <dd>Description:freemark的工具类</dd>
 * <dd>Copyright: Copyright (C) 2007</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Aug 14, 2008</dd>
 * </dl>
 * 
 * @author Fuwenbin
 */
public class FreeMarkerUtil extends BaseObject {
	private static FreeMarkerUtil instance = new FreeMarkerUtil();
	private FreeMarkerConfigurer freeMarkerConfigurer;    
	
	private FreeMarkerUtil() {
		try {
			this.freeMarkerConfigurer = (FreeMarkerConfigurer)BeanFactoryUtil.getBean("freeMarkerConfigurer");
			if (this.freeMarkerConfigurer == null) {
				logger.error("未配置freeMarkerConfigurer bean!");
				throw new ServiceException("未配置freeMarkerConfigurer bean!");
			}
		} catch (BeanFactoryException e) {
			logger.error("获取freeMarkerConfigurer bean失败!");
			throw new ServiceException("获取freeMarkerConfigurer bean失败：",e);
		}
	}
	
	/**
	 * 将模板实例成string
	 * @param templateName 模板名称
	 * @param model	模板中要填充的对
	 * @return
	 *
	 * @author Fuwenbin
	 * @date Aug 14, 2008 2:14:48 PM 
	 */
	public static String processTemplateIntoString(String templateName, Map model){
		try {
			Configuration configuration = instance.freeMarkerConfigurer.getConfiguration();
			Template t = configuration.getTemplate(templateName);
			return FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
		} catch (TemplateException e) {
			instance.logger.error("处理freemarker模板出错：", e);
			throw new ServiceException("处理freemarker模板出错：",e);
		} catch (FileNotFoundException e) {
			instance.logger.error("freemarker模板文件不存在：", e);
			throw new ServiceException("freemarker模板文件不存在：",e);
		} catch (IOException e) {
			instance.logger.error("处理freemarker模板出错：", e);
			throw new ServiceException("处理freemarker模板出错：",e);
		}  
	}
	
	/**
	 * 将模板实例成指定文件
	 * @param templateName 模板名称
	 * @param model 模板中要填充的对
	 * @param fileName 文件名
	 *
	 * @author Fuwenbin
	 * @date Aug 14, 2008 2:15:12 PM 
	 */
	public static void processTemplateIntoFile(String templateName, Map model, String fileName){
		FileWriter fileWriter = null;
		try {
			Configuration configuration = instance.freeMarkerConfigurer.getConfiguration();
			Template t = configuration.getTemplate(templateName);
			
			File file = new File(fileName);
			if (file.exists() && !file.delete()) {
				throw new ServiceException("删除文件失败！");
			}

			fileWriter = new FileWriter(fileName);
			t.process(model, fileWriter);
		} catch (TemplateException e) {
			instance.logger.error("处理freemarker模板出错：", e);
			throw new ServiceException("处理freemarker模板出错：",e);
		} catch (FileNotFoundException e) {
			instance.logger.error("freemarker模板文件不存在：", e);
			throw new ServiceException("freemarker模板文件不存在：",e);
		} catch (IOException e) {
			instance.logger.error("处理freemarker模板出错：", e);
			throw new ServiceException("处理freemarker模板出错：",e);
		} finally {
				try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				throw new ServiceException("文件关闭错误:", e);
			}
		}

		
//		FileWriter fileWriter = null;
//		try {
//			File file = new File(fileName);
//			if (file.exists()) {
//				file.delete();
//			}
//
//			fileWriter = new FileWriter(fileName);
//			fileWriter.write(processTemplateIntoString(templateName,model));
//		} catch (IOException e) {
//			instance.logger.warn("写文件错误：" + e.getMessage());
//			throw new ServiceException("写文件错误:", e);
//		} finally {
//			try {
//				if (fileWriter != null) {
//					fileWriter.close();
//				}
//			} catch (IOException e) {
//				throw new ServiceException("文件关闭错误:", e);
//			}
//		}
	}
}
