package com.project.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.response.ResponsePayLoad;
import com.project.onboarding.service.ProjectTasksService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sheeba VR
 * @description : Controller class for fetch the task details based on project.
 * @date : 10 August 2022
 */

@Slf4j
@RestController
@RequestMapping("api/v1/project-tasks")
public class ProjectTasksController {

	@Autowired
	ProjectTasksService projecTaskService;

	/**
	 * Description : API for fetch task details based on project
	 * 
	 * @Param : projectId
	 * @Return: List of Task object
	 * 
	 */
	@GetMapping("/fetch-project-tasks/{projectId}")
	public ResponseEntity<ResponsePayLoad> getAllTaskByProject(@PathVariable String projectId) {
		try {
			log.info("Started project task fetch api method");
			List<Object> tasksList = new ArrayList<Object>();
			tasksList.addAll(projecTaskService.getProjectTasksByProjectId(projectId));

			log.info("Return the selected project task details");

			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(tasksList, ProjectOnboardingConstant.TASKLIST_FETCH_SUCCESS, ""),
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
}