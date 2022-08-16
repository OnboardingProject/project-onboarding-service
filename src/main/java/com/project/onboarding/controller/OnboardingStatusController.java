package com.project.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

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

import lombok.extern.slf4j.Slf4j;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Controller for OnboardingStatus module
 * @date : 10 August 2022
 */
@Slf4j
@RestController
@RequestMapping("api/v1")
public class OnboardingStatusController {

	@Autowired
	OnboardingStatusService onboardingStatusService;

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
			log.info("In preview status report controller");
			
			StatusReport statusReport = onboardingStatusService.getPreviewStatusReport(projectId, userId);
			List<Object> statusReportObject = new ArrayList<Object>();
			statusReportObject.add(statusReport);
			
			log.info("Status report is returned successfully in controller");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(statusReportObject,
					ProjectOnboardingConstant.API_GET_PREVIEW_REPORT_SUCCESS, ""), HttpStatus.OK);
			
		} catch (ProjectOnboardingException ex) {
			log.error("Project/User not found, preview report failed in controller");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getErrorMessage()),
					HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			log.error("Preview report failed in controller");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
