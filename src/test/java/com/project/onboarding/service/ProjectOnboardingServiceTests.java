package com.project.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.AccountDocument;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.Task;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.Types;
import com.project.onboarding.model.User;
import com.project.onboarding.response.ProjectDetailsResponse;
import com.project.onboarding.response.UserDetailsResponse;
import com.project.onboarding.util.ProjectOnboardingUtil;

/**
 * @author Amrutha Joseph
 * @description : jUnit testcases for Project Onboarding service.
 * @date : 17 August 2022
 */

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectOnboardingServiceTests {
	
	@Mock
	private MongoTemplate mongoTemplate;
	
	@Mock
	private ProjectOnboardingUtil projectOnboardingUtil;
	
	@InjectMocks
	private ProjectOnboardingService projectOnboardingService;
	
	List<User> users = new ArrayList<>();
	List<AccountDocument> accountDocuments = new ArrayList<>();
	List<ProjectTaskDetails> projectTaskDetailsList = new ArrayList<>();	
	List<TaskDetails> taskDetailsList = new ArrayList<>();
	TaskDetails taskDetails = new TaskDetails();	
	ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();
	AccountDocument accountDocument = new AccountDocument();
	User user, user1;
	
	List<Project> projects = new ArrayList<>();
	List<Task> tasks = new ArrayList<>();
	List<String> userIds = new ArrayList<>();	
	Task task = new Task();
	Project project = new Project();
	
	Types type = new Types();
	
	ProjectDetailsResponse expectedProjectDetails = new ProjectDetailsResponse();
	List<ProjectDetailsResponse> expectedProjectDetailsList = new ArrayList<>();
	
	UserDetailsResponse expectedUserDetails = new UserDetailsResponse();
	List<UserDetailsResponse> expectedUserDetailsList = new ArrayList<>();
	
	@BeforeEach
	public void setup() {
		
		userIds.add("U11");
		userIds.add("U12");
		
		List<String> designations = new ArrayList<String>();
		designations.add("Java Developer");
		
		task = new Task(1, "Seat allocation", "User needs a chair", designations);
		tasks.add(task);
		
		LocalDateTime date = LocalDateTime.now();
		
		project = new Project("P_001", "Employee", "Employee allocation", date, "Admin", "Admin", date, userIds, tasks);
		projects.add(project);
		
		taskDetails = new TaskDetails(1, "Seat allocation", "In-progress");
		taskDetailsList.add(taskDetails);
		
		projectTaskDetails = new ProjectTaskDetails("P_001", taskDetailsList);
		projectTaskDetailsList.add(projectTaskDetails);
		
		accountDocument = new AccountDocument("D_001", "Agree");
		accountDocuments.add(accountDocument);
		
		user = new User("U11", "Priya", "Demo", "Priya", "Gopal", "priya@gmail.com", "9898989893", "Software developer", date, "Admin", "Admin", date, 3, accountDocuments, "Priya", projectTaskDetailsList);
		users.add(user);
		user1 = new User("U12", "Anu", "Demo", "Anu", "Mathew", "anu@gmail.com", "9654329899", "Software developer", date, "Priya", "Priya", date, 4, accountDocuments, "Anu,Priya", projectTaskDetailsList);
		users.add(user1);
		
		type = new Types("ROLE", 4, "Resource", "Edit");
		
		expectedProjectDetails = new ProjectDetailsResponse("P_001", "Employee");
		expectedProjectDetailsList.add(expectedProjectDetails);
		
		expectedUserDetails = new UserDetailsResponse("U12", "Anu Mathew");
		expectedUserDetailsList.add(expectedUserDetails);
	}
	
	@Test
	public void getProjectsBasedOnUserSuccessTest() {
		setQueryAndCriteria();
		when(mongoTemplate.findOne(Query.query(Criteria.where("userId").is("U11")), User.class)).thenReturn(user);
		when(mongoTemplate.findOne(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(project);

        List<ProjectDetailsResponse> actualDetailsList = projectOnboardingService.getProjectsBasedOnUser("U11");
  
        assertEquals(expectedProjectDetailsList.get(0).getProjectId(), actualDetailsList.get(0).getProjectId());
        assertEquals(expectedProjectDetailsList.get(0).getProjectName(), actualDetailsList.get(0).getProjectName());

		
	}
	
	@Test
	public void getProjectsBasedOnUserWhenNoUserExistFailureTest() {
		
		user = null;
		when(mongoTemplate.findOne(Query.query(Criteria.where("userId").is("U11")), User.class)).thenReturn(user);
		
		ProjectOnboardingException actualErrormsg = assertThrows(ProjectOnboardingException.class, () -> {
			projectOnboardingService.getProjectsBasedOnUser("U11");			
		});
		
		assertEquals(ProjectOnboardingConstant.USER_NOT_FOUND, actualErrormsg.getErrorMessage());
		
	}
	
	@Test
	public void getProjectsBasedOnUserWhenNoProjectsAssignedFailureTest() {
		
		project = null;
		setQueryAndCriteria();
		when(mongoTemplate.findOne(Query.query(Criteria.where("userId").is("U11")), User.class)).thenReturn(user);
		when(mongoTemplate.findOne(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(project);
		
		ProjectOnboardingException actualErrormsg = assertThrows(ProjectOnboardingException.class, () -> {
			projectOnboardingService.getProjectsBasedOnUser("U11");			
		});
		
		assertEquals(ProjectOnboardingConstant.PROJECT_NOT_FOUND, actualErrormsg.getErrorMessage());
	}
	
	@Test
	public void getUsersBasedOnProjectSuccessTest() {
		setQueryAndCriteria();
		when(mongoTemplate.findOne(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(project);		
		when(mongoTemplate.findOne(Query.query(new Criteria().andOperator(Criteria.where("typeName").is("ROLE"),Criteria.where("typeDesc").is("Resource"))), Types.class)).thenReturn(type);		
		when(mongoTemplate.findOne(Query.query(new Criteria().andOperator(Criteria.where("userId").is("U12"),Criteria.where("roleId").is(type.getTypeId()))), User.class)).thenReturn(user1);
		
		List<UserDetailsResponse> actualDetailsList = projectOnboardingService.getUsersBasedOnProject("P_001");
		
		assertEquals(expectedUserDetailsList.get(0).getUserId(), actualDetailsList.get(0).getUserId());
		assertEquals(expectedUserDetailsList.get(0).getUserName(), actualDetailsList.get(0).getUserName());
	}
	
	@Test
	public void getUsersBasedOnProjectWhenNoProjectExistFailureTest() {
		
		project = null;
		when(mongoTemplate.findOne(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(project);
	
		ProjectOnboardingException actualErrormsg = assertThrows(ProjectOnboardingException.class, () -> {
			projectOnboardingService.getUsersBasedOnProject("P_001");			
		});
		
		assertEquals(ProjectOnboardingConstant.PROJECT_NOT_FOUND, actualErrormsg.getErrorMessage());
	}
	
	@Test
	public void getTasksListByProjectIdUserIdSuccessTest() throws Exception {
		when(mongoTemplate.find(Query.query(new Criteria().andOperator(Criteria.where("userId").is("U11"),
				Criteria.where("projectIds.projectId").is("P_001"))), User.class)).thenReturn(users);
		
		setQueryAndCriteriaForFetchTaskList();
		List<TaskDetails> resultList = projectOnboardingService.fetchTaskList("P_001", "U11");
		assertEquals(taskDetailsList, resultList);
	}

	@Test
	public void getTasksListByProjectIdUserIdSFailureTest() {
		when(mongoTemplate.find(Query.query(new Criteria().andOperator(Criteria.where("userId").is("U11"),
				Criteria.where("projectIds.projectId").is("P_001"))), User.class)).thenReturn(users);

		assertThrows(ProjectOnboardingException.class, () -> {
			projectOnboardingService.fetchTaskList("P_001", "U11");
		});
	}
	
	public void setQueryAndCriteriaForFetchTaskList() {
		Criteria criteria = new Criteria().andOperator(Criteria.where("userId").is("U11"),
				Criteria.where("projectIds.projectId").is("P_001"));
		Query query = new Query();
		query.addCriteria(criteria);
		
		when(projectOnboardingUtil.createQuery(criteria)).thenReturn(query);
	}
	
	public void setQueryAndCriteria() {
		Criteria criteria = Criteria.where("userId").is("U11");
		Query query = new Query();
		query.addCriteria(criteria);
		
		when(projectOnboardingUtil.createQuery(criteria)).thenReturn(query);
		
		criteria = Criteria.where("projectId").is("P_001");
		query = new Query();
		query.addCriteria(criteria);
		
		when(projectOnboardingUtil.createQuery(criteria)).thenReturn(query);

		criteria = new Criteria().andOperator(Criteria.where("userId").is("U12"),
				Criteria.where("roleId").is(type.getTypeId()));
		query = new Query();
		query.addCriteria(criteria);

		when(projectOnboardingUtil.createQuery(criteria)).thenReturn(query);
	}
}
