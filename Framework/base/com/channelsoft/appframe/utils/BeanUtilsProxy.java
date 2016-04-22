package com.channelsoft.appframe.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * commons的BeanUtilsBean的代理，只在包内可见，由ConvertUtil使用； 提供对java对象的属性复制功能，可以忽略指定的属性
 */
class BeanUtilsProxy // extends BeanUtilsBean {
{
	private BeanUtils beanUtilBeanInstance;
	private Log log = LogFactory.getLog(getClass().getName());
	
	private BeanUtilsProxy() {
		// beanUtilBeanInstance = BeanUtils.getInstance();
		beanUtilBeanInstance = new BeanUtils();
	}

	/**实现Lazy Initialization线程安全
	 * Fuwenbin 2009-02-07
	 */
	private static class BeanUtilsProxyHolder {
		static BeanUtilsProxy instance = new BeanUtilsProxy();
	}

	public static BeanUtilsProxy getInstance() {
		return BeanUtilsProxyHolder.instance;
//		if (instance == null) {
//			instance = new BeanUtilsProxy();
//		}
//		return instance;
	}

	// private PropertyUtilsBean propertyUtilsBean = null;

	public Object convertOmit(Object dest, Object src, String[] omit)
			throws IllegalAccessException, InvocationTargetException {

		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (src == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}
		if (log.isDebugEnabled()) {
			log.debug("BeanUtils.copyProperties(" + dest + ", " + src + ")");
		}
		org.apache.commons.beanutils.Converter converter = ConvertUtils
				.lookup(java.util.Date.class);
		System.out.println("ConvertUtils.lookup( java.util.Date.class): "
				+ converter);

		org.apache.commons.beanutils.Converter converter2 = ConvertUtils
				.lookup(java.lang.String.class);
		System.out.println("ConvertUtils.lookup( java.lang.String.class): "
				+ converter2);
		// Copy the properties, converting as necessary
		if (src instanceof DynaBean) {
			copyDynaBean(dest, src);
		} else if (src instanceof Map) {
			copyMap(dest, src);
		} else /* if (src is a standard JavaBean) */{
			copyJavaBean(dest, src, omit);
		}
		return dest;
	}

	/**
	 * @param dest
	 * @param src
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void copyJavaBean(Object dest, Object src, String[] omit)
			throws IllegalAccessException, InvocationTargetException {
		// PropertyDescriptor srcDescriptors[] =
		// beanUtilBeanInstance.getPropertyUtils()
		// .getPropertyDescriptors(src);
		PropertyDescriptor srcDescriptors[] = PropertyUtils
				.getPropertyDescriptors(src);
		for (int i = 0; i < srcDescriptors.length; i++) {
			String name = srcDescriptors[i].getName();

			if (omitted(name, omit)) {
				continue;
			}
			if ("class".equals(name)) {
				continue; // No point in trying to set an object's class
			}
			// if (beanUtilBeanInstance.getPropertyUtils().isReadable(src, name)
			// && beanUtilBeanInstance.getPropertyUtils().isWriteable(dest,
			// name)) {
			if (PropertyUtils.isReadable(src, name)
					&& PropertyUtils.isWriteable(dest, name)) {
				try {
					// Object value =
					// beanUtilBeanInstance.getPropertyUtils().getSimpleProperty(src,
					// name);
					Object value = PropertyUtils.getSimpleProperty(src, name);
					beanUtilBeanInstance.copyProperty(dest, name, value);
				} catch (NoSuchMethodException e) {
					// Should not happen
				}
			}
		}
	}

	/**
	 * 判断name是否在忽略列表omit中
	 * 
	 * @param name
	 * @param omit
	 * @return
	 */
	private boolean omitted(String name, String[] omit) {
		if (omit == null) {
			return false;
		}
		for (int i = 0; i < omit.length; i++) {
			if (name.equals(omit[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param dest
	 * @param src
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void copyDynaBean(Object dest, Object src)
			throws IllegalAccessException, InvocationTargetException {
		DynaProperty origDescriptors[] = ((DynaBean) src).getDynaClass()
				.getDynaProperties();
		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			// if (beanUtilBeanInstance.getPropertyUtils().isWriteable(dest,
			// name)) {
			if (PropertyUtils.isWriteable(dest, name)) {
				Object value = ((DynaBean) src).get(name);
				beanUtilBeanInstance.copyProperty(dest, name, value);
			}
		}
	}

	/**
	 * @param dest
	 * @param src
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void copyMap(Object dest, Object src)
			throws IllegalAccessException, InvocationTargetException {
		Iterator names = ((Map) src).keySet().iterator();
		while (names.hasNext()) {
			String name = (String) names.next();
			// if (beanUtilBeanInstance.getPropertyUtils().isWriteable(dest,
			// name)) {
			if (PropertyUtils.isWriteable(dest, name)) {
				Object value = ((Map) src).get(name);
				beanUtilBeanInstance.copyProperty(dest, name, value);
			}
		}
	}
}
