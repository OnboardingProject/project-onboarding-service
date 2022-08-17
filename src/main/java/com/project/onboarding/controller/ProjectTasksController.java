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
import com.project.onboarding.model.Task;
import com.project.onboarding.service.ProjectTaskService;



@RestController
@RequestMapping("/project_task")
public class ProjectTasksController {

	@Autowired
	ProjectTaskService projecTaskService;

	private static final Logger logger = LoggerFactory.getLogger(ProjectTasksController.class);

	/**
	 * Description : API for fetch task details based on project
	 * 
	 * @Param : projectId
	 * 
	 * @Return: List of Task object
	 * 
	 */
	@GetMapping("/fetch_project_task/{projectId}")
	public ResponseEntity<TaskPayload> getAllTaskByProject(@PathVariable String projectId) {
		try {
			logger.info("Started project task fetch api method");
			List<Task> tasksList = projecTaskService.getProjectTasksByProjectId(projectId);
			logger.info("Return the selected project task details");

			return new ResponseEntity<TaskPayload>(new TaskPayload(tasksList, ProjectOnboardingConstant.SUCCESS, ""),
					HttpStatus.OK);
		} catch (ProjectOnboardingException projectOnboardingException) {
			logger.error("Throw project not found exception");
			return new ResponseEntity<TaskPayload>(
					new TaskPayload(null, "", ProjectOnboardingConstant.PROJECTIDNOTFOUND), HttpStatus.NOT_FOUND);
		}
	}

}
