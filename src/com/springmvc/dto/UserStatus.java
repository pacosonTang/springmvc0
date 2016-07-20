package com.springmvc.dto;

public class UserStatus {
	private String isAvailabe;
	private String name;
	
	public UserStatus(String isAvailabe, String name) {
		this.isAvailabe = isAvailabe;
		this.name = name;
	}
	
	public String getIsAvailabe() {
		return isAvailabe;
	}
	public void setIsAvailabe(String isAvailabe) {
		this.isAvailabe = isAvailabe;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
