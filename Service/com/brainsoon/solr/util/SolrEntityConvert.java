﻿package com.brainsoon.solr.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

/**
 * 实体类与SolrInputDocument或者SolrDocument转换<br/>
 * 注意：在使用的时候，实体类必须严格遵守 JAVABEAN 规范
 * 
 * @version 0.1
 */
public class SolrEntityConvert {

	private static final Logger logger = Logger.getLogger(SolrEntityConvert.class);

	/**
	 * 实体类与SolrInputDocument转换
	 * 
	 * @param obj
	 * @return SolrInputDocument
	 */
	public static SolrInputDocument entity2SolrInputDocument(Object obj) {
		if (obj != null) {
			Class<?> cls = obj.getClass();
			Field[] filedArrays = cls.getDeclaredFields();
			Method m = null;
			SolrInputDocument sid = new SolrInputDocument();
			Object value = null;
			for (Field f : filedArrays) {
				try {
					m = cls.getMethod(getMethodName(f.getName(), "get"));
					value = m.invoke(obj);
					//if( value==null ) continue;
					sid.addField(f.getName(), value);
				} catch (IllegalArgumentException e) {
					logger.error(e);
				} catch (IllegalAccessException e) {
					logger.error(e);
				} catch (SecurityException e) {
					logger.error(e);
				} catch (NoSuchMethodException e) {
					logger.error(e);
				} catch (InvocationTargetException e) {
					logger.error(e);
				}
			}
			return sid;
		}
		logger.warn("即将要转换的实体类为null！");
		return null;
	}

	/**
	 * SolrDocument与实体类转换
	 * 
	 * @param sd
	 * @param cls
	 * @return <T>
	 */
	public static <T> T solrDocument2Entity(SolrDocument sd, Class<T> cls) {
		if (sd != null) {
			try {
				Object obj = cls.newInstance();
				Method m = null;
				Field f = null;
				Class<?> fieldType = null;
				for (String fieldName : sd.getFieldNames()) {

					f = cls.getDeclaredField(fieldName);
					fieldType = f.getType();
					m = cls.getMethod(getMethodName(fieldName, "set"),
							fieldType);
					// 如果是 int, float等基本类型，则需要转型
					if (fieldType.equals(Integer.TYPE)) {
						fieldType = Integer.class;
					} else if (fieldType.equals(Float.TYPE)) {
						fieldType = Float.class;
					} else if (fieldType.equals(Double.TYPE)) {
						fieldType = Double.class;
					} else if (fieldType.equals(Boolean.TYPE)) {
						fieldType = Boolean.class;
					} else if (fieldType.equals(Short.TYPE)) {
						fieldType = Short.class;
					} else if (fieldType.equals(Long.TYPE)) {
						fieldType = Long.class;
					} 
					Object solrFiled = sd.getFieldValue(fieldName);
					if( solrFiled instanceof List){
						solrFiled = SolrEntityConvert.convertListToString((List)solrFiled,fieldName);
						m.invoke(obj, solrFiled);
					}else{
						m.invoke(obj, fieldType.cast(sd.getFieldValue(fieldName)));
					}
				}
				return cls.cast(obj);
			} catch (ClassCastException e) {
				logger.error("请检查schema.xml中的各个field的数据类型与PO类的是否一致！");
				logger.error(e);
			} catch (SecurityException e) {
				logger.error(e);
			} catch (NoSuchMethodException e) {
				logger.error("请检查PO类中的field对应的各个setter和getter是否存在！");
				logger.error(e);
			} catch (IllegalArgumentException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			} catch (InvocationTargetException e) {
				logger.error(e);
			} catch (InstantiationException e) {
				logger.error(e);
			} catch (NoSuchFieldException e) {
				logger.error("请检查schema中的field是否不存在于PO类中！");
				logger.error(e);
			}
		}
		logger.warn("即将要转换的SolrDocument为null！");
		return null;
	}

	/**
	 * 批量转换, 将实体类的List转换为SolrInputDocument Collection
	 * 
	 * @param entityList
	 * @return Collection<SolrInputDocument>
	 */
	public static <T> Collection<SolrInputDocument> entityList2SolrInputDocument(
			List<T> entityList) {
		if (entityList != null && entityList.size() > 0) {
			Collection<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
			SolrInputDocument sid = null;
			for (Object o : entityList) {
				sid = entity2SolrInputDocument(o);
				if (sid != null) {
					solrInputDocumentList.add(sid);
				}
			}
			return solrInputDocumentList;
		}
		logger.warn("即将要转换的entityList为null或者size为0！");
		return null;
	}

	/**
	 * 批量转换, 将solrDocumentList转换为实体类 List
	 * 
	 * @param solrDocumentList
	 * @param cls
	 * @return List<T>
	 */
	public static <T> List<T> solrDocument2Entity(
			SolrDocumentList solrDocumentList, Class<T> cls) {
		if (solrDocumentList != null && solrDocumentList.size() > 0) {
			List<T> objectList = new ArrayList<T>();
			for (SolrDocument sd : solrDocumentList) {
				Object obj = solrDocument2Entity(sd, cls);
				if (obj != null) {
					objectList.add(cls.cast(obj));
				}
			}
			return objectList;
		}
		logger.warn("即将要转换的solrDocumentList为null或者size为0！");
		return null;
	}

	/**
	 * 更新数据时用到，给出要更新的对象，Id为必须给出的属性，然后加上要更新的属性 如果对应的属性的值为空或者为0，这不需要更新
	 * 
	 * @param sd 查询到得SolrDocument
	 * @param object
	 * @return SolrInputDocument
	 */
	public static SolrInputDocument solrDocument2SolrInputDocument(
			SolrDocument sd, Object object) {
		if (object != null && sd != null) {
			SolrInputDocument sid = new SolrInputDocument();
			Collection<String> fieldNameCollection = sd.getFieldNames();// 得到所有的属性名

			Class<?> cls = object.getClass();
			Object o = null;
			for (String fieldName : fieldNameCollection) {
				try {
					// 如果对应的属性的值为空或者为0，这不需要更新
					o = cls.getMethod(
							SolrEntityConvert.getMethodName(fieldName, "get"))
							.invoke(object);

					Class<?> fieldType = cls.getDeclaredField(fieldName)
							.getType();

					if (fieldType.equals(Integer.TYPE)) {
						Integer fieldValue = Integer.class.cast(o);
						if (fieldValue != null && fieldValue.compareTo(0) != 0) {
							sid.addField(fieldName, fieldValue);
						}
					} else if (fieldType.equals(Float.TYPE)) {
						Float fieldValue = Float.class.cast(o);
						if (fieldValue != null && fieldValue.compareTo(0f) != 0) {
							sid.addField(fieldName, fieldValue);
						}
					} else if (fieldType.equals(Double.TYPE)) {
						Double fieldValue = Double.class.cast(o);
						if (fieldValue != null && fieldValue.compareTo(0d) != 0) {
							sid.addField(fieldName, fieldValue);
						}
					} else if (fieldType.equals(Short.TYPE)) {
						Short fieldValue = Short.class.cast(o);
						if (fieldValue != null
								&& fieldValue.compareTo((short) 0) != 0) {
							sid.addField(fieldName, fieldValue);
						}
					} else if (fieldType.equals(Long.TYPE)) {
						Long fieldValue = Long.class.cast(o);
						if (fieldValue != null
								&& fieldValue.compareTo((long) 0) != 0) {
							sid.addField(fieldName, fieldValue);
						}
					} else {
						if (o != null) {
							sid.addField(fieldName, o.toString());
						}
					}
				} catch (IllegalArgumentException e) {
					logger.error(e);
				} catch (SecurityException e) {
					logger.error(e);
				} catch (IllegalAccessException e) {
					logger.error(e);
				} catch (InvocationTargetException e) {
					logger.error(e);
				} catch (NoSuchMethodException e) {
					logger.error("请检查PO类中的field对应的各个setter和getter是否存在！");
					logger.error(e);
				} catch (NoSuchFieldException e) {
					logger.error("请检查schema中的field是否不存在于PO类中！");
					logger.error(e);
				}
			}
			return sid;
		}
		logger.warn("即将要转换的SolrDocument或者要更新的Object为null");
		return null;
	}

	/**
	 * 批量更新数据时用到
	 * 
	 * @param sdl
	 *            查询到得SolrDocumentList
	 * @param idName
	 * @param objectMap
	 * @return List<SolrInputDocument>
	 */
	public static List<SolrInputDocument> solrDocumentList2SolrInputDocumentList(
			SolrDocumentList sdl, String idName, Map<Object, Object> objectMap) {
		List<SolrInputDocument> solrInputDocuemntList = new ArrayList<SolrInputDocument>();

		// 获得元素的主键的类型，即Map的key类型
		Class<?> cls = null;
		try {
			cls = objectMap.get(0).getClass().getDeclaredField(idName)
					.getType();
		} catch (SecurityException e) {
			logger.error(e);
		} catch (NoSuchFieldException e) {
			logger.error(e);
		}

		for (SolrDocument sd : sdl) {
			Collection<String> fieldNameCollection = sd.getFieldNames();
			SolrInputDocument sid = null;

			Object object = objectMap.get(cls.cast(sd.getFieldValue(idName)));

			Object o = null;

			if (object != null) {
				sid = new SolrInputDocument();
				for (String fieldName : fieldNameCollection) {
					try {
						// 如果对应的属性的值为空或者为0，这不需要更新
						o = cls.getMethod(
								SolrEntityConvert.getMethodName(fieldName,
										"get")).invoke(object);

						Class<?> fieldType = cls.getDeclaredField(fieldName)
								.getType();

						if (fieldType.equals(Integer.TYPE)) {
							Integer fieldValue = Integer.class.cast(o);
							if (fieldValue != null
									&& fieldValue.compareTo(0) != 0) {
								sid.addField(fieldName, fieldValue);
							}
						} else if (fieldType.equals(Float.TYPE)) {
							Float fieldValue = Float.class.cast(o);
							if (fieldValue != null
									&& fieldValue.compareTo(0f) != 0) {
								sid.addField(fieldName, fieldValue);
							}
						} else if (fieldType.equals(Double.TYPE)) {
							Double fieldValue = Double.class.cast(o);
							if (fieldValue != null
									&& fieldValue.compareTo(0d) != 0) {
								sid.addField(fieldName, fieldValue);
							}
						} else if (fieldType.equals(Short.TYPE)) {
							Short fieldValue = Short.class.cast(o);
							if (fieldValue != null
									&& fieldValue.compareTo((short) 0) != 0) {
								sid.addField(fieldName, fieldValue);
							}
						} else if (fieldType.equals(Long.TYPE)) {
							Long fieldValue = Long.class.cast(o);
							if (fieldValue != null
									&& fieldValue.compareTo((long) 0) != 0) {
								sid.addField(fieldName, fieldValue);
							}
						} else {
							if (o != null) {
								sid.addField(fieldName, o.toString());
							}
						}
					} catch (IllegalArgumentException e) {
						logger.error(e);
					} catch (SecurityException e) {
						logger.error(e);
					} catch (IllegalAccessException e) {
						logger.error(e);
					} catch (InvocationTargetException e) {
						logger.error(e);
					} catch (NoSuchMethodException e) {
						logger.error("请检查PO类中的field对应的各个setter和getter是否存在！");
						logger.error(e);
					} catch (NoSuchFieldException e) {
						logger.error("请检查schema中的field是否不存在于PO类中！");
						logger.error(e);
					}
				}
			}
		}
		return solrInputDocuemntList;
	}

	/**
	 * 获得类的方法名，按照JAVABEAN的规范
	 * 
	 * @param property 属性名称
	 * @param prefix  前缀，一般为set 或 get
	 * @return String
	 */
	public static String getMethodName(String property, String prefix) {
		String prop = Character.toUpperCase(property.charAt(0))
				+ property.substring(1);
		return prefix + prop;
	}

	public static String convertListToString(List list, String fieldName){
		StringBuffer out = new StringBuffer();
		if( list!=null && list.size()>0 ){
			
			if(StringUtils.equals("title", fieldName)||
					StringUtils.equals("author", fieldName)||
					StringUtils.equals("publishing", fieldName)) {
				String name = new String();
				name =  (String)list.get(0);              
        		if(list.size() > 1)
        			name =  (String)list.get(1); 
        		out.append(name);
			} else {
				for(int i=0;i<list.size();i++){
					out.append(list.get(i));
					if(i!=list.size()-1) out.append(" ");
				}
			}
			
		}
		if( out.length()>50 ){
			return out.substring(0,out.length()>300?300:out.length()) + "......";
		}
		return out.toString();
	}
	
}
