package com.project.onboarding.controller;

/**
 * @author UST
 * @description : Pay_load class for displaying the task details
 * @date : 08 August 2022
 */

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.project.onboarding.model.Task;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskPayload {

	private List<Task> taskList = new ArrayList<Task>();
	private String successMessage;
	private String errorMessage;

}

