package com.project.onboarding.controller;

import java.util.ArrayList;
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
import com.project.onboarding.response.ResponsePayLoad;
import com.project.onboarding.service.ProjectOnboardingService;

/**
 * @author Amrutha Joseph
 * @description controller class for project onboarding
 * @created_Date 17/08/2022
 */

@RestController
@RequestMapping("/api/v1")
public class ProjectOnboardingController {

	@Autowired
	ProjectOnboardingService projectOnboardingService;

	private static final Logger log = LoggerFactory.getLogger(ProjectOnboardingController.class);

	/**
	 * @param userId
	 * @return ResponseEntity<List<ProjectDetails>>
	 * @description : Fetch all projects based on user
	 */

	@GetMapping("/projects/{userId}")
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
	public ResponseEntity<?> getUsers(@PathVariable String projectId) {

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
}
