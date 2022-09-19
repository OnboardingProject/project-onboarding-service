package com.account.onboarding.exception;

/**
 * This is the custom exception that used when user updating is not happened
 * 
 * @author
 *
 */
public class UserUpdateException extends RuntimeException {

	String msg;

	public UserUpdateException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}