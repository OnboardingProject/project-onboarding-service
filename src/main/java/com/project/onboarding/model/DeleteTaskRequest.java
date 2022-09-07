package com.project.onboarding.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author UST
 * @description : Entity class for storing project details.
 * @date : 24 August 2022
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTaskRequest {
	@NotNull(message="ProjectId cannot be blank")
	private String projectId;
	@NotEmpty(message="No tasks sletced for deletion")
	List<Integer> taskIdList;

}