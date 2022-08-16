package com.project.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.AccountDocument;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.ProjectTasksOverview;
import com.project.onboarding.model.StatusReport;
import com.project.onboarding.model.Task;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.User;

@ExtendWith(MockitoExtension.class)
public class OnboardingStatusServiceTests {
	@Mock
    private MongoTemplate mongoTemplate;
	
    @InjectMocks
    private OnboardingStatusService onboardingStatusService;
    
	public static Map<String, Integer> TASK_STATUS_PERCENTAGE;
	
	List<Project> projects = new ArrayList<Project>();
	
	List<String> userIds = new ArrayList<String>();
	
	List<String> designations = new ArrayList<String>();
	
	List<Task> tasks = new ArrayList<Task>();
	
	List<User> users1 = new ArrayList<User>();
	
	List<User> users2 = new ArrayList<User>();

	List<User> users3 = new ArrayList<User>();
	
	List<AccountDocument> accountDocuments = new ArrayList<AccountDocument>();
	
	List<ProjectTaskDetails> projectTaskDetailsList = new ArrayList<ProjectTaskDetails>();
	
	List<TaskDetails> taskDetailsList = new ArrayList<TaskDetails>();
	
	TaskDetails taskDetails = new TaskDetails();
	
	ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();
	
	StatusReport statusReport;
	
	User user1, user2, user3;
	
    @BeforeAll
    public static void init() {
    	Map<String, Integer> taskPercentageList = new HashMap<String, Integer>();
		taskPercentageList.put("Yet to start", 0);
		taskPercentageList.put("In-progress", 50);
		taskPercentageList.put("Done", 100);
		TASK_STATUS_PERCENTAGE = Collections.unmodifiableMap(taskPercentageList);
    }
    
    @BeforeEach
    public void setup(){
    	designations.add("Software Developer");
    	Task task = new Task("T_1", "seat allocation", "seat allocation", designations); 
    	tasks.add(task);
    	
    	userIds.add("U13");
    	userIds.add("U14");
    	userIds.add("U15");
    	
    	LocalDateTime dateTime = LocalDateTime.now();
    	Project project = new Project("P_1", "Onboarding", "Onboarding resources", dateTime, "U13", "U13", dateTime, userIds, tasks);
    	projects.add(project);
    	
    	AccountDocument accountDocument = new AccountDocument("D_1", "read");
    	accountDocuments.add(accountDocument);
    	
    	projectTaskDetails = new ProjectTaskDetails("P_1", taskDetailsList);
    	long phoneNumber = 1245232542;
    	
    	user1 = new User("U13", "Thara", "Test", "Thara", "P", "thara.pattuamveetil@ust.com", phoneNumber, "Project head", dateTime, "Admin", "Admin", dateTime, "Project Admin", accountDocuments, "Thara", projectTaskDetailsList);
    	user2 = new User("U14", "Sumitha", "Test", "Sumitha", "Vidhukumar", "sumitha.vidhukumar@ust.com", phoneNumber, "Project manager", dateTime, "U13", "U13", dateTime, "Project Owner", accountDocuments, "Thara, Sumitha", projectTaskDetailsList);
    	
    	taskDetails = new TaskDetails("T_1", "seat allocation", "In-progress");
    	taskDetailsList.add(taskDetails);
    	projectTaskDetails = new ProjectTaskDetails("P_1", taskDetailsList);
    	projectTaskDetailsList.add(projectTaskDetails);
    	user3 = new User("U15", "Janaki", "Test", "Janaki", "Perumal", "janaki.perumal@ust.com", phoneNumber, "Software Engineer", dateTime, "U14", "U14", dateTime, "Resource", accountDocuments, "Thara, Sumitha, Janaki", projectTaskDetailsList);

    	ProjectTasksOverview projectTasksOverview = new ProjectTasksOverview("U15", "Janaki Perumal", 50);
    	statusReport = new StatusReport("Onboarding", "Sumitha Vidhukumar", "Onboarding resources", projectTasksOverview);

    	users2.add(user2);
    	users3.add(user3);
    	
    }
    
    @AfterEach
	public void tearDown() {
    	designations.clear();
    	tasks.clear();
    	userIds.clear();
    	projects.clear();
    	users1.clear();
    	users2.clear();
    	users3.clear();
    	accountDocuments.clear();
    	projectTaskDetailsList.clear();
    	taskDetailsList.clear();
    }
    
    @DisplayName("JUnit test for getPreviewStatusReport success scenario")
    @Test
    public void givenProjectIdAndUserId_whenGetPreviewStatusReport_thenReturnStatusReportObject() throws Exception{
    	when(mongoTemplate.find(Query.query(Criteria.where("userId").is("U15")), User.class)).thenReturn(users3);
    	
    	stubQueryValues();
    	
    	StatusReport statusReportFromService = onboardingStatusService.getPreviewStatusReport("P_1", "U15");
        assertNotNull(statusReportFromService);
        assertEquals(statusReport.getProjectTasksOverview().getUsername(), statusReportFromService.getProjectTasksOverview().getUsername());
        assertEquals(statusReport.getProjectTasksOverview().getTaskPercentage(), statusReportFromService.getProjectTasksOverview().getTaskPercentage());
    }
    
	
	@DisplayName("JUnit test for getPreviewStatusReport failure scenario if the project not exists")
	@Test
	public void givenProjectIdAndUserId_whenGetPreviewStatusReport_AndProjectNotExists_thenThrowException() {
		projects.clear();
    	when(mongoTemplate.find(Query.query(Criteria.where("projectId").is("P_1")), Project.class)).thenReturn(projects).thenThrow(new ProjectOnboardingException("Project not found"));

		assertThrows(ProjectOnboardingException.class, () -> {
			onboardingStatusService.getPreviewStatusReport("P_1", "U15");
		});
	}
	
	@DisplayName("JUnit test for getPreviewStatusReport failure scenario if the user not exists")
	@Test
	public void givenProjectIdAndUserId_whenGetPreviewStatusReport_AndUserNotExists_thenThrowException() {
    	when(mongoTemplate.find(Query.query(Criteria.where("projectId").is("P_1")), Project.class)).thenReturn(projects);
		when(mongoTemplate.find(Query.query(Criteria.where("userId").is("U15")), User.class)).thenReturn(users1).thenThrow(new ProjectOnboardingException("User not found"));

		assertThrows(ProjectOnboardingException.class, () -> {
			onboardingStatusService.getPreviewStatusReport("P_1", "U15");
		});
	}
	
	@DisplayName("JUnit test for getPreviewStatusReport success scenario if the user does not have any tasks associated")
	@Test
	public void givenProjectIdAndUserId_whenGetPreviewStatusReport_AndUserDoesNotHaveTasks_thenReturnStatusReportObject() throws Exception{
		
		List<TaskDetails> taskDetailsList = new ArrayList<TaskDetails>();
		user3.getProjectIds().get(0).setTasks(taskDetailsList);
		
    	when(mongoTemplate.find(Query.query(Criteria.where("userId").is("U15")), User.class)).thenReturn(users3);
    	  
    	stubQueryValues();
    	
    	StatusReport statusReportFromService = onboardingStatusService.getPreviewStatusReport("P_1", "U15");
        assertNotNull(statusReportFromService);
        assertEquals(statusReport.getProjectTasksOverview().getUsername(), statusReportFromService.getProjectTasksOverview().getUsername());
        assertEquals(0, statusReportFromService.getProjectTasksOverview().getTaskPercentage());

	}
	
	@DisplayName("JUnit test for getPreviewStatusReport success scenario if the user does not have any projects associated")
	@Test
	public void givenProjectIdAndUserId_whenGetPreviewStatusReport_AndUserDoesNotProjectsAssociated_thenReturnStatusReportObject() throws Exception{
		List<ProjectTaskDetails> projectTaskDetailsList = new ArrayList<ProjectTaskDetails>();
		user3.setProjectIds(projectTaskDetailsList);

    	when(mongoTemplate.find(Query.query(Criteria.where("userId").is("U15")), User.class)).thenReturn(users3);

    	stubQueryValues();
    	
    	StatusReport statusReportFromService = onboardingStatusService.getPreviewStatusReport("P_1", "U15");
        assertNotNull(statusReportFromService);
        assertEquals(statusReport.getProjectTasksOverview().getUsername(), statusReportFromService.getProjectTasksOverview().getUsername());
        assertEquals(0, statusReportFromService.getProjectTasksOverview().getTaskPercentage());

	}
	  
	public void stubQueryValues() {
		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is("P_1")), Project.class)).thenReturn(projects);

		when(mongoTemplate.find(Query.query(new Criteria().andOperator(Criteria.where("userId").is("U13"),
				Criteria.where("roleId").is("Project Owner"))), User.class)).thenReturn(users1);
    	when(mongoTemplate.find(Query.query(new Criteria().andOperator(Criteria.where("userId").is("U14"),
				Criteria.where("roleId").is("Project Owner"))), User.class)).thenReturn(users2);
    	when(mongoTemplate.find(Query.query(new Criteria().andOperator(Criteria.where("userId").is("U15"),
				Criteria.where("roleId").is("Project Owner"))), User.class)).thenReturn(users1);
	}
	 
}
