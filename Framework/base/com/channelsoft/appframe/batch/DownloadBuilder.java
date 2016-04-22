package com.channelsoft.appframe.batch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;

import com.channelsoft.appframe.exception.ServiceException;

/**
 * 批量导出的模版类，通过CallBack的方式，可以提高效率，适合大数据量导出。 在写文件的环节，通过增量写文件，减少对缓存的占用
 * 
 * @author liwei
 * 
 */
public class DownloadBuilder {
	protected transient final Log logger = LogFactory.getLog(getClass());

	private JdbcTemplate jdbcTemplate;

	/**
	 * 输出文件名称
	 */
	private String fileName;

	/**
	 * 查询数据库的SQL语句
	 */
	private String sql;

	/**
	 * 查询数据库的SQL语句的参数列表
	 */
	private List params;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List getParams() {
		return params;
	}

	public void setParams(List params) {
		this.params = params;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// 获得导出数据的行数
	private int size;

	public void setExportSize(int size) {
		this.size = size;
	}

	public int getExportSize() {
		return size;
	}
	public void process(IDownloadable downloadable) {
		if (logger.isDebugEnabled()) {
			logger.debug("开始处理批量导出:sql=" + sql);
		}
		CallbackHandler handler = new CallbackHandler(fileName, downloadable);		
		if (params == null) {
			jdbcTemplate.query(sql, handler);
		} else {
			Object[] ojbs = new Object[params.size()];
			jdbcTemplate.query(sql,  params.toArray(ojbs), handler);
		}
		setExportSize(handler.getRowCount());
		handler.flush();
		if (logger.isDebugEnabled()) {
			logger.debug("完成处理批量导出");
		}
	} 
}
/**
 * 回叫对象，负责处理每行数据
 * @author liwei
 *
 */
class CallbackHandler extends RowCountCallbackHandler {
	// 每100条记录存一次盘
	private static final int BUFFER_SIZE = 100;

	protected transient final Log logger = LogFactory.getLog(getClass()); 
	private int bufferSize = BUFFER_SIZE; // give a default value?

	private StringBuffer buffer;

	private File file;

	private IDownloadable downloadable;

	public CallbackHandler(String localFolderName, IDownloadable downloadable) {
		super();
		file = new File(localFolderName);
		this.downloadable = downloadable;
		buffer = new StringBuffer();

	}

	public void processRow(ResultSet rs, int rowCount) {
		// 存储到缓存
		try {
			saveResults2Buffer(rs, rowCount);
		} catch (SQLException sqle) {
			logger.warn(sqle.getMessage());
			try {
				throw new ServiceException("当读取结果集时发生错误", sqle);
			} catch (ServiceException e) {
				logger.warn(e.getMessage());
			}
		}
		if (rowCount % bufferSize == 0) {
			// 存储到文件
			try {
				saveBuffer2File();
			} catch (IOException e) {
				logger.warn(e.getMessage());
				try {
					throw new ServiceException("当写入到临时文件时发生错误", e);
				} catch (ServiceException e1) {
					logger.warn(e1.getMessage());
				}
			}
		}
	}

	public void flush() {
		// 必须由本类的client调用，用于确保缓存都保存到了文件中
		try {
			saveBuffer2File();
		} catch (IOException e) {
			logger.warn(e.getMessage());
			try {
				throw new ServiceException("当写入到临时文件时发生错误", e);
			} catch (ServiceException e1) {
				logger.warn(e1.getMessage());
			}
		}
	}

	private void saveBuffer2File() throws IOException {
		// 保存buffer到文件
		FileWriter writer = new FileWriter(file, true);
		try {
			writer.write(buffer.toString());
		} finally {
			writer.close();
		}
		// 清空buffer
		buffer = new StringBuffer();
	}

	private void saveResults2Buffer(ResultSet rs, int rowCount)
			throws SQLException {
		// 调用者实现的接口
		downloadable.processRow(rs, rowCount, buffer);
		buffer.append(SystemUtils.LINE_SEPARATOR);
	}

}
