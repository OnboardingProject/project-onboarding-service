
package com.project.onboarding.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.controller.ProjectTasksController;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectTaskRequest;
import com.project.onboarding.model.Task;

@Service
public class ProjectOnboardingService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectTasksController.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	SequenceGeneratorService sequenceGenerator;

	public Project addTask(ProjectTaskRequest projectTaskRequest) {
		logger.info("In addTask Service");
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(projectTaskRequest.getProjectId()));
		List<Project> project = mongoTemplate.find(query, Project.class);
		if (!CollectionUtils.isEmpty(project)) {
			Task taskObj = new Task();
			List<Task> tasks = project.get(0).getTasks();
			String taskId = projectTaskRequest.getTask().getTaskId();
			if (taskId == null) {
				taskId = "T_" + sequenceGenerator.generateSequence(Task.SEQUENCE_NAME);
			} else {
				List<Task> taskList = tasks.stream()
						.filter(t -> t.getTaskId().equals(projectTaskRequest.getTask().getTaskId()))
						.collect(Collectors.toList());
				if (taskList.size() > 0)
					taskObj = taskList.get(0);
				else {
					logger.warn("Task not found, edit task failed");
					throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND);
				}
			}

			taskObj.setTaskId(taskId);
			taskObj.setName(projectTaskRequest.getTask().getName());
			taskObj.setDescription(projectTaskRequest.getTask().getDescription());
			taskObj.setDesignation(projectTaskRequest.getTask().getDesignation());
			tasks.add(taskObj);

			Update update = new Update();
			update.set("tasks", tasks);
			mongoTemplate.upsert(query, update, Project.class);

			logger.info("Task Added/Edited successfully");
			return project.get(0);
		} else {
			logger.warn("Project not found, Add/edit task failed");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}
	}
}
