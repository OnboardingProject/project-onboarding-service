package com.project.onboarding.controller;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Types;
import com.project.onboarding.service.ProjectOnboardingService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author UST
 * @description : Controller class for fetch task status.
 * @date : 08 August 2022
 */
@RestController
@RequestMapping("/projectOnboarding")
public class ProjectOnboardingController {

	@Autowired
	ProjectOnboardingService projectOnboardingService;

	private static final Logger logger = LoggerFactory.getLogger(ProjectOnboardingController.class);

	/**
	 * Description : API for fetch task status 
	 * 
	 * @Param :
	 * 
	 * @Return: List of Types object
	 * 
	 */
	@GetMapping("/fetchTaskStatus")
	public ResponseEntity<TypesPayload> fetchAllTaskStatus() {
		try {
			logger.info("Starting of fetch all task status");

			List<Types> fetchStatusList = projectOnboardingService.getAllTaskStatus();
			logger.info("Return the task details");
			return new ResponseEntity<TypesPayload>(
					new TypesPayload(fetchStatusList, ProjectOnboardingConstant.SUCCESSSTATUSLIST, ""), HttpStatus.OK);
		}

		catch (ProjectOnboardingException projectOnboardingException) {
			logger.error("The task status list is empty");
			return new ResponseEntity<TypesPayload>(new TypesPayload(null, "", ProjectOnboardingConstant.LIST_EMPTY),
					HttpStatus.CONFLICT);
		}
	}

}
