package com.brainsoon.solr.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.brainsoon.solr.util.OptionType;
import com.channelsoft.appframe.action.BaseAction;

public class SolrBaseAction extends BaseAction{
	protected Map<String,String> params = new HashMap<String,String>();
	
	//参数检查通过返回null
	protected String checkParams(){
		return null;
	}
	
	protected static String getResult(String status, String msg) {
		return createResultElement(status, msg).asXML();
	}
	/**
	 * 根据操作类型和返回码构造返回消息
	 * @param opt 操作类型
	 * @param code 返回码
	 * @return
	 */
	protected static String getResultByOpt(String opt,String code){
		return getResult(code,OptionType.getCodeDesc(opt, code));
	}
	/**
	 * 根据操作类型和返回码及额外的信息构造返回消息
	 * @param opt 操作类型
	 * @param code 返回码
	 * @return
	 */
	protected static String getResultByOpt(String opt,String code,String otherMsg){
		return getResult(code,OptionType.getCodeDesc(opt, code)+otherMsg);
	}
	protected static String getSuccessResult(){
		return getResult("0", "操作成功");
	}
	protected static Element getSuccessResultElement() {
		return createResultElement("0", "操作成功");
	}
	/**
	 * 
	 * @param doc 给定的xml文档
	 * @param node 在根节点下添加节点的名称
	 * @param dataContent 添加节点内的文本
	 * @return
	 */
	protected static String appendDataNode(Element doc, String node,String dataContent) {
		doc.addElement(node).addText(dataContent);
		return doc.asXML();
	}
	
	protected static String appendDataNode(String docStr, String node,String dataContent) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(docStr);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		doc.getRootElement().addElement(node).addText(dataContent);
		return doc.asXML();
	}
	protected static Element appendDataNodeReturnElement(Element doc, String node,String dataContent) {
		doc.addElement(node).addText(dataContent);
		return doc;
	}
	protected void collectParam(){
		Map<String,String[]> m = this.getRequest().getParameterMap();
		Set<String> keySet = m.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			params.put(string, m.get(string)[0]);
		}
	}
	protected void collectPostParam() throws IOException{
		 BufferedReader br = new BufferedReader(new InputStreamReader(this.getRequest().getInputStream()));  
		 String line = null;  
		 StringBuilder sb = new StringBuilder();  
		 while((line = br.readLine())!=null){  
		        sb.append(line);  
		 }
		 String para = sb.toString();
		 if(StringUtils.isNotBlank(para)) {
			 String[] paras =  para.split("&");
			 for(String p : paras) {
				 String[] ps = p.split("=");
				 if(ps != null & ps.length >= 2) {
					 params.put(ps[0], ps[1]); 
				 }
			 }
		 }		
	}
	private static Element createResultElement(String status, String msg) {
        Element response = DocumentHelper.createDocument().addElement("response");
        response.addElement("status").addText(status);
        response.addElement("message").addText(msg);
        
        return response;
	}
	
	protected static String getResult(String status,String msg,String otherData){
		String xml = "<response><status>{0}</status><message>{1}</message>{2}</response>";
		MessageFormat mf = new MessageFormat(xml);
		return mf.format(new String[]{status,msg,otherData});
	}
	
	protected static String getResultRO(String status,String msg,String rek){
		String xml = "<response><status>{0}</status><message>{1}</message><ro>{2}</ro></response>";
		MessageFormat mf = new MessageFormat(xml);
		return mf.format(new String[]{status,msg,rek});
	}
}
