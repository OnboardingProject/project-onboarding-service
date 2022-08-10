package com.project.onboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectOnboardingException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String errorMessage;
}
