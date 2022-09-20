package com.account.onboarding.exception;

@SuppressWarnings("serial")
public class DataNotFoundException extends RuntimeException {

	private String errorMessage;

	public DataNotFoundException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
