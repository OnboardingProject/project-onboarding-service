package com.account.onboarding.exception;

@SuppressWarnings("serial")
public class NoResourceFoundException extends RuntimeException {

	String msg;

	public NoResourceFoundException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
