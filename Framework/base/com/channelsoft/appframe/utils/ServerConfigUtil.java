/**
 * 
 */
package com.channelsoft.appframe.utils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * <dl>
 * <dt>USBOSS_ZJ114</dt>
 * <dd>Description:服务器配置表，用户内网IP地址和公网IP地址配置映射关系。</dd>
 * <dd>配置文件使用server_config.xml,如果在classpath下没有配置文件则不做转换。</dd>
 * <dd>所有系统中配置的地址以内网地址为准</dd>
 * <dd>如果配置了代理服务器则优先处理代理服务器，访问用户是从代理服务器访问时转换为公网地址</dd>
 * <dd>如果没有配置代理服务器则处理内网IP地址段，如果匹配上前缀则返回内网地址，否则返回公网地址</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2006-3-31</dd>
 * </dl>
 * 
 * @author 李大鹏
 */
public class ServerConfigUtil {
	private final IpType PUBLIC_IP = new IpType("public-ip"); // 公网IP地址

	private final IpType PRIVATE_IP = new IpType("private-ip"); // 内网IP地址

	/**
	 * 需要转换的IP地址类型，保存的是配置文件中server节点的属性名称。
	 * @author samboy
	 *
	 */
	private class IpType {
		private String type;

		private IpType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public String getIpaddress(Node configNode) {
			if (configNode == null)
				return "";

			Node ipNode = configNode.selectSingleNode('@' + getType());
			if (ipNode == null) {
				return "";
			}  
			if (logger.isDebugEnabled()) {
				logger.debug(getType() + ": " + ipNode.getText());
			}
			return ipNode.getText();
			 
		}
	}

	private final String CONFIG_FILE = "/server_config.xml"; // 配置文件路径

	private final Log logger = LogFactory.getLog(ServerConfigUtil.class);

	private Document configFile = null; // 保存配置文件的DOM对象

	private static ServerConfigUtil instance = new ServerConfigUtil();

	/**
	 * 获取服务器配置实例
	 * @return
	 */
	public static ServerConfigUtil getInstance() {
		return instance;
	}

	/**
	 * 刷新服务器配置数据
	 */
	public static void refresh() {
		instance = new ServerConfigUtil();
	}

	/**
	 * IP地址转换, 将url中的IP地址转换为配置文件中的IP地址。
	 * 根据remoteIp判断，如果remoteIp是配置文件中指定的代理服务器的IP地址，则转换为公网地址。
	 * @param url 需要转换的地址
	 * @param remoteIp 访问url的远程地址
	 * @return 转换后的url字符串
	 */
	public String converUrl(String url, String remoteIp) {
		if (configFile == null)
		{
			logger.debug("没有配置文件[" + CONFIG_FILE + "]不做转换.");
			return url;
		}
		
		if(StringUtils.isEmpty(remoteIp))
		{
			logger.debug("remote ipaddress is null");
			return url;
		}

		IpType ipType = PRIVATE_IP;
		if (isProxyIp(remoteIp) || !isPrivateIp(remoteIp)) {
			ipType = PUBLIC_IP;
		}

		try {
			URL sourceUrl = new URL(url);
			String newIp = getIp(sourceUrl, ipType);
			if (logger.isDebugEnabled())
				logger.debug("new ipaddress is: " + newIp);

			URL descUrl = new URL(sourceUrl.getProtocol(), newIp, sourceUrl
					.getPort(), sourceUrl.getFile());
			if (logger.isDebugEnabled())
				logger.debug("return ipaddress: " + descUrl.toString());

			return descUrl.toString();
		} catch (MalformedURLException exp) {
			logger.error("地址转换异常", exp);
			return url;
		}
	}

	/**
	 * 判断给定的IP地址是否是配置文件中的代理服务器地址
	 * @param ipaddress ip地址
	 * @return true 是代理服务器，false 不是代理服务器
	 */
	public boolean isProxyIp(String ipaddress) {
		if (configFile == null || StringUtils.isEmpty(ipaddress))
			return false;

		Node node = configFile.selectSingleNode("/server-config/proxy[@ip='"
				+ ipaddress + "']");

		if (node == null) {
			if (logger.isDebugEnabled())
				logger.debug("不是代理服务器：" + ipaddress);
			return false;
		}
		if (logger.isDebugEnabled())
			logger.debug("代理服务器地址：" + ipaddress);

		return true;
	}
	
	public boolean isPrivateIp(String ipaddress)
	{
		logger.debug("判断内网IP段：" + ipaddress);
		
		if(configFile == null || StringUtils.isEmpty(ipaddress))
			return false;
		
		List nodeList = configFile.selectNodes("/server-config/private-ip/@prefix");
		
		for(Iterator iter=nodeList.iterator(); iter.hasNext(); )
		{
			Node node = (Node)iter.next();
			if(ipaddress.startsWith(node.getText()))
			{
				logger.debug("内网IP段：" + ipaddress);
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 根据ipType将url中的IP地址转换为配置文件中的地址
	 * @param url
	 * @param ipType
	 * @return
	 */
	public String getIp(URL url, IpType ipType) {
		logger.debug("转换IP地址：ipaddress=" + url.getHost());

		if (configFile == null) {
			logger.debug("没有配置文件，不做IP地址转换.");
			return url.getHost();
		}

		Node node = configFile.selectSingleNode("/server-config/server[@private-ip='"
				+ url.getHost() + "']");
		if (node == null) {
			logger.debug("没有配置IP地址，不做转换.");
			return url.getHost();
		}
		String ipaddress = ipType.getIpaddress(node);
		logger.debug("IP地址转换成功：" + url.getHost() + "=>" + ipaddress);
		return ipaddress;
	}

	/**
	 * 私有构造函数，装载配置文件
	 *
	 */
	private ServerConfigUtil() {
		SAXReader reader = new SAXReader();
		InputStream input = ServerConfigUtil.class
				.getResourceAsStream(CONFIG_FILE);
		try {
			configFile = reader.read(input);
		} catch (DocumentException exp) {
			logger.error("Load " + CONFIG_FILE + " error." + exp);
			configFile = null;
		}
	}
}
