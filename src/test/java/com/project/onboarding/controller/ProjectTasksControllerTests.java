package com.project.onboarding.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.Task;
import com.project.onboarding.service.ProjectTaskService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ProjectTasksControllerTests {

	@InjectMocks
	ProjectTasksController projectTasksController;

	@Mock
	ProjectTaskService projecTaskService;

	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(projectTasksController).build();
	}

	@DisplayName("JUnit test for getProjectTasksByProjectId success scenario ")
	@Test
	public void getAllTaskByProjectSuccessTest() throws Exception {
		List<Project> projectList = new ArrayList<Project>();
		List<Task> taskList = new ArrayList<Task>();

		Project project = new Project();
		project.setProjectId("P_001");
		Task task = new Task();
		task.setTaskId(9);
		taskList.add(task);
		project.setTasks(taskList);
		projectList.add(project);

		when(projecTaskService.getProjectTasksByProjectId("P_001")).thenReturn(taskList);
		mockMvc.perform(MockMvcRequestBuilders.get("/project_task/fetch_project_task/P_001")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@DisplayName("JUnit test for getProjectTasksByProjectId failure scenario ")
	@Test
	public void getProjectTasksByProjectIdFailuerTest() throws Exception {

		Task tasks = new Task();

		when(projecTaskService.getProjectTasksByProjectId("P_001")).thenThrow(
				new ProjectOnboardingException(ProjectOnboardingConstant.PROJECTIDNOTFOUND, HttpStatus.NOT_FOUND));
		mockMvc.perform(MockMvcRequestBuilders.get("/project_task/fetch_project_task/P_001")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(tasks))).andExpect(status().isNotFound())
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(ProjectOnboardingConstant.PROJECTIDNOTFOUND));
		verify(projecTaskService, times(1)).getProjectTasksByProjectId("P_001");

	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}


