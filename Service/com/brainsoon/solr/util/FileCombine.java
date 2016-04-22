package com.brainsoon.solr.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 文件合并工具类
 * 
 * @author zuo
 * @update 20121112
 * 
 */
public class FileCombine {
	public static final Logger logger = Logger.getLogger(FileCombine.class);

	private static String separatorChar = "---";
	private static final String newLineChar = System.getProperty("line.separator");

	private static boolean isMacthFile(File f, String tg) {
		String file = f.getName();
		String hz = file.substring(file.lastIndexOf(".") + 1).toLowerCase();
		if (hz.compareTo(tg) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 合并文件
	 * 
	 * @param srcPath
	 * @param destFile
	 * @param fileType
	 */
	public static void combineFile(String srcPath, String destFile,
			String fileType) {
		combineFile(srcPath, destFile, fileType, "", true, false);
	}

	/**
	 * 合并文件
	 * 
	 * @param srcPath
	 * @param destFile
	 * @param fileType
	 * @param separator
	 * @param newLine
	 *            是否新起一行追加
	 * @param isLeave
	 *            是否保留源文件
	 * @throws IOException
	 */
	public static void combineFile(String srcPath, String destFile,
			String fileType, String separator, boolean newLine, boolean isLeave){
		File srcFile = new File(srcPath);
		if (srcFile.exists() && srcFile.isDirectory()) {
			BufferedWriter bufferedWriter = null;
			OutputStreamWriter outStream = null;
			FileOutputStream fileOutStream = null;
			ArrayList<File> fileList = null;
			try{
				fileList = extractMatchFiles(srcPath, fileType);
	
				if (!StringUtils.isBlank(separator)) {
					separatorChar = separator;
				}
				int fileListSize = fileList.size();
				if(fileListSize == 0){
					 logger.error("没有要合并的文件");
				}
				File currentFile = null;
				StringBuffer fileContentTemp = null;
				Date dt = new Date(); 
			    long begintime = dt.getTime();   
				for (int i = 0; i < fileListSize; i++) {
					currentFile = (File) fileList.get(i);
					if (null == currentFile) {
						continue;
					}
					if (i == 0) {
						fileOutStream = new FileOutputStream(destFile);
						outStream = new OutputStreamWriter(fileOutStream, "UTF-8");
						bufferedWriter = new BufferedWriter(outStream);
					}
					fileContentTemp = getFileContent(fileContentTemp,currentFile.getAbsolutePath(),currentFile.getName());
					if(i== fileListSize-1){
						if(fileContentTemp != null){
							String combineContent = fileContentTemp.toString();
							bufferedWriter.write(combineContent);
						}
						 Date dt2 = new Date();  		
				         long endtime = dt2.getTime();  			
				         long timecha = (endtime - begintime);  			
//				         String totaltime = BaseCommonUtil.sumTime(timecha);  
//				         logger.info("【" +  currentFile.getName() + "】文件合成完毕,共用时: " + totaltime + "  秒");
					}
				}
				
			}catch (Exception e) {
				logger.error("文件合并出现异常",e);
				throw new com.channelsoft.appframe.exception.ServiceException(e);
			}finally{
				try {
					if(null != fileOutStream){
						fileOutStream.flush();
						outStream.flush();
						bufferedWriter.flush();
						fileOutStream.close();
					}
					if(null != outStream){
						outStream.close();
					}
					if(null != bufferedWriter){
						bufferedWriter.close();
						bufferedWriter = null;
					}
					if (!isLeave) {
						for (int i = 0; i < fileList.size(); i++) {
							File currentFile = (File) fileList.get(i);
							if (null == currentFile) {
								continue;
							}
						}
					}
				} catch (IOException e) {
					logger.error("文件合并出现关闭异常",e);
				}
			}
		}
	}

	
	
	/**
	 * 抽取符合文件类型的文件集合
	 * 
	 * @param srcPath
	 * @param fileType
	 * @return
	 */
	private static ArrayList<File> extractMatchFiles(String srcPath,String fileType) {
		File[] totolList = null;
		ArrayList<File> rtnList = new ArrayList<File>();
		File srcFile = new File(srcPath);
		if (srcFile.exists() && srcFile.isDirectory()) {
			totolList = srcFile.listFiles();
			for (int i = 0; i < totolList.length; i++) {
				File file = totolList[i];
				if (file.isDirectory()) {
					File[] files = file.listFiles();
					for(int j = 0;j < files.length; j++) {
						File newfile = files[j];
						if(newfile.isDirectory()) {
							continue;
						}
						if (fileType.equals("*")) {
							rtnList.add(newfile);
						} else if (isMacthFile(newfile, fileType)) {
							rtnList.add(newfile);
						}
					}
				}
				if (fileType.equals("*")) {
					rtnList.add(file);
				} else if (isMacthFile(file, fileType)) {
					rtnList.add(file);
				}
			}
		}
		return rtnList;
	}
	
	
	/**
	 * 读取文件并放入StringBuffer中
	 * @param fileContentTemp
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static  StringBuffer getFileContent(StringBuffer fileContentTemp,String filePath,String fileName){
		if (null == fileContentTemp) {
			fileContentTemp = new StringBuffer();
		}
		StringBuffer fileContentCurrent = new StringBuffer();
		FileInputStream fInputStream = null;
		InputStreamReader inputStreamReader = null;
		FileReader fileReader = null;
		BufferedReader in = null;
		try {
			//String encode = FileUtils.codeString(filePath);
			String encode = "UTF-8";
			//logger.debug("文件"+filePath+"编码:"+encode);
			if(!"UTF-8".equalsIgnoreCase(encode)){
				encode = "GBK";
			}
			fInputStream = new FileInputStream(filePath);  
			inputStreamReader = new InputStreamReader(fInputStream, encode);  
			in = new BufferedReader(inputStreamReader);
			
			String strTmp = "";  
			
			//按行读取   
			while (( strTmp = in.readLine()) != null) {
				fileContentCurrent.append(strTmp).append(newLineChar);  
			}  
			String contentCurrent = fileContentCurrent.toString();
			contentCurrent = SolrUtil.Html2Text(contentCurrent);
			//contentCurrent = contentCurrent.replaceAll("\\s*|\t|\r|\n", "");
			//contentCurrent = contentCurrent.substring(contentCurrent.lastIndexOf("[")+1, contentCurrent.lastIndexOf("]]"));
			contentCurrent = filterOffUtf8Mb4(contentCurrent);
			fileContentTemp.append(contentCurrent);
		} catch (Exception e) {
			logger.error("获取文件出现异常",e);
		}finally{
			try {
				if(null != fInputStream){
					fInputStream.close();
					fInputStream = null;
				}
				if(null != inputStreamReader){
					inputStreamReader.close();
					inputStreamReader = null;
				}
				if(null != fileReader){
					fileReader.close();
				}
				if(null != in){
					in.close();
					in = null;
				}
				fileContentCurrent = null;
			} catch (IOException e) {
				logger.error("文件合并出现关闭异常",e);
			}
		}
		return fileContentTemp;
	}
	
	/**
	 * 读取文件并放入StringBuffer中
	 * @param fileContentTemp
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static  StringBuffer getFileContent2(StringBuffer fileContentTemp,String filePath,String fileName){
		if (null == fileContentTemp) {
			fileContentTemp = new StringBuffer();
		}
		StringBuffer fileContentCurrent = new StringBuffer();
		FileInputStream fInputStream = null;
		InputStreamReader inputStreamReader = null;
		FileReader fileReader = null;
		BufferedReader in = null;
		try {
			fInputStream = new FileInputStream(filePath);  
			inputStreamReader = new InputStreamReader(fInputStream, "UTF-8");  
			in = new BufferedReader(inputStreamReader);
			
			String strTmp = "";  
			
			//按行读取   
			while (( strTmp = in.readLine()) != null) {
				fileContentCurrent.append(strTmp).append(newLineChar);  
			}  
			String contentCurrent = fileContentCurrent.toString();
			contentCurrent = SolrUtil.Html2Text(contentCurrent);
			contentCurrent = contentCurrent.substring(contentCurrent.lastIndexOf("[")+1, contentCurrent.lastIndexOf("]]"));
			contentCurrent = contentCurrent.replaceAll("\\s*|\t|\r|\n", "");
			contentCurrent = contentCurrent.replaceAll("<[^>]+>", "");
			contentCurrent = filterOffUtf8Mb4(contentCurrent);
			fileContentTemp.append(contentCurrent);
		} catch (Exception e) {
			logger.error("获取文件出现异常",e);
		}finally{
			try {
				if(null != fInputStream){
					fInputStream.close();
					fInputStream = null;
				}
				if(null != inputStreamReader){
					inputStreamReader.close();
					inputStreamReader = null;
				}
				if(null != fileReader){
					fileReader.close();
				}
				if(null != in){
					in.close();
					in = null;
				}
				fileContentCurrent = null;
			} catch (IOException e) {
				logger.error("文件合并出现关闭异常",e);
			}
		}
		return fileContentTemp;
	}
	
	/**
     * 
    * @Title: filterOffUtf8Mb4
    * @Description: 过滤掉非UTF-8字符方法
    * @param text
    * @return
    * @throws UnsupportedEncodingException    参数
    * @return String    返回类型
    * @throws
     */
//    public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException { 
//    	String regEx = "[АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюяї۰۩۞]";
//		Pattern p = Pattern.compile(regEx);
//		Matcher m = p.matcher(text);	
//		text = m.replaceAll("").trim();
//        byte[] bytes = text.getBytes("UTF-8");  
//        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);  
//        int i = 0;  
//        while (i < bytes.length) {  
//            short b = bytes[i];  
//            if (b > 0) {  
//                buffer.put(bytes[i++]);  
//                continue;  
//            }  
//            b += 256;  
//            if ((b ^ 0xC0) >> 4 == 0) {  
//                buffer.put(bytes, i, 2);  
//                i += 2;  
//            }  
//            else if ((b ^ 0xE0) >> 4 == 0) {  
//                buffer.put(bytes, i, 3);  
//                i += 3;  
//            }  
//            else if ((b ^ 0xF0) >> 4 == 0) {  
//                i += 4;  
//            }  
//        }  
//        buffer.flip();  
//        return new String(buffer.array(), "utf-8");  
//    } 
    
    /**
	  * 过滤西里尔字符及扩展字符
	  * @param str
	  * @return
	  */
	 public static String filterByUnicode(String str){
	      if(str == null || str.length() == 0){
	          return "";
	      }
	//      String[][] filterCode = {{"0400","04FF"},{"0500","052F"},{"0600","06FF"},{"A640","A69F"}}; 
//	      String[][] filterCode = {{"0400","07FF"}};
	      String[][] filterCode = {{"0400","07FF"},{"A000","F8FF"},{"200000","3FFFFFF"},{"4000000","7FFFFFFF"}};
	      StringBuffer sb = new StringBuffer();
	      for(int i=0;i<str.length()-1;i++){
	   	   int ch = str.charAt(i);
	   	   String charStr = "";
	   	   int a = 0;
	          for (int k = 0; k < filterCode.length; k++) { 
	       	   if(a == 0){
	       		   String[] filterStr = filterCode[k]; 
	           	   String startCode = filterStr[0];  
	           	   String endCode = filterStr[1];  
	           	   int min = Integer.parseInt(startCode, 16);
	                  int max = Integer.parseInt(endCode, 16);
	                  if(ch >= min && ch <= max){
	               	   charStr = "";
	               	   a = 1;
	                  }else{
	               	   charStr = (char)ch + "";
	                  }
	       	   }
	          }
	          
	          if(!charStr.equals("")){
	       	   sb.append(charStr); 
	          }
	      }
	      return sb.toString();
  }
	 
  /**
   * 
   * @Title: filterOffUtf8Mb4
   * @Description: 过滤掉非UTF-8字符方法
   * @param text
   * @return
   * @throws UnsupportedEncodingException 参数
   * @return String    返回类型
   * @throws
    */
   public static  String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {  
	   	text = filterByUnicode(text);
		String regEx = "[?]";//?∑ф??
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(text);	
		text = m.replaceAll("").trim();
////		text = ToDBC(text);
       byte[] bytes = text.getBytes("UTF-8");  
       ByteBuffer buffer = ByteBuffer.allocate(bytes.length);  
       int i = 0;  
       while (i < bytes.length) {  
           short b = bytes[i];  
           if (b > 0) {  
               buffer.put(bytes[i++]);  
               continue;  
           }  
           b += 256;  
           if ((b ^ 0xC0) >> 4 == 0) {  
               buffer.put(bytes, i, 2);  
               i += 2;  
           }else if ((b ^ 0xE0) >> 4 == 0) { 
               buffer.put(bytes, i, 3);  
               i += 3;  
           }else if ((b ^ 0xF0) >> 4 == 0) {  
               i += 4;  
           }  
       }  
       buffer.flip();  
       return new String(buffer.array(),0,buffer.limit(), "utf-8");  
   }  
   
   public static void getFileContent2IndexFile(String srcPath, String indexPath){
	   File srcFile = new File(srcPath);
		if (srcFile.exists()) {
			BufferedWriter bufferedWriter = null;
			OutputStreamWriter outStream = null;
			FileOutputStream fileOutStream = null;
			try{
				StringBuffer fileContentTemp = null;
				Date dt = new Date(); 
			    long begintime = dt.getTime();
			    
				fileOutStream = new FileOutputStream(indexPath);
				outStream = new OutputStreamWriter(fileOutStream, "UTF-8");
				bufferedWriter = new BufferedWriter(outStream);
				
				fileContentTemp = getFileContent2(fileContentTemp,srcPath,srcFile.getName());
				
				if(fileContentTemp != null){
					String combineContent = fileContentTemp.toString();
					bufferedWriter.write(combineContent);
				}
				
			}catch (Exception e) {
				logger.error("文件合并出现异常",e);
				throw new com.channelsoft.appframe.exception.ServiceException(e);
			}finally{
				try {
					if(null != fileOutStream){
						fileOutStream.flush();
						outStream.flush();
						bufferedWriter.flush();
						fileOutStream.close();
					}
					if(null != outStream){
						outStream.close();
					}
					if(null != bufferedWriter){
						bufferedWriter.close();
						bufferedWriter = null;
					}
				} catch (IOException e) {
					logger.error("文件合并出现关闭异常",e);
				}
			}
		}
   }
   
   	public static void main(String[] args) throws UnsupportedEncodingException {
   		//String srcPath = "C:\\workhj\\workspace\\bsrcm_xhyb\\WebRoot\\fileDir\\fileRoot\\文章\\N_1001666X_399\\A_1001666X_399_00054\\正文XML\\article.xml";
   		//String indexPath = "C:\\workhj\\workspace\\bsrcm_xhyb\\WebRoot\\fileDir\\fileRoot\\文章\\N_1001666X_399\\A_1001666X_399_00054\\正文XML\\index.txt";
   		//getFileContent2IndexFile(srcPath, indexPath);
   		
   		
   		
   		String contentCurrent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
								"<Article>"+
								"	<Title>李鹏总理在庆祝中华人民共和国成立四十五周年招待会上的讲话</Title>"+
								"	<Author/>"+
								"	<Magazine>新华月报</Magazine>"+
								"	<MagazineYear>1994·10</MagazineYear>"+
								"	<MagazineNum>600</MagazineNum>"+
								"	<Sort>政治</Sort>"+
								"	<PageNum>5</PageNum>"+
								"	<PDFPageNum>7</PDFPageNum>"+
								"	<Content><![CDATA[<p>各位来宾，各位朋友，各位同志：</p>"+
								"<p>在伟大的中华人民共和国成立四十五周年的喜庆时刻，我代表中共中央和国务院，向在各条战线辛勤劳动、工作和学习的全国各族人民，致以节日的祝贺！向台湾同胞、港澳同胞和海外侨胞，表示亲切的问候！向出席今天招待会的各国朋友，向一切关心和支持我国现代化建设事业的国际友人，表示深切的谢意！</p>"+
								"<p>一九四九年十月一日，毛泽东主席宣告中华人民共和国成立，结束了中华民族灾难深重的漫长岁月，开始了中国历史的新纪元，中国人民从此站起来了！</p>"+
								"<p>四十五年来，全国各族人民在中国共产党领导下，艰苦奋斗，百折不挠，创造了举世瞩目的辉煌业绩。特别是党的十一届三中全会以来，在邓小平同志建设有中国特色社会主义理论的指引下，我国各族人民坚持以经济建设为中心，坚持四项基本原则，坚持改革开放，锐意进取，开拓创新、社会生产力获得新的解放，国家面貌发生了历史性的巨大变化。国民经济持续、快速、健康发展，综合国力不断增强，人民生活明显改善，政治稳定，社会进步，民族团结，各项事业不断发展。从城市到农村，从沿海到边疆，到处欣欣向荣，生机勃勃。勤劳勇敢的中国人民，正满怀信心地在社会主义大道上胜利前进！</p>"+
								"<p>今年以来，全国各地遵照党中央确定的“抓住机遇、深化改革、扩大开放、促进发展、保持稳定”的大局，妥善处理改革、发展和稳定的关系，努力做好各方面的工作，改革开放和经济建设取得新的进展。各项重大改革措施顺利出台，在建立社会主义市场经济体制方面迈出了决定性的步伐。国民经济在前两年快速增长的基础上，继续保持快速、健康发展的势头，宏观调控取得积极成效。农业生产和农村经济持续发展，农民收入增长加快，虽然部分地区遭受了不同程度的自然灾害，全国仍然可望获得一个好的收成。工业获得新的成就，结构有所调整，效益有所提高，重点建设得到加强。对外开放继续扩大，对外经济技术交流活跃，进出口贸易持续增长，外商投资保持较高水平。社会主义民主法制建设和精神文明建设得到加强，各项社会事业蓬勃发展。我们在前进中还存在一些困难和问题，主要是物价涨幅过高，投资规模仍然偏大，部分国有企业生产经营比较困难，一些地方社会治安状况不够好，这些问题正在采取措施积极解决。我们有信心有能力解决前进道路上的困难和问题，把改革开放和现代化建设不断向前推进。</p>"+
								"<p>来宾们，朋友们，同志们！</p>"+
								"<p>明年是我国全面完成国民经济和社会发展第八个五年计划的最后一年。我们要以企业改革为重点，继续巩固和完善宏观管理体系各项重大改革措施，向建立社会主义市场经济体制迈出新的步伐。要千方百计加强农业基础，争取好的收成，搞好国有大中型企业，狠抓产业结构和产品结构的调整，大力加强科技和教育工作，继续抑制通货膨胀，改善人民生活，为制定和实施第九个五年计划打下好的基础。我们坚信，在邓小平同志建设有中国特色社会主义理论和党的基本路线指引下，在以江泽民同志为核心的党中央领导下，全国各族人民共同努力，再过五年时间，当我们庆祝中华人民共和国成立五十周年的时候，我国的现代化建设必将出现新的飞跃，我们的明天一定会更加美好！</p>"+
								"<p>来宾们，朋友们，同志们！</p>"+
								"<p>台湾是中国神圣领土不可分割的一部分。最近一段时间，台湾当局的一系列有碍祖国和平统一的言论和行动，理所当然地引起包括台湾人民在内的全国各族人民的极大关注。任何制造“两个中国”、“一中一台”以及其他分裂中国的图谋，都是中国政府和人民坚决反对的，都是不能得逞的。我们对香港的基本立场和方针，是众所周知和坚定不移的，无论发生什么周折，中国都将按期恢复对香港行使主权，并且有决心有能力保持香港的长期繁荣和稳定。中葡两国在澳门问题上一直进行着友好合作。祖国统一是人心所向，大势所趋。我们坚信，在全国各族同胞，包括台湾同胞、港澳同胞和海外侨胞的团结努力下，一定能够完成统一祖国的千秋大业。</p>"+
								"<p>来宾们，朋友们，同志们！</p>"+
								"<p>当今国际形势正处在深刻而复杂的变动之中，世界加速向多极化格局发展。总的形势有利于世界的和平和发展，经济优先正在成为国际关系发展的趋势，但霸权主义和强权政治仍然存在，天下并不太平。承认世界的多样性，在相互尊重主权和领土完整、互不侵犯、互不干涉内政、平等互利和和平共处的基础上建立和平、稳定、公正、合理的国际政治经济新秩序，加强国际间的相互了解与合作，已经成为世界各国人民的普遍要求。中国坚定不移地奉行独立自主的和平外交政策，在国际事务中发挥着日益重要的作用。我国的国际交往不断增加，国际地位不断提高，我们的朋友遍天下。中国的发展离不开世界，世界的进步也离不开中国。中国政府和人民愿意同各国政府和人民一道，进一步加强友好合作，为建设一个美好、和平繁荣的世界而努力奋斗！</p>"+
								"<p>最后，我提议：</p>"+
								"<p>为中华人民共和国成立四十五周年，</p>"+
								"<p>为中国的富强、民族的团结和人民的幸福，</p>"+
								"<p>为中国人民与世界人民的友谊和合作，</p>"+
								"<p>为世界的和平与发展，</p>"+
								"<p>为各位来宾、驻华使节和夫人的健康，</p>"+
								"<p>为在座的朋友们和同志们的健康，</p>"+
								"<p>干杯！</p>"+
								"<p>国务院总理李鹏9月30日晚在京举行盛大招待会，热烈庆祝全国各族人民的光辉节日——中华人民共和国成立45周年。党和国家领导人、各界人士和来自五大洲的宾客3500余人欢聚一堂，共度佳节。</p>"+
								"<p>晚上6时整，江泽民、李鹏、乔石、李瑞环、朱镕基、刘华清、胡锦涛、荣毅仁等党和国家领导人与外国贵宾一起步入宴会大厅，全场响起长时间热烈掌声。招待会在雄壮的国歌声中开始。李鹏总理走上讲台发表重要讲话。</p>"+
								"<p>正在北京访问的柬埔寨国王诺罗敦·西哈努克和王后莫尼列·西哈努克，朝鲜国家副主席李钟玉，阿曼协商会议主席卡塔比，印度国大党总书记库马尔·辛德，玻利维亚众议院副议长卡瓦列罗等应邀出席招待会。</p>"+
								"<p>出席招待会的领导人还有：丁关根、田纪云、李岚清、李铁映、邹家华、陈希同、尉健行、温家宝、王汉斌、宋平、薄一波、宋任穷、张震、任建新、倪志福、陈慕华、费孝通、孙起孟、雷洁琼、秦基伟、李锡铭、王丙乾、王光英、程思远、卢嘉锡、布赫、铁木尔·达瓦买提、李沛瑶、吴阶平、迟浩田、宋健、李贵鲜、陈俊生、司马义·艾买提、彭珮云、罗干、张思卿、吴学谦、杨汝岱、王兆国、阿沛·阿旺晋美、赛福鼎·艾则孜、洪学智、赵朴初、钱伟长、胡绳、钱正英、丁光训、孙孚凌、安子介、朱光亚、万国权。出席招待会的还有：在京的中共中央委员、候补委员，原中顾委委员，中纪委委员，全国人大常委会委员，全国政协常委；中共中央各部门、中央国家机关各部门主要负责人，中央军委委员，解放军各总部、各军种、各兵种和武警总部负责人；各民主党派和无党派人士，以及各人民团体主要负责人：科学技术、教育、文化艺术、新闻、出版、卫生、体育界知名人士；劳动模范和先进人物代表，解放军和武警英模代表；离休老红军、老干部代表；藏传佛教噶举派大活佛噶玛巴·伍金赤列一行，港澳台同胞、华侨、华人代表等。各国驻华使节、国际组织驻华代表和夫人；长期在华居留的外国专家，临时来华工作并做出贡献的外国专家和夫人等，也应邀出席招待会。</p>"+
								"<p>——编者</p>]]></Content>"+
									"<ArticleSource>新华社讯，10月1日《人民日报》</ArticleSource>"+
								"</Article>";
   		
   		contentCurrent = contentCurrent.substring(contentCurrent.lastIndexOf("[")+1, contentCurrent.lastIndexOf("]]"));
		contentCurrent = contentCurrent.replaceAll("\\s*|\t|\r|\n", "");
		contentCurrent = contentCurrent.replaceAll("<[^>]+>", "");
		contentCurrent = filterOffUtf8Mb4(contentCurrent);
		
		
	}
}
