package com.account.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.request.SaveTaskStatusRequest;
import com.account.onboarding.response.ResponsePayLoad;
import com.account.onboarding.service.impl.ProjectOnboardingService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Amrutha Joseph
 * @description controller class for project onboarding
 * @created_Date 17/08/2022
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/project-onboarding")
public class ProjectOnboardingController {

	@Autowired
	ProjectOnboardingService projectOnboardingService;

	/**
	 * @param userId
	 * @return ResponseEntity<List<ProjectDetails>>
	 * @description : Fetch all projects based on user
	 */

	@GetMapping("/projects/{userId}")
	@Operation(summary = "Get Projects for a user", description = "This API is used to get projects of a user")
	public ResponseEntity<ResponsePayLoad> getProjects(@PathVariable String userId) {
		try {
			log.info("Inside project onboarding controller getProjects try block");
			List<Object> projects = new ArrayList<Object>();
			projects.addAll(projectOnboardingService.getProjectsBasedOnUser(userId));

			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(projects, ProjectOnboardingConstant.API_GET_PROJECTS_SUCCESS, ""),
					HttpStatus.FOUND);
		} catch (ProjectOnboardingException ex) {
			log.error("Inside project onboarding controller getProjects ProjectOnboardingException catch block");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getErrorMessage()),
					HttpStatus.CONFLICT);
		} catch (Exception ex) {
			log.error("Inside project onboarding controller getProjects Exception catch block");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * @param projectId
	 * @return ResponseEntity<List<UserDetails>>
	 * @description : Fetch all resources based on project
	 */

	@GetMapping("/resources/{projectId}")
	@Operation(summary = "Get users of a project", description = "This API is used to get users of a project")
	public ResponseEntity<ResponsePayLoad> getUsers(@PathVariable String projectId) {
		try {

			log.info("Inside project onboarding controller getUsers try block");
			List<Object> users = new ArrayList<Object>();
			users.addAll(projectOnboardingService.getUsersBasedOnProject(projectId));

			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(users, ProjectOnboardingConstant.API_GET_RESOURCES_SUCCESS, ""),
					HttpStatus.FOUND);

		} catch (ProjectOnboardingException ex) {
			log.error("Inside project onboarding controller getUsers ProjectOnboardingException catch block");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getErrorMessage()),
					HttpStatus.CONFLICT);
		} catch (Exception ex) {
			log.error("Inside project onboarding controller getUsers Exception catch block");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @param projectId, resourceId
	 * @return ResponseEntity<TaskDetails>
	 * @description : Show Task List associated with Project and Resource
	 */

	@GetMapping("/view-tasks/{projectId}/{resourceId}")
	@Operation(summary = "Get all tasks of a user for a project", description = "This API is used to get all tasks of a user for a project")
	public ResponseEntity<ResponsePayLoad> getAllTasks(@PathVariable String projectId,
			@PathVariable String resourceId) {
		try {
			log.info("In fetch Task controller");

			List<Object> allTasks = new ArrayList<Object>();
			allTasks.addAll(projectOnboardingService.fetchTaskList(projectId, resourceId));

			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(allTasks, ProjectOnboardingConstant.API_TASK_LIST_FETCH_SUCCESS, ""),
					HttpStatus.OK);

		} catch (ProjectOnboardingException projectOnboardingException) {
			log.warn("Exception while fetching Task List");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(null, "", projectOnboardingException.getErrorMessage()), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			log.error("Inside project onboarding controller getAllTasks Exception catch block");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @param projectId, userId, taskId, taskStatus
	 * @return ResponseEntity<TaskDetails>
	 * @description : Save Task Status based on User and project Tasks.
	 */

	@PutMapping("/save-task-status")
	@Operation(summary = "Save list of task statuses for a user in a project", description = "This API is used to save list of task statuses for a user in a project")
	public ResponseEntity<ResponsePayLoad> saveTaskStatus(@RequestBody SaveTaskStatusRequest saveTaskStatusRequest) {
		try {
			log.info("In save status controller");

			List<Object> taskDetailsList = new ArrayList<Object>();
			taskDetailsList.addAll(projectOnboardingService.saveStatus(saveTaskStatusRequest));

			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(taskDetailsList, ProjectOnboardingConstant.API_TASK_STATUS_SAVE_SUCCESS, ""),
					HttpStatus.OK);
		} catch (ProjectOnboardingException projectOnboardingException) {
			log.warn("Exception while saving task status");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(null, "", projectOnboardingException.getErrorMessage()), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			log.error("Inside project onboarding controller saveTaskStatus Exception catch block");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Description : API for fetch task status
	 * 
	 * @param
	 * @return: List of Types object, ProjectOnboardingException, Exception
	 */
	@GetMapping("/fetch-task-status")
	@Operation(summary = "Get list of all task statuses for a user in a project", description = "This API is used to save list of task statuses for a user in a project")
	public ResponseEntity<ResponsePayLoad> fetchAllTaskStatus() {
		try {
			log.info("Starting of fetch all task status");

			List<Object> statusList = new ArrayList<Object>();
			statusList.addAll(projectOnboardingService.getAllTaskStatus());

			log.info("Return the task details");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(statusList, ProjectOnboardingConstant.API_TASK_STATUS_FETCH_SUCCESS, ""),
					HttpStatus.OK);
		} catch (ProjectOnboardingException projectOnboardingException) {
			log.error("The task status list is empty");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(null, "", projectOnboardingException.getErrorMessage()), HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.error("Fetch status list failed");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", exception.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
