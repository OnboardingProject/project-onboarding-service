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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.controller.ProjectOnboardingController;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.SaveTaskStatusRequest;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.TaskStatusRequest;
import com.project.onboarding.model.User;

/**
 * @author Athira Rajan
 * @description : Service class for Project Onboarding.
 * @date : 12 August 2022
 */

@Service
public class ProjectOnboardingService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectOnboardingController.class);
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	SequenceGeneratorService sequenceGenerator;
	
	/**
	 * @param projectId, resourceId
	 * @return Task List object
	 * @throws ProjectOnboardingException
	 * @description : Show Task List associated with Project and Resource
	 */

	public List<TaskDetails> fetchTaskList(String projectId,String resourceId) {
		logger.info("In fetch task list Service");
		Query query = new Query();
		
		query.addCriteria(
			    new Criteria().andOperator(
			        Criteria.where("userId").is(resourceId),
			        Criteria.where("projectIds.projectId").is(projectId)
			    )
			);
		
		List<User> users = mongoTemplate.find(query, User.class);
		if (!CollectionUtils.isEmpty(users)) {
			List<ProjectTaskDetails> projectIds1 = users.get(0).getProjectIds();
			List<TaskDetails> tasks = projectIds1.get(0).getTasks();
			logger.info("Task fetched successfully");
			return tasks;	
		}
			else
			{
				logger.error("User or Project or Tasks associated are not found, fetching task list failed");
				throw new ProjectOnboardingException(ProjectOnboardingConstant.TASKLIST_NOT_FOUND,HttpStatus.CONFLICT);
			}
	}
	
	/**
	 * @param projectId, userId, taskId, taskStatus
	 * @return Task List object
	 * @throws ProjectOnboardingException
	 * @description : Save Task Status based on User and project Tasks.
	 */
	
	public List<TaskDetails> saveStatus(SaveTaskStatusRequest saveTaskStatusRequest) {
		logger.info("Method for saving the task status");
		Query query = new Query();
		
		query.addCriteria(
		    new Criteria().andOperator(	
		        Criteria.where("userId").is(saveTaskStatusRequest.getUserId()),
		        Criteria.where("projectIds.projectId").is(saveTaskStatusRequest.getProjectId())
		   ) );
		
		
		List<User> users = mongoTemplate.find(query, User.class);
		
		if (!CollectionUtils.isEmpty(users)) {
			List<ProjectTaskDetails> projectIds1 = users.get(0).getProjectIds();
			List<TaskDetails> tasks = projectIds1.get(0).getTasks();
			for(TaskStatusRequest taskStatusRequest:saveTaskStatusRequest.getTaskStatusRequest()) {
				
				List<TaskDetails> taskWithGivenTaskId = tasks.stream().filter(s->s.getTaskId()==taskStatusRequest.getTaskId()).collect(Collectors.toList());
				if(!CollectionUtils.isEmpty(taskWithGivenTaskId)) {
					taskWithGivenTaskId.get(0).setTaskStatus(taskStatusRequest.getTaskStatus());	
				Update update = new Update();
					update.set("projectIds", projectIds1);
					mongoTemplate.updateFirst(query, update, User.class);
				} else {
					logger.error("Task not found");
					throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND,HttpStatus.CONFLICT);
				}
			}
			
			return tasks;
		
		} else {
			logger.error("ProjectId not found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND,HttpStatus.CONFLICT);
		}
		
	}
}

