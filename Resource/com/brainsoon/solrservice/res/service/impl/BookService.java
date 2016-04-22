package com.brainsoon.solrservice.res.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.brainsoon.solrservice.res.po.Book;
import com.brainsoon.solrservice.res.po.BookWork;
import com.brainsoon.solrservice.res.service.IBookService;
import com.channelsoft.appframe.service.impl.BaseServiceObject;


public class BookService extends BaseServiceObject implements IBookService {

	private Log logger = LogFactory.getLog(BookService.class);
	@Override
	public boolean isExsitBook(Map<String, String> oresBaseMap) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List getBookIds() {
		String sql = "SELECT b.bookid FROM cciph_book b LEFT JOIN cciph_book_work bw ON b.bookid = bw.bookid WHERE bw.status = 6 OR bw.st_status=6";
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return list;
		}
		return null;
	}

	@Override
	public Map getBookWork(Long bookId) {
		String sql = "SELECT b.bookid, b.title, b.description, b.tags, b.attribute AS attribute,b.stream AS stream, b.price, p.name AS pressname, a.name AS authorname, bw.isbn,bt.typeid AS typeid,bt.name AS typename, b.isprint, FROM_UNIXTIME(bw.pubdate,'%Y年%m月') AS publishdate,bw.pubdate AS pubdate,bw.isprobation AS isprobation,bw.ispdf AS ispdf,bw.isvip AS isvip, bw.published AS onlinetime, bw.status AS status,  bw.st_status AS ststatus,bw.pbooks_price AS stprice, bs.sale AS salesnum, bs.st_sales AS stsalesnum, b.thumb AS thumb, bw.listprice AS liprice"+ 
				  " FROM cciph_book b LEFT JOIN cciph_book_work bw ON b.bookid=bw.bookid "+
				  " LEFT JOIN cciph_press_book pb ON pb.bookid=b.bookid LEFT JOIN cciph_press p ON pb.pressid=p.pressid "+
				  " LEFT JOIN cciph_author_book ab ON ab.bookid=b.bookid LEFT JOIN cciph_author a ON  ab.authorid=a.authorid"+
				  " LEFT JOIN cciph_book_work_type bwt ON bwt.bookid=b.bookid LEFT JOIN cciph_book_type bt ON bt.typeid=bwt.typeid"+ 
				  " LEFT JOIN cciph_book_count bs ON bs.bookid=b.bookid "+
				  " WHERE b.isprint=1 AND b.bookid = " + bookId;
		logger.debug(sql);
		List list = this.getBaseJdbcDao().queryForList(sql);
		if(list!=null && list.size()>0) {
			return (Map)list.get(0);
		}
		return null;
	}

	@Override
	public Map getBookPrices(Long bookId) {
		String sql = "SELECT b.price, bw.pbooks_price AS stprice, bs.sale AS salesnum, bs.st_sales AS stsalesnum, bw.listprice AS liprice, b.thumb AS thumb, bw.published AS onlinetime"+ 
				  " FROM cciph_book b LEFT JOIN cciph_book_work bw ON b.bookid=bw.bookid "+
				  " LEFT JOIN cciph_book_count bs ON bs.bookid=b.bookid "+
				  " WHERE b.isprint=1 AND b.bookid = " + bookId;
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
	public BookWork getBookWorkinfo(Long bookId) {
		String hql = "FROM Book WHERE bookid=:bookId";
		logger.debug(hql);
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("bookId", bookId);
		List<BookWork> bookworks = this.getBaseDao().query(hql, m);
		if(bookworks!=null&&bookworks.size()>0){
			return bookworks.get(0);
		}
		return null;
	}

	@Override
	public String getOrgIds(Long bookId) {
		String sql = "SELECT DISTINCT(organizationid) FROM cciph_collect_list WHERE isvalid=1 AND bookid=" + bookId;
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
		String sql = "SELECT DISTINCT(lb.booklistid) FROM cciph_list_book lb LEFT JOIN cciph_list l ON lb.booklistid = l.booklistid WHERE l.status =1 AND lb.bookid=" + bookId;
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
