package com.brainsoon.solrservice.res.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.brainsoon.solrservice.res.po.Book;
import com.brainsoon.solrservice.res.service.IBookArticleService;
import com.channelsoft.appframe.service.impl.BaseServiceObject;


public class BookArticleService extends BaseServiceObject implements IBookArticleService {

	private Log logger = LogFactory.getLog(BookArticleService.class);
	@Override
	public boolean isExsitBook(Map<String, String> oresBaseMap) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List getBookIds() {
		String sql = "select bookid from xhyb_book_article";
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return list;
		}
		return null;
	}

	@Override
	public Map getBookArticle(Long bookId) {
		String sql = "SELECT b.bookid, b.title, b.description, b.attribute AS attribute,b.stream AS stream, b.price, ba.genre, ba.era, ba.author AS authorname, FROM_UNIXTIME(ba.releasedate,'%Y年%m月') AS publishdate,ba.releasedate AS pubdate, ba.created AS onlinetime, bs.sales AS salesnum "+ 
				  " FROM xhyb_book b LEFT JOIN xhyb_book_article ba ON b.bookid=ba.bookid "+
				  " LEFT JOIN xhyb_book_stat bs ON bs.bookid=b.bookid "+
				  " WHERE b.bookid = " + bookId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return (Map)list.get(0);
		}
		return null;
	}

	@Override
	public Book getBookinfo(Long bookId) {
		String hql = "FROM Book WHERE bookid=:bookId";
		logger.debug(hql);
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("bookId", bookId);
		List<Book> books = this.getBaseDao().query(hql, m);
		if(books!=null&&books.size()>0){
			return books.get(0);
		}
		return null;
	}
	
	@Override
	public String getOrgIds(Long bookId) {
		String sql = "SELECT DISTINCT(organizationid) FROM xhyb_collect_list WHERE isvalid=1 AND bookid=" + bookId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			String orgIds = "";
			for(int i=0;i<list.size();i++) {
				Map item = (Map)list.get(i);
				orgIds += item.get("organizationid").toString();
				if(i!=list.size()-1) {
					orgIds += " ";
				}
			}
			return orgIds;
		}
		return null;
	}

	@Override
	public String getSpeIds(Long bookId) {
		String sql = "SELECT DISTINCT(lb.booklistid) FROM xhyb_list_book lb LEFT JOIN xhyb_list l ON lb.booklistid = l.booklistid WHERE l.status =1 AND lb.bookid=" + bookId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			String speIds = "";
			for(int i=0;i<list.size();i++) {
				Map item = (Map)list.get(i);
				speIds += item.get("booklistid").toString();
				if(i!=list.size()-1) {
					speIds += " ";
				}
			}
			return speIds;
		}
		return null;
	}
    
}
