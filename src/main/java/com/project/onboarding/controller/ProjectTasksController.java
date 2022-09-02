package com.project.onboarding.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.DeleteTaskRequest;
import com.project.onboarding.model.Task;
import com.project.onboarding.service.ProjectTaskService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;


/**
 * @author UST
 * @description : Controller class for fetch the task details based on project.
 * @date : 10 August 2022
 */
@Slf4j
@RestController
@RequestMapping("/projectTask")
public class ProjectTasksController {

	@Autowired
	ProjectTaskService projecTaskService;
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectTasksController.class);

	/**
	 * Description : API for fetch task details based on project
	 * @Param : projectId
	 * @Return: List of Task object
	 * 
	 */
	@GetMapping("/fetchProjectTask/{projectId}")
	public ResponseEntity<TaskPayload> getAllTaskByProject(@PathVariable String projectId) {
		try {
			logger.info("Started project task fetch api method");
			List<Task> tasksList = projecTaskService.getProjectTasksByProjectId(projectId);
			logger.info("Return the selected project task details");

			return new ResponseEntity<TaskPayload>(new TaskPayload(tasksList, ProjectOnboardingConstant.SUCCESSTASKFETCH, ""),
					HttpStatus.OK);
		} catch (ProjectOnboardingException projectOnboardingException) {
			logger.error("Project not found exception");
			return new ResponseEntity<TaskPayload>(
					new TaskPayload(null, "",  ProjectOnboardingConstant.PROJECTIDNOTFOUND), HttpStatus.CONFLICT);
		}
		catch(Exception exception)
		{
			logger.error("Fetch tasks Failed");
			return new ResponseEntity<TaskPayload>(
					new TaskPayload(null, "",exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	/**
	 * Description : API for delete the task based on project
	 * @Param : DeleteTaskRequest
	 * @Return: List of Task object
	 * 
	 */
	@GetMapping("/deleteProjectTask")
	public ResponseEntity<TaskPayload> deleteTaskByProject(@RequestBody DeleteTaskRequest deleteTaskRequest) {
		try {
			logger.info("Started project task delete api method");
				List<Task> tasksList = projecTaskService.deleteTask(deleteTaskRequest);
			logger.info("Return the selected project task details");

				return new ResponseEntity<TaskPayload>(new TaskPayload(tasksList, ProjectOnboardingConstant.DELETESUCCESS, ""),
					HttpStatus.OK);
		}
		catch (ProjectOnboardingException projectOnboardingException) {
			logger.error("Throw project not found exception");
			return new ResponseEntity<TaskPayload>(
					new TaskPayload(null, "", projectOnboardingException.getErrorMessage()), HttpStatus.CONFLICT);
		}
		catch(Exception exception)
		{
			logger.error("Deletion Failed");
			return new ResponseEntity<TaskPayload>(
					new TaskPayload(null, "",exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
