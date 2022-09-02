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
import com.project.onboarding.model.ProjectDetails;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.Task;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.Types;
import com.project.onboarding.model.User;
import com.project.onboarding.model.UserDetails;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectOnboardingServiceTest {
	
	@Mock
	private MongoTemplate mongoTemplate;
	
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
	
	ProjectDetails expectedProjectDetails = new ProjectDetails();
	List<ProjectDetails> expectedProjectDetailsList = new ArrayList<>();
	
	UserDetails expectedUserDetails = new UserDetails();
	List<UserDetails> expectedUserDetailsList = new ArrayList<>();
	
	@BeforeEach
	public void setup() {
		
		userIds.add("U11");
		userIds.add("U12");
		
		task = new Task(1, "Seat allocation", "User needs a chair", "Java Developer");
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
		
		expectedProjectDetails = new ProjectDetails("P_001", "Employee");
		expectedProjectDetailsList.add(expectedProjectDetails);
		
		expectedUserDetails = new UserDetails("U12", "Anu Mathew");
		expectedUserDetailsList.add(expectedUserDetails);
	}
	
	@Test
	public void getProjectsBasedOnUserSuccessTest() {
		
		when(mongoTemplate.findOne(Query.query(Criteria.where("userId").is("U11")), User.class)).thenReturn(user);
		when(mongoTemplate.findOne(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(project);

        List<ProjectDetails> actualDetailsList = projectOnboardingService.getProjectsBasedOnUser("U11");
  
		//assertEquals(expectedDetailsList, actualDetailsList);
        
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
		when(mongoTemplate.findOne(Query.query(Criteria.where("userId").is("U11")), User.class)).thenReturn(user);
		when(mongoTemplate.findOne(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(project);
		
		ProjectOnboardingException actualErrormsg = assertThrows(ProjectOnboardingException.class, () -> {
			projectOnboardingService.getProjectsBasedOnUser("U11");			
		});
		
		assertEquals(ProjectOnboardingConstant.PROJECT_NOT_FOUND, actualErrormsg.getErrorMessage());
	}
	
	@Test
	public void getUsersBasedOnProjectSuccessTest() {
		
		when(mongoTemplate.findOne(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(project);		
		when(mongoTemplate.findOne(Query.query(new Criteria().andOperator(Criteria.where("typeName").is("ROLE"),Criteria.where("typeDesc").is("Resource"))), Types.class)).thenReturn(type);		
		when(mongoTemplate.findOne(Query.query(new Criteria().andOperator(Criteria.where("userId").is("U12"),Criteria.where("roleId").is(type.getTypeId()))), User.class)).thenReturn(user1);
				
		List<UserDetails> actualDetailsList = projectOnboardingService.getUsersBasedOnProject("P_001");
		
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
	
		

}
