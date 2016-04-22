package com.brainsoon.solr.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.brainsoon.solr.po.SolrFile;
import com.brainsoon.solr.po.SolrQueue;
import com.brainsoon.solr.service.ISolrQueueFacede;
import com.brainsoon.solr.util.FileCombine;
import com.brainsoon.solr.util.OSCommandUtil;
import com.channelsoft.appframe.dao.query.Operator;
import com.channelsoft.appframe.dao.query.QueryConditionItem;
import com.channelsoft.appframe.dao.query.QueryConditionList;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.service.impl.BaseServiceObject;
import com.channelsoft.appframe.utils.WebappConfigUtil;

public class TestCreateWordDbBak extends BaseServiceObject implements
        ISolrQueueFacede {

    /**
     * 通过状态返回创建索引信息的集合
     * 
     * @param status
     * @return
     */
    @Override
    public List<SolrQueue> getSolrQueueByStatus(String status) {
        List<SolrQueue> result = null;
        try {
            QueryConditionList conditions = new QueryConditionList();
            conditions.addCondition(new QueryConditionItem("status",
                    Operator.EQUAL, status));
            conditions.setSortProperty("createTime");
            conditions.setSortMode(QueryConditionList.SORT_MODE_ASC);
            result = getListBy(SolrQueue.class, conditions);
        } catch (Exception ex) {
            logger.error("建索引信息的集合异常！", ex);
        }
        return result;
    }

    /**
     * 通过标识更新创建索引信息的状态
     * 
     * @param id
     * @param status
     * @return
     */
    @Override
    public void updateSolrQueueStatus(Long id, String status) {

        SolrQueue solrQueue = (SolrQueue) getBaseDao().getByPk(SolrQueue.class,
                id);
        solrQueue.setStatus(status);
    }
    
    /**
	 * 更新文件创建索引日志
	 * @param fileId
	 * @param message
	 */
    @Override
    public void updateSolrFileStatusMsg(Long fileId, String status, String message) {
    	SolrFile solrFile = (SolrFile)getBaseDao().getByPk(SolrFile.class,
    			fileId);
    	solrFile.setStatus(status);
    	solrFile.setMessage(message);
    	getBaseDao().update(solrFile);
    }

    /**
     * 创建索引失败状态
     * 
     * @param id
     */
    @Override
    public void createIndexFailed(Long id) {

        String hql = "update SolrQueue sq set sq.status=2 where sq.id=" + id;

        this.getBaseDao().executeUpdate(hql, new HashMap());
    }

    /**
     * 创建索引成功状态
     * 
     * @param id
     */
    @Override
    public void createIndexSuccess(Long id) {
//        String hql = "update SolrQueue sq set sq.status = 1 where sq.id= " + id;
    	String hql = "delete from SolrFile where queue_id= " + id;
    	logger.debug(hql);
    	this.getBaseDao().executeUpdate(hql, new HashMap());
    	hql = "delete from SolrQueue sq where sq.id= " + id;
    	logger.debug(hql);
        this.getBaseDao().executeUpdate(hql, new HashMap());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.brainsoon.bsrcm.search.service.ISolrQueueFacede#saveOrUpdate(com.
     * brainsoon.bsrcm.search.po.SolrQueue)
     */
    @Override
    public void saveOrUpdate(SolrQueue solrQueue) {
    	
    	Set<SolrFile> solrFiles = new LinkedHashSet<SolrFile>();
    	solrFiles.addAll(solrQueue.getSolrFiles());
    	
    	/* 索引状态未创建 */
    	solrQueue.setStatus("0");
    	/* 索引创建时间  */
	    solrQueue.setCreateTime(new Date());
	    solrQueue.getSolrFiles().clear();
	    /* 先保存索引队列数据 */
	    getBaseDao().saveOrUpdate(solrQueue);
	    for (SolrFile solrFile : solrFiles) {
	    	solrFile.setSolrQueue(solrQueue);
	    	getBaseDao().saveOrUpdate(solrFile);
	    }
    }

	public void doBookIndex(Long bookId, Long orgId, String orgAction, Long speId, String speAction, String onlinetime, String price, String salesnum, String stsalesnum, Map bookinfo,
			String actions) throws ServiceException {
//		if(StringUtils.isNotEmpty(bookid)) {
//			throw new ServiceException("参数【bookid】不能为空！");
//		}
		

		if(bookinfo == null) {
			throw new ServiceException("【"+ bookId +"】对应的应用资源不存在！");
		}
//		String stream = null;
//		if(bookinfo.get("stream")!=null&&!"".equals(bookinfo.get("stream").toString())) {
//			stream = bookinfo.get("stream").toString();
//		} else {
//			throw new ServiceException("图书参数【stream】不能为空！");
//		}
		String stream = null;
		if(bookinfo.get("stream") != null) {
			stream = bookinfo.get("stream").toString();
		}
		
		//文件格式
		String fileType = null;
		String prodDir = null;
//		if(StringUtils.equals("0", ispdf)) {
//			fileType = "xml";
//			prodDir = WebappConfigUtil.getParameter("prodDir") 
//	        		+ "book" + File.separator + "online" + File.separator + "stream" + File.separator + bookId/1000 + File.separator + bookId;
//		} else {
			fileType = "pdf";
			if(stream != null) {
				prodDir = WebappConfigUtil.getParameter("prodDir") + stream;
			}
			
	        		
//		}
		
		logger.info(prodDir);
				
	 	SolrQueue solrQueue = new SolrQueue();
	 	/* 产品标识 */
	 	solrQueue.setProdId(bookId);
        /*资源类型*/
        solrQueue.setResType(Long.valueOf("1"));
        
        /* 机构标识 */
        solrQueue.setOrgId(orgId);
        
        solrQueue.setOrgAction(orgAction);
        
        /* 专题库标识 */
        solrQueue.setSpeId(speId);
        
        solrQueue.setSpeAction(speAction);
        
        solrQueue.setOnlinetime(onlinetime);
        
        solrQueue.setPrice(price);
        
        solrQueue.setSalesnum(salesnum);
        
        solrQueue.setStsalesnum(stsalesnum);

        /*索引动作(0创建索引，1删除索引) */
        solrQueue.setActions(actions);      
        String indexFile = null;
        if(prodDir != null) {
        	indexFile = prodDir + "index.txt";
        }        
        logger.debug("indexFile9==============================" + indexFile);
        if(StringUtils.equals("0", actions)){
	        if(StringUtils.equals("pdf", fileType)&&indexFile != null&&(!new File(indexFile).exists())) {
	        	String pageDir = prodDir + File.separator + "pdf" + File.separator + "H" + File.separator + "pages"; 
//		        	File file = new File(indexFile);
//		        	if(file.exists()) {
//		        		logger.debug("文件存在============================" + indexFile);
//		        		file.delete();
//		        		if(!file.exists()) {
//		        			logger.debug("文件不存在了============================" + indexFile);
//		        		}
//		        	}
	        	try{
	    			FileCombine.combineFile(pageDir, indexFile, "txt");
	    		}catch (Exception e) {
	    			throw new ServiceException("合并index.txt创建索引文件失败！");
				}
	        	logger.debug("IndexFile============================" + indexFile);
//		        handleIndexFile(indexFile);
	        }
	        
//	        if(StringUtils.equals("xml", fileType)&&(!new File(indexFile).exists())) {
//	        	String pageDir = prodDir + File.separator + "xml" + File.separator + "html"; 
//	        	logger.debug(pageDir);
//	        	try{
//	    			FileCombine.combineFile(pageDir, indexFile, "html");
//	    		}catch (Exception e) {
//	    			throw new ServiceException("合并index.txt创建索引文件失败！");
//				}
//	        }
        } 
        if(new File(indexFile).exists()) {
			SolrFile solrFile = new SolrFile();
			 /* 设置文件的绝对路径*/
			 //solrFile.setPath(prodDir + File.separator + "index.txt");
			 solrFile.setPath(indexFile);
			 solrQueue.getSolrFiles().add(solrFile);
			 /* 保存表信息 */				
       }
        
        saveOrUpdate(solrQueue);
	}
	
	public SolrQueue doBookIndexNoSave(Long bookId, Long orgId, String orgAction, Long speId, String speAction, String onlinetime, String price, String salesnum, String stsalesnum, Map bookinfo,
			String actions) throws ServiceException {
		if(bookinfo == null) {
			throw new ServiceException("【"+ bookId +"】对应的应用资源不存在！");
		}

		String stream = null;
		if(bookinfo.get("stream") != null) {
			stream = bookinfo.get("stream").toString();
		}
		
		String fileType = null;
		String prodDir = null;
		fileType = "pdf";
		if(stream != null) {
			prodDir = WebappConfigUtil.getParameter("prodDir") + stream;
		}		
		
		logger.info(prodDir);
				
	 	SolrQueue solrQueue = new SolrQueue();
	 	/* 产品标识 */
	 	solrQueue.setProdId(bookId);
        /*资源类型*/
        solrQueue.setResType(Long.valueOf("1"));
        
        /* 机构标识 */
        solrQueue.setOrgId(orgId);
        
        solrQueue.setOrgAction(orgAction);
        
        /* 专题库标识 */
        solrQueue.setSpeId(speId);
        
        solrQueue.setSpeAction(speAction);
        
        solrQueue.setOnlinetime(onlinetime);
        
        solrQueue.setPrice(price);
        
        solrQueue.setSalesnum(salesnum);
        
        solrQueue.setStsalesnum(stsalesnum);

        /*索引动作(0创建索引，1删除索引) */
        solrQueue.setActions(actions);      
        
        String indexFile = null;
        if(prodDir != null) {
        	indexFile = prodDir + "index.txt";
        	logger.debug(indexFile);
        }         
        
        if(indexFile != null) {
	        if(StringUtils.equals("0", actions)){
		        if(StringUtils.equals("pdf", fileType)&&(!new File(indexFile).exists())) {
		        	String pageDir = prodDir + File.separator + "pdf" + File.separator + "L" + File.separator + "pages"; 
		        	try{
		    			FileCombine.combineFile(pageDir, indexFile, "txt");
		    		}catch (Exception e) {
		    			throw new ServiceException("合并index.txt创建索引文件失败！");
					}
		        }	        
	        } 
	        if(new File(indexFile).exists()) {
				SolrFile solrFile = new SolrFile();
				solrFile.setPath(indexFile);
				solrQueue.getSolrFiles().add(solrFile);
	        } 
        }
        return solrQueue;
	}
	
	public void doBookArticleIndex(Long bookId, Long orgId, String orgAction, Long speId, String speAction, String onlinetime, String price, String salesnum, Map bookarticleinfo,
			String actions) throws ServiceException {

		if(bookarticleinfo == null) {
			throw new ServiceException("【"+ bookId +"】对应的应用资源不存在！");
		}
		//文件格式
		String fileType = "xml";
				
		SolrQueue solrQueue = new SolrQueue();
	 	/* 产品标识 */
	 	solrQueue.setProdId(bookId);
        /*资源类型*/
        solrQueue.setResType(Long.valueOf("3"));
        
        /* 机构标识 */
        solrQueue.setOrgId(orgId);
        
        solrQueue.setOrgAction(orgAction);
        
        /* 专题库标识 */
        solrQueue.setSpeId(speId);
        
        solrQueue.setSpeAction(speAction);
        
        solrQueue.setOnlinetime(onlinetime);
        
        solrQueue.setPrice(price);
        
        solrQueue.setSalesnum(salesnum);

        /*索引动作(0创建索引，1删除索引) */
        solrQueue.setActions(actions);
        
        String prodDir = WebappConfigUtil.getParameter("prodDir") 
        		+ "article" + File.separator + bookId/1000 + File.separator + bookId;
        
        String indexFile = prodDir + File.separator + "index.txt";
        
        logger.debug(indexFile);
        
        if(StringUtils.equals("0", actions)){
	        
	        if(StringUtils.equals("xml", fileType)&&(!new File(indexFile).exists())) {
	        	String pageDir = prodDir + File.separator + "xml" + File.separator + "html"; 
	        	try{
	    			FileCombine.combineFile(pageDir, indexFile, "html");
	    		}catch (Exception e) {
	    			throw new ServiceException("合并index.txt创建索引文件失败！");
				}
	        }
        } 
        if(new File(indexFile).exists()) {
			SolrFile solrFile = new SolrFile();
			 /* 设置文件的绝对路径*/
			 //solrFile.setPath(prodDir + File.separator + "index.txt");
			 solrFile.setPath(indexFile);
			 solrQueue.getSolrFiles().add(solrFile);
			 /* 保存表信息 */
			 saveOrUpdate(solrQueue);
       } else {
       		throw new ServiceException("创建索引或删除索引失败，index.txt不存在！");
       }
	}
	
	public void doArticleIndex(Long articleId,
			String actions) throws ServiceException {
		SolrQueue solrQueue = new SolrQueue();
		solrQueue.setResType(Long.valueOf("2"));
		solrQueue.setProdId(articleId);
		/*索引动作(0创建索引，1删除索引) */
        solrQueue.setActions(actions);
        /* 保存表信息 */
		saveOrUpdate(solrQueue);
	}
	
	public void doSpecialIndex(Long specialId,
			String actions, Long selectType, Long orgid) throws ServiceException {
		SolrQueue solrQueue = new SolrQueue();
		solrQueue.setResType(Long.valueOf("4"));
		solrQueue.setProdId(specialId);
		solrQueue.setSelectType(selectType);
		solrQueue.setOrgId(orgid);
		/*索引动作(0创建索引，1删除索引) */
        solrQueue.setActions(actions);
        /* 保存表信息 */
		saveOrUpdate(solrQueue);
	}
	
	
	/**
	 * 处理参数
	 * @param arg
	 * @return
	 */
	private static List<File> searchSolrFiles(String arg, List<File> fileNames) {
		File rootFile = null;
		rootFile = new File(arg);
		File[] files = rootFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if(files[i].isFile()){
				Pattern cdpPattern  = Pattern.compile("^.*\\.pdf$");
				Pattern epubPattern = Pattern.compile("^.*\\.xml$");
				Matcher cdpfMatcher = cdpPattern.matcher(files[i].getName());
				Matcher epubfMatcher = epubPattern.matcher(files[i].getName());
				if (cdpfMatcher.matches()) {
					fileNames.add(files[i]);
				}
				if (epubfMatcher.matches()) {
					fileNames.add(files[i]);
				}
			} else if(files[i].isDirectory()) {
				fileNames = searchSolrFiles(files[i].getAbsolutePath(),fileNames);
			}
		}
		return fileNames;
	}

	public void deleteProdIndex(String prodId) throws ServiceException {
		
	}
	
	/**
     * 创建索引时，文件类型过滤
     * @param fileName 文件名称,包括绝对路径
     */
    public static boolean fileTypeFilter(String fullFileName) {
    	
    	 /*文件名称*/
    	String fileName = new File(fullFileName).getName();
    	
    	 /* 文件的类型 */
		String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
		fileType = fileType.toLowerCase();
		
		if(StringUtils.equals("txt", fileType)
				||StringUtils.equals("xml", fileType) 
				||StringUtils.equals("html", fileType)
				||StringUtils.equals("htm", fileType)
				||StringUtils.equals("doc", fileType)
				||StringUtils.equals("docx", fileType)
				||StringUtils.equals("pdf", fileType)
				||StringUtils.equals("iso", fileType)) {
			return true;
		} 
		
    	return false;
    }

    private void handleIndexFile(String indexFile) {
    	if(StringUtils.isNotBlank(indexFile)) {
    		String[] indexFiles = indexFile.split("/");
    		logger.debug(indexFiles);
    		int len = indexFiles.length;
    		if(len > 2) {
    			logger.debug("len============================" + len);
    			logger.debug("indexFile============================" + indexFile);
    			String indexFileNew = "/home/shuke/book/" + indexFiles[len - 2] + "/" + indexFiles[len - 1];
    			logger.debug("IndexFileNew============================" + indexFileNew);
    			File file = new File(indexFileNew);
    			file.getParentFile().mkdirs();
    			try {
					OSCommandUtil.copyFile(indexFile, indexFileNew);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	
    }
}
