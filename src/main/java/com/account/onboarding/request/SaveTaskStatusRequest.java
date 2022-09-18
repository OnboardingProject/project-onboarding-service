package com.account.onboarding.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Athira Rajan
 * @description : Structure for storing task status and it's Tasks details to
 *              the User entity.
 * @date : 08 August 2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveTaskStatusRequest {
	private String projectId;
	private String userId;
	private List<TaskStatusRequest> taskStatusRequest;
}
