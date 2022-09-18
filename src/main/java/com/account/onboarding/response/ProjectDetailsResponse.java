package com.account.onboarding.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Amrutha Joseph
 * @description : API response structure for project details.
 * @date : 17 August 2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectDetailsResponse {
	private String projectId;
	private String projectName;
}
