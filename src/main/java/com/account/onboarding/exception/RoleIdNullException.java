package com.account.onboarding.exception;

/**
 * This is the custom exception class which throws exception when invalid roleId
 * is given
 * 
 * @author
 *
 */
@SuppressWarnings("serial")
public class RoleIdNullException extends RuntimeException {

	String msg;

	public RoleIdNullException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
