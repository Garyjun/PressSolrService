/*
 * Author 张乐雷  created July 23, 2006
 * 
 * changed by daiy 2006-08-18 将类名修改为当前名称
 */

package com.channelsoft.appframe.batch;

public class CfgBatchOperationConstants {
	/*
	 * 批量操作类型
	 */
	public static final String BATCH_TYPE_IMPORT = "1";//批量导入

	public static final String BATCH_TYPE_DELETE = "2";//批量删除
	
	public static final String BATCH_TYPE_EXPORT = "3";//批量导出
	
	public static final String BATCH_TYPE_UPDATE = "4";//批量更新,add by liweilu on 2006.08.29

	
	/*
	 * 批量操作状态，包括批量导入、批量导出、批量删除
	 */
	public static final String BATCH_STATUS_SUCCESS = "1"; // 操作的全部数据都成功的导入或导出

	public static final String BATCH_STATUS_FAIL = "2"; // 操作的全部数据都没有成功的导入或导出

	public static final String BATCH_STATUS_PART_SUCCESS = "3"; // 操作的部分数据已经成功的导入或导出
	
	/*
	 * 批量操作中，存放上传文件和导出文件的文件夹名称
	 */
	public static final String BATCH_IMPORT_FILE_FOLDER_NAME = "BatchImportFolder";  // 批量导入时，存放源文件的文件夹名称

	public static final String BATCH_IMPORT_ERR_FILE_FOLDER_NAME = "BatchImportErrFolder"; // 批量导入时，存放错误文件的文件夹名称

	public static final String BATCH_EXPORT_FILE_FOLDER_NAME= "BatchExportFolder"; // 批量导出时， 存放导出文件夹的名称

}
