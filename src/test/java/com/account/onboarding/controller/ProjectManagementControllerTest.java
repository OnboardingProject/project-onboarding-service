package com.account.onboarding.controller;

import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.account.onboarding.model.Project;
import com.account.onboarding.request.ProjectDTO;
import com.account.onboarding.response.UserResponse;
import com.account.onboarding.service.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectManagementControllerTest {
	@InjectMocks
	private ProjectManagementController projectManagementController;

	@Mock
	private ProjectService projectService;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(projectManagementController).build();
	}

	private List<Project> projectList = new ArrayList<>();
	private List<String> userId1 = new LinkedList<String>(Arrays.asList("u12", "u15", "u45"));
	private List<String> userId2 = new LinkedList<String>(Arrays.asList("u44", "u14", "u43"));
	private List<String> userId3 = new LinkedList<String>(Arrays.asList("u17", "u25", "u95"));
	Date time = new Date();

	private ProjectDTO getProjectTestData() {
		List<String> userId = new LinkedList<String>();
		userId.add("U23");
		userId.add("U36");
		ProjectDTO projectDTO = new ProjectDTO("Pjt111", "PJT_ONBOARDING", "About on boarding", null, "U111", null,
				"U111", userId);

		return projectDTO;
	}

	private List<UserResponse> getUserTestData() {
		List<UserResponse> users = new ArrayList<UserResponse>();
		UserResponse user = new UserResponse("u111", "JEENA", 4);
		UserResponse user1 = new UserResponse("u112", "Mareena", 4);
		users.add(user1);
		users.add(user);
		return users;
	}

	@Test
	public void addProjectTest() throws ParseException, Exception {
		ProjectDTO pjtDTO = getProjectTestData();
		when(projectService.createProject(Mockito.any(ProjectDTO.class))).thenReturn(pjtDTO);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/project/add-project")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(pjtDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void whenRequestParameterIsInvalid_thenReturnsStatus400Test() throws Exception {
		ProjectDTO projectDTO1 = new ProjectDTO(" ", "PJT_ONBOARDING", "About on boarding", null, "U111", null, "U111",
				null);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/project/add-project")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(projectDTO1)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

	@Test
	public void getAllResourceTest() throws ParseException, Exception {
		when(projectService.getAllUsers()).thenReturn(getUserTestData());
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project/get-resources").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void updateProjectTest() throws Exception {
		ProjectDTO projectDTO = getProjectTestData();
		List<String> userlist = Arrays.asList("u12", "u34");
		projectDTO.setProjectName("ATM Project");
		projectDTO.setProjectDescription("Description of the project");
		projectDTO.setLastUpdateBy("U112");
		projectDTO.setUserId(userlist);
		projectDTO.setLastUpdateTime(null);

		when(projectService.editProject(Mockito.any(ProjectDTO.class))).thenReturn(projectDTO);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/project/edit-project")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(projectDTO)))
				.andExpect(MockMvcResultMatchers.status().isAccepted());
	}

	@Test
	public void viewAllProjectsSuccessTest() throws JsonProcessingException, Exception {
		Project project1 = new Project("62f47fb13ff026663334d220", "onboarding", "stringstri", time, "Mareena",
				"Nishanti", time, userId1, null);
		Project project2 = new Project("62f47fb13ff026663334d221", "starter", "stringstri", time, "Nishanti", "Jeena",
				time, userId2, null);
		Project project3 = new Project("62f47fb13ff026663334d222", "social", "stringstri", time, "Jeena", "Mareena",
				time, userId3, null);
		projectList.add(project1);
		projectList.add(project2);
		projectList.add(project3);

		when(projectService.getAllProjects()).thenReturn(projectList);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/project/view-all-projects").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void viewAllProjectsFailureTest() throws JsonProcessingException, Exception {
		projectList.clear();

		when(projectService.getAllProjects()).thenReturn(projectList);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/project/view-all-projects").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void searchByOwnerTest() throws JsonProcessingException, Exception {
		Project project1 = new Project("62f47fb13ff026663334d220", "onboarding", "stringstri", time, "Mareena",
				"Nishanti", time, userId1, null);
		Project project2 = new Project("62f47fb13ff026663334d221", "starter", "stringstri", time, "Nishanti", "Jeena",
				time, userId2, null);
		Project project3 = new Project("62f47fb13ff026663334d222", "social", "stringstri", time, "Jeena", "Mareena",
				time, userId3, null);
		projectList.add(project1);
		projectList.add(project2);
		projectList.add(project3);

		when(projectService.searchByCreatedBy("Mareena")).thenReturn(projectList);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project/view-projects-by-owner/Mareena")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	public void searchByOwnerTestFailureTest() throws JsonProcessingException, Exception {
		projectList.clear();
		when(projectService.searchByCreatedBy("Mareena")).thenReturn(projectList);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project/view-projects-by-owner/Mareena")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void getProjectByIdTest() throws Exception {
		Project project1 = new Project("62f47fb13ff026663334d220", "onboarding", "stringstri", time, "Mareena",
				"Nishanti", time, userId1, null);

		when(projectService.getProjectById(Mockito.anyString())).thenReturn(project1);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project/view-project/62f47fb13ff026663334d220")
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void getProjectByIdTestFailure() throws Exception {

		when(projectService.getProjectById(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project/view-project/62f47fb13ff026663334d229")
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
