package com.project.onboarding.controller;

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

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.response.ResponsePayLoad;
import com.project.onboarding.response.StatusReportResponse;
import com.project.onboarding.response.TaskPercentageReportResponse;
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
			@Valid @PathVariable("projectId") @NotNull @Size(min = 1, message = "Invalid id, please provide valid id") String projectId,
			@PathVariable("userId") @NotNull @Size(min = 1, message = "Invalid id, please provide valid id") String userId) {
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
	 * @return ResponseEntity<ResponsePayLoad>
	 * @description : Download status report in excel format for a particular user of a project
	 */
	@GetMapping("/exportReport/{projectId}/{userId}")
	public ResponseEntity<ResponsePayLoad> exportStatusReportInExcelFormat(
			@Valid @PathVariable("projectId") @Size(min = 1, message = "Invalid id, please provide valid id") String projectId,
			@PathVariable("userId") @Size(min = 1, message = "Invalid id, please provide valid id") String userId) {

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
