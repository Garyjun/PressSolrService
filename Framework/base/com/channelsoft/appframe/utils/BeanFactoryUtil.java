/**
 * FileName: BeanFactoryUtil.java
 */
package com.channelsoft.appframe.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.brainsoon.solr.thread.ThreadManager;
import com.channelsoft.appframe.common.BaseObject;
import com.channelsoft.appframe.exception.utils.BeanFactoryException;

/**
 * <dl>
 * <dt>BeanFactoryUtil</dt>
 * <dd>Description:spring bean工厂工具类</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Jan 5, 2009</dd>
 * </dl>
 */
public class BeanFactoryUtil extends BaseObject implements
		ServletContextListener {
	private static final Logger logger = Logger
			.getLogger(BeanFactoryUtil.class);

	private static ApplicationContext context;

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent event) {
		logger.info("初始化BeanFactory....");
		context = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		ThreadManager.startThread();//启动索引任务多线程
		logger.info("初始化BeanFactory....OK.");
	}

	/**
	 * 获取Spring中的Bean
	 * 
	 * @param beanName
	 *            Bean的名称
	 * @return 如果成功则反回Bean对象，如果失败则抛出异常.
	 */
	public static Object getBean(String beanName) throws BeanFactoryException {
		if (context == null) {
			logger.warn("ApplicationContext 没有初始化！您无法获得业务处理对象，系统无法正常运行");
			throw new BeanFactoryException(
					"ApplicationContext 没有初始化！您无法获得业务处理对象，系统无法正常运行");
		}
		try {
			return context.getBean(beanName);
		} catch (BeansException exp) {
			if (exp instanceof NoSuchBeanDefinitionException) {
				logger.debug("bean[" + beanName + "]尚未装载到容器中！");
			} else {
				logger.error("读取[" + beanName + "]失败！", exp);
			}
			throw new BeanFactoryException("读取[" + beanName + "]失败！", exp);
		}
	}
}
