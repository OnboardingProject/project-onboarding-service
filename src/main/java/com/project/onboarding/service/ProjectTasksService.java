package com.project.onboarding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.ArrayList;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.Task;
import com.project.onboarding.util.ProjectOnboardingUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.CollectionUtils;

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

	/**
	 * @param ProjectId
	 * @return List of Tasks
	 * @throws ProjectOnboardingException
	 * @description Fetch all task based on projectId
	 */
	public List<Task> getProjectTasksByProjectId(String projectId) {
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
}
