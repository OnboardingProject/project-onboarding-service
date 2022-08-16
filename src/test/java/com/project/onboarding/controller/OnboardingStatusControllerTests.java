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
import com.project.onboarding.model.ProjectTasksOverview;
import com.project.onboarding.model.StatusReport;
import com.project.onboarding.service.OnboardingStatusService;

@ExtendWith(MockitoExtension.class)
public class OnboardingStatusControllerTests {
	@Mock
	private OnboardingStatusService onboardingStatusService;

	@InjectMocks
	private OnboardingStatusController onboardingStatusController;

	private MockMvc mockMvc;

	private StatusReport statusReport = new StatusReport();

	private ProjectTasksOverview projectTasksOverview = new ProjectTasksOverview();

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(onboardingStatusController).build();

		statusReport.setProjectName("Project Onboarding");
		statusReport.setProjectDescription("Onboarding reources to project");
		statusReport.setProjectOwner("Vanisha");

		projectTasksOverview.setUserId("U13");
		projectTasksOverview.setUsername("Kulsu");
		projectTasksOverview.setTaskPercentage(50);
		statusReport.setProjectTasksOverview(projectTasksOverview);
	}

	@AfterEach
	public void tearDown() {
		statusReport = null;
		projectTasksOverview = null;
	}

	@DisplayName("JUnit test for getPreviewStatusReport API success scenario")
	@Test
	public void givenProjectIdAndUserId_whenGetPreviewStatusReport_thenStatusReportObject() throws Exception {
		when(onboardingStatusService.getPreviewStatusReport(any(), any())).thenReturn(statusReport);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/previewReport/P_1/U13")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(statusReport))).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].projectTasksOverview.taskPercentage")
						.value(statusReport.getProjectTasksOverview().getTaskPercentage()));
		verify(onboardingStatusService, times(1)).getPreviewStatusReport(any(), any());
	}

	@DisplayName("JUnit test for getPreviewStatusReport API failure scenarios")
	@Test
	public void givenProjectIdAndUserId_whenGetPreviewStatusReport_thenThrowProjectOnboardingException()
			throws Exception {
		when(onboardingStatusService.getPreviewStatusReport(any(), any()))
				.thenThrow(new ProjectOnboardingException("Project Not Found"));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/previewReport/P_1/U13").contentType(MediaType.APPLICATION_JSON))
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
				MockMvcRequestBuilders.get("/api/v1/previewReport/P_1/U13").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Internal Server Error"));
		verify(onboardingStatusService, times(1)).getPreviewStatusReport(any(), any());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
