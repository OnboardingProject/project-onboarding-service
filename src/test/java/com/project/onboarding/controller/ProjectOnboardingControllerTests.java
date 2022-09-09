package com.project.onboarding.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.Types;
import com.project.onboarding.request.SaveTaskStatusRequest;
import com.project.onboarding.request.TaskStatusRequest;
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

	TaskDetails taskDetails = new TaskDetails();
	List<TaskDetails> taskList = new ArrayList<TaskDetails>();

	SaveTaskStatusRequest saveTaskStatusRequest;
	
	private Types types;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(projectOnboardingController).build();

		projectDetails.setProjectId("P_001");
		projectDetails.setProjectName("Employee");
		projectDetailsList.add(projectDetails);

		userDetails.setUserId("U12");
		userDetails.setUserName("Anu Mathew");
		userDetailsList.add(userDetails);

		taskDetails.setTaskId(1);
		taskDetails.setTaskName("Laptop Allocation");
		taskDetails.setTaskStatus("Done");

		taskList.add(taskDetails);

		List<TaskStatusRequest> taskStatusRequests = new ArrayList<TaskStatusRequest>();
		taskStatusRequests.add(new TaskStatusRequest(1, "In-progress"));

		saveTaskStatusRequest = new SaveTaskStatusRequest("P_001", "U11", taskStatusRequests);
	}

	@DisplayName("JUnit test for get projects based on user success scenario ")
	@Test
	public void getProjectsBasedOnUserSuccessTest() throws Exception {

		when(projectOnboardingService.getProjectsBasedOnUser(any())).thenReturn(projectDetailsList);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/projects/U11").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@DisplayName("JUnit test for get projects based on user failure scenario ")
	@Test
	public void getProjectsBasedOnUserFailureTest() throws Exception {

		when(projectOnboardingService.getProjectsBasedOnUser(any()))
				.thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.USER_NOT_FOUND));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/projects/U11").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isConflict())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.USER_NOT_FOUND));
	}

	@DisplayName("JUnit test for get resources based on project success scenario ")
	@Test
	public void getResourcesBasedOnProjectSuccessTest() throws Exception {

		when(projectOnboardingService.getUsersBasedOnProject(any())).thenReturn(userDetailsList);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/resources/P_001").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@DisplayName("JUnit test for get resources based on project failure scenario ")
	@Test
	public void getResourcesBasedOnProjectFailureTest() throws Exception {

		when(projectOnboardingService.getUsersBasedOnProject(any()))
				.thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/resources/P_001").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isConflict())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.PROJECT_NOT_FOUND));

	}

	@DisplayName("Testcases for getProjectsBasedOnUser API failure scenarios")
    @Test
    public void getProjectsBasedOnUserFailureThrowExceptionTest() throws Exception {
        when(projectOnboardingService.getProjectsBasedOnUser(any())).thenThrow(new Exception(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/projects/U11")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
                
    }
	
	@DisplayName("Testcases for getResourcesBasedOnProject API failure scenarios")
    @Test
    public void getResourcesBasedOnProjectFailureThrowExceptionTest() throws Exception {
        when(projectOnboardingService.getUsersBasedOnProject(any())).thenThrow(new Exception(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/resources/P_001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
   }
	 
	@DisplayName("JUnit test for fetching task list success scenario ")
	@Test
	public void getAllTasklistSuccessTest() throws Exception {
		when(projectOnboardingService.fetchTaskList("P_001", "U11")).thenReturn(taskList);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/view-tasks/P_001/U11").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@DisplayName("JUnit test for fetching task list failure scenario ")
	@Test
	public void getAllTasklistFailureTest() throws Exception {
		when(projectOnboardingService.fetchTaskList("P_001", "U11"))
				.thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.TASK_LIST_NOT_FOUND));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/view-tasks/P_001/U11")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(taskList)))
				.andExpect(MockMvcResultMatchers.status().isConflict()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(ProjectOnboardingConstant.TASK_LIST_NOT_FOUND));
		verify(projectOnboardingService, times(1)).fetchTaskList("P_001", "U11");
	}

	@DisplayName("JUnit test for saving task status success scenario ")
	@Test
	public void saveTaskStatusSuccessTest() throws Exception {
		when(projectOnboardingService.saveStatus(any())).thenReturn(taskList);

		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/project-onboarding/save-task-status").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(saveTaskStatusRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@DisplayName("JUnit test for saving task status failure scenario ")
	@Test
	public void saveTaskStatusFailureTest() throws Exception {
		when(projectOnboardingService.saveStatus(any()))
				.thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.TASK_LIST_NOT_FOUND));

		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/project-onboarding/save-task-status").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(saveTaskStatusRequest))).andExpect(MockMvcResultMatchers.status().isConflict())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.TASK_LIST_NOT_FOUND));
		verify(projectOnboardingService, times(1)).saveStatus(any());

	}

	@DisplayName("JUnit test for getAllTaskStatus success scenario ")
	@Test
	public void testFetchAllTaskStatusSucess() throws Exception {
		Types types = new Types();
		types.setTypeName("TASK_STATUS");
		types.setTypeId(10);
		types.setTypeDesc("In-progress");
		types.setPermission(null);
		List<Types> statusList = new ArrayList<Types>();
		statusList.add(types);

		when(projectOnboardingService.getAllTaskStatus()).thenReturn(statusList);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/fetch-task-status")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@DisplayName("JUnit test for getAllTaskStatus failure scenario ")
	@Test
	public void testFetchAllTaskStatusFailure_ThenThrowException() throws Exception {

		when(projectOnboardingService.getAllTaskStatus())
				.thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.STATUS_LIST_EMPTY));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-onboarding/fetch-task-status")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(types))).andExpect(MockMvcResultMatchers.status().isConflict())
				.andDo(MockMvcResultHandlers.print()).andExpect(
						MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.STATUS_LIST_EMPTY));
		verify(projectOnboardingService, times(1)).getAllTaskStatus();

	}

	@DisplayName("JUnit test for saveTaskStatus API failure scenario when throws Exception")
	@Test
	public void givenSaveStatusRequest_whenSaveTaskStatus_thenThrowException() throws Exception {
		when(projectOnboardingService.saveStatus(any())).thenThrow(new Exception(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));

		mockMvc.perform(
				MockMvcRequestBuilders.put("/api/v1/project-onboarding/save-task-status").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(saveTaskStatusRequest)))
				.andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
		verify(projectOnboardingService, times(1)).saveStatus(any());
	}
	
	@DisplayName("JUnit test for getAllTasks API failure scenario when throws Exception")
	@Test
	public void givenProjectIdAndUserId_whenGetAllTasks_thenThrowException() throws Exception {
		when(projectOnboardingService.fetchTaskList(any(), any())).thenThrow(new Exception(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/project-onboarding/view-tasks/P_001/U11").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
		verify(projectOnboardingService, times(1)).fetchTaskList(any(), any());
	}
	
	@DisplayName("JUnit test for getAllTaskStatus internal server failure scenario ")
    @Test
        public void testtestFetchAllTaskStatusInternalServerFailure_ThenThrowException() throws Exception {
             when(projectOnboardingService.getAllTaskStatus()).thenThrow(new Exception(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
            mockMvc.perform(
            		MockMvcRequestBuilders.get("/api/v1/project-onboarding/fetch-task-status").contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(types)))
                    .andExpect(status().isInternalServerError())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
                  
            verify(projectOnboardingService, times(1)).getAllTaskStatus();
        }

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
