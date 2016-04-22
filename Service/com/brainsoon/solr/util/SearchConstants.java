package com.brainsoon.solr.util;

import org.apache.commons.lang.StringUtils;

import com.brainsoon.common.utils.ScaConfigUtil;
import com.channelsoft.appframe.taglib.ConstantsMap;
import com.channelsoft.appframe.taglib.ConstantsRepository;

/**
 * <dl>
 * <dt>AuthResultConstants</dt>
 * <dd>Description:全文搜索常量定义</dd>
 * <dd>Copyright: Copyright (C) 2008</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: Oct 18, 2011</dd>
 * </dl>
 * 
 * @author jiangzc
 */
public class SearchConstants {
	
	/*
	 * 分页bar最多显示的页数  5页
	 */
	public static final int PAGE_BAR_WIDTH = 5;
	
	/**
	 * 索引类型
	 * @author Administrator
	 *
	 */
	public static class IndexType {
		public static final String CREATE_INDEX = "0";
		public static final String CREATE_INDEX_DESC = "创建索引";	
		public static final String DELETE_INDEX = "1";
		public static final String DELETE_INDEX_DESC = "删除索引";
		private static ConstantsMap map;
		
    	static {
    		map = new ConstantsMap();
    		map.putConstant(CREATE_INDEX, CREATE_INDEX_DESC);
    		map.putConstant(DELETE_INDEX, DELETE_INDEX_DESC);
    		ConstantsRepository.getInstance().register(IndexType.class, map);
    	}
    	
       	public static String getValueByKey(Object key) {
       		if(key == null){
       			return "";
       		}
    		return (String)map.getDescByValue(key);
    	}
	}
	
	/**
	 *所属资源类型
	 */
	public static class Type {
		public static final String TYPE10 = "10";
		public static final String TYPE10_DESC = "原始资源";	
		public static final String TYPE20 = "20";
		public static final String TYPE20_DESC = "基础资源";
		public static final String TYPE30 = "30";
		public static final String TYPE30_DESC = "产品";	
		private static ConstantsMap map;
		
    	static {
    		map = new ConstantsMap();
    		map.putConstant(TYPE10, TYPE10_DESC);
    		map.putConstant(TYPE20, TYPE20_DESC);
    		map.putConstant(TYPE30, TYPE30_DESC);
    		ConstantsRepository.getInstance().register(Type.class, map);
    	}
    	
       	public static String getValueByKey(Object key) {
       		if(key == null){
       			return "";
       		}
    		return (String)map.getDescByValue(key);
    	}
	}
	//
	
	/**
	 *文件分类
	 */
	public static class FileType {
		public static final String TYPE_PDF = "pdf";
		public static final String TYPE_PDF_DESC = "PDF";	
		public static final String TYPE_DOC = "doc";
		public static final String TYPE_DOC_DESC = "DOC";
		public static final String TYPE_TXT = "txt";
		public static final String TYPE_TXT_DESC = "TXT";	
		private static ConstantsMap map;
    	static {
    		map = new ConstantsMap();
    		map.putConstant(TYPE_PDF, TYPE_PDF_DESC);
    		map.putConstant(TYPE_DOC, TYPE_DOC_DESC);
    		map.putConstant(TYPE_TXT, TYPE_TXT_DESC);
    		ConstantsRepository.getInstance().register(FileType.class, map);
    	}
       	public static String getValueByKey(Object key) {
       		if(key == null){
       			return "";
       		}
    		return (String)map.getDescByValue(key);
    	}
	}
	
	/**
	 *资源分类
	 */
	public static class ResTypeDesc {
		public static final String TYPE10 = "10";
		public static final String TYPE10_DESC = "原始资源";	
		public static final String TYPE20 = "20";
		public static final String TYPE20_DESC = "基础资源";
		private static ConstantsMap map;
    	static {
    		map = new ConstantsMap();
    		map.putConstant(TYPE10, TYPE10_DESC);
    		map.putConstant(TYPE20, TYPE20_DESC);
    		ConstantsRepository.getInstance().register(ResTypeDesc.class, map);
    	}
       	public static String getValueByKey(Object key) {
       		if(key == null){
       			return "";
       		}
    		return (String)map.getDescByValue(key);
    	}
	}
	
	/**
	 *资源分类
	 */
	public static class ResType {
		public static final String TYPE10 = "10";
		public static final String TYPE10_DESC = "出版物";	
		public static final String TYPE20 = "20";
		public static final String TYPE20_DESC = "原创";
		public static final String TYPE30 = "30";
		public static final String TYPE30_DESC = "其他";	
		private static ConstantsMap map;
    	static {
    		map = new ConstantsMap();
    		map.putConstant(TYPE10, TYPE10_DESC);
    		map.putConstant(TYPE20, TYPE20_DESC);
    		map.putConstant(TYPE30, TYPE30_DESC);
    		ConstantsRepository.getInstance().register(ResType.class, map);
    	}
       	public static String getValueByKey(Object key) {
       		if(key == null){
       			return "";
       		}
    		return (String)map.getDescByValue(key);
    	}
	}
	
	/**
	 * 全文检索类型
	 * @author jiangzc
	 *
	 */
	public static class ResSearchType {
		public static final String TYPE0 = "files";
		public static final String TYPE0_DESC = "内容";
		public static final String TYPE1 = "author";
		public static final String TYPE1_DESC = "作者";
		public static final String TYPE2 = "title";
		public static final String TYPE2_DESC = "书名";
		public static final String TYPE3 = "publishing";
		public static final String TYPE3_DESC = "出版社";
		
		private static ConstantsMap map;
    	static {
    		map = new ConstantsMap();
    		map.putConstant(TYPE0, TYPE0_DESC);
    		map.putConstant(TYPE1, TYPE1_DESC);
    		map.putConstant(TYPE2, TYPE2_DESC);
    		map.putConstant(TYPE3, TYPE3_DESC);
    		ConstantsRepository.getInstance().register(ResSearchType.class, map);
    	}
       	public static String getValueByKey(Object key) {
       		if(key == null){
       			return "";
       		}
    		return (String)map.getDescByValue(key);
    	}
	}
	
	/**
	 * 全部查询查询标识
	 * @author jiangzc
	 *
	 */
	public static class allSearchIdentifier {
		public static final String ISBN = "isbn";
		public static final String ISBN_DESC = "ISBN号";
		public static final String BOOKID = "resId";
		public static final String BOOKID_DESC = "资源ID";
		public static final String ASIN = "asin";
		public static final String ASIN_DESC = "asin标识";
		private static ConstantsMap map;
    	static {
    		map = new ConstantsMap();
    		map.putConstant(ISBN, ISBN_DESC);
    		map.putConstant(BOOKID, BOOKID_DESC);
    		
    		String sm = ScaConfigUtil.getParameter("smbook");
    		if(StringUtils.isNotEmpty(sm)) {
    			map.putConstant(ASIN, ASIN_DESC);
    		}
    		ConstantsRepository.getInstance().register(allSearchIdentifier.class, map);
    	}
       	public static String getValueByKey(Object key) {
       		if(key == null){
       			return "";
       		}
    		return (String)map.getDescByValue(key);
    	}
	}
	
	/**
	 * 全部查询查询标识
	 * @author jiangzc
	 *
	 */
	public static class allSearchType {
		public static final String ORES = "ores";
		public static final String ORES_DESC = "原始资源";
		public static final String BRES = "bres";
		public static final String BRES_DESC = "基础资源";
		public static final String PROD = "prod";
		public static final String PROD_DESC = "产品资源";
		
		private static ConstantsMap map;
    	static {
    		map = new ConstantsMap();
    		map.putConstant(ORES, ORES_DESC);
    		map.putConstant(BRES, BRES_DESC);
    		map.putConstant(PROD, PROD_DESC);
    		ConstantsRepository.getInstance().register(allSearchType.class, map);
    	}
       	public static String getValueByKey(Object key) {
       		if(key == null){
       			return "";
       		}
    		return (String)map.getDescByValue(key);
    	}
	}
	
	/**
	 * 全部查询查询标识
	 * @author jiangzc
	 *
	 */
	public static class viewUtil {
		public static final String BOOK_IDENTIFIER = "str025";
		public static final String BOOK_IDENTIFIER_DESC = "出版图书唯一标识";
		public static final String BOOK_TITLE = "str028";
		public static final String BOOK_TITLE_DESC = "出版图书书名";
		public static final String BOOK_CREATOR = "str027";
		public static final String BOOK_CREATOR_DESC = "出版图书作者";
		public static final String BOOK_ISBN = "str024";
		public static final String BOOK_ISBN_DESC = "出版图书ISBN";
		
		public static final String ORIGINAL_IDENTIFIER = "str002";
		public static final String ORIGINAL_IDENTIFIER_DESC = "出版图书唯一标识";
		public static final String ORIGINAL_TITLE = "str007";
		public static final String ORIGINAL_TITLE_DESC = "出版图书书名";
		public static final String ORIGINAL_CREATOR = "str006";
		public static final String ORIGINAL_CREATOR_DESC = "出版图书作者";
		
		private static ConstantsMap map;
    	static {
    		map = new ConstantsMap();
    		map.putConstant(BOOK_IDENTIFIER, BOOK_IDENTIFIER_DESC);
    		map.putConstant(BOOK_TITLE, BOOK_TITLE_DESC);
    		map.putConstant(BOOK_CREATOR, BOOK_CREATOR_DESC);
    		map.putConstant(BOOK_ISBN, BOOK_ISBN_DESC);
    		
    		map.putConstant(ORIGINAL_IDENTIFIER, ORIGINAL_IDENTIFIER_DESC);
    		map.putConstant(ORIGINAL_TITLE, ORIGINAL_TITLE_DESC);
    		map.putConstant(ORIGINAL_CREATOR, ORIGINAL_CREATOR);
    		ConstantsRepository.getInstance().register(viewUtil.class, map);
    	}
       	public static String getValueByKey(Object key) {
       		if(key == null){
       			return "";
       		}
    		return (String)map.getDescByValue(key);
    	}
	}
	
}





