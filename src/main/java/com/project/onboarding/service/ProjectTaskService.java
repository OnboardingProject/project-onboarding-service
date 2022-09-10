package com.project.onboarding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.DeleteTaskRequest;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.Task;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.User;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sheeba V R
 * @description : Service class for fetch task details based on project.
 * @date : 08 August 2022
 */
@Slf4j
@Service
public class ProjectTaskService {

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(ProjectTaskService.class);

	/**
	 * @param ProjectId
	 * @return List of Tasks
	 * @throws ProjectOnboardingException
	 * @description Fetch all task based on projectId
	 */
	public List<Task> getProjectTasksByProjectId(String projectId) throws Exception 
	{
		logger.info("Method for fetch the project task list started");
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(projectId));
		List<Project> project = mongoTemplate.find(query, Project.class);
		if (!CollectionUtils.isEmpty(project))
		{
			List<Task> projectTask = new ArrayList<Task>();
			projectTask = project.get(0).getTasks();
			logger.info("Return the task list details of selected projet");
			return projectTask;

		} else
		{
			logger.error("ProjectId not found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECTIDNOTFOUND, HttpStatus.CONFLICT);
		}
	}

	
	/**
	 * @param ProjectId
	 * @return List of Tasks
	 * @throws ProjectOnboardingException
	 * @description Delete the tasks based on projectId
	 */

	public List<Task> deleteTask(DeleteTaskRequest deleteTaskRequest) 
	{
			log.info("Method for delete the project task based on project Id");
			List<Task> deleteTaskList = new ArrayList<Task>();
			Query query = new Query();
			query.addCriteria(Criteria.where("projectId").is(deleteTaskRequest.getProjectId()));
			List<Project> project = mongoTemplate.find(query, Project.class);
			if (!CollectionUtils.isEmpty(project))
			{
				List<Task> projectTask = project.get(0).getTasks();
				List<Integer> taskIdList = deleteTaskRequest.getTaskIdList();
				// Task Id checking
				log.info("Task Id checking for deletion");
				deleteTaskList = projectTask.stream().filter(s -> taskIdList.contains(s.getTaskId()))
						.collect(Collectors.toList());
				if (deleteTaskList.size() < taskIdList.size()) 
				{
						log.error("Task(s) not found");
						throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND,HttpStatus.CONFLICT);
				}
			 
			// Remove the selected tasks from the project
				log.info("Delete tasks");
				projectTask.removeAll(deleteTaskList);
	
			//Delete the tasks from Users
			 log.info("Delete the task(s) from User");
            List<User> userList = new ArrayList<User>();
            Query userDeleteQuery = new Query();
            userDeleteQuery.addCriteria(Criteria.where("projectIds.projectId").is(deleteTaskRequest.getProjectId())
                                                .andOperator(Criteria.where("projectIds.tasks.taskId").in(taskIdList)));
            userList = mongoTemplate.find(userDeleteQuery, User.class);
            for (User user : userList)
            {  List<ProjectTaskDetails> projectTaskList = user.getProjectIds()
            		               .stream().filter(s -> s.getProjectId().equals(deleteTaskRequest.getProjectId())).collect(Collectors.toList());
                 List<TaskDetails> tasksForUser = projectTaskList.stream()
                		                                         .map(m -> m.getTasks())
                		                                         .flatMap(List::stream).collect(Collectors.toList());
                 if (!CollectionUtils.isEmpty(tasksForUser))
                 {
 					List<TaskDetails> userTasksToBeDeleted = tasksForUser.stream().filter(t -> (taskIdList.contains(t.getTaskId())))
 																				.collect(Collectors.toList());
 				tasksForUser.removeAll(userTasksToBeDeleted);
 				
 				Query userUpdateQuery = new Query(Criteria.where("projectIds.projectId").is(deleteTaskRequest.getProjectId())
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
 				}
 				 else
 				  {
 				  log.error("ProjectId not found");
 				  throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECTIDNOTFOUND ,HttpStatus.CONFLICT);
 				  }
 		}


     
            
   
}