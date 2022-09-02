package com.project.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.User;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectOnboardingServiceTests {
	@Mock
	private MongoTemplate mongoTemplate;

	@InjectMocks
	private ProjectOnboardingService projectOnboardingService;

	

	@Test
	public void getTasksListByProjectIdUserIdSuccessTest() throws Exception {
		List<User> userList = new ArrayList<User>();

		List<ProjectTaskDetails> ProIdDetails = new ArrayList<ProjectTaskDetails>();

		List<TaskDetails> taskList = new ArrayList<TaskDetails>();
		
		User user = new User();
		user.setUserId("U11");
		ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();
		projectTaskDetails.setProjectId("P_001");
		TaskDetails tasks = new TaskDetails();
		tasks.setTaskId(001);

		taskList.add(tasks);
		projectTaskDetails.setTasks(taskList);
		ProIdDetails.add(projectTaskDetails);
		user.setProjectIds(ProIdDetails);
		userList.add(user);
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("userId").is("U11"),
				Criteria.where("projectIds.projectId").is("P_001")));

		Mockito.when(mongoTemplate.find(query, User.class)).thenReturn(userList);
		List<TaskDetails> resultList = projectOnboardingService.fetchTaskList("P_001", "U11");
		assertEquals(taskList, resultList);
	}

	@Test
	public void getTasksListByProjectIdUserIdSFailureTest() {
		List<User> userList = new ArrayList<User>();

		List<ProjectTaskDetails> ProIdDetails = new ArrayList<ProjectTaskDetails>();

		List<TaskDetails> taskList = new ArrayList<TaskDetails>();

		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("userId").is("U11"),
				Criteria.where("projectIds.projectId").is("P_001")));
		Mockito.when(mongoTemplate.find(query, User.class)).thenReturn(userList);

		Assertions.assertThrows(ProjectOnboardingException.class, () -> {
			projectOnboardingService.fetchTaskList("P_001", "U11");
		});
	}
	
	
	
}
