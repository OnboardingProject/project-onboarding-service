package com.project.onboarding.model;

import java.util.List;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Athira Rajan
 * @description : Structure for storing task details to a project.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	@Transient
    public static final String SEQUENCE_NAME = "Task_Sequence";
	
	private String taskId;
	private String name;
	private String description;
	private List<String> designation;
}
