package com.project.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.AccountDocument;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.Task;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.User;
import com.project.onboarding.request.DeleteTaskRequest;
import com.project.onboarding.request.ProjectTaskRequest;
import com.project.onboarding.util.ProjectOnboardingUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectTasksServiceTests {

	@InjectMocks
	ProjectTasksService projectTaskService;

	@Mock
	MongoTemplate mongoTemplate;
	
	@Mock
	ProjectOnboardingUtil projectOnboardingUtil;
	
	@Mock
	SequenceGeneratorService sequenceGenerator;

	ProjectTaskRequest projectTaskRequest;
	String designation;
	Task task;
	int taskId;
	Project project;
	List<TaskDetails> userTaskLists = new ArrayList<TaskDetails>();
	TaskDetails userTasks;
	List<User> userList =  new ArrayList<User>();
	User user;
	LocalDateTime localDate = LocalDateTime.now();
	List<AccountDocument> accountDocuments = new ArrayList<AccountDocument>();
	List<ProjectTaskDetails> projectTaskDetails =  new ArrayList<ProjectTaskDetails>();
	List<Integer> deleteList = new ArrayList<Integer>();
	ProjectTaskDetails projectTaskUser ;
	List<Project> projectList = new ArrayList<Project>();
	List<Task> tasksRemaining= new ArrayList<Task>();
	List<TaskDetails> userTasksRemaining=new ArrayList<TaskDetails>();
	List<Task> taskList = new ArrayList<Task>();
	DeleteTaskRequest deleteTaskRequest;

	@BeforeEach
	public void setup() {
		List<String> userIds = new ArrayList<String>();
		userIds.add("U11");
		userIds.add("U12");
		userIds.add("U13");
		
		task = new Task(1, "Seat allocation", "User Need Seat Allocation", "Software Engineer");
		taskList.add(task);
		task = new Task(2, "Chair allocation", "User Need Chair Allocation", "System Analyst");
		taskList.add(task);
		task = new Task(3, "Laptop allocation", "User Need Laptop Allocation", "Software Engineer");
		taskList.add(task);
		
		projectTaskRequest = new ProjectTaskRequest("P_001", new Task(0, "Employee allocation", "Project Needs Employee Allocation", "Software Engineer"));

		project = new Project("P_001", "Employee Allocation", "Employee allocation Project", localDate, "U1", "U12",
				localDate, userIds, taskList);
		projectList.add(project);
		
		userTasks = new TaskDetails(1, "Seat Allocation", "yet to start");
		userTaskLists.add(userTasks);
		userTasks = new TaskDetails(2, "Chair allocation", "yet to start");
		userTaskLists.add(userTasks);
		userTasks = new TaskDetails(3, "Laptop allocation", "yet to start");
		userTaskLists.add(userTasks);
		
		projectTaskUser = new ProjectTaskDetails("P_001",userTaskLists);
		projectTaskDetails.add(projectTaskUser);
		
		user = new User("U11", "Deepa", "Walmart", "Deepa", "G K", "deepa.gudalkallesh@ust.com", "9786277492", "Software Engineer",
				localDate, "U10", "U10", localDate, 1, accountDocuments, "Deepa", projectTaskDetails);
		userList.add(user);
		
		deleteList.add(2);
		deleteList.add(3);
		
		deleteTaskRequest = new DeleteTaskRequest("P_001", deleteList);
		
		tasksRemaining.addAll(taskList);
		tasksRemaining.remove(2);
		tasksRemaining.remove(1);

		userTasksRemaining.addAll(userTaskLists);
		userTasksRemaining.remove(2);
		userTasksRemaining.remove(1);
	}
	
	@DisplayName("JUnit test for getProjectTasksByProjectId success scenario ")
	@Test
	public void getProjectTasksByProjectIdSuccessTest() throws Exception{
		setQueryCriteriaForProject();
		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(projectList);
		List<Task> resultList = projectTaskService.getProjectTasksByProjectId("P_001");
		
		assertEquals(taskList, resultList);
	}

	@DisplayName("JUnit test for getProjectTasksByProjectId failure scenario ")
	@Test
	public void getProjectTasksByProjectIdFailureTest() {
		List<Project> projectList = new ArrayList<Project>();

		setQueryCriteriaForProject();
		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(projectList);

		assertThrows(ProjectOnboardingException.class, () -> {
			projectTaskService.getProjectTasksByProjectId("P_001");
		});
	}
	
	@DisplayName("Junit test for add task success scenario")
	@Test
	public void testAddTasksSuccess() throws Exception{
		int value = 2;
		
		setQueryCriteriaForProject();
		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is(projectTaskRequest.getProjectId())),
				Project.class)).thenReturn(projectList);
		when(sequenceGenerator.generateSequence(Task.SEQUENCE_NAME)).thenReturn((value));
		
		Project actualResult = projectTaskService.addOrEditTask(projectTaskRequest);
		assertEquals(actualResult.getTasks().size(), 4);
	}
	
	@DisplayName("Junit test for add task failure scenario if project not found")
	@Test
	public void testAddTasksFailureIfProjectNotFound() {
		projectList.clear();
		setQueryCriteriaForProject();
		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is("P_001")), Project.class)).thenReturn(projectList).thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND));

		assertThrows(ProjectOnboardingException.class, () -> {
			projectTaskService.addOrEditTask(projectTaskRequest);
		});
	}
	
	@DisplayName("Junit test for add task for a user success scenario")
	@Test
	public void testAddTasksUserSuccess() throws Exception{
		int value = 2;

		setQueryCriteriaForProject();
		userTaskLists.clear();
		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is(projectTaskRequest.getProjectId())),
				Project.class)).thenReturn(projectList);
		when(sequenceGenerator.generateSequence(Task.SEQUENCE_NAME)).thenReturn((value));
		
		setQueryCriteriaForDesignationAndProjectId();
		when(mongoTemplate.find(Query.query(Criteria.where("designation").in(projectTaskRequest.getTask().getDesignation())
				.andOperator(Criteria.where("projectIds.projectId").in(projectTaskRequest.getProjectId()))),User.class)).thenReturn(userList);	
		
		Project actualResult = projectTaskService.addOrEditTask(projectTaskRequest);
		assertEquals(actualResult.getTasks().size(), 4);
		assertEquals(actualResult.getTasks().get(3).getTaskName(), "Employee allocation");
	}
	
	@DisplayName("Junit test for add task for a user failure scenario if project id not found for the user")
	@Test
	public void testAddTasksUserFailureIfProjectIdNotFoundOnUser() throws Exception{
		int value = 2;
		Criteria criteria = Criteria.where("designation").in(projectTaskRequest.getTask().getDesignation())
				.andOperator(Criteria.where("projectIds.projectId").in(projectTaskRequest.getProjectId()));
		setQueryCriteriaForProject();

		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is(projectTaskRequest.getProjectId())),
				Project.class)).thenReturn(projectList);
		when(sequenceGenerator.generateSequence(Task.SEQUENCE_NAME)).thenReturn((value));
		
		setQueryCriteriaForDesignationAndProjectId();
		when(mongoTemplate.find(Query.query(criteria),User.class)).thenReturn(userList);	
		projectTaskUser.setProjectId("P_002");
		
		Project actualResult = projectTaskService.addOrEditTask(projectTaskRequest);
		assertEquals(actualResult.getTasks().size(), 4);
		
		Update userUpdate = new Update();
		userUpdate.set("projectIds", user.getProjectIds().toString());
		verify(mongoTemplate, never()).upsert(Query.query(criteria), userUpdate, User.class);
	}
	
	@DisplayName("Junit test for edit task success scenario")
	@Test
	public void testEditTasksProjectSuccess() throws Exception{
		setQueryCriteriaForProject();
		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is(projectTaskRequest.getProjectId())),
				Project.class)).thenReturn(projectList);
		projectTaskRequest.setTask(new Task(1, "Seat Allocation", "Allocation of seats to resources", "Software Engineer"));

		Project actualResultEdit = projectTaskService.addOrEditTask(projectTaskRequest);
		assertEquals(actualResultEdit.getTasks().get(0).getTaskDesc(), "Allocation of seats to resources");
		assertEquals(actualResultEdit.getTasks().size(), 3);
	}
	
	@DisplayName("Junit test for edit task failure scenario if no task found")
	@Test
	public void testEditTasksProjectFailureIfNoTaskfound() {
		taskList.get(0).setTaskId(3);
		projectTaskRequest.getTask().setTaskId(1);
		setQueryCriteriaForProject();
		Criteria criteria = Criteria.where("projectId").is("P_001");
		
		when(mongoTemplate.find(Query.query(criteria), Project.class)).thenReturn(projectList).thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND));

		assertThrows(ProjectOnboardingException.class, () -> {
			projectTaskService.addOrEditTask(projectTaskRequest);
		});
		
		Update update = new Update(); 
		update.set("tasks", taskList);
		verify(mongoTemplate, never()).upsert(Query.query(criteria), update, Project.class);
	}
	
	@DisplayName("Junit test for edit task on user success scenario")
	@Test
	public void testEditTasksUserSuccess() throws Exception{
		Criteria criteria = Criteria.where("designation").in(projectTaskRequest.getTask().getDesignation())
				.andOperator(Criteria.where("projectIds.projectId").in(projectTaskRequest.getProjectId()));
		
		setQueryCriteriaForProject();
		setQueryCriteriaForDesignationAndProjectId();

		projectTaskRequest.getTask().setTaskId(1);
		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is(projectTaskRequest.getProjectId())),
				Project.class)).thenReturn(projectList);
		
		when(mongoTemplate.find(Query.query(criteria), User.class)).thenReturn(userList);	
		
		Project actualResult = projectTaskService.addOrEditTask(projectTaskRequest);
		assertEquals(actualResult.getTasks().size(), 3);
		
		Update userUpdate = new Update();
		userUpdate.set("projectIds", user.getProjectIds());
		verify(mongoTemplate, times(1)).upsert(Query.query(criteria), userUpdate, User.class);
	}
	
	@DisplayName("Junit test for edit task on user failure scenario if task is not found")
	@Test
	public void testEditTasksUserFailureIfTaskNotFound() throws Exception{
		Criteria criteria = Criteria.where("designation").in(projectTaskRequest.getTask().getDesignation())
				.andOperator(Criteria.where("projectIds.projectId").in(projectTaskRequest.getProjectId()));
		
		setQueryCriteriaForProject();
		setQueryCriteriaForDesignationAndProjectId();

		projectTaskRequest.getTask().setTaskId(1);
		when(mongoTemplate.find(Query.query(Criteria.where("projectId").is(projectTaskRequest.getProjectId())),
				Project.class)).thenReturn(projectList);
		
		when(mongoTemplate.find(Query.query(criteria), User.class)).thenReturn(userList);	
		userTaskLists.get(0).setTaskId(2);	
		
		assertThrows(ProjectOnboardingException.class, () -> {
			projectTaskService.addOrEditTask(projectTaskRequest);
		});
		
		
		Update userUpdate = new Update();
		userUpdate.set("projectIds", user.getProjectIds());
		verify(mongoTemplate, never()).upsert(Query.query(criteria), userUpdate, User.class);
	}
	
	@DisplayName("JUnit test for delete tasks from Project success scenario ")
	@Test
	public void deleteProjectTasksByProjectIdSuccessTest() throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is("P_001"));
		when(mongoTemplate.find(query, Project.class)).thenReturn(projectList);
		setQueryCriteriaForProject();
		
		List<Task> actualTasks = projectTaskService.deleteTask(deleteTaskRequest);
		assertEquals(actualTasks.size(), 1);
	    assertEquals(actualTasks.get(0).getTaskId(), tasksRemaining.get(0).getTaskId());
		
		}
	@DisplayName("JUnit test for delete tasks from Project failure scenario")
	@Test
	public void  deleteProjectTasksByProjectIdFailureTest() {
		projectList.clear();
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is("P_001"));
		when(mongoTemplate.find(query, Project.class)).thenReturn(projectList);
		setQueryCriteriaForProject();
		
		assertThrows(ProjectOnboardingException.class, () -> {
			projectTaskService.deleteTask(deleteTaskRequest);
			
		});
		verify(mongoTemplate, never()).upsert(query, update,Project.class);
	}
	
	@DisplayName("JUnit test for delete tasks no task found in user failure scenario")
	@Test
	public void deleteProjectTasks_TaskNotFoundInUser() throws Exception {
		userTaskLists.clear();
		Query query = new Query();
		Update update = new Update();
		update.set("projectIds.$.tasks", userTasksRemaining);
		
		setQueryCriteriaForProject();
		setQueryCriteriaForProjectIdAndTaskIdForDeletion();

		query.addCriteria(Criteria.where("projectId").is("P_001"));
		when(mongoTemplate.find(query, Project.class)).thenReturn(projectList);
		
		Query userDeleteQuery = new Query();
        userDeleteQuery.addCriteria(Criteria.where("projectIds.projectId").is(deleteTaskRequest.getProjectId())
                                               .andOperator(Criteria.where("projectIds.tasks.taskId").in(deleteList)));
        when(mongoTemplate.find(userDeleteQuery, User.class)).thenReturn(userList);
    
        List<Task> actualTasks = projectTaskService.deleteTask(deleteTaskRequest);
        assertEquals(actualTasks.size(), 1);
	    assertEquals(actualTasks.get(0).getTaskId(), tasksRemaining.get(0).getTaskId());
	    verify(mongoTemplate, never()).upsert(query, update,User.class);
	}

	@DisplayName("JUnit test for delete tasks from Project task not found Success scenario ")
	@Test
	public void deleteProjectTasksByProjectIdTaskNotFoundSuccessTest() {
		Query query = new Query();
		Update update = new Update();
		projectList.get(0).getTasks().remove(2);
		
		setQueryCriteriaForProject();
		query.addCriteria(Criteria.where("projectId").is("P_001"));
		when(mongoTemplate.find(query, Project.class)).thenReturn(projectList);
		
		assertThrows(ProjectOnboardingException.class, () -> {
			projectTaskService.deleteTask(deleteTaskRequest);
		});
		verify(mongoTemplate, never()).upsert(query, update,Project.class);
	}

	@DisplayName("Junit test for delete task for a user success scenario")
	@Test
	public void testDeleteTasksUserSuccess() throws Exception {
		Query query = new Query();
		Update userUpdate = new Update();
		userUpdate.set("projectIds.$.tasks",userTasksRemaining);
		setQueryCriteriaForProject();
		setQueryCriteriaForProjectIdAndTaskIdForDeletion();
		setQueryCriteriaForProjectIdAndUserId();
		
		query.addCriteria(Criteria.where("projectId").is("P_001"));
		when(mongoTemplate.find(query, Project.class)).thenReturn(projectList);
	
		Query userDeleteQuery = new Query();
        userDeleteQuery.addCriteria(Criteria.where("projectIds.projectId").is(deleteTaskRequest.getProjectId())
                                               .andOperator(Criteria.where("projectIds.tasks.taskId").in(deleteList)));
        when(mongoTemplate.find(userDeleteQuery, User.class)).thenReturn(userList);
    	Query userUpdateQuery = new Query(Criteria.where("projectIds.projectId").is("P_001")
					.andOperator(Criteria.where("userId").is("U11")));
    	when(mongoTemplate.find(userUpdateQuery,User.class)).thenReturn(userList);
    	  
		List<Task> actualResult = projectTaskService.deleteTask(deleteTaskRequest);
		assertEquals(actualResult.size(), 1);
		verify(mongoTemplate, times(1) ).upsert(userUpdateQuery, userUpdate, User.class);
		
	}
	
	@DisplayName("JUnit test for delete tasks from User failure scenario")
	@Test
	public void  testDeleteTasksUserFailure() throws Exception {
		userList.clear();
		
		setQueryCriteriaForProject();
		setQueryCriteriaForProjectIdAndTaskIdForDeletion();
		setQueryCriteriaForProjectIdAndUserId();
		
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is("P_001"));
		when(mongoTemplate.find(query, Project.class)).thenReturn(projectList);
	
		Query userDeleteQuery = new Query();
        userDeleteQuery.addCriteria(Criteria.where("projectIds.projectId").is(deleteTaskRequest.getProjectId())
                                               .andOperator(Criteria.where("projectIds.tasks.taskId").in(deleteList)));
        when(mongoTemplate.find(userDeleteQuery, User.class)).thenReturn(userList);
               
    	Query userUpdateQuery = new Query(Criteria.where("projectIds.projectId").is("P_001")
					.andOperator(Criteria.where("userId").is("U11")));
    	when(mongoTemplate.find(userUpdateQuery,User.class)).thenReturn(userList);
    	
    	List<Task> actualTasks = projectTaskService.deleteTask(deleteTaskRequest);
		assertEquals(actualTasks.size(), 1);
	    assertEquals(actualTasks.get(0).getTaskId(),tasksRemaining.get(0).getTaskId());
	}
	
	
	public void setQueryCriteriaForProject() {
		Criteria criteria = Criteria.where("projectId").is("P_001");
		Query query = new Query();
		query.addCriteria(criteria);

		when(projectOnboardingUtil.createQuery(criteria)).thenReturn(query);
	}
	
	public void setQueryCriteriaForDesignationAndProjectId() {
		Criteria criteria = Criteria.where("designation").in(projectTaskRequest.getTask().getDesignation())
				.andOperator(Criteria.where("projectIds.projectId").in(projectTaskRequest.getProjectId()));
		Query query = new Query();
		query.addCriteria(criteria);

		when(projectOnboardingUtil.createQuery(criteria)).thenReturn(query);
	}
	
	public void setQueryCriteriaForProjectIdAndTaskIdForDeletion() {
		Criteria criteria = Criteria.where("projectIds.projectId").is(deleteTaskRequest.getProjectId())
                .andOperator(Criteria.where("projectIds.tasks.taskId").in(deleteList));
		Query query = new Query();
		query.addCriteria(criteria);

		when(projectOnboardingUtil.createQuery(criteria)).thenReturn(query);
	}
	
	public void setQueryCriteriaForProjectIdAndUserId() {
		Criteria criteria = Criteria.where("projectIds.projectId").is("P_001")
				.andOperator(Criteria.where("userId").is("U11"));
		Query query = new Query();
		query.addCriteria(criteria);

		when(projectOnboardingUtil.createQuery(criteria)).thenReturn(query);
	}
}
