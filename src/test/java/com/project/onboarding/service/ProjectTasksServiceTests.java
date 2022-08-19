package com.project.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.Task;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectTasksServiceTests {

	@InjectMocks
	ProjectTaskService projectTaskService;

	@Mock
	MongoTemplate mongoTemplate;

	@DisplayName("JUnit test for getProjectTasksByProjectId success scenario ")
	@Test
	public void getProjectTasksByProjectIdSuccessTest() {
		List<Project> projectList = new ArrayList<Project>();

		List<Task> taskList = new ArrayList<Task>();

		Project project = new Project();
		project.setProjectId("P_001");
		Task task = new Task();
		task.setTaskId(9);
		taskList.add(task);
		project.setTasks(taskList);
		projectList.add(project);

		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is("P_001"));
		Mockito.when(mongoTemplate.find(query, Project.class)).thenReturn(projectList);
		List<Task> resultList = projectTaskService.getProjectTasksByProjectId("P_001");
		assertEquals(taskList, resultList);
	}

	@DisplayName("JUnit test for getProjectTasksByProjectId failure scenario ")
	@Test
	public void getProjectTasksByProjectIdFailuerTest() {
		List<Project> projectList = new ArrayList<Project>();

		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is("P_001"));
		Mockito.when(mongoTemplate.find(query, Project.class)).thenReturn(projectList);

		Assertions.assertThrows(ProjectOnboardingException.class, () -> {
			projectTaskService.getProjectTasksByProjectId("P_001");
		});
	}

}
