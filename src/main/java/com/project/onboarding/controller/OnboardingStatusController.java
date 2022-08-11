package com.project.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

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
import com.project.onboarding.model.StatusReport;
import com.project.onboarding.model.ResponsePayLoad;
import com.project.onboarding.service.OnboardingStatusService;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Controller for OnboardingStatus module
 * @date : 10 August 2022
 */

@RestController
@RequestMapping("api/v1")
public class OnboardingStatusController {

	private static final Logger logger = LoggerFactory.getLogger(OnboardingStatusController.class);

	@Autowired
	OnboardingStatusService onboardingStatusReport;

	/**
	 * @param projectId, userId
	 * @return ResponseEntity<ResponsePayLoad>
	 * @description : Preview status report of a particular user for a project
	 */
	@GetMapping("/previewReport/{projectId}/{userId}")
	public ResponseEntity<ResponsePayLoad> getPreviewStatusReport(
			@Valid @PathVariable("projectId") @Size(min = 1, message = "Invalid id, please provide valid id") String projectId,
			@PathVariable("userId") @Size(min = 1, message = "Invalid id, please provide valid id") String userId) {
		try {
			logger.info("In preview status report controller");
			
			StatusReport statusReport = onboardingStatusReport.getPreviewStatusReport(projectId, userId);
			List<Object> statusReportObject = new ArrayList<Object>();
			statusReportObject.add(statusReport);
			
			logger.info("Status report is returned successfully in controller");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(statusReportObject,
					ProjectOnboardingConstant.API_GET_PREVIEW_REPORT_SUCCESS, ""), HttpStatus.OK);
			
		} catch (ProjectOnboardingException ex) {
			logger.warn("Project/User not found, preview report failed in controller");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getErrorMessage()),
					HttpStatus.NOT_FOUND);
		}

	}
}
