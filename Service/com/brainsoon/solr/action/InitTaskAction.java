package com.brainsoon.solr.action;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.brainsoon.common.utils.JSONConvertor;
import com.brainsoon.solr.json.SolrIndexResult;
import com.brainsoon.solr.po.Resource;
import com.brainsoon.solr.po.SolrFile;
import com.brainsoon.solr.po.SolrQueue;
import com.brainsoon.solr.service.ICreateIndexFacede;
import com.brainsoon.solr.service.ISearchFacede;
import com.brainsoon.solr.service.ISolrQueueFacede;
import com.brainsoon.solr.thread.ThreadManager;
import com.brainsoon.solr.util.FileCombine;
import com.brainsoon.solr.util.OptionType;
import com.brainsoon.solr.util.PageSupport;
import com.brainsoon.solr.util.SearchDocument;
import com.brainsoon.solr.util.SolrUtil;
import com.brainsoon.solrservice.res.service.IArticleService;
import com.brainsoon.solrservice.res.service.IBookArticleService;
import com.brainsoon.solrservice.res.service.IBookService;
import com.brainsoon.solrservice.res.service.IEventService;
import com.brainsoon.solrservice.res.service.IJournalService;
import com.brainsoon.solrservice.res.service.INewsService;
import com.brainsoon.solrservice.res.service.ISpecialService;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.utils.BeanFactoryUtil;
import com.channelsoft.appframe.utils.WebappConfigUtil;

public class InitTaskAction extends SolrBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log logger = LogFactory.getLog(InitTaskAction.class);
//	private IBookService bookService;

	@Override
	protected String checkParams() {
		collectParam();
		if (StringUtils.isBlank(params.get("bookid"))) {
			return this.getResultByOpt(OptionType.CREATE_INDEX,
					OptionType.PARAM_200001, "(bookid)");
		}
		return null;
	}

	/**
	 * @Title: initNewsTask
	 * @Description: 初始化资讯
	 * @throws Exception    参数
	 * @return void    返回类型
	 * @throws
	 */
	public void initNewsTask() throws Exception {
		logger.info("[initNewsTask]初始化资讯---------");
		SolrIndexResult solrIndex = new SolrIndexResult();
		try {
			logger.info("[initNewsTask]清空资讯索引---------");
			getSearchFacede().deleteByQuery("restype", "2");
			// 获取文章id
			List newsIds = (List) getNewsService().getNewsIds();
	
			// 添加到新建任务中
			Iterator itr = newsIds.iterator();
			while (itr.hasNext()) {
				Map news = (Map) itr.next();
				String contentid = news.get("contentid").toString();
				logger.info("[initNewsTask]初始化资讯 ID :" + contentid +"---------");
				Map newsInfo = getNewsService().getNewsinfo(Long.valueOf(contentid));
				if (newsInfo != null) {
					try {
						
						Resource resource = new Resource();
						
						// 设置产品标识
						if(newsInfo.get("contentid")!=null && !"".equals(newsInfo.get("contentid"))) 
							resource.setProdid(newsInfo.get("contentid").toString());
						
						// 设置标题
						if(newsInfo.get("title")!=null && !"".equals(newsInfo.get("title"))) 
							resource.setTitle(newsInfo.get("title").toString());
						
						// 设置作者
						if(newsInfo.get("author")!=null && !"".equals(newsInfo.get("author"))) 
							resource.setAuthor(newsInfo.get("author").toString());
						
						// 设置编辑
						if(newsInfo.get("editor")!=null && !"".equals(newsInfo.get("editor"))) 
							resource.setEditor(newsInfo.get("editor").toString());
						
						// 设置摘要
						if(newsInfo.get("description")!=null && !"".equals(newsInfo.get("description"))) 
							resource.setDescription(newsInfo.get("description").toString());

						//设置文章内容
						if(newsInfo.get("content")!=null && !"".equals(newsInfo.get("content"))) 
							resource.setFiles(SolrUtil.Html2Text(newsInfo.get("content").toString()));
						
						//设置上架时间
						if(newsInfo.get("onlinetime")!=null && !"".equals(newsInfo.get("onlinetime"))) 
							resource.setOnlinetime(newsInfo.get("onlinetime").toString());
						
						//分类标识
						if(newsInfo.get("typeid")!=null && !"".equals(newsInfo.get("typeid"))) 
							resource.setTypeid(newsInfo.get("typeid").toString());
						
						//路径
						if(newsInfo.get("url")!=null && !"".equals(newsInfo.get("url"))) 
							resource.setUrl(newsInfo.get("url").toString());
						
						//缩略图
						if(newsInfo.get("thumb")!=null && !"".equals(newsInfo.get("thumb"))) 
							resource.setThumb(newsInfo.get("thumb").toString());
						
						//来源
						if(newsInfo.get("sourceid")!=null && !"".equals(newsInfo.get("sourceid"))) 
							resource.setSourceid(newsInfo.get("sourceid").toString());
						
						//关键词
						if(newsInfo.get("keywords")!=null && !"".equals(newsInfo.get("keywords"))) 
							resource.setKeywords(newsInfo.get("keywords").toString());
						
						//设置资源类型
						resource.setRestype("2");
						//0 创建索引  1删除索引
						resource.setActions("0");
						//UUID
						String uuid = createUUID(resource);
						resource.setUuid(uuid);
						
						ThreadManager.addTask(resource);
						
					} catch(Exception e) {
						logger.debug("[initNewsTask] 未初始化资讯ID:" + contentid +"。原因：" + e.getMessage());
						continue;
					}
				}
	
			}
			solrIndex.setStatus("0");
			solrIndex.setMsg("初始化资讯索引提交成功！");
		} catch (Exception ex) {
			solrIndex.setStatus("-1");
			String errMsg = ex.getMessage();
			if(errMsg.length()>200) {
				errMsg = errMsg.substring(0,200);
			}
			solrIndex.setMsg(errMsg);
		}
	}
	
	/**
	 * 
	* @Title: initArticleTask
	* @Description: 初始化文章索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void initArticleTask() throws Exception {
		logger.info("[initArticleTask]初始化文章索引---------");
		SolrIndexResult solrIndex = new SolrIndexResult();
		
		try {
			logger.info("[initArticleTask]清空文章索引---------");
			getSearchFacede().deleteByQuery("restype", "3");
			// 获取读书id
			List articleids = (List) getArticleService().getArticleIds();

			// 添加到新建任务中
			Iterator itr = articleids.iterator();
			while (itr.hasNext()) {
				Map article = (Map) itr.next();
				String articleid = article.get("bookid").toString();
				Map articleInfo = getArticleService().getArticleInfo(Long.valueOf(articleid));
				if (articleInfo != null) {
					try {
						Resource resource = new Resource();
						/*Book basicBookinfo = bookArticleService.getBookinfo(articleId);
						String orgIds = bookArticleService.getOrgIds(bookId);
						String speIds = bookArticleService.getSpeIds(bookId);
						
						//设置专题库标识
						if(speIds!=null&&!"".equals(speIds)) resource.setSpeid(speIds);
						 //设置机构标识
						if(orgIds!=null&&!"".equals(orgIds)) resource.setOrgid(orgIds)*/;
						
						//设置资源类型
						resource.setRestype("3");
						//设置产品标识
						resource.setProdid(articleid);
						//UUID
						String uuid = createUUID(resource);
						resource.setUuid(uuid);
						//0 创建索引  1删除索引
						resource.setActions("0");	
						 //设置正题名
						if(articleInfo.get("title")!=null && !"".equals(articleInfo.get("title").toString()) ) 
							resource.setTitle(articleInfo.get("title").toString());
						
						//设置所属期刊
						String belong = articleInfo.get("belong").toString();
						if(StringUtils.isNotBlank(belong)){
							Long lbelong = Long.valueOf(belong);
							String belongjournal = getArticleService().getbelJoulBybookId(lbelong);
							resource.setBelongjournal(belongjournal);
						}
							
						//设置文章分类
						if(articleInfo.get("typeid")!=null && !"".equals(articleInfo.get("typeid"))) 
							resource.setTypeid(articleInfo.get("typeid").toString());
						//设置作者
						if(articleInfo.get("author")!=null&&!"".equals(articleInfo.get("author"))) 
							resource.setAuthor(articleInfo.get("author").toString());
						
						//设置价格
						if(articleInfo.get("price")!=null && !"".equals(articleInfo.get("price"))) 
							resource.setPrice(articleInfo.get("price").toString());
						
						//设置折扣价格
						if(articleInfo.get("actprice")!=null && !"".equals(articleInfo.get("actprice"))) 
							resource.setActprice(articleInfo.get("actprice").toString());
						
						//设置初始化价格
						if(articleInfo.get("batchprice")!=null && !"".equals(articleInfo.get("batchprice"))) 
							resource.setBatchprice(articleInfo.get("batchprice").toString());
						
						//设置年份
						if(articleInfo.get("magazineyear")!=null && !"".equals(articleInfo.get("magazineyear"))) 
							resource.setMagazineyear(articleInfo.get("magazineyear").toString());
						
						//设置期数
						if(articleInfo.get("magazineyearnum")!=null && !"".equals(articleInfo.get("magazineyearnum"))) 
							resource.setMagazineyearnum(articleInfo.get("magazineyearnum").toString());
						
						//设置总期数
						if(articleInfo.get("magazinenum")!=null && !"".equals(articleInfo.get("magazinenum"))) 
							resource.setMagazinenum(articleInfo.get("magazinenum").toString());
						
						//设置摘要
						if(articleInfo.get("description")!=null&&!"".equals(articleInfo.get("description"))) 
							resource.setDescription(articleInfo.get("description").toString());
						
						//设置缩略图
						if(articleInfo.get("thumb")!=null&&!"".equals(articleInfo.get("thumb"))) 
							resource.setThumb(articleInfo.get("thumb").toString());
						
						 //设置关键词  
						if(articleInfo.get("keywords")!=null&&!"".equals(articleInfo.get("keywords"))) 
							resource.setKeywords(articleInfo.get("keywords").toString());
						
						 //设置上架时间
						if( articleInfo.get("onlinetime")!=null&&!"".equals(articleInfo.get("onlinetime")) ) 
							resource.setOnlinetime(articleInfo.get("onlinetime").toString());
						
						//设置内容
						String stream = articleInfo.get("stream").toString();
						if (StringUtils.isNotBlank(stream)) {
							
							String prodDir = WebappConfigUtil.getParameter("prodDir") + File.separator + stream;
					        
							String indexFile = prodDir + File.separator + "index.txt";
					        String articleFile = prodDir + File.separator + "article.xml";
						        
					        if (new File(indexFile).exists()) {
					        	new File(indexFile).delete();
					        	logger.info("[initArticleTask]  存在索引文件-删除文件："+indexFile);
							}
					        if(new File(articleFile).exists()) {
					        	if (!new File(indexFile).exists()) {
					        		FileCombine.getFileContent2IndexFile(articleFile, indexFile);
								}
					        	if (new File(indexFile).exists() && new File(indexFile).length()>4) {
					        		resource.setFile(new File(indexFile));
					        		logger.info("[initArticleTask] 文章索引 索引文件："+indexFile);
								}
					        	
					        	ThreadManager.addTask(resource);
					        	
					        }else {
					        	solrIndex.setStatus("-1");
								solrIndex.setMsg("文章xml文件不存在");
							}
						}
						ThreadManager.addTask(resource);
					} catch(Exception e) {
						logger.debug("未初始化文章ID:" + articleid +"。原因：" + e.getMessage());
						continue;
					} 
				}else {
					solrIndex.setStatus("-1");
					solrIndex.setMsg("初始化文章索引提交失败！");
				}
			}
			solrIndex.setStatus("0");
			solrIndex.setMsg("初始化文章索引提交成功！");
		} catch (Exception ex) {
			solrIndex.setStatus("-1");
			String errMsg = ex.getMessage();
			if(errMsg.length()>200) {
				errMsg = errMsg.substring(0,200);
			}
			solrIndex.setMsg(errMsg);
		} 
	}
	
	
	/**
	* @Title: initBookTask
	* @Description: 初始化期刊索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void initJournalTask() throws Exception {
		logger.info("[initJournalTask]初始化期刊索引添加任务--------");
		SolrIndexResult solrIndex = new SolrIndexResult();
		try {
			logger.info("[initJournalTask]清空期刊索引--------");
			getSearchFacede().deleteByQuery("restype", "1");
			// 获取读书id
			List journalids = (List) getJournalService().getJournalIds();
			// 添加到新建任务中
			Iterator itr = journalids.iterator();
			while (itr.hasNext()) {
				Map journal = (Map) itr.next();
				String journalid = journal.get("bookid").toString();
				Map journalinfo = getJournalService().getJournalInfo(Long.valueOf(journalid));
				if (journalinfo != null) {
					try {
						Resource resource = new Resource();
						/*Book basicBookinfo = bookArticleService.getBookinfo(articleId);
						String orgIds = bookArticleService.getOrgIds(bookId);
						String speIds = bookArticleService.getSpeIds(bookId);
						
						//设置专题库标识
						if(speIds!=null&&!"".equals(speIds)) resource.setSpeid(speIds);
						 //设置机构标识
						if(orgIds!=null&&!"".equals(orgIds)) resource.setOrgid(orgIds)*/;
						
						//设置资源类型
						resource.setRestype("1");
						//设置产品标识
						resource.setProdid(journalid);
						//UUID
						String uuid = createUUID(resource);
						resource.setUuid(uuid);
						//0 创建索引  1删除索引
						resource.setActions("0");	
						 //设置正题名
						if(journalinfo.get("title")!=null && !"".equals(journalinfo.get("title").toString()) ) 
							resource.setTitle(journalinfo.get("title").toString());
						
						//设置价格
						if(journalinfo.get("price")!=null && !"".equals(journalinfo.get("price"))) 
							resource.setPrice(journalinfo.get("price").toString());
						
						//设置折扣价格
						if(journalinfo.get("actprice")!=null && !"".equals(journalinfo.get("actprice"))) 
							resource.setActprice(journalinfo.get("actprice").toString());
						
						//设置初始化价格
						if(journalinfo.get("batchprice")!=null && !"".equals(journalinfo.get("batchprice"))) 
							resource.setBatchprice(journalinfo.get("batchprice").toString());
						
						//设置年份
						if(journalinfo.get("magazineyear")!=null && !"".equals(journalinfo.get("magazineyear"))) 
							resource.setMagazineyear(journalinfo.get("magazineyear").toString());
						
						//设置期刊分类---按年份分类
						if(journalinfo.get("magazineyear")!=null && !"".equals(journalinfo.get("magazineyear"))){
							String magazineyear = journalinfo.get("magazineyear").toString();
							if (StringUtils.isNotBlank(magazineyear) && "1949".equals(magazineyear)) {
								magazineyear="1950";
							}else {
								magazineyear = Integer.parseInt(magazineyear)/10*10 +"";
							}
							resource.setTypeid(magazineyear);
						}  
						
						//设置期数
						if(journalinfo.get("magazineyearnum")!=null && !"".equals(journalinfo.get("magazineyearnum"))) 
							resource.setMagazineyearnum(journalinfo.get("magazineyearnum").toString());
						
						//设置总期数
						if(journalinfo.get("magazinenum")!=null && !"".equals(journalinfo.get("magazinenum"))) 
							resource.setMagazinenum(journalinfo.get("magazinenum").toString());
						
						//设置摘要
						if(journalinfo.get("description")!=null&&!"".equals(journalinfo.get("description"))) 
							resource.setDescription(journalinfo.get("description").toString());
						
						//设置缩略图
						if(journalinfo.get("thumb")!=null&&!"".equals(journalinfo.get("thumb"))) 
							resource.setThumb(journalinfo.get("thumb").toString());
						
						 //设置上架时间
						if( journalinfo.get("onlinetime")!=null&&!"".equals(journalinfo.get("onlinetime")) ) 
							resource.setOnlinetime(journalinfo.get("onlinetime").toString());
						
						//设置内容
						String stream = journalinfo.get("stream").toString();
						if (StringUtils.isNotBlank(stream)) {
							
							String prodDir = WebappConfigUtil.getParameter("prodDir") + File.separator + stream;
					        
							String indexFile = prodDir + File.separator + "index.txt";
					        String pageDir = prodDir + File.separator + "pages";
						        
					        if (new File(indexFile).exists()) {
					        	new File(indexFile).delete();
					        	logger.info("[initJournalTask]  存在索引文件-删除文件："+indexFile);
							}
					        if(new File(pageDir).exists()) {
					        	if (!new File(indexFile).exists()) {
					        		FileCombine.combineFile(pageDir, indexFile, "txt");
								}
					        	if (new File(indexFile).exists() && new File(indexFile).length()>4) {
					        		resource.setFile(new File(indexFile));
					        		logger.info("[initJournalTask] 期刊索引 索引文件："+indexFile);
								}
					        	
					        	ThreadManager.addTask(resource);
					        	
					        }else {
					        	solrIndex.setStatus("-1");
								solrIndex.setMsg("期刊pages目录不存在");
							}
						}
					} catch(Exception e) {
						logger.debug("未初始化期刊ID:" + journalid +"。原因：" + e.getMessage());
						continue;
					} 
				}else {
					solrIndex.setStatus("-1");
					solrIndex.setMsg("初始化期刊索引提交失败！");
				}

			}
			solrIndex.setStatus("0");
			solrIndex.setMsg("初始化期刊索引提交成功！");
		} catch (Exception ex) {
			solrIndex.setStatus("-1");
			String errMsg = ex.getMessage();
			if(errMsg.length()>200) {
				errMsg = errMsg.substring(0,200);
			}
			solrIndex.setMsg(errMsg);
		}
	}
	
	/**
	* @Title: initBookTask
	* @Description: 初始化大事辑览索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void initEventTask() throws Exception {
		logger.info("[initEventTask]初始化大事辑览索引添加任务--------");
		SolrIndexResult solrIndex = new SolrIndexResult();
		try {
			logger.info("[initEventTask]清空大事辑览索引--------");
			getSearchFacede().deleteByQuery("restype", "5");
			// 获取读书id
			List eventids = (List) getEventService().getEventIds();
			// 添加到新建任务中
			Iterator itr = eventids.iterator();
			while (itr.hasNext()) {
				Map event = (Map) itr.next();
				String eventid = event.get("id").toString();
				Map eventinfo = getEventService().getEventInfo(Long.valueOf(eventid));
				if (eventinfo != null) {
					try {
						Resource resource = new Resource();
						
						//设置资源类型
						resource.setRestype("5");
						//设置产品标识
						resource.setProdid(eventid);
						//UUID
						String uuid = createUUID(resource);
						resource.setUuid(uuid);
						//0 创建索引  1删除索引
						resource.setActions("0");	
						 //设置正题名
						if(eventinfo.get("title")!=null && !"".equals(eventinfo.get("title").toString()) ) 
							resource.setTitle(eventinfo.get("title").toString());
						
						//设置所属期刊
						if(eventinfo.get("belongjournal")!=null && !"".equals(eventinfo.get("belongjournal"))) 
							resource.setBelongjournal(eventinfo.get("belongjournal").toString());
						
						//设置折扣价格
						if(eventinfo.get("typeid")!=null && !"".equals(eventinfo.get("typeid"))) 
							resource.setTypeid(eventinfo.get("typeid").toString());
						
						//设置出版时间
						if(eventinfo.get("published")!=null && !"".equals(eventinfo.get("published"))) 
							resource.setPublished(eventinfo.get("published").toString());
						
						//设置内容
						if(eventinfo.get("text")!=null && !"".equals(eventinfo.get("text"))) 
							resource.setFiles(eventinfo.get("text").toString());
						
						ThreadManager.addTask(resource);
						
					} catch(Exception e) {
						logger.debug("未初始化大事辑览ID:" + eventid +"。原因：" + e.getMessage());
						continue;
					} 
				}else {
					solrIndex.setStatus("-1");
					solrIndex.setMsg("初始化大事辑览索引提交失败！");
				}

			}
			solrIndex.setStatus("0");
			solrIndex.setMsg("初始化大事辑览索引提交成功！");
		} catch (Exception ex) {
			solrIndex.setStatus("-1");
			String errMsg = ex.getMessage();
			if(errMsg.length()>200) {
				errMsg = errMsg.substring(0,200);
			}
			solrIndex.setMsg(errMsg);
		}
	}
	
	/**
	 * 
	* @Title: initSpecialTask
	* @Description: 初始化默认专题库图书索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void initSpecialBookTask() throws Exception {//
		logger.info("[initSpecialTask]初始化默认专题库图书索引添加任务--------");
		SolrIndexResult solrIndex = new SolrIndexResult();
		try {
			// 获取文章id
			List specialids = (List) getSpecialService().getSpecialIds();
	
			// 添加到新建任务中
			Iterator itr1 = specialids.iterator();
			while (itr1.hasNext()) {//循环主题库
				Map special = (Map) itr1.next();
				String specialid = special.get("specialid").toString();
				List BookIds = getSpecialService().getSpecialBooksIds(Long.parseLong(specialid));			
				Iterator itr2 = BookIds.iterator();
				while (itr2.hasNext()) {//循环主题库图书
					Map book = (Map) itr2.next();
					String bookid = book.get("bookid").toString();			
					Map bookinfo = getSpecialService().getAttributeByid(Long.valueOf(bookid));
					if(bookinfo != null) {
						//0:文章,1为期刊
						if(bookinfo.get("attribute")!=null && !"".equals(bookinfo.get("attribute").toString()) ) {
							boolean attribute = (Boolean)bookinfo.get("attribute");
							if (attribute) {//期刊
								createSpecialJournalIndex(bookid,specialid,"0");
							}else {
								createSpecialArticleIndex(bookid,specialid,"0");
							}
						}
					}
				}
			}
			solrIndex.setStatus("0");
			solrIndex.setMsg("初始化默认专题库图书索引提交成功！");
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
	* @Title: initSpecialTask
	* @Description: 初始化采选单图书索引
	* @throws Exception    参数
	* @return void    返回类型
	* @throws
	 */
	public void initCollectBookTask() throws Exception {//
		logger.info("[initCollectBookTask]初始化采选单图书索引--------");
		SolrIndexResult solrIndex = new SolrIndexResult();
		try {
			//查出表中相关数据				
			List collects = getSpecialService().getCollects();
	
			// 添加到新建任务中
			Iterator itr1 = collects.iterator();
			while (itr1.hasNext()) {//循环主题库
				Map collect = (Map) itr1.next();
				
				String collectid = "";
				if(collect.get("collectid")!=null && !"".equals(collect.get("collectid"))) 
					collectid = collect.get("collectid").toString();
				
				String orgid = "";
				if(collect.get("organizationid")!=null && !"".equals(collect.get("organizationid"))) 
					orgid = collect.get("organizationid").toString();
				
				int type = -1 ;
				if(collect.get("type")!=null && !"".equals(collect.get("type"))) 
					type = (Integer) collect.get("type");
				
				String special = "";
				if(collect.get("special")!=null && !"".equals(collect.get("special"))) 
					special = collect.get("special").toString();
				
				if (type>0) {
					if (type == 2) {//采选单采选的是主题库
						logger.info("[initCollectBookTask]采选单id"+collectid+"采选的是专题库:"+special+"--------");
						if (StringUtils.isNotBlank(special)) {
							String[] specialIds = special.split(",");
							for (String specialid : specialIds) {//循环主题库
								
								List BookIds = getSpecialService().getSpecialBooksIds(Long.parseLong(specialid));			
								Iterator itr2 = BookIds.iterator();
								while (itr2.hasNext()) {//循环主题库图书
									Map book = (Map) itr2.next();
									String bookid = book.get("bookid").toString();			
									Map bookinfo = getSpecialService().getAttributeByid(Long.valueOf(bookid));
									if(bookinfo != null) {
										//0:文章,1为期刊
										if(bookinfo.get("attribute")!=null && !"".equals(bookinfo.get("attribute").toString()) ) {
											boolean attribute = (Boolean)bookinfo.get("attribute");
											if (attribute) {//期刊
												createSpecialJournalIndex(bookid,specialid,orgid);
											}else {
												createSpecialArticleIndex(bookid,specialid,orgid);
											}
										}
									}
								}
							}
						}
					}else if (type==0 || type==1){//采选单采选的是单个文章或期刊
						logger.info("[initCollectBookTask]采选单id"+collectid+"采选的是0文章1期刊2专题库:"+type+"--------");
						List BookIds = getSpecialService().getCollectBooksIds(Long.parseLong(collectid));
						Iterator itr = BookIds.iterator();
						while (itr.hasNext()) {
							Map book = (Map) itr.next();
							String bookid = book.get("bookid").toString();			
							if ("1".equals(type)) {//期刊
								createSpecialJournalIndex(bookid,"0",orgid);
							}else {
								createSpecialArticleIndex(bookid,"0",orgid);
							}
						}
					}
					
				}
			}
			solrIndex.setStatus("0");
			solrIndex.setMsg("初始化采选单图书索引提交成功！");
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
	
	private void createSpecialJournalIndex(String bookid, String prodid,String orgid) {
		Map journalinfo = getJournalService().getJournalInfo(Long.valueOf(bookid));
		if (journalinfo != null) {
			Resource resource = new Resource();
			
			//设置资源类型
			resource.setRestype("1");
			//设置产品标识
			resource.setProdid(bookid);
			//设置机构标识
			resource.setOrgid(orgid);
			//设置专题库标识
			resource.setSpeid(prodid);
			//UUID
			String uuid = createSpecialBookUUID(resource);
			resource.setUuid(uuid);
			//0 创建索引  1删除索引
			resource.setActions("2");
			
			 //设置正题名
			if(journalinfo.get("title")!=null && !"".equals(journalinfo.get("title").toString()) ) 
				resource.setTitle(journalinfo.get("title").toString());
			
			//设置价格
			if(journalinfo.get("price")!=null && !"".equals(journalinfo.get("price"))) 
				resource.setPrice(journalinfo.get("price").toString());
			
			//设置折扣价格
			if(journalinfo.get("actprice")!=null && !"".equals(journalinfo.get("actprice"))) 
				resource.setActprice(journalinfo.get("actprice").toString());
			
			//设置初始化价格
			if(journalinfo.get("batchprice")!=null && !"".equals(journalinfo.get("batchprice"))) 
				resource.setBatchprice(journalinfo.get("batchprice").toString());
			
			//设置年份
			if(journalinfo.get("magazineyear")!=null && !"".equals(journalinfo.get("magazineyear"))) 
				resource.setMagazineyear(journalinfo.get("magazineyear").toString());

			//设置期刊分类---按年份分类
			if(journalinfo.get("magazineyear")!=null && !"".equals(journalinfo.get("magazineyear"))){
				String magazineyear = journalinfo.get("magazineyear").toString();
				if (StringUtils.isNotBlank(magazineyear) && "1949".equals(magazineyear)) {
					magazineyear="1950";
				}else {
					magazineyear = Integer.parseInt(magazineyear)/10*10 +"";
				}
				resource.setTypeid(magazineyear);
			} 
			
			//设置期数
			if(journalinfo.get("magazineyearnum")!=null && !"".equals(journalinfo.get("magazineyearnum"))) 
				resource.setMagazineyearnum(journalinfo.get("magazineyearnum").toString());
			
			//设置总期数
			if(journalinfo.get("magazinenum")!=null && !"".equals(journalinfo.get("magazinenum"))) 
				resource.setMagazinenum(journalinfo.get("magazinenum").toString());
			
			//设置摘要
			if(journalinfo.get("description")!=null&&!"".equals(journalinfo.get("description"))) 
				resource.setDescription(journalinfo.get("description").toString());
			
			//设置缩略图
			if(journalinfo.get("thumb")!=null&&!"".equals(journalinfo.get("thumb"))) 
				resource.setThumb(journalinfo.get("thumb").toString());
			
			 //设置上架时间
			if( journalinfo.get("onlinetime")!=null&&!"".equals(journalinfo.get("onlinetime")) ) 
				resource.setOnlinetime(journalinfo.get("onlinetime").toString());
			
			//设置内容
			String stream = journalinfo.get("stream").toString();
			if (StringUtils.isNotBlank(stream)) {
				
				String prodDir = WebappConfigUtil.getParameter("prodDir") + File.separator + stream;
		        
				String indexFile = prodDir + File.separator + "index.txt";
		        String pageDir = prodDir + File.separator + "pages";
			        
		        if (new File(indexFile).exists()) {
		        	new File(indexFile).delete();
		        	logger.info("[initSpecialBookTask]  存在索引文件-删除文件："+indexFile);
				}
		        if(new File(pageDir).exists()) {
		        	if (!new File(indexFile).exists()) {
		        		FileCombine.combineFile(pageDir, indexFile, "txt");
					}
		        	if (new File(indexFile).exists() && new File(indexFile).length()>4) {
		        		resource.setFile(new File(indexFile));
		        		logger.info("[initSpecialBookTask] 期刊索引 索引文件："+indexFile);
					}
		        	
		        	ThreadManager.addTask(resource);
		        	
		        }else {
		        	logger.info("[initSpecialBookTask] ERROR 索引文件不存在："+indexFile);
				}
			}
		}
	}
	private void createSpecialArticleIndex(String bookid, String prodid,String orgid) {
		
		Map articleinfo = getArticleService().getArticleInfo(Long.valueOf(bookid));
		if (articleinfo != null) {
			Resource resource = new Resource();
			//设置资源类型
			resource.setRestype("3");
			//设置产品标识
			resource.setProdid(bookid);
			//设置机构标识
			resource.setOrgid(orgid);
			//设置专题库标识
			resource.setSpeid(prodid);
			//UUID
			String uuid = createSpecialBookUUID(resource);
			resource.setUuid(uuid);
			//0 创建索引  1删除索引 2创建专题库索引
			resource.setActions("2");
						
			 //设置正题名
			if(articleinfo.get("title")!=null && !"".equals(articleinfo.get("title").toString()) ) 
				resource.setTitle(articleinfo.get("title").toString());
			
			//设置文章分类
			if(articleinfo.get("typeid")!=null && !"".equals(articleinfo.get("typeid"))) 
				resource.setTypeid(articleinfo.get("typeid").toString());
			//设置作者
			if(articleinfo.get("author")!=null&&!"".equals(articleinfo.get("author"))) 
				resource.setAuthor(articleinfo.get("author").toString());
			
			//设置价格
			if(articleinfo.get("price")!=null && !"".equals(articleinfo.get("price"))) 
				resource.setPrice(articleinfo.get("price").toString());
			
			//设置折扣价格
			if(articleinfo.get("actprice")!=null && !"".equals(articleinfo.get("actprice"))) 
				resource.setActprice(articleinfo.get("actprice").toString());
			
			//设置初始化价格
			if(articleinfo.get("batchprice")!=null && !"".equals(articleinfo.get("batchprice"))) 
				resource.setBatchprice(articleinfo.get("batchprice").toString());
			
			//设置年份
			if(articleinfo.get("magazineyear")!=null && !"".equals(articleinfo.get("magazineyear"))) 
				resource.setMagazineyear(articleinfo.get("magazineyear").toString());
			
			//设置期数
			if(articleinfo.get("magazineyearnum")!=null && !"".equals(articleinfo.get("magazineyearnum"))) 
				resource.setMagazineyearnum(articleinfo.get("magazineyearnum").toString());
			
			//设置总期数
			if(articleinfo.get("magazinenum")!=null && !"".equals(articleinfo.get("magazinenum"))) 
				resource.setMagazinenum(articleinfo.get("magazinenum").toString());
			
			//设置摘要
			if(articleinfo.get("description")!=null&&!"".equals(articleinfo.get("description"))) 
				resource.setDescription(articleinfo.get("description").toString());
			
			//设置缩略图
			if(articleinfo.get("thumb")!=null&&!"".equals(articleinfo.get("thumb"))) 
				resource.setThumb(articleinfo.get("thumb").toString());
			
			 //设置关键词  
			if(articleinfo.get("keywords")!=null&&!"".equals(articleinfo.get("keywords"))) 
				resource.setKeywords(articleinfo.get("keywords").toString());
			
			 //设置上架时间
			if( articleinfo.get("onlinetime")!=null&&!"".equals(articleinfo.get("onlinetime")) ) 
				resource.setOnlinetime(articleinfo.get("onlinetime").toString());
			
			//设置内容
			String stream = articleinfo.get("stream").toString();
			if (StringUtils.isNotBlank(stream)) {
				
				String prodDir = WebappConfigUtil.getParameter("prodDir") + File.separator + stream;
		        
				String indexFile = prodDir + File.separator + "index.txt";
		        String articleFile = prodDir + File.separator + "article.xml";
			        
		        if (new File(indexFile).exists()) {
		        	new File(indexFile).delete();
		        	logger.info("[initSpecialBookTask]  存在索引文件-删除文件："+indexFile);
				}
		        if(new File(articleFile).exists()) {
		        	if (!new File(indexFile).exists()) {
		        		FileCombine.getFileContent2IndexFile(articleFile, indexFile);
					}
		        	if (new File(indexFile).exists() && new File(indexFile).length()>4) {
		        		resource.setFile(new File(indexFile));
		        		logger.info("[initSpecialBookTask] 文章索引 索引文件："+indexFile);
					}
		        	
		        	ThreadManager.addTask(resource);
		        	
		        }else {
		        	logger.info("[initSpecialBookTask] ERROR 索引文件不存在："+indexFile);
				}
			}
		}
		
	}
	
	private String createUUID(Resource resource) {
		String uuid = String.valueOf(resource.getRestype());
		uuid = uuid + "_" + String.valueOf(resource.getProdid()); 
		logger.debug("uuid:" + uuid);
		return uuid;
	}
	
	private String createSpecialBookUUID(Resource resource) {
		String prodid = resource.getProdid();
		String restype = resource.getRestype();
		String orgid = resource.getOrgid();
		String speid = resource.getSpeid();
		String uuid = restype+"_"+prodid+"_"+orgid+"_"+speid;
		logger.debug("uuid:" + uuid);
		return uuid;
	}
	
	private ISpecialService getSpecialService() {
		return (ISpecialService) BeanFactoryUtil.getBean("specialService");
	}

	private ISearchFacede getSearchFacede() {
    	return (ISearchFacede)BeanFactoryUtil.getBean("searchFacede");
    }
	
	private INewsService getNewsService() {
		return (INewsService) BeanFactoryUtil.getBean("newsService");
	}
	
	private IArticleService getArticleService() {
		return (IArticleService) BeanFactoryUtil.getBean("articleService");
	}
	
	private IJournalService getJournalService() {
		return (IJournalService) BeanFactoryUtil.getBean("journalService");
	}

	private IEventService getEventService() {
		return (IEventService) BeanFactoryUtil.getBean("eventService");
	}

	public ICreateIndexFacede getCreateIndexService() {
		return (ICreateIndexFacede)BeanFactoryUtil.getBean("createIndexService");
	}
	
}
