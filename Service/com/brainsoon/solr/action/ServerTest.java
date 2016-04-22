package com.brainsoon.solr.action;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.testng.annotations.Test;

import com.brainsoon.solrservice.res.service.IBookService;
import com.channelsoft.appframe.utils.BeanFactoryUtil;
import com.opensymphony.xwork2.interceptor.annotations.After;
import com.opensymphony.xwork2.interceptor.annotations.Before;

public class ServerTest {
	private Log logger = LogFactory.getLog(ServerTest.class);
    private SolrServer server;
    private HttpSolrServer httpServer;
    
    
    private static final String DEFAULT_URL = "http://localhost:8983/solr/core0";
    
    @Before
    public String init() throws MalformedURLException {
        server = new HttpSolrServer(DEFAULT_URL);
		httpServer = new HttpSolrServer(DEFAULT_URL);
		
		System.out.println("初始化完");
		this.addDoc();
		return null;
    }
    
    public String test() throws MalformedURLException {
    	String orgids =   getBookService().getOrgIds(Long.valueOf("1202"));
    	String speids = getBookService().getSpeIds(Long.valueOf("1202"));
    	logger.debug(speids);
    	return null;
    }
    
    @After
    public String destory() {
        server = null;
        httpServer = null;
        System.runFinalization();
        System.gc();
        System.out.println("destory完成");
		return null;
    }
    
    public final String fail(Object o) {
        System.out.println(o);
		return null;
    }
    
    /**
     * <b>function:</b> 测试是否创建server对象成功
     * @author hoojo
     * @createDate 2011-10-21 上午09:48:18
     */
    @Test
    public String server() {
        fail(server);
        fail(httpServer);
        System.out.println("server完成");
		return null;
    }
 
    /**
     * <b>function:</b> 根据query参数查询索引
     * @author hoojo
     * @createDate 2011-10-21 上午10:06:39
     * @param query
     */
    public void query(String query) {
        SolrParams params = new SolrQuery(query);
        try {
            QueryResponse response = server.query(params);
            
            SolrDocumentList list = response.getResults();
            for (int i = 0; i < list.size(); i++) {
                fail(list.get(i));
                //System.out.println(list.get(i));
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
    }
    
    public void addDoc() {
        for(int i=3;i<=100;i++)
        {
        	//创建doc文档
            SolrInputDocument doc = new SolrInputDocument();
	        doc.addField("id", i);
	        doc.addField("name", "Solr Input Document对中文的支持"+i);

	        try {
	            //添加doc文档
	            UpdateResponse response = server.add(doc);
	            fail(server.commit());//commit后才保存到索引库
	            fail(response);
	            fail("query time:" + response.getQTime());
	            fail("Elapsed Time:" + response.getElapsedTime());
	            fail("status:" + response.getStatus());
	        } catch (SolrServerException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        }
        query("name:中文");
    }

    private IBookService getBookService() {
		return (IBookService) BeanFactoryUtil.getBean("bookService");
	}

}