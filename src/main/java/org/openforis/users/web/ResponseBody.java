package org.openforis.users.web;

public class ResponseBody {

	private int status = 500;
	private String code;
	private String message;

	public ResponseBody() {
	}

	public ResponseBody(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toJson() {
		return String.format("{\"status\": %d, \"code\": \"%s\", \"message\": \"%s\"}", this.status, this.code, this.message);
	}

}
