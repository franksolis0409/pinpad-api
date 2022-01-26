package com.pinpad.exceptions;

public class ApiErrorResponse {

	private Integer code;
	private boolean success;
	private String message;
	private Object errorData;

	public ApiErrorResponse() {
		super();
	}

	public ApiErrorResponse(Integer code, boolean success, String message, Object errorData) {
		super();
		this.code = code;
		this.success = success;
		this.message = message;
		this.errorData = errorData;
	}

	public int getCode() {
		return code;
	}

	public void setCode(Integer code) {
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

	public Object getErrorData() {
		return errorData;
	}

	public void setErrorData(Object data) {
		this.errorData = data;
	}

}