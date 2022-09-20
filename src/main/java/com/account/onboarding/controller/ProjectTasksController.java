package com.account.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.request.DeleteTaskRequest;
import com.account.onboarding.request.ProjectTaskRequest;
import com.account.onboarding.response.ResponsePayLoad;
import com.account.onboarding.service.impl.ProjectTasksService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sheeba VR
 * @description : Controller class for fetch the task details based on project.
 * @date : 10 August 2022
 */

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1/project-tasks")
public class ProjectTasksController {

	@Autowired
	ProjectTasksService projectTasksService;

	/**
	 * Description : API for fetch task details based on project
	 * 
	 * @Param : projectId
	 * @Return: List of Task object
	 * 
	 */
	@GetMapping("/fetch-project-tasks/{projectId}")
	@Operation(summary = "Get all tasks for a project", description = "This API is used to get all tasks of a project")
	public ResponseEntity<ResponsePayLoad> getAllTaskByProject(@PathVariable String projectId) {
		try {
			log.info("Started project task fetch api method");
			List<Object> tasksList = new ArrayList<Object>();
			tasksList.addAll(projectTasksService.getProjectTasksByProjectId(projectId));

			log.info("Return the selected project task details");

			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(tasksList, ProjectOnboardingConstant.API_TASK_LIST_FETCH_SUCCESS, ""),
					HttpStatus.OK);

		} catch (ProjectOnboardingException projectOnboardingException) {
			log.error("Project not found exception");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(null, "", projectOnboardingException.getErrorMessage()), HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.error("Fetch tasks Failed");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", exception.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Description : API for Add/Edit task of a project
	 * 
	 * @Param : projectTaskRequest
	 * @Return: Project
	 */
	@PostMapping("/add-or-edit-task")
	@Operation(summary = "Add/Edit a task inside a project", description = "This API is used to add/edit a task in a project")
	public ResponseEntity<ResponsePayLoad> addOrEditTask(@RequestBody ProjectTaskRequest projectTaskRequest) {
		log.info("In Add or Edit Task controller");
		try {
			List<Object> newProject = new ArrayList<Object>();
			newProject.add(projectTasksService.addOrEditTask(projectTaskRequest));

			log.info("Task is added or edited successsfully");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(newProject, "SucessFully Tasks Added", " "),
					HttpStatus.CREATED);
		} catch (ProjectOnboardingException projectOnboardingException) {
			log.warn("Exception while add/edit task");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(null, "", projectOnboardingException.getErrorMessage()), HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			log.error("Add/Edit tasks Failed");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", exception.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Description : API for delete the task based on project
	 * 
	 * @Param : DeleteTaskRequest
	 * @Return: List of Task object
	 * 
	 */
	@PutMapping("/delete-task")
	@Operation(summary = "Delete list of tasks in a project", description = "This API is used to delete list of tasks in a project")
	public ResponseEntity<ResponsePayLoad> deleteTaskByProject(
			@Valid @RequestBody DeleteTaskRequest deleteTaskRequest) {
		try {
			log.info("Started project task delete api method");
			List<Object> tasksList = new ArrayList<Object>();
			tasksList.add(projectTasksService.deleteTask(deleteTaskRequest));

			log.info("Return the selected project task details");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(tasksList, ProjectOnboardingConstant.API_DELETE_TASKS_SUCCESS, ""),
					HttpStatus.OK);
		} catch (ProjectOnboardingException projectOnboardingException) {
			log.error("Throw Exception");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(null, "", projectOnboardingException.getErrorMessage()), HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.error("Deletion Failed");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", exception.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
