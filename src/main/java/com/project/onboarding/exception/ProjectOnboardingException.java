package com.project.onboarding.exception;

import org.springframework.http.HttpStatus;
/**
 * @author UST
 * @description : Exception class.
 * @date : 08 August 2022
 */
public class ProjectOnboardingException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private final String errorMessage;
	private final HttpStatus httpStatus;
		
	public ProjectOnboardingException() {
		this.errorMessage="";
		this.httpStatus=null;
	}
	public ProjectOnboardingException(String errorMessage, HttpStatus httpStatus) 
	{
		super();
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	

}


