package com.project.onboarding.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.Task;
import com.project.onboarding.service.ProjectOnboardingService;
import com.project.onboarding.service.ProjectTaskService;

@RestController
@RequestMapping("/project_task")
public class ProjectTasksController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectTasksController.class);

	@Autowired
	ProjectTaskService projecTaskService;
	
	@GetMapping("/fetch_project_task")
	
	public ResponseEntity<?> getAllTaskByProject(@PathVariable String projectId){
	try{
		List<Task> tasksList=projecTaskService.getProjectTasksByProjectId(projectId);
	    return new ResponseEntity<List<Task>>(tasksList,HttpStatus.FOUND);
       }
	catch(ProjectOnboardingException projectOnboardingException)
	{
		return new ResponseEntity<String>(ProjectOnboardingConstant.PROJECTIDNOTFOUND, HttpStatus.NOT_FOUND);
	}
	}
	
}
