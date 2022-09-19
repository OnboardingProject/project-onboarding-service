package com.account.onboarding.exception;

import java.time.LocalDateTime;

/**
 * This is the error informations that we give
 * when custom exceptions throw
 * @author 
 *
 */
public class ErrorInfo {
	private String errorMessage; 
    private LocalDateTime time;

    public ErrorInfo() {
        super();
    }

    public ErrorInfo(String errorMessage, LocalDateTime time) {
        super();
        this.errorMessage = errorMessage;
        this.time = time;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getTime() {
        return time;
    }

    
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}
