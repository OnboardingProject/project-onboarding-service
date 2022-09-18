package com.account.onboarding.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Athira Rajan
 * @description : Structure for storing Task status and it's Tasks details to
 *              the User entity.
 * @date : 08 August 2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusRequest {
	private Integer taskId;
	private String taskStatus;
}
