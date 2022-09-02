package com.project.onboarding.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * @author Athira Rajan
 * @description : Structure for storing task status and it's Tasks details to the User entity.
 * @date : 08 August 2022
 */
@Getter
public class SaveTaskStatusRequest {
	private String projectId;
	private String userId;
	private List<TaskStatusRequest> taskStatusRequest;
}
