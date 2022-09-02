package com.project.onboarding.model;

import lombok.Getter;
/**
 * @author Athira Rajan
 * @description : Structure for storing Task status and it's Tasks details to the User entity.
 * @date : 08 August 2022
 */
@Getter
public class TaskStatusRequest {
	private Integer taskId;
	private String taskStatus;
}
