package com.account.onboarding.exception;

/**
 * This is the custom exception class that throws when user gives same roleId to
 * update
 * 
 * @author
 *
 */
public class RoleAlreadySameException extends RuntimeException {

	String msg;

	public RoleAlreadySameException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
