package com.project.onboarding.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.response.ProjectDetailsResponse;
import com.project.onboarding.response.UserDetailsResponse;
import com.project.onboarding.service.ProjectOnboardingService;

/**
 * @author Amrutha Joseph
 * @description : jUnit testcases for Project Onboarding controller.
 * @date : 17 August 2022
 */

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectOnboardingControllerTests {
	
	@Mock
	private ProjectOnboardingService projectOnboardingService;
	
	@InjectMocks
	private ProjectOnboardingController projectOnboardingController;
	
	@Autowired
	private MockMvc mockMvc;
	
	ProjectDetailsResponse projectDetails = new ProjectDetailsResponse();
	List<ProjectDetailsResponse> projectDetailsList = new ArrayList<>();
	
	UserDetailsResponse userDetails = new UserDetailsResponse();
	List<UserDetailsResponse> userDetailsList = new ArrayList<>();
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(projectOnboardingController).build();
		
		projectDetails.setProjectId("P_001");
		projectDetails.setProjectName("Employee");
		projectDetailsList.add(projectDetails);
		
		userDetails.setUserId("U12");
		userDetails.setUserName("Anu Mathew");
		userDetailsList.add(userDetails);
		
	}
	
	@Test
	public void getProjectsBasedOnUserSuccessTest() throws Exception {
		
		when(projectOnboardingService.getProjectsBasedOnUser(any())).thenReturn(projectDetailsList);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/U11")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isFound());	
	}
	
	@Test
	public void getProjectsBasedOnUserFailureTest() throws Exception {
		
		when(projectOnboardingService.getProjectsBasedOnUser(any())).thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.USER_NOT_FOUND));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/U11")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isConflict())
				//.andExpect(content().string(ProjectOnboardingConstant.USER_NOT_FOUND));
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.USER_NOT_FOUND));
		
		
	}
	
	@Test
	public void getResourcesBasedOnProjectSuccessTest() throws Exception {
		
		when(projectOnboardingService.getUsersBasedOnProject(any())).thenReturn(userDetailsList);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/resources/P_001")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isFound());			
	}
	
	@Test
	public void getResourcesBasedOnProjectFailureTest() throws Exception {
		
		when(projectOnboardingService.getUsersBasedOnProject(any())).thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/resources/P_001")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isConflict())
				//.andExpect(content().string(ProjectOnboardingConstant.PROJECT_NOT_FOUND));
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.PROJECT_NOT_FOUND));

	}

}
