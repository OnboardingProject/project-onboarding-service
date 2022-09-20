package com.account.onboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Exception class for project onboarding module
 * @date : 10 August 2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectOnboardingException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String errorMessage;
}
