package com.account.onboarding.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : API response structure.
 * @date : 12 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePayLoad {
	private List<Object> data;
	private String successMessage;
	private String errorMessage;
}
