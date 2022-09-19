package com.account.onboarding.exception;

/**
 * This is the custom exception class that throws exception when user does not
 * exists
 * 
 * @author
 *
 */
public class UserNotFoundException extends RuntimeException {

	String msg;

	public UserNotFoundException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
