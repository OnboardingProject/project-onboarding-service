package com.account.onboarding.exception;

/**
 * This is the custom exception that throws when phone number is not valid
 * 
 * @author
 *
 */
public class PhoneNumberNotValidException extends RuntimeException {
	String msg;

	public PhoneNumberNotValidException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}