package com.project.onboarding.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.response.ProjectTasksOverviewResponse;
import com.project.onboarding.response.StatusReportResponse;
import com.project.onboarding.response.TaskPercentageReportResponse;
import com.project.onboarding.service.OnboardingStatusService;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : jUnit testcases for Onboarding status controller.
 * @date : 12 August 2022
 */

@ExtendWith(MockitoExtension.class)
public class OnboardingStatusControllerTests {
	@Mock
	private OnboardingStatusService onboardingStatusService;

	@InjectMocks
	private OnboardingStatusController onboardingStatusController;

	private MockMvc mockMvc;

	private StatusReportResponse statusReport = new StatusReportResponse();

	private ProjectTasksOverviewResponse projectTasksOverview = new ProjectTasksOverviewResponse();

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(onboardingStatusController).build();

		TaskPercentageReportResponse taskPercentageReport = new TaskPercentageReportResponse();
		taskPercentageReport.setProjectName("Project Onboarding");
		taskPercentageReport.setProjectDescription("Onboarding reources to project");
		taskPercentageReport.setProjectOwner("Vanisha");

		projectTasksOverview.setUserId("U13");
		projectTasksOverview.setUserName("Kulsu");
		projectTasksOverview.setTaskPercentage(50);
		taskPercentageReport.setProjectTasksOverview(projectTasksOverview);
		
		statusReport.setTaskPercentageReport(taskPercentageReport);
	}

	@AfterEach
	public void tearDown() {
		statusReport = null;
		projectTasksOverview = null;
	}

	@DisplayName("JUnit test for getPreviewStatusReport API success scenario")
	@Test
	public void givenProjectIdAndUserId_whenGetPreviewStatusReport_thenReturnStatusReportObject() throws Exception {
		when(onboardingStatusService.getPreviewStatusReport(any(), any())).thenReturn(statusReport.getTaskPercentageReport());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/onboarding-status/preview-report/P_1/U13")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(statusReport))).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].projectTasksOverview.taskPercentage")
						.value(statusReport.getTaskPercentageReport().getProjectTasksOverview().getTaskPercentage()));
		verify(onboardingStatusService, times(1)).getPreviewStatusReport(any(), any());
	}

	@DisplayName("JUnit test for getPreviewStatusReport API failure scenarios")
	@Test
	public void givenProjectIdAndUserId_whenGetPreviewStatusReport_thenThrowProjectOnboardingException()
			throws Exception {
		when(onboardingStatusService.getPreviewStatusReport(any(), any()))
				.thenThrow(new ProjectOnboardingException("Project Not Found"));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/onboarding-status/preview-report/P_1/U13").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Project Not Found"));
		verify(onboardingStatusService, times(1)).getPreviewStatusReport(any(), any());
	}

	@DisplayName("JUnit test for getPreviewStatusReport API failure scenarios")

	@Test
	public void givenProjectIdAndUserId_whenGetPreviewStatusReport_thenThrowException() throws Exception {
		when(onboardingStatusService.getPreviewStatusReport(any(), any()))
				.thenThrow(new Exception("Internal Server Error"));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/onboarding-status/preview-report/P_1/U13").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Internal Server Error"));
		verify(onboardingStatusService, times(1)).getPreviewStatusReport(any(), any());
	}
	
	@DisplayName("JUnit test for exportStatusReportInExcelFormat API success scenario")
	@Test
	public void givenProjectIdAndUserId_whenExportStatusReportInExcelFormat_thenReturnStatusReportObject() throws Exception {
		when(onboardingStatusService.exportStatusReportInExcelFormat(any(), any())).thenReturn(statusReport);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/onboarding-status/export-report/P_1/U13")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(statusReport))).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].taskPercentageReport.projectTasksOverview.taskPercentage")
						.value(statusReport.getTaskPercentageReport().getProjectTasksOverview().getTaskPercentage()));
		verify(onboardingStatusService, times(1)).exportStatusReportInExcelFormat(any(), any());
	}
	
	@DisplayName("JUnit test for exportStatusReportInExcelFormat API failure scenarios")
	@Test
	public void givenProjectIdAndUserId_whenExportStatusReportInExcelFormat_thenThrowProjectOnboardingException()
			throws Exception {
		when(onboardingStatusService.exportStatusReportInExcelFormat(any(), any()))
				.thenThrow(new ProjectOnboardingException("Project Not Found"));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/onboarding-status/export-report/P_1/U13").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Project Not Found"));
		verify(onboardingStatusService, times(1)).exportStatusReportInExcelFormat(any(), any());
	}

	@DisplayName("JUnit test for exportStatusReportInExcelFormat API failure scenarios")

	@Test
	public void givenProjectIdAndUserId_whenExportStatusReportInExcelFormat_thenThrowException() throws Exception {
		when(onboardingStatusService.exportStatusReportInExcelFormat(any(), any()))
				.thenThrow(new Exception("Internal Server Error"));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/onboarding-status/export-report/P_1/U13").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Internal Server Error"));
		verify(onboardingStatusService, times(1)).exportStatusReportInExcelFormat(any(), any());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
