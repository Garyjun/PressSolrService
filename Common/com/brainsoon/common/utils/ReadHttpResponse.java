package com.brainsoon.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.brainsoon.common.support.JsonDataObject;
import com.channelsoft.appframe.exception.ServiceException;
import com.google.gson.Gson;

/**
 * 
 * @ClassName: ReadHttpResponse 
 * @Description:  SCA http请求，返回 JsonDataObject<?> 对象
 * @author tanghui 
 * @date 2013-4-3 上午10:53:21 
 *
 */
public class ReadHttpResponse {
	
	private static Logger logger = Logger.getLogger(ReadHttpResponse.class);
	private static final String SERVLET_POST = "POST" ;
	private static final String SERVLET_GET = "GET" ;
	private static final String SERVLET_DELETE = "DELETE" ;
	private static final String SERVLET_PUT = "PUT" ;
	//连接主机的超时时间（单位：毫秒） 1000毫秒 = 1秒
	private static final int DEFAULTCONNECTTIMEOUT = 20000; 
	//从主机读取数据的超时时间（单位：毫秒） 
	private static final int DEFAULTREADTIMEOUT = 20000;
	 
	
	
	/**
	 * 
	 * @Title: getJsonString  GET
	 * @Description: 返回json串
	 * @param   
	 * @return String 
	 * @throws
	 */
	public static String getJsonString(String ipConfig,String urlParam){
		HttpURLConnection conn = null;
		BufferedReader br =  null;
		InputStream in = null;
		String jsonString = ""; //返回json串
		String url = getUrl(ipConfig, urlParam);
		if(StringUtils.isBlank(url)){
			logger.error("接口调用失败！ URL is null.");
		}else{
			try{
				URL urlObj = new URL(url);
			    conn = (HttpURLConnection)urlObj.openConnection();
				//连接主机的超时时间（单位：毫秒） 
				conn.setConnectTimeout(DEFAULTCONNECTTIMEOUT);  
				//从主机读取数据的超时时间（单位：毫秒）
				conn.setReadTimeout(DEFAULTREADTIMEOUT); 
				//设定请求的方法为"POST"，默认是GET   
				conn.setRequestMethod(SERVLET_GET);
				conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
				// 获取响应代码
				int code = conn.getResponseCode();
				if (code != 200) {
					throw new ServiceException();
				}
				conn.connect();
				in = conn.getInputStream(); 
			    br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				jsonString = outRs(br).toString();
			} catch (Exception e) {
				logger.error("接口调用失败！请确认接口服务是否正常。");
				logger.error("["+url+"]:" + e.getMessage());
			} finally{
				try {
					 if(br != null) br.close();
					 if(in != null) in.close();
					 if(conn!=null) conn.disconnect(); // 断开连接
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
		}
		
		return jsonString;
	}
	
	
	/**
	 * 
	 * @Title: postJsonString POST
	 * @Description: 返回json串
	 * @param   
	 * @return String 
	 * @throws
	 */
	public static String postJsonString(String ipConfig,String urlAndParam){
		HttpURLConnection conn = null;
		BufferedReader br =  null;
		InputStream in = null;
		OutputStream out = null;
		String jsonString = ""; //返回json串
		String urlStr = getUrl(ipConfig, urlAndParam);
		String url =""; //最终的URL
		String param = ""; //post提交的参数
		if(StringUtils.isBlank(urlStr)){
			logger.error("接口调用失败！ URL is null.");
		}else{
			int idx = StringUtils.indexOf(urlStr, "?");
			if(idx > 0){
				param = StringUtils.substring(urlStr, idx+1);
				url = StringUtils.substring(urlStr,0,idx);
			}else{
				url = urlStr;
			}
			try{
				URL urlObj = new URL(url);
			    conn = (HttpURLConnection)urlObj.openConnection();
				//连接主机的超时时间（单位：毫秒） 
				conn.setConnectTimeout(DEFAULTCONNECTTIMEOUT);  
				//从主机读取数据的超时时间（单位：毫秒）
				conn.setReadTimeout(DEFAULTREADTIMEOUT); 
				//设定请求的方法为"POST"，默认是GET   
				conn.setRequestMethod(SERVLET_POST);
				// 设置是否从httpUrlConnection读入，默认情况下是true;  
				conn.setDoInput(true);
				// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在   
				// http正文内，因此需要设为true, 默认情况下是false;   
				conn.setDoOutput(true);
				// Post请求不能使用缓存，默认使用
				conn.setUseCaches(false);
				// 设置编码
				conn.setRequestProperty("charset", "UTF-8");
				// 设定传送的内容类型是可序列化的java对象   
				// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
				// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
				// 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
				conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");//用于指导实体数据的内容类型 
				conn.setRequestProperty("Content-Length",param.toString());//entity为要传输的数据格式为  title=hello&time=20//可以对该数据编码 
				// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
				conn.setInstanceFollowRedirects(true);
				// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成
				//conn.connect();
				// 获取响应代码
				
				// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，   
				// 所以在开发中不调用上述的connect()也可以)。
				out = conn.getOutputStream();     
				// 向对象输出流写出数据，这些数据将存到内存缓冲区中   
				out.write(param.toString().getBytes("UTF-8")); 
				// 此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
				out.flush();
				
				int code = conn.getResponseCode();
				if (code != 200) {
					logger.error("接口调用失败！链接无响应. " + new ServiceException());
				}
				
		        // 调用HttpURLConnection连接对象的getInputStream()函数,   
				// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。   
				in = conn.getInputStream(); // <===注意，实际发送请求的代码段就在这里
			    br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				jsonString = outRs(br).toString();
			} catch (Exception e) {
				String errInfo = "接口调用失败！请确认接口服务是否正常。";
				logger.error(errInfo + "["+url+"]:" + e.getMessage());
			} finally{
				try {
					 if(br != null) br.close();
					 if(in != null) in.close();
					 if(out != null) out.close(); //关闭流对象
					 if(conn!=null) conn.disconnect(); // 断开连接
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
		}
		
		return jsonString;
		
	}
	
	
	/**
	 * 
	 * @Title: doPost 
	 * @Description: post
	 * @param   
	 * @return JsonDataObject<?> 
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static JsonDataObject<?> doPost(Type type,String ipConfig,String urlAndParam){
		HttpURLConnection conn = null;
		BufferedReader br =  null;
		OutputStream out = null;
		InputStream in = null;
		JsonDataObject<?> jsonDateObj = new JsonDataObject();
		String urlStr = getUrl(ipConfig, urlAndParam);
		String url =""; //最终的URL
		String param = ""; //post提交的参数
		if(StringUtils.isBlank(urlStr)){
			jsonDateObj.setMsg("接口调用失败！ URL is null.");
			jsonDateObj.setStatus("-1");
		}else{
			int idx = StringUtils.indexOf(urlStr, "?");
			if(idx > 0){
				param = StringUtils.substring(urlStr, idx+1);
				url = StringUtils.substring(urlStr,0,idx);
			}else{
				url = urlStr;
			}
			try{
				URL urlObj = new URL(url);
			    conn = (HttpURLConnection)urlObj.openConnection();
				//连接主机的超时时间（单位：毫秒） 
				conn.setConnectTimeout(DEFAULTCONNECTTIMEOUT);  
				//从主机读取数据的超时时间（单位：毫秒）
				conn.setReadTimeout(DEFAULTREADTIMEOUT); 
				//设定请求的方法为"POST"，默认是GET   
				conn.setRequestMethod(SERVLET_POST);
				// 设置是否从httpUrlConnection读入，默认情况下是true;  
				conn.setDoInput(true);
				// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在   
				// http正文内，因此需要设为true, 默认情况下是false;   
				conn.setDoOutput(true);
				// Post请求不能使用缓存，默认使用
				conn.setUseCaches(false);
				// 设置编码
				conn.setRequestProperty("charset", "UTF-8");
				// 设定传送的内容类型是可序列化的java对象   
				// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
				// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
				// 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
				conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");//用于指导实体数据的内容类型
				conn.setRequestProperty("Content-Length",param.toString());//entity为要传输的数据格式为  title=hello&time=20//可以对该数据编码 
				// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
				conn.setInstanceFollowRedirects(true);
				// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成
				//conn.connect();
				// 获取响应代码
				
				// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，   
				// 所以在开发中不调用上述的connect()也可以)。
				out = conn.getOutputStream();     
				// 向对象输出流写出数据，这些数据将存到内存缓冲区中   
				out.write(param.toString().getBytes("UTF-8")); 
				// 此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
				out.flush();
				
				int code = conn.getResponseCode();
				if (code != 200) {
					jsonDateObj.setMsg("接口调用失败！链接无响应. " + new ServiceException());
					jsonDateObj.setStatus("-1");
				}
				
		        // 调用HttpURLConnection连接对象的getInputStream()函数,   
				// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。   
				in = conn.getInputStream(); // <===注意，实际发送请求的代码段就在这里
			    br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				jsonDateObj = new Gson().fromJson(outRs(br).toString(), type);
			} catch (Exception e) {
				String errInfo = "接口调用失败！请确认接口服务是否正常。";
				logger.error(errInfo + "["+url+"]:" + e.getMessage());
				jsonDateObj.setMsg(e.getMessage());
				jsonDateObj.setStatus("-1");
			} finally{
				try {
					 if(br != null) br.close();
					 if(in != null) in.close();
					 if(out != null) out.close(); //关闭流对象
					 if(conn!=null) conn.disconnect(); // 断开连接
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }

		}
		
		
		return jsonDateObj;
		
	}
	
	
	/**
	 * 
	 * @Title: doGet 
	 * @Description: get
	 * @param   
	 * @return JsonDataObject<?> 
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static JsonDataObject<?> doGet(Type type,String ipConfig,String urlAndParam){
		HttpURLConnection conn = null;
		BufferedReader br =  null;
		InputStream in = null;
		JsonDataObject<?> jsonDateObj = new JsonDataObject();
		String url = getUrl(ipConfig, urlAndParam);
		if(StringUtils.isBlank(url)){
			jsonDateObj.setMsg("接口调用失败！ URL is null.");
			jsonDateObj.setStatus("-1");
		}else{
			try{
				URL urlObj = new URL(url);
				conn = (HttpURLConnection)urlObj.openConnection();
				//连接主机的超时时间（单位：毫秒） 
				conn.setConnectTimeout(DEFAULTCONNECTTIMEOUT);  
				//从主机读取数据的超时时间（单位：毫秒）
				conn.setReadTimeout(DEFAULTREADTIMEOUT); 
				//设定请求的方法为"POST"，默认是GET   
				conn.setRequestMethod(SERVLET_GET);
				conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
				// 获取响应代码
				int code = conn.getResponseCode();
				if (code != 200) {
					throw new ServiceException();
				}
				conn.connect();
				in = conn.getInputStream();
				br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				jsonDateObj = new Gson().fromJson(outRs(br).toString(), type);
			} catch (Exception e) {
				String errInfo = "接口调用失败！请确认接口服务是否正常。";
				logger.error(errInfo + "["+url+"]:" + e.getMessage());
				jsonDateObj.setMsg(e.getMessage());
				jsonDateObj.setStatus("-1");
			} finally{
				try {
					 if(br != null) br.close();
					 if(in != null) in.close();
					 if(conn!=null) conn.disconnect(); // 断开连接
				} catch (IOException e) {
					e.printStackTrace();
				}
			 } 
		}
		
		return jsonDateObj;
	}
	
	/**
	 * 
	 * @Title: doPut 
	 * @Description: put
	 * @param   
	 * @return JsonDataObject<?> 
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static JsonDataObject<?> doPut(Type type,String ipConfig,String urlAndParam){
		HttpURLConnection conn = null;
		BufferedReader br =  null;
		OutputStream out = null;
		InputStream in = null;
		JsonDataObject<?> jsonDateObj = new JsonDataObject();
		String url = getUrl(ipConfig, urlAndParam);
		if(StringUtils.isBlank(url)){
			jsonDateObj.setMsg("接口调用失败！ URL is null.");
			jsonDateObj.setStatus("-1");
		}else{
			String param = "";
			int idx = StringUtils.indexOf(url, "?");
			if(idx > 0){
				param = StringUtils.substring(url, idx+1);
			}
			try{
				URL urlObj = new URL(url);
				conn = (HttpURLConnection)urlObj.openConnection();
				//连接主机的超时时间（单位：毫秒） 
				conn.setConnectTimeout(DEFAULTCONNECTTIMEOUT);  
				//从主机读取数据的超时时间（单位：毫秒）
				conn.setReadTimeout(DEFAULTREADTIMEOUT); 
				//设定请求的方法为"POST"，默认是GET   
				conn.setRequestMethod(SERVLET_PUT);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				// 获取响应代码
				int code = conn.getResponseCode();
				if (code != 200) {
					throw new ServiceException();
				}
				out = conn.getOutputStream();     
				out.write(param.toString().getBytes("utf-8"));	
				in = conn.getInputStream();
				br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				jsonDateObj = new Gson().fromJson(outRs(br).toString(), type);
			} catch (Exception e) {
				String errInfo = "接口调用失败！请确认接口服务是否正常。";
				logger.error(errInfo + "["+url+"]:" + e.getMessage());
				jsonDateObj.setMsg(e.getMessage());
				jsonDateObj.setStatus("-1");
			} finally{
				try {
					 if(br != null) br.close();
					 if(in != null) in.close();
					 if(out != null) out.close(); //关闭流对象
					 if(conn!=null) conn.disconnect(); // 断开连接
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
		 }
		return jsonDateObj;
				
	}
	
	
	/**
	 * 
	 * @Title: doDelete 
	 * @Description: 删除
	 * @param   
	 * @return JsonDataObject<?> 
	 * @throws
	 */
	public static JsonDataObject<?> doDelete(Type type,String ipConfig,String urlAndParam){
		HttpURLConnection conn = null;
		BufferedReader br =  null;
		InputStream in = null;
		@SuppressWarnings("rawtypes")
		JsonDataObject<?> jsonDateObj = new JsonDataObject();
		String url = getUrl(ipConfig, urlAndParam);
		if(StringUtils.isBlank(url)){
			jsonDateObj.setMsg("接口调用失败！ URL is null.");
			jsonDateObj.setStatus("-1");
		}else{
			try{
				URL urlObj = new URL(url);
				conn = (HttpURLConnection)urlObj.openConnection();
				//连接主机的超时时间（单位：毫秒） 
				conn.setConnectTimeout(DEFAULTCONNECTTIMEOUT);  
				//从主机读取数据的超时时间（单位：毫秒）
				conn.setReadTimeout(DEFAULTREADTIMEOUT); 
				//设定请求的方法为"POST"，默认是GET   
				conn.setRequestMethod(SERVLET_DELETE);
				conn.setDoOutput(true);
				// 获取响应代码
				int code = conn.getResponseCode();
				if (code != 200) {
					throw new ServiceException();
				}
				in = conn.getInputStream();
				br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				jsonDateObj = new Gson().fromJson(outRs(br).toString(), type);
			} catch (Exception e) {
				String errInfo = "接口调用失败！请确认接口服务是否正常。";
				logger.error(errInfo + "["+ url+"]:" + e.getMessage());
				jsonDateObj.setMsg(e.getMessage());
				jsonDateObj.setStatus("-1");
			} finally{
				try {
					 if(br != null) br.close();
					 if(in != null) in.close();
					 if(conn!=null) conn.disconnect(); // 断开连接
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
		 }
		return jsonDateObj;
	}
	
	
	/**
	 * 
	 * @Title: getUrl 
	 * @Description: 返回url
	 * @param   
	 * @return String 
	 * @throws
	 */
	public static String getUrl(String ipConfig,String urlParam){
		String ip = ScaConfigUtil.getParameter(ipConfig);
		String url= "";
		if(StringUtils.isNotEmpty(ipConfig)){
			if(StringUtils.isEmpty(ip)){
				logger.error("应用的IP地址为空.");
			}else if(StringUtils.isEmpty(urlParam)){
				logger.error("应用的访问参数为空.");
			}else{
				if(urlParam.startsWith("//")){
					url = "http://" + ip + urlParam.substring(1, urlParam.length());
				}else if(urlParam.startsWith("/")){
					url = "http://" + ip + urlParam;
				}else{
					url = "http://" + ip + "/" + urlParam;
				}
			}
		}else{
			logger.error("在 sca-config.xml 中找不到【" + ipConfig + "】的配置参数.");
		}
		
		logger.info("调用的URL为：" + url);
		
		return url;
	}
	
	/**
     * 从输入流中读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }

	
    /**
     * 从输入流中读取数据
     * @param br
     * @return
     * @throws Exception
     */
	protected static StringBuffer outRs(BufferedReader br) throws IOException {
		String line ;
		StringBuffer rtn = new StringBuffer();
		while( (line =br.readLine()) != null ){
			rtn.append(new String(line.getBytes(), "UTF-8"));
		}
		logger.debug("调用结果为:" + rtn.toString());
		return rtn;
	}
	
	
	public static void main(String[] args) throws Exception{
//		String urlStr = "sys/category/queryCode?id=3";
//		java.lang.reflect.Type type = new TypeToken<JsonDataObject<ResCategoryJson>>(){}.getType();
//		ReadHttpResponse.doPost(type,"BSRCM_SM_SCA",urlStr).getMsg();
	}
}

