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

import com.project.onboarding.model.ProjectDetails;
import com.project.onboarding.model.UserDetails;
import com.project.onboarding.exception.ProjectOnboardingException;
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
	public ResponseEntity<?> getProjects( @PathVariable String userId) {
		
		try {
			
			log.info("Inside project onboarding controller getProjects try block");
			List<ProjectDetails> projects = projectOnboardingService.getProjectsBasedOnUser(userId);
			return new ResponseEntity<List<ProjectDetails>>(projects, HttpStatus.FOUND);
			
		} catch (ProjectOnboardingException ex) {
			
			log.info("Inside project onboarding controller getProjects catch block");
			return new ResponseEntity<String>(ex.getErrorMessage(), HttpStatus.CONFLICT);
		}

		
	}
	
	/**
	 * @param projectId
	 * @return ResponseEntity<List<UserDetails>>
	 * @description : Fetch all resources based on project 
	 */
	
	@GetMapping("/resources/{projectId}")
	public ResponseEntity<?> getUsers( @PathVariable String projectId) {
		
		try {
			
			log.info("Inside project onboarding controller getUsers try block");
			List<UserDetails> users = projectOnboardingService.getUsersBasedOnProject(projectId);
			return new ResponseEntity<List<UserDetails>>(users, HttpStatus.FOUND);
			
		} catch (ProjectOnboardingException ex) {
			
			log.info("Inside project onboarding controller getUsers catch block");
			return new ResponseEntity<String>(ex.getErrorMessage(), HttpStatus.CONFLICT);
		}

		
	}

}


