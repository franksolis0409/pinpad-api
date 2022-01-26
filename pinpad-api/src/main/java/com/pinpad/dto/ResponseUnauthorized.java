/**
 * 
 */
package com.pinpad.dto;

import java.io.Serializable;

import javax.ws.rs.core.Response.Status;

/**
 * @author H P
 *
 */
public class ResponseUnauthorized implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;
	private boolean success;
	private String message;
	private Object data;

	public ResponseUnauthorized(String message, Object data) {
		super();
		this.code = Status.UNAUTHORIZED.getStatusCode();
		this.success = false;
		this.message = message;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}