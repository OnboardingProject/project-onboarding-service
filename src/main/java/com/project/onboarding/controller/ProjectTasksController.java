package com.project.onboarding.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectTaskRequest;
import com.project.onboarding.service.ProjectOnboardingService;

@RestController
@RequestMapping("/project_task")
public class ProjectTasksController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectTasksController.class);

	@Autowired
	ProjectOnboardingService projectOnboardingService;

	@PostMapping("/addTask")

	public ResponseEntity<Project> addTask(@RequestBody ProjectTaskRequest projectTaskRequest) {
		logger.info("In Add Task controller");
		try {
			Project newProject = projectOnboardingService.addTask(projectTaskRequest);
			logger.info("Task is added successsfully");
			return new ResponseEntity<Project>(newProject, HttpStatus.CREATED);
		} catch (ProjectOnboardingException ex) {
			logger.warn("Exception while add/edit task");
			return new ResponseEntity<Project>(HttpStatus.NOT_FOUND);
		}
	}
}