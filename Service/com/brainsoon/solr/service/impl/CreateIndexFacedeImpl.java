package com.brainsoon.solr.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.brainsoon.solr.po.Resource;
import com.brainsoon.solr.po.SolrFile;
import com.brainsoon.solr.po.SolrQueue;
import com.brainsoon.solr.service.ICreateIndexFacede;
import com.brainsoon.solr.service.ISearchFacede;
import com.brainsoon.solr.service.ISolrQueueFacede;
import com.brainsoon.solr.util.PageSupport;
import com.brainsoon.solr.util.SearchDocument;
import com.brainsoon.solr.util.SolrUtil;
import com.brainsoon.solrservice.res.po.Book;
import com.brainsoon.solrservice.res.service.IBookService;
import com.brainsoon.solrservice.res.service.ISpecialService;
import com.channelsoft.appframe.exception.ServiceException;
import com.channelsoft.appframe.service.impl.BaseServiceObject;
import com.channelsoft.appframe.utils.BeanFactoryUtil;

public class CreateIndexFacedeImpl extends BaseServiceObject implements ICreateIndexFacede {
	private ISearchFacede searchFacede;
	
	private IBookService bookService;
	
	private ISolrQueueFacede solrQueueFacede;
	
	public void createSpecialIndex(SolrQueue queue) {//
		try {
			long selectType = queue.getSelectType();
			Resource resource = new Resource();
			if(selectType == 1) {
				// 加载数据资源
				resource = loadSpecialResource(queue);
				if(resource == null) {
					getSolrQueueFacede().createIndexFailed(queue.getId());
					logger.error("创建索引失败！专题库不存在");
					return;
				}
			} else if(selectType == 2) {
				resource = loadCollectResource(queue);
				if(resource == null) {
					getSolrQueueFacede().createIndexFailed(queue.getId());
					logger.error("创建索引失败！采选单不存在");
					return;
				}
			}
			String uuid = createSpecialUUID(queue);
			resource.setUuid(uuid);

			// 创建索引
			createSpecialIndex(resource);

			// 更新资源对象状态为已创建
			getSolrQueueFacede().createIndexSuccess(queue.getId());
			logger.debug("创建索引成功！");
		} catch (Exception se) {
			// 更新资源对象状态创建失败
			getSolrQueueFacede().createIndexFailed(queue.getId());
			logger.error("创建索引失败！", se);
		}
	}
	
	public void createSpecialIndex(Resource resource) throws Exception {
		String prodid = resource.getProdid();
		if(prodid == null) {
			logger.error("专题库或采选单id不可为空！");
			return;
		}
		long selectType = resource.getSelectType();
		
		if(selectType == 1) {
			createSpecialBookIndex(resource);
			getSearchFacede().addSpecialIndex(resource);
		} else {
			createCollectBookIndex(resource);
		}
	}	
	
	public void createSpecialBookIndex(SolrQueue queue) {
		
		try {
			Resource resource = null;
			String uuid = createBookUUID(queue);
			//查看索引中是已经创建索引
			SearchDocument doc = new SearchDocument();
			doc.setStart(0);
			doc.setRows(1);
			doc.setSortWay("salesnum");
			PageSupport pages = getSearchFacede().searchOneDocument(uuid, doc);
			List items = pages.getItems();
			if(items.size()>0) {
				resource = (Resource)items.get(0);
				String price = queue.getPrice();
				String salesnum = queue.getSalesnum();
				String stsalesnum = queue.getStsalesnum();
				String onlinetime = queue.getOnlinetime();
				String orgid = null;
				if(queue.getOrgId()!=null) {
					orgid = queue.getOrgId().toString();
				}
				String speid = null;
				if(queue.getSpeId()!=null) {
					speid = queue.getSpeId().toString();
				}
						
				//价格变化后更改
				if(price!=null&&!"".equals(price)&&!price.equals(resource.getPrice())) {
					resource.setPrice(price);
				}

/*				//销量
				if(salesnum!=null&&!"".equals(salesnum)&&!salesnum.equals(resource.getSalesnum())) {
					resource.setSalesnum(salesnum);
				}
				
				//实体书销量
				if(stsalesnum!=null&&!"".equals(stsalesnum)&&!stsalesnum.equals(resource.getStsalesnum())) {
					resource.setStsalesnum(stsalesnum);
				}*/
				
				//上线时间
				if(onlinetime!=null&&!"".equals(onlinetime)&&!onlinetime.equals(resource.getOnlinetime())) {
					resource.setOnlinetime(onlinetime);
				}
				
				supplyBookPrices(resource);
				
				//机构馆
				String orgids = resource.getOrgid();
				String[] orgArray = null;
				if(orgids!=null&&!"".equals(orgids)) {
					orgArray = orgids.split(" ");
				}
				
				if(orgid!=null&&!"".equals(orgid)) {					
					//0-添加 1-删除
					if(queue.getOrgAction().equals("0")) {
						orgids = "";
						if(orgArray!=null) {
							for (int i=0; i<orgArray.length; i++) { 
								if(!orgid.equals(orgArray[i])) {
									orgids = orgids + orgArray[i] + " ";
								}
					        }
						}
						orgids = orgids + orgid;
					} else {
						orgids = "";
						if(orgArray!=null) {
							for (int i=0; i<orgArray.length; i++) { 
								if(!orgid.equals(orgArray[i])) {
									orgids = orgids + orgArray[i];
									if(i!=orgArray.length-2) {
										orgids = orgids + " ";
									}
								}
					        }
						}
					}
					resource.setOrgid(orgids);
				}
				
				//专题库
				String speids = resource.getSpeid();
				String[] speArray = null;
				if(speids!=null&&!"".equals(speids)) {
					speArray = speids.split(" ");
				}
				
				if(speid!=null&&!"".equals(speid)) {					
					//0-添加 1-删除
					if(queue.getSpeAction().equals("0")) {
						speids = "";
						if(speArray!=null) {
							for (int i=0; i<speArray.length; i++) { 
								if(!speid.equals(speArray[i])) {
									speids = speids + speArray[i] + " ";
								}
					        }
						}
						speids = speids + speid;

					} else {
						speids = "";
						if(speArray!=null) {
							for (int i=0; i<speArray.length; i++) { 
								if(!speid.equals(speArray[i])) {
									speids = speids + speArray[i];
									if(i!=speArray.length-2) {
										speids = speids + " ";
									}
								}
					        }
						}
					}
					resource.setSpeid(speids);
				}
				
				
			} else {
				 //加载数据资源
				resource = loadBookResource(queue);
			}
			
			Set<SolrFile> solrFiles = queue.getSolrFiles();
			logger.debug(solrFiles.size());
			if(solrFiles.size()>0) {
				for(SolrFile solrFile :solrFiles ) {
					
					try {
						 //包括文件名称及格式
						String path = solrFile.getPath();
						if(!new File(path).exists()) {
							continue;
						}
						 //文件所在目录
						String sourcePath = new File(path).getParent();
						 //文件名称，包括格式
						String fileNameSource = new File(path).getName();
						 //原文件名称 没有格式名称
						String fileName = fileNameSource.substring(0,fileNameSource.lastIndexOf("."));
						 //文件的类型
						String fileType = fileNameSource.substring(fileNameSource.lastIndexOf(".")+1);
						fileType = fileType.toLowerCase();
						/*if(StringUtils.equals("pdf", fileType)) {
							resource.setFiletype("pdf");
						} else if(StringUtils.equals("doc", fileType)
								||StringUtils.equals("docx", fileType)) {
							resource.setFiletype("doc");
						} else if(StringUtils.equals("txt", fileType)
								||StringUtils.equals("xml", fileType) 
								||StringUtils.equals("html", fileType)
								||StringUtils.equals("htm", fileType)
								||StringUtils.equals("iso", fileType)) {
							resource.setFiletype("txt");
						} else {
							logger.debug("不支持该类型文件：" + fileType + "！索引未创建，");
							continue;
						}*/
						logger.debug("文件类型：" + fileType );					
						
						File  file = null;
						 //如果为pdf格式，则提取文本
						if("pdf".equals(fileType)) {
							file = new File(path);
						}else if("xml".equals(fileType)) {
							if(SolrUtil.xml2Text( path )) {
								file = new File( sourcePath + File.separator + fileName + ".txt");
							} else {
								logger.debug("索引未创建，xml过滤特殊字符后文件为空！索引未创建，");
								continue;
							}
						} else {
							file = new File( path );
						}
						  //文件的大小
						Long fileLength = file.length();
						  //如果文件的大小小于4个字节，就是两个汉字就没有的情况
						  //则调过，不创建索引
						if( fileLength < 4) {
							//更新失败状态和日志信息
							logger.debug("索引未创建，该文件为空！索引未创建，");
							continue;
						}
						if(file != null) {
							resource.setUuid(uuid);
							logger.debug(solrFile.getId());
							 //设置资源文件标识
							//resource.setResfileid(String.valueOf(solrFile.getId()));
							 //创建索引
							try {
								getSearchFacede().addSpecialBookIndex(resource, file);
								 //清除临时文件 
								clearTempFile( file, fileType );
								String msg = "索引已创建，创建索引成功！";
								msg.getBytes("UTF-8");
								 //更新成功状态和日志信息								
								logger.debug("创建索引{"+ uuid +"}成功！");
							}catch (Exception e) {
								String errorMsg = e.getMessage();
								logger.debug(errorMsg);
							}
						} 
					
					}catch (Exception e) {
						String errorMsg = e.getMessage();
						logger.debug("索引未创建，创建索引异常！" + errorMsg);
					} 
				}
			} else {
				try {
					resource.setUuid(uuid);
					getSearchFacede().addSpecialBookIndex(resource);
					String msg = "索引已创建，创建索引成功！";
					msg.getBytes("UTF-8");
					logger.debug("创建索引{"+ uuid +"}成功！");
				}catch (Exception e) {
					String errorMsg = e.getMessage();
					logger.debug(errorMsg);
				}
			}
			logger.debug("创建索引成功！");
		} catch (Exception se) {
			logger.error("创建索引失败！" , se);
		}
	}
	
	public void createSpecialBookIndex(Resource resource) throws Exception {
		String prodid = resource.getProdid();
		String[] prodids = prodid.split(",");
		String orgid = resource.getOrgid();
		logger.debug(prodid);
		for(String spid : prodids) {
			List sBookInfos = getSpecialService().getSpecialBooksIds(Long.parseLong(spid));			
			Iterator itr = sBookInfos.iterator();
			while (itr.hasNext()) {
				Map bookInfo = (Map) itr.next();
				String bookid = bookInfo.get("bookid").toString();				
				Object priceO = bookInfo.get("price");
				String price = "0";
				if(priceO != null) {
					price = priceO.toString();
				}
				String onlinetime = bookInfo.get("onlinetime").toString();
				try {				
					//查出表中相关数据				
					Map bookinfo = new HashMap();
					bookinfo = getBookService().getBookWork(Long.valueOf(bookid));
					if(bookinfo == null) {
						throw new ServiceException("【"+ bookid +"】对应的图书资源不存在！");
					}
					Long bookId = Long.valueOf(bookinfo.get("bookid").toString());
					SolrQueue solrQueue = getSolrQueueFacede().doBookIndexNoSave(bookId, Long.parseLong(orgid), null, Long.parseLong(spid), null, onlinetime, price, null, null, bookinfo, "0");
					createSpecialBookIndex(solrQueue);
				} catch (Exception ex) {
					logger.error("专题库【" + spid + "】，选配图书索引创建失败，图书ID【" + bookid + "】");
				}
			}
		}
	}
	
	public void createCollectBookIndex(Resource resource) throws Exception {
		String prodid = resource.getProdid();
		String orgid = resource.getOrgid();
		String spid = "0"; 
		List sBookInfos = getSpecialService().getCollectBooksIds(Long.parseLong(prodid));
		Iterator itr = sBookInfos.iterator();
		while (itr.hasNext()) {
			Map bookInfo = (Map) itr.next();
			String bookid = bookInfo.get("bookid").toString();				
			Object priceO = bookInfo.get("price");
			String price = "0";
			if(priceO != null) {
				price = priceO.toString();
			}
			String onlinetime = bookInfo.get("onlinetime").toString();
			try {				
				//查出表中相关数据				
				Map bookinfo = new HashMap();
				bookinfo = getBookService().getBookWork(Long.valueOf(bookid));
				if(bookinfo == null) {
					throw new ServiceException("【"+ bookid +"】对应的图书资源不存在！");
				}
				Long bookId = Long.valueOf(bookinfo.get("bookid").toString());
				SolrQueue solrQueue = getSolrQueueFacede().doBookIndexNoSave(bookId, Long.parseLong(orgid), null, Long.parseLong(spid), null, onlinetime, price, null, null, bookinfo, "0");
				createSpecialBookIndex(solrQueue);
			} catch (Exception ex) {
				logger.error("专题库【" + spid + "】，选配图书索引创建失败，图书ID【" + bookid + "】");
			}
		}
	}
	
	private Resource loadSpecialResource(SolrQueue queue) throws Exception {

		Resource resource = new Resource();

		Long specialId = queue.getProdId();
		@SuppressWarnings("rawtypes")
		Map specialinfo = new HashMap();
		specialinfo = getSpecialService().getSpecialinfo(specialId);

		if(specialinfo == null)
			return null;
		//设置资源类型		
		resource.setRestype(String.valueOf(queue.getResType()));
		
		// 设置产品标识
		resource.setProdid(String.valueOf(queue.getProdId()));
		
		Long orgid = 0l;
		if(queue.getOrgId() != null)
			orgid = queue.getOrgId();
		resource.setOrgid(String.valueOf(orgid));
		
		resource.setSelectType(queue.getSelectType());
		
		// 设置标题
		if(specialinfo.get("title")!=null&&!"".equals(specialinfo.get("title"))) resource.setTitle(specialinfo.get("title").toString());

		// 设置摘要
		if(specialinfo.get("description")!=null&&!"".equals(specialinfo.get("description"))) resource.setDescription(specialinfo.get("description").toString());

		//设置发布时间
		if(specialinfo.get("onlinetime")!=null&&!"".equals(specialinfo.get("onlinetime"))) resource.setOnlinetime(specialinfo.get("onlinetime").toString());
		
		//分类标识
		if(specialinfo.get("typeid")!=null&&!"".equals(specialinfo.get("typeid"))) resource.setTypeid(specialinfo.get("typeid").toString());
		
		//分类名称
		//if(specialinfo.get("typename")!=null&&!"".equals(specialinfo.get("typename"))) resource.setTypename(specialinfo.get("typename").toString());
		
		//年限
		//if(specialinfo.get("era")!=null&&!"".equals(specialinfo.get("era"))) resource.setEra(specialinfo.get("era").toString());
		
		//标签
		//if(specialinfo.get("tags")!=null&&!"".equals(specialinfo.get("tags"))) resource.setTags(specialinfo.get("tags").toString());
		
		// 创建时间
		//resource.setDate(new Date());

		//缩略图
		//if(specialinfo.get("thumb")!=null&&!"".equals(specialinfo.get("thumb"))) resource.setGenre(specialinfo.get("thumb").toString());
		
		return resource;
	}
	
	private Resource loadCollectResource(SolrQueue queue) throws Exception {
		Resource resource = new Resource();
		
		Long coid = queue.getProdId();
		
		@SuppressWarnings("rawtypes")
		Map specialinfo = new HashMap();
		specialinfo = getSpecialService().getCollectInfo(coid);
		
		if(specialinfo == null)
			return null;
		//设置资源类型
		resource.setRestype(String.valueOf(queue.getResType()));
		
		// 设置产品标识
		resource.setProdid(String.valueOf(queue.getProdId()));
		
		Long orgid = 0l;
		if(queue.getOrgId() != null)
			orgid = queue.getOrgId();
		resource.setOrgid(String.valueOf(orgid));
		
		resource.setSelectType(queue.getSelectType());
		
		//resource.setDate(new Date());
		
		return resource;
	}
	
	private Resource loadBookResource(SolrQueue queue) throws Exception  {
		
		Resource resource = new Resource();
		
		Long bookId = queue.getProdId();
		@SuppressWarnings("rawtypes")
		Map bookinfo = new HashMap();
		
		Book basicBookinfo = new Book();
		basicBookinfo = getBookService().getBookinfo(bookId);
		String attribute = basicBookinfo.getAttribute().toString();
		
		bookinfo = getBookService().getBookWork(bookId);
		
		if(bookinfo==null) {
			return null;
		}
		
		if(String.valueOf(queue.getProdId())==null) {
			return null;
		}
		String orgIds = null;
		if(queue.getOrgId() != null) {
			orgIds = queue.getOrgId() + "";
		}
		String speIds = null;
		if(queue.getSpeId() != null) {
			speIds = queue.getSpeId() + "";
		}
		 //设置资源类型(原始资源或基础资源)
		resource.setRestype(String.valueOf(queue.getResType()));
		 //设置产品标识
		resource.setProdid(String.valueOf(queue.getProdId()));
					
		 //设置正题名
		String title = (String)bookinfo.get("title");
		if(title==null) {
			title = "";
		}
		resource.setTitle(title);
		 //设置出日期
		//if(bookinfo.get("publishdate")!=null&&!"".equals(bookinfo.get("publishdate"))) resource.setPublishdate(bookinfo.get("publishdate").toString());
		//if(bookinfo.get("pubdate")!=null&&!"".equals(bookinfo.get("pubdate"))) resource.setPubdate(bookinfo.get("pubdate").toString());
		//设置图书资源属性
		//resource.setAttribute(attribute);
		 //设置作者 
		String authorname = (String)bookinfo.get("authorname");
		if(authorname==null) {
			authorname = "";			
		}
		//resource.setAuthorname(authorname);
		 //设置摘要
		String description = (String)bookinfo.get("description");
		if(description==null) {
			description = "";
		}
		resource.setDescription(description);
		 //设置关键词
		//if(bookinfo.get("tags")!=null&&!"".equals(bookinfo.get("tags"))) resource.setTags(bookinfo.get("tags").toString());
		 //设置出版社  
		//if(bookinfo.get("pressname")!=null&&!"".equals(bookinfo.get("pressname"))) resource.setPressname(bookinfo.get("pressname").toString());
		 //设置主题词
		//if( bookinfo.get("title")!=null&&!"".equals(bookinfo.get("title")) ) resource.setSubject(bookinfo.get("title").toString());
		 //设置isbn
		//if(bookinfo.get("isbn")!=null&&!"".equals(bookinfo.get("isbn"))) resource.setIsbn(bookinfo.get("isbn").toString());

		 //设置专题库标识
		if(speIds!=null&&!"".equals(speIds)) resource.setSpeid(speIds);
		 //设置机构标识
		if(orgIds!=null&&!"".equals(orgIds)) resource.setOrgid(orgIds);

		if(bookinfo.get("price")!=null&&!"".equals(bookinfo.get("price"))) resource.setPrice(bookinfo.get("price").toString());

		//if(bookinfo.get("stprice")!=null&&!"".equals(bookinfo.get("stprice"))) resource.setPbooks_price(bookinfo.get("stprice").toString());

		//if(bookinfo.get("liprice")!=null&&!"".equals(bookinfo.get("liprice"))) resource.setListprice(bookinfo.get("liprice").toString());
		
		//上架时间t
		if(bookinfo.get("onlinetime")!=null&&!"".equals(bookinfo.get("onlinetime"))) resource.setOnlinetime(bookinfo.get("onlinetime").toString());
		//销售量
		/*if(bookinfo.get("salesnum")!=null&&!"".equals(bookinfo.get("salesnum"))) {
			resource.setSalesnum(bookinfo.get("salesnum").toString());
		} else {
			resource.setSalesnum("0");
		}
		//销售量
		if(bookinfo.get("stsalesnum")!=null&&!"".equals(bookinfo.get("stsalesnum"))) {
			resource.setStsalesnum(bookinfo.get("stsalesnum").toString());
		} else {
			resource.setStsalesnum("0");
		}*/
		//分类标识
		if(bookinfo.get("typeid")!=null&&!"".equals(bookinfo.get("typeid"))) resource.setTypeid(bookinfo.get("typeid").toString());
		//分类名称
		//if(bookinfo.get("typename")!=null&&!"".equals(bookinfo.get("typename"))) resource.setTypename(bookinfo.get("typename").toString());
		
		//if(bookinfo.get("thumb")!=null&&!"".equals(bookinfo.get("thumb"))) resource.setGenre(bookinfo.get("thumb").toString());
		
		 //创建时间
		//resource.setDate(new Date());
		return resource;
	}
	
	private void supplyBookPrices(Resource resource) throws Exception  {
		String pid = resource.getProdid();
		if(StringUtils.isBlank(pid)) {
			return; 
		}		
		Long bookId = Long.parseLong(pid);		
		@SuppressWarnings("rawtypes")
		Map bookinfo = new HashMap();
		bookinfo = bookService.getBookPrices(bookId);
		if(bookinfo.get("price")!=null&&!"".equals(bookinfo.get("price"))) resource.setPrice(bookinfo.get("price").toString());
		//if(bookinfo.get("stprice")!=null&&!"".equals(bookinfo.get("stprice"))) resource.setPbooks_price(bookinfo.get("stprice").toString());
		//if(bookinfo.get("liprice")!=null&&!"".equals(bookinfo.get("liprice"))) resource.setListprice(bookinfo.get("liprice").toString());
		if(bookinfo.get("salesnum")!=null&&!"".equals(bookinfo.get("salesnum"))) {
			//resource.setSalesnum(bookinfo.get("salesnum").toString());
		} else {
			//resource.setSalesnum("0");
		}		
		if(bookinfo.get("stsalesnum")!=null&&!"".equals(bookinfo.get("stsalesnum"))) {
			//resource.setStsalesnum(bookinfo.get("stsalesnum").toString());
		} else {
			//resource.setStsalesnum("0");
		}
		//if(bookinfo.get("thumb")!=null&&!"".equals(bookinfo.get("thumb"))) resource.setGenre(bookinfo.get("thumb").toString());
		if(bookinfo.get("onlinetime")!=null&&!"".equals(bookinfo.get("onlinetime"))) resource.setOnlinetime(bookinfo.get("onlinetime").toString());
	}
	
	private void clearTempFile(File  tempFile, String sourceType) {
		
		  //因为只有xml生成了临时文件，所以只删除这两种格式的文件类型
		if(StringUtils.equals("xml", sourceType)) {
			
			tempFile.delete();
		}
	}
	
	private String createSpecialUUID(SolrQueue queue) {
		String uuid = String.valueOf(queue.getResType());
		uuid = uuid + "_" + String.valueOf(queue.getProdId()); 
		Long orgId = queue.getOrgId();
		uuid = uuid + "_" + orgId; 
		logger.debug("uuid:" + uuid);
		return uuid;
	}
	
	private String createBookUUID(SolrQueue queue) {
		String uuid = String.valueOf(queue.getResType());
		uuid = uuid + "_" + String.valueOf(queue.getProdId()); 
		Long orgId = queue.getOrgId();
		uuid = uuid + "_" + orgId; 
		Long speId = queue.getSpeId();
		uuid = uuid + "_" + speId; 
		logger.debug("uuid:" + uuid);
		return uuid;
	}
	
	private ISearchFacede getSearchFacede() {
    	return (ISearchFacede)BeanFactoryUtil.getBean("searchFacede");
    }
	
	private IBookService getBookService() {
		return (IBookService) BeanFactoryUtil.getBean("bookService");
	}
	
	private ISolrQueueFacede getSolrQueueFacede() {
		return (ISolrQueueFacede) BeanFactoryUtil.getBean("solrQueueFacede");
	}
	
	private ISpecialService getSpecialService() {
		return (ISpecialService) BeanFactoryUtil.getBean("specialService");
	}
}
