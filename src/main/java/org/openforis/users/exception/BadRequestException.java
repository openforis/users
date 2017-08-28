package org.openforis.users.exception;

public class BadRequestException extends Exception {

	private static final long serialVersionUID = 8295452069496600945L;

	public BadRequestException() {
		super("");
	}

	public BadRequestException(String msg) {
		super(msg);
	}

}
