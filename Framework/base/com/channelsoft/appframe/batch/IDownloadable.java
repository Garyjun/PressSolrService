package com.channelsoft.appframe.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 需要文件下载时需要实现此接口。
 * @author zhengwei
 *
 */
public interface IDownloadable {
	public void processRow(ResultSet rs, int rowCount, StringBuffer buffer) throws SQLException;
}
