package com.account.onboarding.exception;

/**
 * This is the custom exception class to throw an exception when email is not
 * valid
 * 
 * @author
 *
 */
public class EmailNotValidException extends RuntimeException {

	String msg;

	public EmailNotValidException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}