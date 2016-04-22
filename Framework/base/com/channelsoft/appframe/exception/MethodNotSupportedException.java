/**
 * 
 */
package com.channelsoft.appframe.exception;

/**
 * 运行期异常，表示该方法不支持，调用失败
 * @author liwei
 *
 */
public class MethodNotSupportedException extends BaseRuntimeException {

	/**
	 * 
	 */
	public MethodNotSupportedException() {
		super();
	}

	/**
	 * @param message
	 */
	public MethodNotSupportedException(String message) {
		super(message);
	}
 

}
