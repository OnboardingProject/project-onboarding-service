
package com.project.onboarding.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.User;
import com.project.onboarding.service.ProjectOnboardingService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ProjectOnboardingControllerTests {

	@InjectMocks
	ProjectOnboardingController projectOnboardingController;

	@Mock
	ProjectOnboardingService projectOnboardingService;
	
	@Autowired
	private MockMvc mockMvc;
	
	//private User user;
	//List<User> userList = new ArrayList<User>();

	//List<ProjectTaskDetails> ProIdDetails = new ArrayList<ProjectTaskDetails>();
	
	TaskDetails taskDetails = new TaskDetails();
	List<TaskDetails> taskList = new ArrayList<TaskDetails>();

	@BeforeEach
	public void init() {
		//MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(projectOnboardingController).build();
//
//		User user = new User();
//		user.setUserId("U11");
//		ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();
//		projectTaskDetails.setProjectId("P_001");
//		TaskDetails tasks = new TaskDetails();
//		tasks.setTaskId(001);
//
//		taskList.add(tasks);
//		projectTaskDetails.setTasks(taskList);
//		ProIdDetails.add(projectTaskDetails);
//		user.setProjectIds(ProIdDetails);
//		userList.add(user);
		
		taskDetails.setTaskId(1);
		taskDetails.setTaskName("Laptop Allocation");
		taskDetails.setTaskStatus("Done");
		
		taskList.add(taskDetails);
	}

	@DisplayName("JUnit test for fetching task list success scenario ")
	@Test
	public void getAllTasklistSuccessTest() throws Exception {

		when(projectOnboardingService.fetchTaskList("P_001", "U11")).thenReturn(taskList);
		mockMvc.perform(MockMvcRequestBuilders.get("/viewTasks/P_001/U11").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@DisplayName("JUnit test for fetching task list failure scenario ")
	@Test
	public void getAllTasklistFailureTest() throws Exception {
		when(projectOnboardingService.fetchTaskList("P_001", "U11")).thenThrow(
				new ProjectOnboardingException(ProjectOnboardingConstant.TASKLIST_NOT_FOUND, HttpStatus.CONFLICT));
		mockMvc.perform(MockMvcRequestBuilders.get("/viewTasks/P_001/U11").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(taskList))).andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(ProjectOnboardingConstant.TASKLIST_NOT_FOUND));
		verify(projectOnboardingService, times(1)).fetchTaskList("P_001", "U11");

	}

	@DisplayName("JUnit test for saving task status success scenario ")
	@Test
	public void saveTaskStatusSuccessTest() throws Exception {

		when(projectOnboardingService.saveStatus(any())).thenReturn(taskList);
		mockMvc.perform(MockMvcRequestBuilders.put("/saveTaskStatus").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(taskList)))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
//when(projectOnboardingService.saveStatus(any())).thenReturn(taskList);
//        
//        mockMvc.perform(MockMvcRequestBuilders.put("/saveTaskStatus")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
        
	}
	
	@DisplayName("JUnit test for saving task status failure scenario ")
	@Test
	public void saveTaskStatusFailureTest() throws Exception {
		when(projectOnboardingService.saveStatus(any())).thenThrow(
				new ProjectOnboardingException(ProjectOnboardingConstant.TASKLIST_NOT_FOUND, HttpStatus.CONFLICT));
		mockMvc.perform(MockMvcRequestBuilders.put("/saveTaskStatus").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(taskList))).andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(ProjectOnboardingConstant.TASKLIST_NOT_FOUND));
		verify(projectOnboardingService, times(1)).saveStatus(any());

		
    }
	

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
