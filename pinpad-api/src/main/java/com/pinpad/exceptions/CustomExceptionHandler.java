/**
 * 
 */
package com.pinpad.exceptions;

/**
 * @author H P
 *
 */
public class CustomExceptionHandler extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Object data;

	public CustomExceptionHandler(Object data) {
		super();
		this.data = data;
	}

	public CustomExceptionHandler() {
		super();
	}

	public CustomExceptionHandler(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CustomExceptionHandler(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomExceptionHandler(String message) {
		super(message);
	}

	public CustomExceptionHandler(String message, Object data) {
		super(message);
		this.data = data;
	}

	public CustomExceptionHandler(Throwable cause) {
		super(cause);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}