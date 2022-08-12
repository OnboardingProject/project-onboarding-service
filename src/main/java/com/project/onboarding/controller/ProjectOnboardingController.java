package com.project.onboarding.controller;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Types;
import com.project.onboarding.service.ProjectOnboardingService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fetchtaskstatus")
public class ProjectOnboardingController {

	@Autowired
	ProjectOnboardingService projectOnboardingService;

	private static Logger log = LoggerFactory.getLogger(ProjectOnboardingService.class);

	/**
	 * Description : Fetch task status API
	 * 
	 * @Param :
	 * @Return: List of Types object
	 * 
	 */
	@GetMapping
	public ResponseEntity<?> fetchAllTaskStatus() {
		try {
			log.info("Starting of fetch all task status");
			List<Types> fetchStatusList = projectOnboardingService.getAllTaskStatus();
			return new ResponseEntity<List<Types>>(fetchStatusList, HttpStatus.OK);
		}

		catch (ProjectOnboardingException projectOnboardingException) {
			log.error("The list is empty then throw exception");
			return new ResponseEntity<String>(ProjectOnboardingConstant.LIST_EMPTY, HttpStatus.NOT_FOUND);
		}
	}

}

