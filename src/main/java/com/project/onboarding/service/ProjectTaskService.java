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
import java.util.stream.Stream;

import javax.validation.Valid;

import java.util.ArrayList;


import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.controller.ProjectTasksController;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.DeleteTaskRequest;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.Task;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.User;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author UST
 * @description : Service class for fetch task details based on project.
 * @date : 08 August 2022
 */
@Service
@Validated
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
	public List<Task> getProjectTasksByProjectId(String projectId)
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
	 * @description Delete the task based on projectId
	 */
	
	public List<Task> deleteTask(@Valid  DeleteTaskRequest deleteTaskRequest )
	{
			List<Task> deleteTaskList=new ArrayList<Task>();
			List<Task> deletedTaskListNew=new ArrayList<Task>();
		
		/*Project ID checking*/
		if (deleteTaskRequest.getProjectId().trim()=="")
		{
			throw new ProjectOnboardingException(ProjectOnboardingConstant.INVALIDPROJECTID, HttpStatus.CONFLICT);
		}
				
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(deleteTaskRequest.getProjectId()));
		List<Project> project = mongoTemplate.find(query, Project.class);
	
		if (!CollectionUtils.isEmpty(project))
				{
				List<Task> projectTask = project.get(0).getTasks();
				List<Integer> taskIdList=deleteTaskRequest.getTaskIdList();
		
	/*Task Id checking*/
			
				for(int taskId:taskIdList)
				{
					long idCount= projectTask.stream().filter(s->s.getTaskId()==taskId).count();
					if (idCount<=0)	{
							throw new ProjectOnboardingException(ProjectOnboardingConstant.INVALIDTASKID , HttpStatus.CONFLICT);
									}
				}
	/*Remove the selected tasks from the project*/					
				for(int taskId:taskIdList) {
						deleteTaskList= projectTask.stream().filter(s->s.getTaskId()==taskId).collect(Collectors.toList());
						deletedTaskListNew.addAll(deleteTaskList);
						projectTask.remove(deleteTaskList.get(0));
											}
				
	/*====================================================================================================================*/			
			
	/*Find if any user under the project has the designation then delete tasks */
				int i=0;
				  for(int taskuserId:taskIdList)
				  {	  	
					     Query userDeleteQuery=new Query();		
				        
				         userDeleteQuery.addCriteria(Criteria.where("designation").in(deletedTaskListNew.get(i).getDesignation())
		        		 .andOperator(Criteria.where("projectIds.projectId").in(deleteTaskRequest.getProjectId())));
					

						List<User> userList=mongoTemplate.find(userDeleteQuery,User.class);
											   
					   if (!CollectionUtils.isEmpty(userList))
					   {
						   List<TaskDetails> userTaskList = new ArrayList<TaskDetails>();
						   for(User user:userList)
						   {						 					 
							   for (ProjectTaskDetails projectTaskList : user.getProjectIds()) 
							   {
								   if (projectTaskList.getProjectId().equals(deleteTaskRequest.getProjectId()))
								   {
										userTaskList =projectTaskList.getTasks();   
										List<TaskDetails> userTaskDetails = userTaskList.stream()
												.filter(t -> (t.getTaskId()==(taskuserId))).collect(Collectors.toList());
										if (userTaskDetails.isEmpty()==false)
										{
											userTaskDetails.remove(deletedTaskListNew.get(i));
											 i=i+1;
											 
										}
										projectTaskList.setTasks(userTaskDetails);
										   Update userUpdate=new Update();
										   //   userUpdate.set("projectIds", projectTaskList);
										      userUpdate.set("projectIds", user.getProjectIds());
										//		mongoTemplate.upsert(userDeleteQuery, update, User.class);

										   
								    } 
								  
								 }
							  
						      }
						   
					       }
					}
         
				
				
				
				
	/*====================================================================================================================*/				
			  /*Update the project document*/
			//    Update update = new Update();
			//    update.set("tasks", projectTask);
			  //  mongoTemplate.upsert(query, update, Project.class);
			
			    
			    
			    if(!CollectionUtils.isEmpty(deletedTaskListNew))
							return deletedTaskListNew;
			    else
							throw new ProjectOnboardingException(ProjectOnboardingConstant.NOTASKFOUND, HttpStatus.CONFLICT);
		  	
		
		  	}
		  	else 
		  	{
			logger.error("ProjectId not found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECTIDNOTFOUND, HttpStatus.CONFLICT);
		    }
	
	     }	
}

	
