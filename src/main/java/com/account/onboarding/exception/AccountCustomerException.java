package com.account.onboarding.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Exception to deal with account overview and customer.
 * 
 * @author 226732
 */

@Getter
public class AccountCustomerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public AccountCustomerException(String errorMessage, HttpStatus httpStatus) {
		super();
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}

}
