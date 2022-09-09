package com.project.onboarding.request;

import com.project.onboarding.model.Task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Uthara P Unni
 * @description : A Task of a project to store in user entity.
 * @date : 10 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTaskRequest {
	private String projectId;
	private Task task;
}
