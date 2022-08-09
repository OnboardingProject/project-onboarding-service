package com.project.onboarding.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Structure for storing Project and it's Tasks details to the User entity.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTaskDetails {
	private String projectId;
	private List<TaskDetails> tasks;
}
