package com.brainsoon.solr.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.channelsoft.appframe.utils.ZipUtil;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

public class SolrUtil {
	
	private static final Logger logger = Logger.getLogger(SolrUtil.class);
	
	
	public static void main(String[] args) throws Exception  {
//		logger.info("______over_______" + pdfTextStripper("D:\\cdp\\287815_____原文件_248__北大清华学生求职故事.pdf"));
//		try {
//		  String sssString = Html2Text(FileUtils.readFileToString(new File("D:\\channelSpace\\bres\\type15\\30\\20120327195456\\xml\\chapter009.xml"), "UTF-8"));
//		  
//		  Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//		  
//		  Matcher m = p.matcher(sssString);
//		  
//		  String after = m.replaceAll("");
//
//		}catch (Exception e) {
//			
//		}
		String str = "本书以煤矿安全管理为研究对象，以培养本质安全人为目标，以安全系统工程等理论为指导，结合编著者多年煤矿安全生产管理实践，提出并系统阐述了“两管三化四法五字”安全管理法，建立了“六预六防”煤矿安全预控管理体系。“两管”就是管住“人”的不安全行为和“物”的不安全状态；“三化”就是加强基础管理精细化、人员培训制度化和技术装备现代化建设；“四法”是指责任连带、奖惩结合法，誓词提醒、手指口述法，过关帮教、亲情感化法，警示教育、文艺教化法：“五字”指的是“严、细、实、深、恒”。“六预六防”则是指预想、预知、预测、预报、预警、预控，个人防、岗位防、班组防、区队防、安检防、系统防。本书可作为工矿企业管理者、工程";
		str = getWeiBoStr(str, 160);	
		System.out.println(str);
	}
	
	public static boolean pdfTextStripper( String source ) {
		
		 // 文件名称，包括格式
		String fileNameSource = new File(source).getName();
		 // 原文件名称 没有格式名称
		String fileName = fileNameSource.substring(0,fileNameSource.lastIndexOf("."));
		 // 文件的类型
		String fileType = fileNameSource.substring(fileNameSource.lastIndexOf(".")+1);
		
		if("pdf".equals(fileType)) {
			logger.info("支持pdf文件格式！");
		} else {
			logger.info("不支持非pdf文件格式！");
			return false;
		}
		
		boolean flag = false;
		
		String sourcePath =  new File(source).getParent();
		
		String pdfPath = sourcePath + File.separator + "pdf" + File.separator ;
		
		String txtPath = sourcePath + File.separator + fileName +".txt";
		
		PDDocument document = null;
		
		FileWriter fw = null;
		
		try {
			 // 拆分PDF
			String[] fileNames = pdfSplit(source);
			
			 // 如果有该文件则删除，避免增量增加文本
			if(new File(txtPath).isFile()) {
				new File(txtPath).delete();
			}
			 // 写文件
			fw = new FileWriter( txtPath );
			 // 写缓存
			BufferedWriter bw = new BufferedWriter(fw);
			 // 提取文本对象
			PDFTextStripper stripper = new PDFTextStripper("UTF-8");
			stripper.setSortByPosition(false);
			 // 增量提文件
			for(int i=0;i<fileNames.length;i++) {
				 // 加载pdf文件
				document = PDDocument.load(pdfPath + fileNames[i] +".pdf");
				 // 放入缓存
				bw.write(stripper.getText(document));
			}
			 // 开始写文件
			bw.flush();
			 // 关闭缓存
			bw.close();
			flag = true;
		}catch(Exception ex) {
			logger.error(ex);
		} finally {
			try {
				if (document != null) {
					document.close();
				}
				if(fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				logger.error(e);
			}
			try {
				FileUtils.deleteDirectory(new File(pdfPath));
				new File(pdfPath).delete();
				// 如果提取文件失败，则删除文本文件
				if(!flag) {
					new File(txtPath).delete();
				}
			}catch( IOException ioe) {
				logger.error("删除临时目录失败："+pdfPath);
			}
		}
		
		return flag;
	}
	
	/**
	 * xml去掉特殊字符
	 * @param source
	 * @return
	 */
	public static boolean xml2Text(String source) {
		
		 // 文件名称，包括格式
		String fileNameSource = new File(source).getName();
		 // 原文件名称 没有格式名称
		String fileName = fileNameSource.substring(0,fileNameSource.lastIndexOf("."));
		
		String sourcePath =  new File(source).getParent();
		
		String txtPath = sourcePath + File.separator + fileName +".txt";
		try {
			String fileContent = FileUtils.readFileToString(new File(source), "UTF-8");
			
			String fileTxt = Html2Text(fileContent);
			if(StringUtils.isNotEmpty(fileTxt.trim()) && fileTxt.length()>1) {
				FileUtils.writeStringToFile(new File(txtPath), fileTxt, "UTF-8");
			} else {
				return false;
			}
		} catch (IOException ie) {
			logger.error(ie);
			return false;
		}
		return true;
	}
	
	/**
	 * PDF文件拆分
	 * @param source pdf原文件路径
	 * @param target 图片保存目录
	 * @return
	 */
	public static String[] pdfSplit( String source ) throws Exception {
		
		String fileName = new File(source).getName();
		fileName = fileName.substring(0,fileName.lastIndexOf("."));
		
		String pdfPath = new File(source).getParent() + File.separator + "pdf" + File.separator;
		
		 // pages文件夹路径 
		File pagesDirectory = new File(pdfPath);
		
		 // 创建pages文件夹 
		if (!pagesDirectory.exists()) {
			pagesDirectory.mkdir();
		}
		 // 文件的页数
		int pageNum = 20;
		
		try {
			
			  // 读取总的PDF文件 
	        PdfReader reader = new PdfReader(source);
	        
	         // 总文件的页数
	        int pageTotalNum = reader.getNumberOfPages();
	        
	         // 拆分的文件数
	        int fileNum = pageTotalNum/pageNum;
	         // 文件余下的页数
	        int fileRemainPage = pageTotalNum%pageNum;
	         // 如果文件余下的页数不为零，则在增加一页
	        if(fileRemainPage != 0) {
	        	fileNum = fileNum + 1;
	        }
	         // 文件名的数组
	        String[] fileNameArr = new String[fileNum];
	         // 按规则设置文件名的数组 
	        for(int i=0;i<fileNum;i++) {
	        	fileNameArr[i] = fileName + "_" + i;
	        	//logger.debug("拆分后的文件名为：" + fileNameArr[i]);
	        	
	        	 // 开始页码
	        	int startPageNum = i*pageNum;
	        	 // 结束页码
	        	int endPageNum = (i+1)*pageNum;
	        	 //如果文件余下的页数不为零，则修改页码
	        	if(fileRemainPage != 0 && fileNum == (i+1)) {
	        		endPageNum = (startPageNum + fileRemainPage);
	        	}
	        	
	        	com.itextpdf.text.Document documentiText = new  com.itextpdf.text.Document(reader.getPageSizeWithRotation(startPageNum+1));
	        	
	        	PdfCopy copy = new PdfCopy(documentiText, new FileOutputStream(pdfPath + fileNameArr[i] + ".pdf"));
	        	
	        	copy.setCompressionLevel(9);
	            copy.setFullCompression();
	            documentiText.open();
	            
	            for (int j=startPageNum; j < endPageNum; j++) { 
	            	 PdfImportedPage page = copy.getImportedPage(reader, j+1);
			         copy.addPage(page); 
	            }
	             // 写文件
	            documentiText.close();
	        }
	        
	        // 释放资源
	        reader.close();
	        
	        return fileNameArr;
	        
		} catch(Exception ex) {
			
			logger.error("资源加工，拆分pdf文件异常："+ex);
			return null;
		}
	}
	
	/**
	 * 处理CDP格式文件
	 * @param source
	 */
	public static boolean  fileCDPType( String source ) { 
		 // 原文件的路径信息
		String sourcePath = new File(source).getParent();
		 // 文件名称，包括格式
		String fileNameSource = new File(source).getName();
		 // 原文件名称 没有格式名称
		String fileName = fileNameSource.substring(0,fileNameSource.lastIndexOf("."));
		 // 文件的类型
		String fileType = fileNameSource.substring(fileNameSource.lastIndexOf(".")+1);
		
		if("cdp".equals(fileType)) {
			logger.info("支持cdp文件格式！");
		} else {
			logger.info("不支持非cdp文件格式！");
			return false;
		}
		
		String cdpPath = sourcePath + File.separator + fileName + File.separator;
		File cdpDirectory = new File(cdpPath);
		if (!cdpDirectory.exists()) {
			cdpDirectory.mkdir();
		} else {
			try {
				FileUtils.deleteDirectory(new File(cdpPath));
				new File(cdpPath).delete();
			}catch( IOException ioe) {
				logger.error("删除临时目录失败：" + cdpPath);
			}
		}
		
		 // 解压CPD包
		ZipUtil.unzip( source, cdpPath );
		
		 // 解压后目录
		File dir = new File(cdpPath);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for(int i=0;i<files.length;i++) {
				logger.info("" + files[i].getName());
				
				if(files[i].isDirectory()) {
					logger.info("目录类型：" + files[i].getName() );
					if("pdf".equals(files[i].getName())) {
						
					} else if("xml".equals(files[i].getName())) {
						
					}
				} else {
					logger.info("文件类型：" + files[i].getName() + "暂不处理！");
				}
			}
		} else {
			logger.info("解压目录失败，目录不存在！");
		}
		
		return false;
	}
	/**
	 * 过滤html标签
	 * @param input
	 * @return
	 */
	public static String Html2Text(String input) { 
		
		/* 含html标签的字符串  */
		String htmlStr = input; 
		String textStr =""; 
		java.util.regex.Pattern p_script; 
		java.util.regex.Matcher m_script; 
		java.util.regex.Pattern p_style; 
		java.util.regex.Matcher m_style; 
		java.util.regex.Pattern p_html; 
		java.util.regex.Matcher m_html;
		
		java.util.regex.Pattern p_escape; 
		java.util.regex.Matcher m_escape; 
		
		try { 
			  /* 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> */
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
			  /* 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> */
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";  
			  /* 定义HTML标签的正则表达式  */
			String regEx_html = "<[^>]+>"; 
			 /* 定义转义正则表达式*/
			String regEx_escape = "\\s*|\t|\r|\n";
			
			  /* 过滤script标签  */
			p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
			m_script = p_script.matcher(htmlStr); 
			htmlStr  = m_script.replaceAll(""); 

			  /* 过滤style标签  */
			p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
			m_style = p_style.matcher(htmlStr); 
			htmlStr = m_style.replaceAll(""); 
      
			  /* 过滤html标签  */
			p_html  = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
			m_html  = p_html.matcher(htmlStr); 
          	htmlStr = m_html.replaceAll(""); 
          	 /* 去除字符串中的空格、回车、换行符、制表符转 */
          	p_escape =  Pattern.compile(regEx_escape,Pattern.CASE_INSENSITIVE);
          	m_escape =  p_escape.matcher(htmlStr);
        	htmlStr  = m_escape.replaceAll(""); 
      
          	textStr = htmlStr; 
      
		}catch(Exception e) { 
           logger.error("Html2Text: " + e.getMessage()); 
		} 
		  /* 返回文本字符串  */
		return textStr;
      }   

	/**
	 * 处理CDP格式文件之PDF
	 * @param source
	 */
	public void fileCDPTypeOfPDF( String source ) {
		
	}
	
	 public static String getWeiBoStr(String str, int length) {
		  int count = 0;
		  int offset = 0;
		  char[] c = str.toCharArray();
		  for (int i = 0; i < c.length; i++) {
			   if (c[i] > 256) {
			    offset = 2;
			    count += 2;
			   } else {
			    offset = 1;
			    count++;
			   }
			   if (count == length) {
			    return str.substring(0, i + 1);
			   }
			   if ((count == length + 1 && offset == 2)) {
			    return str.substring(0, i);
			   }
			}
			return "";
	 }

}
