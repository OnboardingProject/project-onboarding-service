package com.project.onboarding.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sheeba VR
 * @description : Request for delete a task in a project
 * @date : 09 September 2022
 */

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTaskRequest {
	@NotEmpty(message = "projectId is a mandatory field")
	private String projectId;
	
	@NotEmpty(message = "No tasks selected for deletion")
	private List<Integer> taskIdList;
}
