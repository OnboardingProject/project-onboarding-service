package com.account.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.account.onboarding.exception.DataNotFoundException;
import com.account.onboarding.exception.NameAlreadyExistingException;
import com.account.onboarding.exception.NoResourceFoundException;
import com.account.onboarding.model.Project;
import com.account.onboarding.model.User;
import com.account.onboarding.repository.ProjectRepository;
import com.account.onboarding.request.ProjectDTO;
import com.account.onboarding.response.UserResponse;
import com.account.onboarding.service.impl.ProjectServiceImpl;

@AutoConfigureMockMvc
@SpringBootTest
class ProjectManagementServiceTests {
	@InjectMocks
	private ProjectServiceImpl projectServiceImpl;

	@Mock
	private ProjectRepository projectRepository;
	@Mock
	private MongoTemplate mongoTemplate;

	private List<Project> projectList = new ArrayList<>();
	private List<Project> projectList1 = new ArrayList<>();
	private List<String> userId1 = new LinkedList<String>(Arrays.asList("u12", "u15", "u45"));
	private List<String> userId2 = new LinkedList<String>(Arrays.asList("u44", "u14", "u43"));
	private List<String> userId3 = new LinkedList<String>(Arrays.asList("u17", "u25", "u95"));
	Date time = new Date();

	@Test
	private ProjectDTO getProjectTestData() {
		List<String> userId = new LinkedList<String>();
		userId.add("U23");
		userId.add("U36");
		Date time = new Date();
		ProjectDTO projectDTO = new ProjectDTO("Pjt111", "PJT_ONBOARDING", "About on boarding", time, "U111", time,
				"U111", userId);
		return projectDTO;
	}

	@Test
	private List<User> getUserTestData() {
		List<User> users = new ArrayList<User>();
		User user = new User("u111", "JEENA", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2345", "U2345", new Date(), 4, null, null, null);
		User user1 = new User("u112", "Mareena", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2345", "U2345", new Date(), 4, null, null, null);
		users.add(user1);
		users.add(user);
		return users;
	}

	@Test
	public void createProjectTest() {
		ProjectDTO pjtDTO = getProjectTestData();
		Project pjt = new Project(pjtDTO.getProjectId(), pjtDTO.getProjectName(), pjtDTO.getProjectDescription(),
				pjtDTO.getCreatedTime(), pjtDTO.getCreatedBy(), pjtDTO.getLastUpdateBy(), pjtDTO.getLastUpdateTime(),
				pjtDTO.getUserId(), null);
		when(projectRepository.save(Mockito.any(Project.class))).thenReturn(pjt);
		assertEquals(pjtDTO, projectServiceImpl.createProject(pjtDTO));
	}

	@Test
	public void createProjectForExceptionTest() {
		ProjectDTO pjtDTO = getProjectTestData();
		Project pjt = new Project(pjtDTO.getProjectId(), pjtDTO.getProjectName(), pjtDTO.getProjectDescription(),
				pjtDTO.getCreatedTime(), pjtDTO.getCreatedBy(), pjtDTO.getLastUpdateBy(), pjtDTO.getLastUpdateTime(),
				pjtDTO.getUserId(), null);
		String Name = "PJT_ONBOARDING";
		when(projectRepository.findByProjectName(Name)).thenReturn(pjt);
		assertThrows(NameAlreadyExistingException.class, () -> projectServiceImpl.createProject(pjtDTO));
	}

	@Test
	public void getAllUserByRoleTest() {
		List<User> users = getUserTestData();
		when(mongoTemplate.findAll(User.class)).thenReturn(users);
		List<UserResponse> usersDTOs = new ArrayList<UserResponse>();
		for (User user : users) {
			UserResponse userDTO = new UserResponse(user.getUserId(), user.getUserName(), user.getRoleId());
			usersDTOs.add(userDTO);
		}
		assertEquals(2, projectServiceImpl.getAllUsers().size());
	}

	@Test
	public void getAllUserByRoleExceptionTest() {
		when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(User.class))).thenReturn(null);
		assertThrows(NoResourceFoundException.class, () -> projectServiceImpl.getAllUsers());
	}

	@Test
	public void getAllProjectsSuccessTest() {
		Project project1 = new Project("62f47fb13ff026663334d220", "onboarding", "stringstri", time, "Mareena",
				"Nishanti", time, userId1, null);
		Project project2 = new Project("62f47fb13ff026663334d221", "starter", "stringstri", time, "Nishanti", "Jeena",
				time, userId2, null);
		Project project3 = new Project("62f47fb13ff026663334d222", "social", "stringstri", time, "Jeena", "Mareena",
				time, userId3, null);
		projectList.add(project1);
		projectList.add(project2);
		projectList.add(project3);

		when(projectRepository.findAll()).thenReturn(projectList);
		List<Project> actualProjs = projectServiceImpl.getAllProjects();
//			System.out.println(projectList);
//			System.out.println(actualProjs);
		assertEquals(projectList, actualProjs);
	}

	@Test
	public void getAllProjectsFailureTest() {
		projectList1.clear();
		when(projectRepository.findAll()).thenReturn(projectList1);
		// List<Project> actualProjs = projectServiceImp.getAllProjects();
		// assertEquals(0, actualProjs.size());
		assertThrows(DataNotFoundException.class, () -> projectServiceImpl.getAllProjects());
	}

	@Test
	public void getProjectByIdTest() {

		Project project = new Project("62f47fb13ff026663334d221", "onboarding", "stringstri", time, "Mareena",
				"Nishanti", time, userId1, null);
		when(projectRepository.findById("62f47fb13ff026663334d220")).thenReturn(Optional.of(project));
		assertEquals(project, projectServiceImpl.getProjectById("62f47fb13ff026663334d220"));
	}

	@Test
	public void getProjectByIdTestFailure() {
		when(projectRepository.findById("62f47fb13ff026663334d2289")).thenReturn(Optional.empty());
		assertThrows(DataNotFoundException.class, () -> projectServiceImpl.getProjectById("62f47fb13ff026663334d2289"));

	}

	@Test
	public void searchByCreatedByTest() {
		Project project2 = new Project("62f47fb13ff026663334d221", "starter", "stringstri", time, "Mareena", "Jeena",
				time, userId2, null);
		Project project1 = new Project("62f47fb13ff026663334d220", "onboarding", "stringstri", time, "Mareen",
				"Nishanti", time, userId1, null);
		projectList1.add(project1);
		projectList1.add(project2);
		when(projectRepository.findByCreatedBy("Mareena")).thenReturn(projectList1);
		List<Project> actualProjs = projectServiceImpl.searchByCreatedBy("Mareena");

		assertEquals(projectList1, actualProjs);
	}

	@Test
	public void searchByCreatedByFailTest() {
		projectList1.clear();
		when(projectRepository.findByCreatedBy("Mareena")).thenReturn(projectList1);
		assertThrows(DataNotFoundException.class, () -> projectServiceImpl.searchByCreatedBy("Mareena"));
	}

	@Test
	public void updateProjectTestSuccess() throws Exception {
		ProjectDTO pjtDTO = getProjectTestData();
		Project project = new Project(pjtDTO.getProjectId(), pjtDTO.getProjectName(), pjtDTO.getProjectDescription(),
				pjtDTO.getCreatedTime(), pjtDTO.getCreatedBy(), pjtDTO.getLastUpdateBy(), pjtDTO.getLastUpdateTime(),
				pjtDTO.getUserId(), null);
		List<String> userlist = Arrays.asList("u12", "u34");
		when(projectRepository.findById(Mockito.anyString())).thenReturn(Optional.of(project));

		pjtDTO.setProjectName("ATM Project");
		pjtDTO.setProjectDescription("Description of the project");
		pjtDTO.setLastUpdateBy("U112");
		pjtDTO.setUserId(userlist);
		pjtDTO.setLastUpdateTime(time);

		when(projectRepository.save(project)).thenReturn(project);
		assertEquals(pjtDTO, projectServiceImpl.editProject(pjtDTO));
	}

	@Test
	public void updateProjectTestFailure() throws Exception {
		ProjectDTO projectDTO = getProjectTestData();
		String id = "pjt111";
		when(projectRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(DataNotFoundException.class, () -> projectServiceImpl.editProject(projectDTO));
	}
}
