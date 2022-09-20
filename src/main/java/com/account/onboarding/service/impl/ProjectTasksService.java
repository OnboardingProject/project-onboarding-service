package com.account.onboarding.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.model.Project;
import com.account.onboarding.model.ProjectTaskDetails;
import com.account.onboarding.model.Task;
import com.account.onboarding.model.TaskDetails;
import com.account.onboarding.model.User;
import com.account.onboarding.request.DeleteTaskRequest;
import com.account.onboarding.request.ProjectTaskRequest;
import com.account.onboarding.util.ProjectOnboardingUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sheeba VR
 * @description : Service class for fetch task details based on project.
 * @date : 08 August 2022
 */

@Slf4j
@Service
public class ProjectTasksService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ProjectOnboardingUtil projectOnboardingUtil;

	/* Class for sequential ID generation */
	@Autowired
	SequenceGeneratorService sequenceGenerator;

	/**
	 * @param ProjectId
	 * @return List of Tasks
	 * @throws ProjectOnboardingException
	 * @description Fetch all task based on projectId
	 */
	public List<Task> getProjectTasksByProjectId(String projectId) throws Exception {
		log.info("Method for fetch the project task list started");

		Query query = projectOnboardingUtil.createQuery(Criteria.where("projectId").is(projectId));
		List<Project> project = mongoTemplate.find(query, Project.class);

		if (!CollectionUtils.isEmpty(project)) {
			List<Task> projectTask = new ArrayList<Task>();
			projectTask = project.get(0).getTasks();
			log.info("Return the task list details of selected projet");
			return projectTask;
		} else {
			log.error("ProjectId not found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}
	}

	/* Method for Add New Task to a Project - Also handles edit task */
	public Project addOrEditTask(ProjectTaskRequest projectTaskRequest) throws Exception {
		log.info("In addOrEditTask Service");

		/* Query to find the project exists */
		Query query = projectOnboardingUtil
				.createQuery(Criteria.where("projectId").is(projectTaskRequest.getProjectId()));
		List<Project> project = mongoTemplate.find(query, Project.class);

		/*
		 * Getting list of tasks from the project and checking if taskID exists if
		 * exists do edit , if null then new task ID assigned and add task
		 */
		if (!CollectionUtils.isEmpty(project)) {
			Task taskObj = new Task();
			List<Task> tasks = project.get(0).getTasks();
			int taskId = projectTaskRequest.getTask().getTaskId();
			if (taskId == 0) {
				taskId = sequenceGenerator.generateSequence(Task.SEQUENCE_NAME);
				tasks.add(taskObj);
			} else {
				List<Task> taskList = tasks.stream()
						.filter(t -> t.getTaskId() == (projectTaskRequest.getTask().getTaskId()))
						.collect(Collectors.toList());
				if (taskList.size() > 0)
					taskObj = taskList.get(0);
				else {
					log.warn("Task not found, edit task failed");
					throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND);
				}
			}

			taskObj.setTaskId(taskId);
			taskObj.setTaskName(projectTaskRequest.getTask().getTaskName());
			taskObj.setTaskDesc(projectTaskRequest.getTask().getTaskDesc());
			taskObj.setDesignation(projectTaskRequest.getTask().getDesignation());

			Update update = new Update();
			update.set("tasks", tasks);
			mongoTemplate.upsert(query, update, Project.class);

			log.info("Task Added/Edited successfully");

			/*
			 * Find if any user under the project has the designation provided for the new
			 * task/edited task
			 */
			query = projectOnboardingUtil
					.createQuery(Criteria.where("designation").in(projectTaskRequest.getTask().getDesignation())
							.andOperator(Criteria.where("projectIds.projectId").in(projectTaskRequest.getProjectId())));

			List<User> users = mongoTemplate.find(query, User.class);
			TaskDetails taskDetails = new TaskDetails();

			/* Add/Edit tasks for the user under the project */
			List<TaskDetails> userTaskList = new ArrayList<TaskDetails>();
			for (User user : users) {
				for (ProjectTaskDetails projectTask : user.getProjectIds()) {
					if (projectTask.getProjectId().equals(projectTaskRequest.getProjectId())) {
						userTaskList = projectTask.getTasks();
						List<TaskDetails> userTaskDetails = userTaskList.stream()
								.filter(t -> t.getTaskId() == (projectTaskRequest.getTask().getTaskId()))
								.collect(Collectors.toList());

						if (userTaskDetails.size() > 0) {
							taskDetails = userTaskDetails.get(0);
							taskDetails.setTaskName(projectTaskRequest.getTask().getTaskName());
						} else if (projectTaskRequest.getTask().getTaskId() == 0) {
							taskDetails.setTaskId(taskId);
							taskDetails.setTaskStatus(ProjectOnboardingConstant.YET_TO_START);
							taskDetails.setTaskName(projectTaskRequest.getTask().getTaskName());
							userTaskList.add(taskDetails);
						} else {
							log.error("Project not found, Add/edit task failed");
							throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND);
						}
						projectTask.setTasks(userTaskList);
					}
				}
				Update userUpdate = new Update();
				userUpdate.set("projectIds", user.getProjectIds());
				mongoTemplate.upsert(query, userUpdate, User.class);
			}
			return project.get(0);
		} else {
			log.warn("Project not found, Add/edit task failed");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}
	}

	/**
	 * @param ProjectId
	 * @return List of Tasks
	 * @throws ProjectOnboardingException
	 * @description Delete the tasks based on projectId
	 */

	public List<Task> deleteTask(DeleteTaskRequest deleteTaskRequest) throws Exception {
		log.info("Method for delete the project task based on project Id");

		List<Task> deleteTaskList = new ArrayList<Task>();

		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(deleteTaskRequest.getProjectId()));
		List<Project> project = mongoTemplate.find(query, Project.class);

		if (!CollectionUtils.isEmpty(project)) {
			List<Task> projectTask = project.get(0).getTasks();
			List<Integer> taskIdList = deleteTaskRequest.getTaskIdList();

			// Task Id checking
			log.info("Task Id checking for deletion");
			deleteTaskList = projectTask.stream().filter(s -> taskIdList.contains(s.getTaskId()))
					.collect(Collectors.toList());
			if (deleteTaskList.size() < taskIdList.size()) {
				log.error("Task(s) not found");
				throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND);
			}

			// Remove the selected tasks from the project
			log.info("Delete tasks");
			projectTask.removeAll(deleteTaskList);

			// Find if any user under the project has the same designation then delete tasks
			log.info("Delete the task(s) from User");
			List<User> userList = new ArrayList<User>();

			Query userDeleteQuery = new Query();
			userDeleteQuery.addCriteria(Criteria.where("projectIds.projectId").is(deleteTaskRequest.getProjectId())
					.andOperator(Criteria.where("projectIds.tasks.taskId").in(taskIdList)));

			userList = mongoTemplate.find(userDeleteQuery, User.class);

			for (User user : userList) {
				List<ProjectTaskDetails> projectTaskList = user.getProjectIds().stream()
						.filter(s -> s.getProjectId().equals(deleteTaskRequest.getProjectId()))
						.collect(Collectors.toList());

				List<TaskDetails> tasksForUser = projectTaskList.stream().map(m -> m.getTasks()).flatMap(List::stream)
						.collect(Collectors.toList());

				if (!CollectionUtils.isEmpty(tasksForUser)) {
					List<TaskDetails> userTasksToBeDeleted = tasksForUser.stream()
							.filter(t -> (taskIdList.contains(t.getTaskId()))).collect(Collectors.toList());
					tasksForUser.removeAll(userTasksToBeDeleted);

					Query userUpdateQuery = new Query(
							Criteria.where("projectIds.projectId").is(deleteTaskRequest.getProjectId())
									.andOperator(Criteria.where("userId").is(user.getUserId())));

					Update userUpdate = new Update();
					userUpdate.set("projectIds.$.tasks", tasksForUser);
					mongoTemplate.upsert(userUpdateQuery, userUpdate, User.class);
				}
			}

			// Removes tasks from the project document
			log.info("Update Project Document");
			Update update = new Update();
			update.set("tasks", projectTask);
			mongoTemplate.upsert(query, update, Project.class);

			return projectTask;
		} else {
			log.error("ProjectId not found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}
	}
}
