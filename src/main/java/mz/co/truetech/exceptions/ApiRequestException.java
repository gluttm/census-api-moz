package mz.co.truetech.exceptions;

import java.io.Serial;

public class ApiRequestException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public ApiRequestException(String msg) {
		super(msg);
	}

	public ApiRequestException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
