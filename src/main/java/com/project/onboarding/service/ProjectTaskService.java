package com.project.onboarding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.ArrayList;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.Task;

import org.springframework.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author UST
 * @description : Service class for fetch task details based on project.
 * @date : 08 August 2022
 */
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
	public List<Task> getProjectTasksByProjectId(String projectId) {
		logger.info("Method for fetch the project task list started");
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(projectId));
		List<Project> project = mongoTemplate.find(query, Project.class);

		if (!CollectionUtils.isEmpty(project)) {
			List<Task> projectTask = new ArrayList<Task>();
			projectTask = project.get(0).getTasks();
			logger.info("Return the task list details of selected projet");
			 return projectTask;

		} else {
			logger.error("ProjectId not found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECTIDNOTFOUND, HttpStatus.CONFLICT);
		}

	}
}
