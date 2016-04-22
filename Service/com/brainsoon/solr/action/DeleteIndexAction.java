package com.brainsoon.solr.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.brainsoon.common.utils.JSONConvertor;
import com.brainsoon.solr.json.SolrIndexResult;
import com.brainsoon.solr.service.ISearchFacede;
import com.brainsoon.solr.service.ISearchSpecialFacede;
import com.brainsoon.solr.service.ISolrQueueFacede;
import com.brainsoon.solrservice.res.service.IBookArticleService;
import com.brainsoon.solrservice.res.service.IBookService;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.utils.BeanFactoryUtil;

public class DeleteIndexAction extends SolrBaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log logger = LogFactory.getLog(DeleteIndexAction.class);

	public void deleteAllIndex() throws Exception {
		SolrIndexResult solrIndex = new SolrIndexResult();
		try {
			getSearchFacede().cleanSolrIndex();
			solrIndex.setStatus("0");
			solrIndex.setMsg("删除所有索引成功！");
		} catch (Exception ex) {
			solrIndex.setStatus("-1");
			String errMsg = ex.getMessage();
			if(errMsg.length()>200) {
				errMsg = errMsg.substring(0,200);
			}
			solrIndex.setMsg(errMsg);
		}
		String result = JSONConvertor.bean2Json(solrIndex);
		this.outputResult(result);
	}
	
	/**
	 * 
	* @Title: deleteNewsIndex
	* @Description: 删除资讯索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void deleteNewsIndex() throws Exception {
		collectParam(); 
		//collectPostParam();
		String ids = params.get("ids");		
		SolrIndexResult solrIndex = new SolrIndexResult();
		if(StringUtils.isBlank(ids)) {
			solrIndex.setStatus("-2");
			solrIndex.setMsg("资讯id不能为空！");
			String result = JSONConvertor.bean2Json(solrIndex);
			this.outputResult(result);
			return;
		}
		String[] newsIds = ids.split(",");		
		try {
			getSearchFacede().deleteNewsIndex(newsIds);
			solrIndex.setStatus("0");
			solrIndex.setMsg("删除资讯索引成功！");
		} catch (Exception ex) {
			solrIndex.setStatus("-1");
			String errMsg = ex.getMessage();
			if(errMsg.length()>200) {
				errMsg = errMsg.substring(0,200);
			}
			solrIndex.setMsg(errMsg);
		}
		String result = JSONConvertor.bean2Json(solrIndex);
		this.outputResult(result);
	}
	
	/**
	 * 
	* @Title: deleteNewsIndex
	* @Description: 删除文章索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void deleteArticleIndex() throws Exception {
		collectParam(); 
		//collectPostParam();
		String ids = params.get("ids");		
		SolrIndexResult solrIndex = new SolrIndexResult();
		if(StringUtils.isBlank(ids)) {
			solrIndex.setStatus("-2");
			solrIndex.setMsg("文章id不能为空！");
			String result = JSONConvertor.bean2Json(solrIndex);
			this.outputResult(result);
			return;
		}
		String[] articleIds = ids.split(",");		
		try {
			getSearchFacede().deleteArticleIndex(articleIds);
			solrIndex.setStatus("0");
			solrIndex.setMsg("删除资讯索引成功！");
		} catch (Exception ex) {
			solrIndex.setStatus("-1");
			String errMsg = ex.getMessage();
			if(errMsg.length()>200) {
				errMsg = errMsg.substring(0,200);
			}
			solrIndex.setMsg(errMsg);
		}
		String result = JSONConvertor.bean2Json(solrIndex);
		this.outputResult(result);
	}
	
	/**
	 * 
	* @Title: deleteNewsIndex
	* @Description: 删除期刊索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void deleteJournalIndex() throws Exception {
		collectParam(); 
		String journalids = params.get("ids");		
		SolrIndexResult solrIndex = new SolrIndexResult();
		if(StringUtils.isBlank(journalids)) {
			solrIndex.setStatus("-2");
			solrIndex.setMsg("期刊id不能为空！");
			String result = JSONConvertor.bean2Json(solrIndex);
			this.outputResult(result);
			return;
		}
		String[] journalidsArr = journalids.split(",");		
		try {
			getSearchFacede().deleteJournalIndex(journalidsArr);
			solrIndex.setStatus("0");
			solrIndex.setMsg("删除期刊索引成功！");
		} catch (Exception ex) {
			solrIndex.setStatus("-1");
			String errMsg = ex.getMessage();
			if(errMsg.length()>200) {
				errMsg = errMsg.substring(0,200);
			}
			solrIndex.setMsg(errMsg);
		}
		String result = JSONConvertor.bean2Json(solrIndex);
		this.outputResult(result);
		
	}
	
	/**
	 * 
	* @Title: deleteNewsIndex
	* @Description: 删除大事辑览索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void deleteEventIndex() throws Exception {
		collectParam(); 
		String eventids = params.get("ids");		
		SolrIndexResult solrIndex = new SolrIndexResult();
		if(StringUtils.isBlank(eventids)) {
			solrIndex.setStatus("-2");
			solrIndex.setMsg("大事辑览id不能为空！");
			String result = JSONConvertor.bean2Json(solrIndex);
			this.outputResult(result);
			return;
		}
		String[] jeventidsArr = eventids.split(",");		
		try {
			getSearchFacede().deleteEventIndex(jeventidsArr);
			solrIndex.setStatus("0");
			solrIndex.setMsg("删除大事辑览索引成功！");
		} catch (Exception ex) {
			solrIndex.setStatus("-1");
			String errMsg = ex.getMessage();
			if(errMsg.length()>200) {
				errMsg = errMsg.substring(0,200);
			}
			solrIndex.setMsg(errMsg);
		}
		String result = JSONConvertor.bean2Json(solrIndex);
		this.outputResult(result);
		
	}
	
	private ISearchFacede getSearchFacede() {
    	return (ISearchFacede)BeanFactoryUtil.getBean("searchFacede");
    }
	public IBookService getBookService() {
		return (IBookService)BeanFactoryUtil.getBean("bookService");
	}
	public IBookArticleService getBookArticleService() {
		return (IBookArticleService)BeanFactoryUtil.getBean("bookArticleService");
	}	
}
