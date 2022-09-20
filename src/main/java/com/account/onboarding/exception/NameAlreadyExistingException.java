package com.account.onboarding.exception;

/**
 * Custom Exception class for Project creation with same name
 * 
 * @author jeena
 *
 */
public class NameAlreadyExistingException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String msg;

	public NameAlreadyExistingException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
