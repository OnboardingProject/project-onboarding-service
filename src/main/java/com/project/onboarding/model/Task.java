package com.project.onboarding.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Structure for storing task details to a project.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	private String taskId;
	private String taskName;
	private String taskDescription;
	private List<String> taskDesignation;
}
