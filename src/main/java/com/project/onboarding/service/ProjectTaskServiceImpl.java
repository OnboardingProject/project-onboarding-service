package com.project.onboarding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.ArrayList;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {

@Autowired
private MongoTemplate mongoTemplate;

private static final Logger logger = LoggerFactory.getLogger(ProjectTaskServiceImpl.class);


	@Override
	public List<Task> getProjectTasksByProjectId(String projectId) {
		logger.info("Fetch the project task list started");
		Project project = mongoTemplate.findById(projectId, Project.class);	
		
		
		if(null !=project)
   { List<Task> projectTask=new ArrayList<Task>();
   projectTask=project.getTasks();
   logger.info("Return task list details");
	   return projectTask;

   } else 
   {
	   logger.error("Throw ProjectId not found exception");
	   throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECTIDNOTFOUND, HttpStatus.NOT_FOUND);
   }
	   
	}

}
