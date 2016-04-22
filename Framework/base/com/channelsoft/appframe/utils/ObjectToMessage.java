package com.channelsoft.appframe.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <li>Description: 得到一个对象的属性和值得描述
 * <li>Copyright: Copyright (c) 2006
 * <li>Company:ChannelSoft
 */
public class ObjectToMessage {

	private static final String GET = "get";

	private Object source;

	/**
	 * 得到一个对象的属性和值得描述
	 * 
	 * @param clazz
	 * @return
	 */
	public String getObjectLogMessage(Object object) {
		source = object;
		StringBuffer poLogMessage = new StringBuffer();
		if (object == null) {
			return poLogMessage.toString();
		}
		Class clazz = object.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		int size = methods.length;
		for (int i = 0; i < size; i++) {
			Method method = methods[i];
			String poMessage = getPropertyValue(method);
			poLogMessage.append(poMessage);
		}
		String desc = poLogMessage.toString();
		if (desc.length() > 400)
			return desc = desc.substring(0, 400);
		return desc;
	}

	/*
	 * 根据方法名称得到属性和其值，如果方法返回非简单对象也将转化为String @param method @return 一个方法对应的属性和值
	 */
	private String getPropertyValue(Method method) {
		StringBuffer propertyMessage = new StringBuffer();
		String methodName = method.getName();

		boolean isMethod = filterMethod(method);
		if (isMethod) {
			return "";
		}
		String propertyName = getPropertyName(methodName);
		try {
			Object reObject = method.invoke(source, null);
			if (reObject != null) {
				propertyMessage.append(propertyName + ":").append(
						reObject.toString() + ";");
			} else {
				return "";
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return propertyMessage.toString();
	}

	/**
	 * 如果get方法返回的类型是Set,List,Po则不返回该方法的值
	 * 
	 * @return 是否被过滤true：被过滤
	 */
	private boolean filterMethod(Method method) {
		filterObject = new ArrayList();
		initFilterObject();
		String methodName = method.getName();
		Class clazz = method.getReturnType();
		if (filterObject.contains(clazz)) {
			return true;
		}
		if (clazz.isInstance(this)) {
			return true;
		}
		if (methodName.indexOf(GET) == -1) {
			return true;
		}
		return false;
	}

	private ArrayList filterObject;

	private void initFilterObject() {
		filterObject.add(Set.class);
		filterObject.add(List.class);
	}

	/**
	 * 获取方法对应的属性名
	 * 
	 * @param methodName
	 * @return
	 */
	private String getPropertyName(String methodName) {
		String startName = methodName.substring(3, 4).toLowerCase();
		String lastName = methodName.substring(4);
		return startName + lastName;
	}
}
