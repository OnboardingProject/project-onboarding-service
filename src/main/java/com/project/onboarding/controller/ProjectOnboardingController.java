package com.project.onboarding.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.SaveTaskStatusRequest;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.service.ProjectOnboardingService;

/**
 * @author Athira Rajan
 * @description : Controller for Project Onboarding module
 * @date : 10 August 2022
 */

@RestController
@RequestMapping
public class ProjectOnboardingController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectOnboardingController.class);
	@Autowired
	ProjectOnboardingService onboardingService;
	
	/**
	 * @param projectId, resourceId
	 * @return ResponseEntity<TaskDetails>
	 * @description : Show Task List associated with Project and Resource 
	 */
	
	
	@GetMapping("/viewTasks/{projectId}/{resourceId}")
	public ResponseEntity<TaskListPayload> getAllTasks(@PathVariable String projectId,@PathVariable String resourceId) {
		try {
			logger.info("In fetch Task controller");
			List<TaskDetails> getAllTasks = onboardingService.fetchTaskList(projectId,resourceId);
			return new ResponseEntity<TaskListPayload>(new TaskListPayload(getAllTasks, ProjectOnboardingConstant.TASKLIST_FETCH_SUCCESS, ""),
					HttpStatus.OK);
		}  catch (ProjectOnboardingException projectOnboardingException) {
			logger.warn("Exception while fetching Task List");
			return new ResponseEntity<TaskListPayload>(
					new TaskListPayload(null, "", ProjectOnboardingConstant.TASKLIST_NOT_FOUND), HttpStatus.CONFLICT);
		}
	}
	
	/**
	 * @param projectId, userId, taskId, taskStatus
	 * @return ResponseEntity<TaskDetails>
	 * @description : Save Task Status based on User and project Tasks.
	 */
	
	@PutMapping("/saveTaskStatus")
	public ResponseEntity<?> saveTaskStatus(@RequestBody SaveTaskStatusRequest saveTaskStatusRequest ){
		try {
			logger.info("In save status controller");
			List<TaskDetails> saveTaskStatus = onboardingService.saveStatus(saveTaskStatusRequest);
			return new ResponseEntity<TaskListPayload>(new TaskListPayload(saveTaskStatus, ProjectOnboardingConstant.TASKSTATUS_SAVE_SUCCESS, ""),
					HttpStatus.OK);
		} catch (ProjectOnboardingException projectOnboardingException) {
			logger.warn("Exception while saving task status");
			return new ResponseEntity<TaskListPayload>(
					new TaskListPayload(null, "", ProjectOnboardingConstant.TASK_NOT_FOUND), HttpStatus.CONFLICT);
		}
		
	}
}

