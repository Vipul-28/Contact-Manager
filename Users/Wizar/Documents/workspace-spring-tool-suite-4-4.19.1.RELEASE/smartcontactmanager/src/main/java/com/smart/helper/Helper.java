package com.smart.helper;

public class Helper {

	public String content;
	public String type;
	public String getMessage() {
		return content;
	}
	public void setMessage(String message) {
		this.content = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Helper(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	
}
