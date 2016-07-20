package com.springmvc.pojo;

public class MyTextMessage {
    private String touser;
    private String text;
    private String curuser;
    
	public MyTextMessage(String text, String touser, String curuser) {
		this.text = text;
		this.touser = touser;
		this.curuser = curuser;
	}

	public String getCuruser() {
		return curuser;
	}

	public void setCuruser(String curuser) {
		this.curuser = curuser;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}
}
