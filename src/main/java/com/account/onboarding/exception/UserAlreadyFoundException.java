package com.account.onboarding.exception;

@SuppressWarnings("serial")
public class UserAlreadyFoundException extends RuntimeException {

	String msg;

	public UserAlreadyFoundException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
