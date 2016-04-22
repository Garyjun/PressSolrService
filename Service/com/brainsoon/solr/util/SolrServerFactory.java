package com.brainsoon.solr.util;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.channelsoft.appframe.utils.WebappConfigUtil;


/**
 * SolrServer工厂类，主要提供两种类型的SolrServer -- EmbeddedSolrServer & CommonsHttpSolrServer
 * <br/>
 */
public class SolrServerFactory {
	
	private static final Logger logger = Logger.getLogger(SolrServerFactory.class);
	
	private static Map<String, SolrServer> solrServerMap = Collections.synchronizedMap(new HashMap<String, SolrServer>());
	
	private SolrServerFactory() {
	}
	
	/**
	 * 获取CommonsHttpSolrServer
	 * 图书、资讯库
	 * @param SOLR_URL
	 * @return SolrServer
	 */
	public static SolrServer getBookSolrServer() {		
		String solrUrl = WebappConfigUtil.getParameter("solrUrl") + "/core0";
		SolrServer solrServer = null;
		if (!solrServerMap.containsKey(solrUrl)) {
			solrServer = new HttpSolrServer(solrUrl);
			if (solrServer != null) {
				solrServerMap.put(solrUrl, solrServer);
			}
			logger.debug(solrUrl);
			logger.debug(solrServerMap);
		}
		return solrServerMap.get(solrUrl);
	}
	
	/**
	 * 获取CommonsHttpSolrServer
	 * 专题库
	 * @param SOLR_URL
	 * @return SolrServer
	 */
	public static SolrServer getSpecialSolrServer() {		
		String solrUrl = WebappConfigUtil.getParameter("solrUrl") + "/core1";
		SolrServer solrServer = null;
		if (!solrServerMap.containsKey(solrUrl)) {
			solrServer = new HttpSolrServer(solrUrl);
			if (solrServer != null) {
				solrServerMap.put(solrUrl, solrServer);
			}
			logger.debug(solrUrl);
			logger.debug(solrServerMap);
		}
		return solrServerMap.get(solrUrl);
	}
}

