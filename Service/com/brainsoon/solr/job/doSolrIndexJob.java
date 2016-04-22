//package com.brainsoon.solr.job;
//
//import java.io.File;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.solr.client.solrj.SolrServer;
//
//import com.brainsoon.solr.po.Resource;
//import com.brainsoon.solr.po.SolrFile;
//import com.brainsoon.solr.po.SolrQueue;
//import com.brainsoon.solr.service.ICreateIndexFacede;
//import com.brainsoon.solr.service.ISearchFacede;
//import com.brainsoon.solr.service.ISolrQueueFacede;
//import com.brainsoon.solr.util.PageSupport;
//import com.brainsoon.solr.util.SearchBookArticle;
//import com.brainsoon.solr.util.SearchDocument;
//import com.brainsoon.solr.util.SolrClient;
//import com.brainsoon.solr.util.SolrUtil;
//import com.brainsoon.solrservice.res.po.Book;
//import com.brainsoon.solrservice.res.service.IArticleService;
//import com.brainsoon.solrservice.res.service.IBookArticleService;
//import com.brainsoon.solrservice.res.service.IBookService;
//import com.brainsoon.solrservice.res.service.ISpecialService;
//import com.channelsoft.appframe.common.BaseObject;
//import com.channelsoft.appframe.utils.BeanFactoryUtil;
//
//public class doSolrIndexJob extends BaseObject {
//	
//	private ISolrQueueFacede solrQueueFacede;
//	
//	private ISearchFacede searchFacede;
//	
//	private IBookService bookService;
//	
//	private IBookArticleService bookArticleService;
//	
//	private IArticleService articleService;
//	
//	private ISpecialService specialService;
//	
//	private ICreateIndexFacede createIndexService;
//	
//    /* 文件创建索引成功 */
//	private final static String FILE_CREATE_SUCCESS = "0";
//	
//	/* 文件创建索引失败 */
//	private final static String FILE_CREATE_FAIL = "1";
//	
//	/* 线程同步标识 */
//	private static boolean isruning = false;
//	
//	private SolrServer solrServer;
//	private SolrClient solrClient;
//		
//	/**
//	 * 初始化数据
//	 */
//	public void initService() {
//		 // 加载数据服务接口
//		loadSolrQueueFacede();
//		 // 加全文索引服务接口
//		loadSearchFacede();
//		
//		// 加载资源
//		loadBookService();
//		loadBookArticleService();
//		loadArticleService();
//		loadSpecialService();
//	}
//
//	/**
//	 * 索引创建任务调度方法
//	 */
//	public void doIndexJob() {
//		logger.debug("索引队列集合数量：" + isruning);
//		  /* 正在处理中则返回 */
//		if (isruning) {
//			return;
//		}
//		 //初始化服务接口
//		initService();
//		isruning = true;
//		try {
//			 //索引服务器是否启动
//			if(searchFacede.isCanUseSolrServer()) {
//				 //查询获取创建索引队列集合
//				List<SolrQueue> solrQueues = solrQueueFacede.getSolrQueueByStatus("0");
//				logger.debug("索引队列集合数量：" + solrQueues.size());
//				if(solrQueues!=null &&solrQueues.size()>0) {
//					for(int i = 0;i< solrQueues.size(); i++) {
//						
//						SolrQueue queue = solrQueues.get(i);
//						 // 如果索引动作为0,则创建索引；如果为1，则删除索引
//						if(StringUtils.equals("0", queue.getActions())) {
//							 //创建索引
//							if(queue.getResType().toString().equals("1")) {
//								createBookIndex(queue);
//							} else if(queue.getResType().toString().equals("3")) {
//								createBookArticleIndex(queue);
//							} else if(queue.getResType().toString().equals("2")) {
//								createArticleIndex(queue);
//							} else if(queue.getResType().toString().equals("4")) {
//								createSpecialIndex(queue);
//							} else {
//								logger.debug("资源类型错误");
//							}
//						} else if(StringUtils.equals("1", queue.getActions())) {
//							
//							 //删除索引
//							if(queue.getResType().toString().equals("1")) {
//								deleteBookIndex(queue);
//							} else if(queue.getResType().toString().equals("3")) {
//								deleteBookArticleIndex(queue);
//							} else if(queue.getResType().toString().equals("2")) {
//								deleteArticleIndex(queue);
//							}  else if(queue.getResType().toString().equals("4")) {
//								deleteSpecialIndex(queue);
//							} else {
//								logger.debug("资源类型错误");
//							}	
//						} else {
//							logger.debug("索引动作信息：{" + queue.getActions() + "}无该索引动作状态！");
//						}
//					}
//				}
//			}
//		} catch(Exception ex) {
//			logger.error(ex);
//		} finally {
//			isruning = false;
//		}
//	}
//	
//	/**
//	 * 创建图书索引
//	 * @param queue
//	 */
//	private void createBookIndex(SolrQueue queue) {
//		
//		try {
//			Resource resource = null;
//			String uuid = createUUID(queue);
//			//查看索引中是已经创建索引
//			SearchDocument doc = new SearchDocument();
//			doc.setStart(0);
//			doc.setRows(1);
//			doc.setSortWay("salesnum");
//			PageSupport pages = getSearchFacede().searchOneDocument(uuid, doc);
//			List items = pages.getItems();
//			if(items.size()>0) {
//				resource = (Resource)items.get(0);
//				String price = queue.getPrice();
//				String salesnum = queue.getSalesnum();
//				String stsalesnum = queue.getStsalesnum();
//				String onlinetime = queue.getOnlinetime();
//				String bookStatus = queue.getBookStatus();
//				String bookStStatus = queue.getBookStStatus();
//				String orgid = null;
//				if(queue.getOrgId()!=null) {
//					orgid = queue.getOrgId().toString();
//				}
//				String speid = null;
//				if(queue.getSpeId()!=null) {
//					speid = queue.getSpeId().toString();
//				}
//						
//				//价格变化后更改
//				if(price!=null&&!"".equals(price)&&!price.equals(resource.getPrice())) {
//					resource.setPrice(price);
//				}
//
//				//销量
//				if(salesnum!=null&&!"".equals(salesnum)&&!salesnum.equals(resource.getSalesnum())) {
//					resource.setSalesnum(salesnum);
//				}
//				
//				//实体书销量
//				if(stsalesnum!=null&&!"".equals(stsalesnum)&&!stsalesnum.equals(resource.getStsalesnum())) {
//					resource.setStsalesnum(stsalesnum);
//				}
//				
//				//上线时间
//				if(onlinetime!=null&&!"".equals(onlinetime)&&!onlinetime.equals(resource.getOnlinetime())) {
//					resource.setOnlinetime(onlinetime);
//				}
//				
//				if(bookStatus!=null) {
//					resource.setStatus(bookStatus);
//				}
//				if(bookStStatus!=null) {
//					resource.setStstatus(bookStStatus);
//				}
//				
//				supplyBookPrices(resource);
//				
//				//机构馆
//				String orgids = resource.getOrgid();
//				String[] orgArray = null;
//				if(orgids!=null&&!"".equals(orgids)) {
//					orgArray = orgids.split(" ");
//				}
//				
//				if(orgid!=null&&!"".equals(orgid)) {					
//					//0-添加 1-删除
//					if(queue.getOrgAction().equals("0")) {
//						orgids = "";
//						if(orgArray!=null) {
//							for (int i=0; i<orgArray.length; i++) { 
//								if(!orgid.equals(orgArray[i])) {
//									orgids = orgids + orgArray[i] + " ";
//								}
//					        }
//						}
//						orgids = orgids + orgid;
//					} else {
//						orgids = "";
//						if(orgArray!=null) {
//							for (int i=0; i<orgArray.length; i++) { 
//								if(!orgid.equals(orgArray[i])) {
//									orgids = orgids + orgArray[i];
//									if(i!=orgArray.length-2) {
//										orgids = orgids + " ";
//									}
//								}
//					        }
//						}
//					}
//					resource.setOrgid(orgids);
//				}
//				
//				//专题库
//				String speids = resource.getSpeid();
//				String[] speArray = null;
//				if(speids!=null&&!"".equals(speids)) {
//					speArray = speids.split(" ");
//				}
//				
//				if(speid!=null&&!"".equals(speid)) {					
//					//0-添加 1-删除
//					if(queue.getSpeAction().equals("0")) {
//						speids = "";
//						if(speArray!=null) {
//							for (int i=0; i<speArray.length; i++) { 
//								if(!speid.equals(speArray[i])) {
//									speids = speids + speArray[i] + " ";
//								}
//					        }
//						}
//						speids = speids + speid;
//
//					} else {
//						speids = "";
//						if(speArray!=null) {
//							for (int i=0; i<speArray.length; i++) { 
//								if(!speid.equals(speArray[i])) {
//									speids = speids + speArray[i];
//									if(i!=speArray.length-2) {
//										speids = speids + " ";
//									}
//								}
//					        }
//						}
//					}
//					resource.setSpeid(speids);
//				}
//				
//				
//			} else {
//				 //加载数据资源
//				resource = loadBookResource(queue);
//			}
//			
//			Set<SolrFile> solrFiles = queue.getSolrFiles();
//			logger.debug(solrFiles.size());
//			if(solrFiles.size()>0) {
//				for(SolrFile solrFile :solrFiles ) {
//					
//					try {
//						 //包括文件名称及格式
//						String path = solrFile.getPath();
//						if(!new File(path).exists()) {
//							continue;
//						}
//						 //文件所在目录
//						String sourcePath = new File(path).getParent();
//						 //文件名称，包括格式
//						String fileNameSource = new File(path).getName();
//						 //原文件名称 没有格式名称
//						String fileName = fileNameSource.substring(0,fileNameSource.lastIndexOf("."));
//						 //文件的类型
//						String fileType = fileNameSource.substring(fileNameSource.lastIndexOf(".")+1);
//						fileType = fileType.toLowerCase();
//						if(StringUtils.equals("pdf", fileType)) {
//							resource.setFiletype("pdf");
//						} else if(StringUtils.equals("doc", fileType)
//								||StringUtils.equals("docx", fileType)) {
//							resource.setFiletype("doc");
//						} else if(StringUtils.equals("txt", fileType)
//								||StringUtils.equals("xml", fileType) 
//								||StringUtils.equals("html", fileType)
//								||StringUtils.equals("htm", fileType)
//								||StringUtils.equals("iso", fileType)) {
//							resource.setFiletype("txt");
//						} else {
//							logger.debug("不支持该类型文件：" + fileType );
//							solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未创建，不支持该文件格式！");
//							continue;
//						}
//						logger.debug("文件类型：" + fileType );
//						
//						
//						
//						File  file = null;
//						 //如果为pdf格式，则提取文本
//						if("pdf".equals(fileType)) {
//							file = new File(path);
//						}else if("xml".equals(fileType)) {
//							if(SolrUtil.xml2Text( path )) {
//								file = new File( sourcePath + File.separator + fileName + ".txt");
//							} else {
//								//更新失败状态和日志信息
//								solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未创建，xml过滤特殊字符后文件为空！");
//								continue;
//							}
//						} else {
//							file = new File( path );
//						}
//						  //文件的大小
//						Long fileLength = file.length();
//						  //如果文件的大小小于4个字节，就是两个汉字就没有的情况
//						  //则调过，不创建索引
//						if( fileLength < 4) {
//							logger.debug("索引未创建，该文件为空！文件长度：" + fileLength);
//							//更新失败状态和日志信息
//							solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未创建，该文件为空！");
//							continue;
//						}
//						if(file != null) {
//							resource.setUuid(uuid);
//							logger.debug(solrFile.getId());
//							 //设置资源文件标识
//							resource.setResfileid(String.valueOf(solrFile.getId()));
//							 //创建索引
//							try {
//								searchFacede.addIndex(resource, file);
//								 //清除临时文件 
//								clearTempFile( file, fileType );
//								String msg = "索引已创建，创建索引成功！";
//								msg.getBytes("UTF-8");
//								 //更新成功状态和日志信息
//								solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_SUCCESS, "索引已创建，创建索引成功！");
//								logger.debug("创建索引{"+ uuid +"}成功！");
//							}catch (Exception e) {
//								String errorMsg = e.getMessage();
//								logger.debug(errorMsg);
//							}
//						} 
//					
//					}catch (Exception e) {
//						String errorMsg = e.getMessage();
//	//					if(errorMsg.length()>512) {
//	//						errorMsg = errorMsg.substring(0,500);
//	//					}
//						//更新失败状态和日志信息
//						solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未创建，创建索引异常！" + errorMsg);
//					} 
//				}
//			} else {
//				try {
//					resource.setUuid(uuid);
//					searchFacede.addIndex(resource);
//					String msg = "索引已创建，创建索引成功！";
//					msg.getBytes("UTF-8");
//					logger.debug("创建索引{"+ uuid +"}成功！");
//				}catch (Exception e) {
//					String errorMsg = e.getMessage();
//					logger.debug(errorMsg);
//				}
//			}
//			//更新资源对象状态为已创建
//			solrQueueFacede.createIndexSuccess( queue.getId() );
//			logger.debug("创建索引成功！");
//		} catch (Exception se) {
//			 //更新资源对象状态创建失败
//			solrQueueFacede.createIndexFailed( queue.getId() );
//			logger.error("创建索引失败！" , se);
//		}
//	}
//	
//	/**
//	 * 创建专题库文章索引
//	 * @param queue
//	 */
//	private void createBookArticleIndex(SolrQueue queue) {
//		
//		try {
//			Resource resource = null;
//			String uuid = createUUID(queue);
//			//查看索引中是已经创建索引
//			SearchBookArticle doc = new SearchBookArticle();
//			doc.setStart(0);
//			doc.setRows(1);
//			doc.setSortWay("salesnum");
//			PageSupport pages = getSearchFacede().searchOneBookArticle(uuid, doc);
//			List items = pages.getItems();
//			if(items.size()>0) {
//				resource = (Resource)items.get(0);
//				String price = queue.getPrice();
//				String salesnum = queue.getSalesnum();
//				String onlinetime = queue.getOnlinetime();
//				String orgid = null;
//				if(queue.getOrgId()!=null) {
//					orgid = queue.getOrgId().toString();
//				}
//				String speid = null;
//				if(queue.getSpeId()!=null) {
//					speid = queue.getSpeId().toString();
//				}
//						
//				//价格变化后更改
//				if(price!=null&&!"".equals(price)&&!price.equals(resource.getPrice())) {
//					resource.setPrice(price);
//				}
//
//				//销量
//				if(salesnum!=null&&!"".equals(salesnum)&&!salesnum.equals(resource.getSalesnum())) {
//					resource.setSalesnum(salesnum);
//				}
//				
//				//上线时间
//				if(onlinetime!=null&&!"".equals(onlinetime)&&!onlinetime.equals(resource.getOnlinetime())) {
//					resource.setOnlinetime(onlinetime);
//				}
//				
//				//机构馆
//				String orgids = resource.getOrgid();
//				String[] orgArray = null;
//				if(orgids!=null&&!"".equals(orgids)) {
//					orgArray = orgids.split(" ");
//				}
//				
//				if(orgid!=null&&!"".equals(orgid)) {					
//					//0-添加 1-删除
//					if(queue.getOrgAction().equals("0")) {
//						orgids = "";
//						if(orgArray!=null) {
//							for (int i=0; i<orgArray.length; i++) { 
//								if(!orgid.equals(orgArray[i])) {
//									orgids = orgids + orgArray[i] + " ";
//								}
//					        }
//						}
//						orgids = orgids + orgid;
//					} else {
//						orgids = "";
//						if(orgArray!=null) {
//							for (int i=0; i<orgArray.length; i++) { 
//								if(!orgid.equals(orgArray[i])) {
//									orgids = orgids + orgArray[i];
//									if(i!=orgArray.length-2) {
//										orgids = orgids + " ";
//									}
//								}
//					        }
//						}	
//							
//					}
//					resource.setOrgid(orgids);
//				}
//				
//				//专题库
//				String speids = resource.getSpeid();
//				String[] speArray = null;
//				if(speids!=null&&!"".equals(speids)) {
//					speArray = speids.split(" ");
//				}
//				
//				if(speid!=null&&!"".equals(speid)) {					
//					//0-添加 1-删除
//					if(queue.getSpeAction().equals("0")) {
//						speids = "";
//						if(speArray!=null) {
//							for (int i=0; i<speArray.length; i++) { 
//								if(!speid.equals(speArray[i])) {
//									speids = speids + speArray[i] + " ";
//								}
//					        }
//						}
//						speids = speids + speid;
//
//					} else {
//						speids = "";
//						if(speArray!=null) {
//							for (int i=0; i<speArray.length; i++) { 
//								if(!speid.equals(speArray[i])) {
//									speids = speids + speArray[i];
//									if(i!=speArray.length-2) {
//										speids = speids + " ";
//									}
//								}
//					        }
//						}
//					}
//					resource.setSpeid(speids);
//				}
//				
//				
//			} else {
//				 //加载数据资源
//				resource = loadBookArticleResource(queue);
//			}
//			logger.debug(resource.getDescription());
//			
//			Set<SolrFile> solrFiles = queue.getSolrFiles();
//			for(SolrFile solrFile :solrFiles ) {
//				
//				try {
//					 //包括文件名称及格式
//					String path = solrFile.getPath();
//					if(!new File(path).exists()) {
//						continue;
//					}
//					 //文件所在目录
//					String sourcePath = new File(path).getParent();
//					 //文件名称，包括格式
//					String fileNameSource = new File(path).getName();
//					 //原文件名称 没有格式名称
//					String fileName = fileNameSource.substring(0,fileNameSource.lastIndexOf("."));
//					 //文件的类型
//					String fileType = fileNameSource.substring(fileNameSource.lastIndexOf(".")+1);
//					fileType = fileType.toLowerCase();
//					if(StringUtils.equals("pdf", fileType)) {
//						resource.setFiletype("pdf");
//					} else if(StringUtils.equals("doc", fileType)
//							||StringUtils.equals("docx", fileType)) {
//						resource.setFiletype("doc");
//					} else if(StringUtils.equals("txt", fileType)
//							||StringUtils.equals("xml", fileType) 
//							||StringUtils.equals("html", fileType)
//							||StringUtils.equals("htm", fileType)
//							||StringUtils.equals("iso", fileType)) {
//						resource.setFiletype("txt");
//					} else {
//						logger.debug("不支持该类型文件：" + fileType );
//						solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未创建，不支持该文件格式！");
//						continue;
//					}
//					logger.debug("文件类型：" + fileType );
//					
//					
//					File  file = null;
//					 //如果为pdf格式，则提取文本
//					if("pdf".equals(fileType)) {
//						file = new File(path);
//					}else if("xml".equals(fileType)) {
//						if(SolrUtil.xml2Text( path )) {
//							file = new File( sourcePath + File.separator + fileName + ".txt");
//						} else {
//							//更新失败状态和日志信息
//							solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未创建，xml过滤特殊字符后文件为空！");
//							continue;
//						}
//					} else {
//						file = new File( path );
//					}
//					  //文件的大小
//					Long fileLength = file.length();
//					  //如果文件的大小小于4个字节，就是两个汉字就没有的情况
//					  //则调过，不创建索引
//					if( fileLength < 4) {
//						//更新失败状态和日志信息
//						solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未创建，该文件为空！");
//						continue;
//					}
//					if(file != null) {
//						resource.setUuid(uuid);
//						logger.debug(solrFile.getId());
//						 //设置资源文件标识
//						resource.setResfileid(String.valueOf(solrFile.getId()));
//						 //创建索引
//						searchFacede.addIndex(resource, file);
//						 //清除临时文件 
//						clearTempFile( file, fileType );
//						String msg = "索引已创建，创建索引成功！";
//						msg.getBytes("UTF-8");
//						 //更新成功状态和日志信息
//						solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_SUCCESS, "索引已创建，创建索引成功！");
//						logger.debug("创建索引{"+ uuid +"}成功！");
//					} 
//				
//				}catch (Exception e) {
//					String errorMsg = e.getMessage();
//					if(errorMsg.length()>512) {
//						errorMsg = errorMsg.substring(0,500);
//					}
//					//更新失败状态和日志信息
//					solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未创建，创建索引异常！" + errorMsg);
//				} 
//			}
//			//更新资源对象状态为已创建
//			solrQueueFacede.createIndexSuccess( queue.getId() );
//			logger.debug("创建索引成功！");
//		} catch (Exception se) {
//			 //更新资源对象状态创建失败
//			solrQueueFacede.createIndexFailed( queue.getId() );
//			logger.error("创建索引失败！" , se);
//		}
//	}	
//	
//	/**
//	 * 创建文章索引
//	 * @param queue
//	 */
//	private void createArticleIndex(SolrQueue queue) {
//		try {
//			// 加载数据资源
//			Resource resource = loadArticleResource(queue);
//			//logger.debug(resource.getDescription());
//
//			String uuid = createUUID(queue);
//			resource.setUuid(uuid);
//
//			// 创建索引
//			searchFacede.addIndex(resource);
//
//			// 更新资源对象状态为已创建
//			solrQueueFacede.createIndexSuccess(queue.getId());
//			logger.debug("创建索引成功！");
//		} catch (Exception se) {
//			// 更新资源对象状态创建失败
//			solrQueueFacede.createIndexFailed(queue.getId());
//			logger.error("创建索引失败！", se);
//		}
//	}
//	
//	/**
//	 * 创建专题库索引
//	 * @param queue
//	 */
//	private void createSpecialIndex(SolrQueue queue) {
//		try {
//			getCreateIndexService().createSpecialIndex(queue);
//		} catch (Exception se) {
//			logger.error("创建索引失败！", se);
//		}
//	}
//	
//	/**
//	 * 删除图书索引
//	 * @param queue
//	 */
//	private void deleteBookIndex(SolrQueue queue) {
//		try {
//			Set<SolrFile> solrFiles = queue.getSolrFiles();
//			for(SolrFile solrFile :solrFiles ) {
//				
//				try {
//					String uuid = createUUID(queue);
//					 //删除索引
//					searchFacede.deleteIndex(uuid);
//					 //更新成功状态和日志信息
//					solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_SUCCESS, "索引已删除，删除索引成功！");
//					logger.debug("删除索引{"+ uuid +"}成功！");
//				}catch (Exception e) {
//					String errorMsg = e.getMessage();
//					if(errorMsg.length()>512) {
//						errorMsg = errorMsg.substring(0,500);
//					}
//					 //更新失败状态和日志信息
//					solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未删除，删除索引异常！" + errorMsg);
//				}
//			}
//			 //更新资源对象状态为已创建
//			solrQueueFacede.createIndexSuccess( queue.getId() );
//		} catch (Exception se) {
//			 //更新资源对象状态创建失败
//			solrQueueFacede.createIndexFailed( queue.getId() );
//			logger.error("删除索引失败！" , se);
//		}
//	}
//	
//	/**
//	 * 删除索引
//	 * @param queue
//	 */
//	private void deleteBookArticleIndex(SolrQueue queue) {
//		try {
//			Set<SolrFile> solrFiles = queue.getSolrFiles();
//			for(SolrFile solrFile :solrFiles ) {
//				
//				try {
//					String uuid = createUUID(queue);
//					 //删除索引
//					searchFacede.deleteIndex(uuid);
//					 //更新成功状态和日志信息
//					solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_SUCCESS, "索引已删除，删除索引成功！");
//					logger.debug("删除索引{"+ uuid +"}成功！");
//				}catch (Exception e) {
//					String errorMsg = e.getMessage();
//					if(errorMsg.length()>512) {
//						errorMsg = errorMsg.substring(0,500);
//					}
//					 //更新失败状态和日志信息
//					solrQueueFacede.updateSolrFileStatusMsg(solrFile.getId(), FILE_CREATE_FAIL, "索引未删除，删除索引异常！" + errorMsg);
//				}
//			}
//			 //更新资源对象状态为已创建
//			solrQueueFacede.createIndexSuccess( queue.getId() );
//		} catch (Exception se) {
//			 //更新资源对象状态创建失败
//			solrQueueFacede.createIndexFailed( queue.getId() );
//			logger.error("删除索引失败！" , se);
//		}
//	}
//	
//	private void deleteArticleIndex(SolrQueue queue) {
//		try {	
//			String uuid = createUUID(queue);
//			 //删除索引
//			searchFacede.deleteIndex(uuid);
//			logger.debug("删除索引{"+ uuid +"}成功！");
//			 //更新资源对象状态为已创建
//			solrQueueFacede.createIndexSuccess( queue.getId() );
//		} catch (Exception se) {
//			 //更新资源对象状态创建失败
//			solrQueueFacede.createIndexFailed( queue.getId() );
//			logger.error("删除索引失败！" , se);
//		}
//	}
//	
//	private void deleteSpecialIndex(SolrQueue queue) {
//		try {	
//			String uuid = createUUID(queue);
//			 //删除索引
//			searchFacede.deleteIndex(uuid);
//			logger.debug("删除索引{"+ uuid +"}成功！");
//			 //更新资源对象状态为已创建
//			solrQueueFacede.createIndexSuccess( queue.getId() );
//		} catch (Exception se) {
//			 //更新资源对象状态创建失败
//			solrQueueFacede.createIndexFailed( queue.getId() );
//			logger.error("删除索引失败！" , se);
//		}
//	}
//	
//	private String createUUID(SolrQueue queue) {
//		String uuid = String.valueOf(queue.getResType());
//		uuid = uuid + "_" + String.valueOf(queue.getProdId()); 
//		
////		if(StringUtils.isNotEmpty(String.valueOf(queue.getOrgId()))
////				&&!"null".equals(String.valueOf(queue.getOrgId()))) {
////			uuid = uuid + "_" + queue.getOrgId(); 
////		}
////		
////		if(StringUtils.isNotEmpty(String.valueOf(queue.getSpeId()))
////				&&!"null".equals(String.valueOf(queue.getSpeId()))) {
////			uuid = uuid + "_" + queue.getSpeId(); 
////		}
//		
//		logger.debug("uuid:" + uuid);
//		return uuid;
//	}
//	
//	private void supplyBookPrices(Resource resource) throws Exception  {
//		String pid = resource.getProdid();
//		if(StringUtils.isBlank(pid)) {
//			return; 
//		}		
//		Long bookId = Long.parseLong(pid);		
//		@SuppressWarnings("rawtypes")
//		Map bookinfo = new HashMap();
//		bookinfo = bookService.getBookPrices(bookId);
//		if(bookinfo.get("price")!=null&&!"".equals(bookinfo.get("price"))) resource.setPrice(bookinfo.get("price").toString());
//		if(bookinfo.get("stprice")!=null&&!"".equals(bookinfo.get("stprice"))) resource.setPbooks_price(bookinfo.get("stprice").toString());
//		if(bookinfo.get("liprice")!=null&&!"".equals(bookinfo.get("liprice"))) resource.setListprice(bookinfo.get("liprice").toString());
//		if(bookinfo.get("salesnum")!=null&&!"".equals(bookinfo.get("salesnum"))) {
//			resource.setSalesnum(bookinfo.get("salesnum").toString());
//		} else {
//			resource.setSalesnum("0");
//		}		
//		if(bookinfo.get("stsalesnum")!=null&&!"".equals(bookinfo.get("stsalesnum"))) {
//			resource.setStsalesnum(bookinfo.get("stsalesnum").toString());
//		} else {
//			resource.setStsalesnum("0");
//		}
//		if(bookinfo.get("thumb")!=null&&!"".equals(bookinfo.get("thumb"))) resource.setGenre(bookinfo.get("thumb").toString());
//		if(bookinfo.get("onlinetime")!=null&&!"".equals(bookinfo.get("onlinetime"))) resource.setOnlinetime(bookinfo.get("onlinetime").toString());
//	}
//	
//	/**
//	 * 封装资源参数
//	 * @param queue
//	 * @return
//	 */
//	private Resource loadBookResource(SolrQueue queue) throws Exception  {
//		
//		Resource resource = new Resource();
//		
//		Long bookId = queue.getProdId();
//		@SuppressWarnings("rawtypes")
//		Map bookinfo = new HashMap();
//		
//		Book basicBookinfo = new Book();
//		basicBookinfo = bookService.getBookinfo(bookId);
//		String attribute = basicBookinfo.getAttribute().toString();
//		
//		bookinfo = bookService.getBookWork(bookId);
//		
//		if(bookinfo==null) {
//			return null;
//		}
//		
//		if(String.valueOf(queue.getProdId())==null) {
//			return null;
//		}
//		
//		String orgIds = bookService.getOrgIds(bookId);
//		String speIds = bookService.getSpeIds(bookId);
//		
//		 //设置资源类型(原始资源或基础资源)
//		resource.setRestype(String.valueOf(queue.getResType()));
//		 //设置产品标识
//		resource.setProdid(String.valueOf(queue.getProdId()));
//					
//		 //设置正题名
//		if( bookinfo.get("title")!=null&&!"".equals(bookinfo.get("title")) ) resource.setTitle(bookinfo.get("title").toString());
//		
//		 //设置出日期
//		if(bookinfo.get("publishdate")!=null&&!"".equals(bookinfo.get("publishdate"))) resource.setPublishdate(bookinfo.get("publishdate").toString());
//		if(bookinfo.get("pubdate")!=null&&!"".equals(bookinfo.get("pubdate"))) resource.setPubdate(bookinfo.get("pubdate").toString());
//		//设置图书资源属性
//		resource.setAttribute(attribute);
//		 //设置作者 
//		if(bookinfo.get("authorname")!=null&&!"".equals(bookinfo.get("authorname"))) resource.setAuthorname(bookinfo.get("authorname").toString());
//		 //设置摘要
//		if(bookinfo.get("description")!=null&&!"".equals(bookinfo.get("description"))) resource.setDescription(bookinfo.get("description").toString());
//		 //设置关键词
//		if(bookinfo.get("tags")!=null&&!"".equals(bookinfo.get("tags"))) resource.setTags(bookinfo.get("tags").toString());
//		 //设置出版社  
//		if(bookinfo.get("pressname")!=null&&!"".equals(bookinfo.get("pressname"))) resource.setPressname(bookinfo.get("pressname").toString());
//		 //设置主题词
//		if( bookinfo.get("title")!=null&&!"".equals(bookinfo.get("title")) ) resource.setSubject(bookinfo.get("title").toString());
//		 //设置isbn
//		if(bookinfo.get("isbn")!=null&&!"".equals(bookinfo.get("isbn"))) resource.setIsbn(bookinfo.get("isbn").toString());
//
//		 //设置专题库标识
//		if(speIds!=null&&!"".equals(speIds)) resource.setSpeid(speIds);
//		 //设置机构标识
//		if(orgIds!=null&&!"".equals(orgIds)) resource.setOrgid(orgIds);
//		 //电子书价格
////		if("6".equals(bookinfo.get("status").toString())) {
//			if(bookinfo.get("price")!=null&&!"".equals(bookinfo.get("price"))) resource.setPrice(bookinfo.get("price").toString());
////		} else {
//			if(bookinfo.get("stprice")!=null&&!"".equals(bookinfo.get("stprice"))) resource.setPbooks_price(bookinfo.get("stprice").toString());
////		}
//			if(bookinfo.get("liprice")!=null&&!"".equals(bookinfo.get("liprice"))) resource.setListprice(bookinfo.get("liprice").toString());
//		//上架时间t
//		if(bookinfo.get("onlinetime")!=null&&!"".equals(bookinfo.get("onlinetime"))) resource.setOnlinetime(bookinfo.get("onlinetime").toString());
//		//销售量
//		if(bookinfo.get("salesnum")!=null&&!"".equals(bookinfo.get("salesnum"))) {
//			resource.setSalesnum(bookinfo.get("salesnum").toString());
//		} else {
//			resource.setSalesnum("0");
//		}
//		//销售量
//		if(bookinfo.get("stsalesnum")!=null&&!"".equals(bookinfo.get("stsalesnum"))) {
//			resource.setStsalesnum(bookinfo.get("stsalesnum").toString());
//		} else {
//			resource.setStsalesnum("0");
//		}
//		//分类标识
//		if(bookinfo.get("typeid")!=null&&!"".equals(bookinfo.get("typeid"))) resource.setTypeid(bookinfo.get("typeid").toString());
//		//分类名称
//		if(bookinfo.get("typename")!=null&&!"".equals(bookinfo.get("typename"))) resource.setTypename(bookinfo.get("typename").toString());
//		
//		if(bookinfo.get("thumb")!=null&&!"".equals(bookinfo.get("thumb"))) resource.setGenre(bookinfo.get("thumb").toString());
//		
//		if(bookinfo.get("status")!=null&&!"".equals(bookinfo.get("status"))) resource.setStatus(bookinfo.get("status").toString());
//		
//		if(bookinfo.get("ststatus")!=null&&!"".equals(bookinfo.get("ststatus"))) resource.setStstatus(bookinfo.get("ststatus").toString());
//		
//		 //创建时间
//		resource.setDate(new Date());
//		return resource;
//	}
//	
//	/**
//	 * 封装专题库文章资源参数
//	 * @param queue
//	 * @return
//	 */
//	private Resource loadBookArticleResource(SolrQueue queue) throws Exception  {
//		
//		Resource resource = new Resource();
//		
//			
//		Long bookId = queue.getProdId();
//		@SuppressWarnings("rawtypes")
//		Map bookinfo = new HashMap();
//		
//		Book basicBookinfo = new Book();
//		basicBookinfo = bookArticleService.getBookinfo(bookId);
//		String attribute = basicBookinfo.getAttribute().toString();
//		
//		bookinfo = bookArticleService.getBookArticle(bookId);
//		
//		if(bookinfo==null) {
//			return null;
//		}
//		
//		if(String.valueOf(queue.getProdId())==null) {
//			return null;
//		}
//		
//		String orgIds = bookArticleService.getOrgIds(bookId);
//		String speIds = bookArticleService.getSpeIds(bookId);
//		
//		//设置资源类型(原始资源或基础资源)
//		resource.setRestype(String.valueOf(queue.getResType()));
//		 //设置产品标识
//		resource.setProdid(String.valueOf(queue.getProdId()));
//					
//		 //设置正题名
//		if( bookinfo.get("title")!=null&&!"".equals(bookinfo.get("title")) ) resource.setTitle(bookinfo.get("title").toString());
//		
//		 //设置出日期
//		if(bookinfo.get("publishdate")!=null&&!"".equals(bookinfo.get("publishdate"))) resource.setPublishdate(bookinfo.get("publishdate").toString());
//		if(bookinfo.get("pubdate")!=null&&!"".equals(bookinfo.get("pubdate"))) resource.setPubdate(bookinfo.get("pubdate").toString());
//		//设置图书资源属性
//		resource.setAttribute(attribute);
//		 //设置作者 
//		if(bookinfo.get("authorname")!=null&&!"".equals(bookinfo.get("authorname"))) resource.setAuthorname(bookinfo.get("authorname").toString());
//		 //设置摘要
//		if(bookinfo.get("description")!=null&&!"".equals(bookinfo.get("description"))) resource.setDescription(bookinfo.get("description").toString());
//		 //设置出版社  
//		if(bookinfo.get("pressname")!=null&&!"".equals(bookinfo.get("pressname"))) resource.setPressname(bookinfo.get("pressname").toString());
//		 //设置主题词
//		if( bookinfo.get("subject")!=null&&!"".equals(bookinfo.get("subject")) ) resource.setSubject(bookinfo.get("subject").toString());
//		 //设置isbn
//		if(bookinfo.get("isbn")!=null&&!"".equals(bookinfo.get("isbn"))) resource.setIsbn(bookinfo.get("isbn").toString());
//			
//		//设置专题库标识
//		if(speIds!=null&&!"".equals(speIds)) resource.setSpeid(speIds);
//		 //设置机构标识
//		if(orgIds!=null&&!"".equals(orgIds)) resource.setOrgid(orgIds);
//		 //价格
//		if(bookinfo.get("price")!=null&&!"".equals(bookinfo.get("price"))) resource.setPrice(bookinfo.get("price").toString());
//		 //上架时间
//		if(bookinfo.get("onlinetime")!=null&&!"".equals(bookinfo.get("onlinetime"))) resource.setOnlinetime(bookinfo.get("onlinetime").toString());
//		  //销售量
//		if(bookinfo.get("salesnum")!=null&&!"".equals(bookinfo.get("salesnum"))) {
//			resource.setSalesnum(bookinfo.get("salesnum").toString());
//		} else {
//			resource.setSalesnum("0");
//		}
//		//分类标识
//		if(bookinfo.get("typeid")!=null&&!"".equals(bookinfo.get("typeid"))) resource.setTypeid(bookinfo.get("typeid").toString());
//		//分类名称
//		if(bookinfo.get("typename")!=null&&!"".equals(bookinfo.get("typename"))) resource.setTypename(bookinfo.get("typename").toString());
//
//		//专题库文章缩略图
//		if(bookinfo.get("genre")!=null&&!"".equals(bookinfo.get("genre"))) resource.setGenre(bookinfo.get("genre").toString());
//		
//		//专题 库文章发表年代
//		if(bookinfo.get("era")!=null&&!"".equals(bookinfo.get("era"))) resource.setEra(bookinfo.get("era").toString());
//		 //创建时间
//		resource.setDate(new Date());
//		
//		return resource;
//	}
//	
//	private Resource loadArticleResource(SolrQueue queue) throws Exception {
//
//		Resource resource = new Resource();
//
//		Long contentId = queue.getProdId();
//		@SuppressWarnings("rawtypes")
//		Map articleinfo = new HashMap();
//		articleinfo = articleService.getArticleinfo(contentId);
//
//		// 设置标题
//		if(articleinfo.get("title")!=null&&!"".equals(articleinfo.get("title"))) resource.setTitle(articleinfo.get("title").toString());
////
////		// 设置作者
////		resource.setAuthor(articleinfo.get("authorname").toString());
////		// 设置摘要
//		if(articleinfo.get("description")!=null&&!"".equals(articleinfo.get("description"))) resource.setDescription(articleinfo.get("description").toString());
//
//		// 设置产品标识
//		resource.setProdid(String.valueOf(queue.getProdId()));
//		
//		//设置资源类型
//		resource.setRestype(String.valueOf(queue.getResType()));
//		
//		//设置文章内容
//		if(articleinfo.get("content")!=null&&!"".equals(articleinfo.get("content"))) resource.setFiles(SolrUtil.Html2Text(articleinfo.get("content").toString()));
//		
//		//设置发布时间
//		if(articleinfo.get("onlinetime")!=null&&!"".equals(articleinfo.get("onlinetime"))) resource.setOnlinetime(articleinfo.get("onlinetime").toString());
//		
//		//分类标识
//		if(articleinfo.get("typeid")!=null&&!"".equals(articleinfo.get("typeid"))) resource.setTypeid(articleinfo.get("typeid").toString());
//		
//		//路径
//		if(articleinfo.get("url")!=null&&!"".equals(articleinfo.get("url"))) resource.setGenre(articleinfo.get("url").toString());
//		
//		//缩略图
//		if(articleinfo.get("thumb")!=null&&!"".equals(articleinfo.get("thumb"))) resource.setEra(articleinfo.get("thumb").toString());
//		
//		// 创建时间
//		resource.setDate(new Date());
//
//		return resource;
//	}
//	
//	private Resource loadSpecialResource(SolrQueue queue) throws Exception {
//
//		Resource resource = new Resource();
//
//		Long specialId = queue.getProdId();
//		@SuppressWarnings("rawtypes")
//		Map specialinfo = new HashMap();
//		specialinfo = specialService.getSpecialinfo(specialId);
//
//		//设置资源类型
//		resource.setRestype(String.valueOf(queue.getResType()));
//		
//		// 设置产品标识
//		resource.setProdid(String.valueOf(queue.getProdId()));
//		
//		// 设置标题
//		if(specialinfo.get("title")!=null&&!"".equals(specialinfo.get("title"))) resource.setTitle(specialinfo.get("title").toString());
//
//		// 设置摘要
//		if(specialinfo.get("description")!=null&&!"".equals(specialinfo.get("description"))) resource.setDescription(specialinfo.get("description").toString());
//
//		//设置发布时间
//		if(specialinfo.get("onlinetime")!=null&&!"".equals(specialinfo.get("onlinetime"))) resource.setOnlinetime(specialinfo.get("onlinetime").toString());
//		
//		//分类标识
//		if(specialinfo.get("typeid")!=null&&!"".equals(specialinfo.get("typeid"))) resource.setTypeid(specialinfo.get("typeid").toString());
//		
//		//分类名称
//		if(specialinfo.get("typename")!=null&&!"".equals(specialinfo.get("typename"))) resource.setTypename(specialinfo.get("typename").toString());
//		
//		//年限
//		if(specialinfo.get("era")!=null&&!"".equals(specialinfo.get("era"))) resource.setEra(specialinfo.get("era").toString());
//		
//		//标签
//		if(specialinfo.get("tags")!=null&&!"".equals(specialinfo.get("tags"))) resource.setTags(specialinfo.get("tags").toString());
//		
//		//采选类型
//		if(specialinfo.get("selectType")!=null&&!"".equals(specialinfo.get("selectType"))) resource.setSelectType(Long.parseLong(specialinfo.get("selectType").toString()));
//		
//		//采选类型
//		if(specialinfo.get("orgid")!=null&&!"".equals(specialinfo.get("orgid"))) resource.setOrgid(specialinfo.get("orgid").toString());
//		
//		// 创建时间
//		resource.setDate(new Date());
//
//		return resource;
//	}
//	
//	/**
//	 * 清理临时文件 
//	 * @param sourceFile 生成的临时文本文件
//	 * @param sourceType 该临时文本原始文件类型
//	 */
//	private void clearTempFile(File  tempFile, String sourceType) {
//		
//		  //因为只有xml生成了临时文件，所以只删除这两种格式的文件类型
//		if(StringUtils.equals("xml", sourceType)) {
//			
//			tempFile.delete();
//		}
//	}
//
//	private void loadSolrQueueFacede() {
//		this.solrQueueFacede = (ISolrQueueFacede) BeanFactoryUtil.getBean("solrQueueFacede");
//	}
//	
//	private void loadSearchFacede() {
//		this.searchFacede = (ISearchFacede) BeanFactoryUtil.getBean("searchFacede");
//	}
//	
//	private void loadBookService() {
//		this.bookService = (IBookService) BeanFactoryUtil.getBean("bookService");
//	}
//	
//	private void loadBookArticleService() {
//		this.bookArticleService = (IBookArticleService) BeanFactoryUtil.getBean("bookArticleService");
//	}
//	
//	private void loadArticleService() {
//		this.articleService = (IArticleService) BeanFactoryUtil.getBean("articleService");
//	}
//	
//	private void loadSpecialService() {
//		this.specialService = (ISpecialService) BeanFactoryUtil.getBean("specialService");
//	}
//	
//	private ISearchFacede getSearchFacede() {
//    	return (ISearchFacede)BeanFactoryUtil.getBean("searchFacede");
//    }
//	
//	public ICreateIndexFacede getCreateIndexService() {
//		return (ICreateIndexFacede)BeanFactoryUtil.getBean("createIndexService");
//	}
//}
