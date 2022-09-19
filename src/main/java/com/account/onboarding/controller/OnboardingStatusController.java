package com.account.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.response.ResponsePayLoad;
import com.account.onboarding.response.StatusReportResponse;
import com.account.onboarding.response.TaskPercentageReportResponse;
import com.account.onboarding.service.impl.OnboardingStatusService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Controller for OnboardingStatus module
 * @date : 10 August 2022
 */
@Slf4j
@RestController
@RequestMapping("api/v1/onboarding-status")
public class OnboardingStatusController {

	@Autowired
	OnboardingStatusService onboardingStatusService;

	/**
	 * @param projectId, userId
	 * @return ResponseEntity<ResponsePayLoad>, ProjectOnboardingException, Exception
	 * @description : Preview status report of a particular user for a project
	 */
	@GetMapping("/preview-report/{projectId}/{userId}")
	@Operation(summary = "Get Preview Status Report", description = "This API is used to get preview status report")
	public ResponseEntity<ResponsePayLoad> getPreviewStatusReport(
			@Valid @PathVariable("projectId") @NotNull @Size(min = 1, message = ProjectOnboardingConstant.INVALID_PROJECT_ID) String projectId,
			@PathVariable("userId") @NotNull @Size(min = 1, message = ProjectOnboardingConstant.INVALID_USER_ID) String userId) {
		try {
			log.info("In preview status report controller");

			TaskPercentageReportResponse taskPercentageReport = onboardingStatusService.getPreviewStatusReport(projectId, userId);
			List<Object> statusReportObject = new ArrayList<Object>();
			statusReportObject.add(taskPercentageReport);

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

	/**
	 * @param projectId, userId
	 * @return ResponseEntity<ResponsePayLoad>, ProjectOnboardingException, Exception
	 * @description : Download status report in excel format for a particular user of a project
	 */
	@GetMapping("/export-report/{projectId}/{userId}")
	@Operation(summary = "Download Status Report in Excel Format", description = "This API is used to download status report in excel format")
	public ResponseEntity<ResponsePayLoad> exportStatusReportInExcelFormat(
			@Valid @PathVariable("projectId") @Size(min = 1, message = ProjectOnboardingConstant.INVALID_PROJECT_ID) String projectId,
			@PathVariable("userId") @Size(min = 1, message = ProjectOnboardingConstant.INVALID_USER_ID) String userId) {

		try {
			log.info("In export status report controller");

			StatusReportResponse statusReport = onboardingStatusService.exportStatusReportInExcelFormat(projectId, userId);
			List<Object> statusReportObject = new ArrayList<Object>();
			statusReportObject.add(statusReport);

			log.info("Status report in excel format is downloaded successfully in controller");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(statusReportObject, ProjectOnboardingConstant.API_EXPORT_REPORT_SUCCESS + ProjectOnboardingConstant.getFileNameForExcelReport(projectId, userId), ""),
					HttpStatus.OK);

		} catch (ProjectOnboardingException ex) {
			log.error("Project/User not found, export report failed in controller");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getErrorMessage()),
					HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			log.error("Export report failed in controller");
			return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(null, "", ex.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
