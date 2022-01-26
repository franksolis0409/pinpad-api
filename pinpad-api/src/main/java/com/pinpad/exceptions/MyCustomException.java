package com.pinpad.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author H P
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyCustomException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	private int code;
	private boolean success;
	private String message;
	private Object data;

    public MyCustomException(boolean success, String message, String reason, int code, Object data) {
        super(message);
        this.success = success;
        this.message = reason;
        this.code = code;
        this.data = data;  
    }

}
