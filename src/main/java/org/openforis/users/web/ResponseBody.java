package org.openforis.users.web;

import com.google.gson.JsonObject;

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

	public ResponseBody(int status) {
		this.status = status;
		this.code = "";
		this.message = "";
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
		JsonObject json = new JsonObject();
		json.addProperty("status", this.status);
		json.addProperty("code", this.code);
		json.addProperty("message", this.message);
		return json.toString();
	}

}
