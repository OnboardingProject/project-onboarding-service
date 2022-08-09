package com.project.onboarding.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Structure of the Account Document details to store in user entity.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDocument {
	private String id;
	private String status;
}
