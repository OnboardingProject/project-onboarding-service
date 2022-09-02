package com.project.onboarding.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Structure for showing user task details in preview report functionality.
 * @date : 12 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTasksOverview {
	private String userId;
	private String userName;
	private double taskPercentage;
	
}
