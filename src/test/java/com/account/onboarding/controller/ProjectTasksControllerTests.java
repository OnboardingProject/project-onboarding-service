package com.account.onboarding.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.model.Project;
import com.account.onboarding.model.Task;
import com.account.onboarding.request.DeleteTaskRequest;
import com.account.onboarding.request.ProjectTaskRequest;
import com.account.onboarding.service.impl.ProjectTasksService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ProjectTasksControllerTests {

	@InjectMocks
	ProjectTasksController projectTasksController;

	@Mock
	ProjectTasksService projectTasksService;

	private MockMvc mockMvc;

	Project project;
	Task task1, task2, task3;
	List<Project> projectList = new ArrayList<Project>();
	List<Task> taskList = new ArrayList<Task>();
	ProjectTaskRequest projectTaskRequest;
	DeleteTaskRequest deleteTaskRequest;
	List<Integer> deleteList = new ArrayList<Integer>();
	List<Task> taskListAfterDeletion = new ArrayList<Task>();
	
	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(projectTasksController).build();

		List<String> userIds = new ArrayList<String>();
		userIds.add("U11");
		userIds.add("U12");
		userIds.add("U13");

		task1 = new Task(1, "Seat Allocation", "User Need Seat Allocation", "Software Engineer");
		taskList.add(task1);
		task2 = new Task(2, "Chair allocation", "User Need Chair Allocation", "System Analyst");
		taskList.add(task2);
		task3 = new Task(3, "Laptop allocation", "User Need Laptop Allocation", "Software Engineer");
		taskList.add(task3);

		project = new Project("P_001", "Employee Allocation", "Employee allocation Project", LocalDateTime.now(), "U1",
				"U12", LocalDateTime.now(), userIds, taskList);

		projectList.add(project);

		projectTaskRequest = new ProjectTaskRequest("P_001", task1);
		
		deleteList.add(2);
		deleteList.add(3);
		
		deleteTaskRequest = new DeleteTaskRequest("P_001", deleteList);
		
		taskListAfterDeletion.add(task1);
	}

	@DisplayName("JUnit test for getProjectTasksByProjectId success scenario ")
	@Test
	public void getAllTaskByProjectSuccessTest() throws Exception {
		when(projectTasksService.getProjectTasksByProjectId("P_001")).thenReturn(taskList);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-tasks/fetch-project-tasks/P_001")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@DisplayName("JUnit test for getProjectTasksByProjectId failure scenario ")
	@Test
	public void getProjectTasksByProjectIdFailuerTest() throws Exception {
		projectList.clear();
		taskList.clear();

		when(projectTasksService.getProjectTasksByProjectId("P_001"))
				.thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-tasks/fetch-project-tasks/P_001")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(taskList)))
				.andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers
						.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.PROJECT_NOT_FOUND));
		verify(projectTasksService, times(1)).getProjectTasksByProjectId("P_001");

	}

	@DisplayName("JUnit test for addOrEditTask to a Project success scenario ")
	@Test
	public void addOrEditTaskSuccessTest() throws Exception {
		task1.setTaskId(0);

		when(projectTasksService.addOrEditTask(any())).thenReturn(project);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/project-tasks/add-or-edit-task")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(projectTaskRequest)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

	}

	@DisplayName("JUnit test for addOrEditTask to a Project failure scenario ")
	@Test
	public void addOrEditTaskFailureTest() throws Exception {
		task1.setTaskId(0);

		when(projectTasksService.addOrEditTask(any()))
				.thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/project-tasks/add-or-edit-task")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(projectTaskRequest)))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(ProjectOnboardingConstant.PROJECT_NOT_FOUND));
	}

	@DisplayName("JUnit test for getProjectTasksByProjectId internal server error scenario ")
	@Test
	public void getProjectTasksByProjectIdInternalServerFailuerTest() throws Exception {
		Task tasks = new Task();
		when(projectTasksService.getProjectTasksByProjectId("P_001"))
				.thenThrow(new Exception(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project-tasks/fetch-project-tasks/P_001")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(tasks)))
				.andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
		verify(projectTasksService, times(1)).getProjectTasksByProjectId("P_001");
	}

	@DisplayName("JUnit test for addOrEditTasks internal server error scenario ")
	@Test
	public void addOrEditTasksInternalServerFailuerTest() throws Exception {
		when(projectTasksService.addOrEditTask(any()))
				.thenThrow(new Exception(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/project-tasks/add-or-edit-task")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(projectTaskRequest)))
				.andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
		verify(projectTasksService, times(1)).addOrEditTask(any());
	}
	
	@DisplayName("JUnit test for deleteTasks success scenario ")
	@Test
	public void deleteTasksSuccessTest() throws Exception {
		when(projectTasksService.deleteTask(deleteTaskRequest)).thenReturn(taskListAfterDeletion);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/project-tasks/delete-task")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(deleteTaskRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0][0].taskId").value(1));
	}
	
	@DisplayName("JUnit test for deleteTasks failure scenario ")
	@Test
	public void deleteTasksFailureTest() throws Exception {
		when(projectTasksService.deleteTask(any()))
								.thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/project-tasks/delete-task")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(deleteTaskRequest)))
				.andExpect(MockMvcResultMatchers.status().isConflict())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(ProjectOnboardingConstant.PROJECT_NOT_FOUND));
		
		verify(projectTasksService, times(1)).deleteTask(any());
	}
	
	@DisplayName("JUnit test for deleteTasks internal server error scenario ")
	@Test
	public void deleteTasksInternalServerFailuerTest() throws Exception {
		when(projectTasksService.deleteTask(any()))
				.thenThrow(new Exception(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/project-tasks/delete-task")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(deleteTaskRequest)))
				.andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(ProjectOnboardingConstant.INTERNAL_SERVER_ERROR));
		verify(projectTasksService, times(1)).deleteTask(any());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
