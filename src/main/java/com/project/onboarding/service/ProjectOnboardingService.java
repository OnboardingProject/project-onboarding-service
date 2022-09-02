package com.project.onboarding.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectDetails;
import com.project.onboarding.model.Types;
import com.project.onboarding.model.User;
import com.project.onboarding.model.UserDetails;

/**
 * @author Amrutha Joseph
 * @description Service class for project onboarding
 * @created_Date 17/08/2022
 */

@Service
public class ProjectOnboardingService {

	@Autowired
	MongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(ProjectOnboardingService.class);

	/**
	 * @param userId
	 * @return ProjectDetails object
	 * @throws ProjectOnboardingException
	 * @description Fetch all the projects assigned to the particular user
	 */

	public List<ProjectDetails> getProjectsBasedOnUser(String userId) {

		log.info("Inside the get Project list service");

		Criteria criteria = Criteria.where("userId").is(userId);
		Query query = createQuery(criteria);

		User user = mongoTemplate.findOne(query, User.class);

		if (user != null) {

			log.info("User found");

			List<ProjectDetails> projectList = user.getProjectIds().stream()
											.map(pId -> getProjectNames(pId.getProjectId()))
											.collect(Collectors.toList());

			log.info("Project list returned");
			return projectList;

		} else {

			log.warn("No user found with given id");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.USER_NOT_FOUND);
		}

	}

	public ProjectDetails getProjectNames(String projectId) {

		log.info("Inside getProjectNames method");

		ProjectDetails projectDetails = new ProjectDetails();

		Criteria criteria = Criteria.where("projectId").is(projectId);
		Query query = createQuery(criteria);

		Project project = mongoTemplate.findOne(query, Project.class);

		if (project != null) {

			log.info("Project found and setting the project details");

			projectDetails.setProjectId(project.getProjectId());
			projectDetails.setProjectName(project.getProjectName());

			return projectDetails;
		} else {

			log.warn("No Project found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);

		}

	}
	
	/**
	 * @param projectId
	 * @return UserDetails object
	 * @throws ProjectOnboardingException
	 * @description Fetch all the resources assigned to the particular project
	 */

	public List<UserDetails> getUsersBasedOnProject(String projectId) {

		log.info("Inside the get user list service");

		Criteria criteria = Criteria.where("projectId").is(projectId);
		Query query = createQuery(criteria);

		Project project = mongoTemplate.findOne(query, Project.class);

		if (project != null) {

			log.info("Project found");

			List<UserDetails> resources = project.getUserIds().stream()
										.map(this::getResource)
										.filter(Objects::nonNull)
										.collect(Collectors.toList());

			log.info("resource list returned");
			return resources;

		} else {

			log.warn("No Project found with given id");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}

	}

	public UserDetails getResource(String userId) {

		log.info("Inside the getResource method");

		UserDetails userDetails = new UserDetails();
		
		Types type = mongoTemplate.findOne(Query.query(new Criteria().andOperator(
				Criteria.where("typeName").is(ProjectOnboardingConstant.TYPE_NAME),
				Criteria.where("typeDesc").is(ProjectOnboardingConstant.TYPE_DESC_RESOURCE))), Types.class);
				
		Criteria criteria = new Criteria().andOperator(
				Criteria.where("userId").is(userId),
				Criteria.where("roleId").is(type.getTypeId()));		

		Query query = createQuery(criteria);

		User user = mongoTemplate.findOne(query, User.class);
		
		if (user != null) {

			log.info("Resource found and setting the user details");

			userDetails.setUserId(user.getUserId());
			userDetails.setUserName(user.getFirstName() + " " + user.getLastName());

			return userDetails;
		}else {
			
			log.info("Given user is not a resource");
			return null;
		}
	}

	public Query createQuery(Criteria criteria) {
		log.info("Inside createQuery method");
		Query query = new Query();
		query.addCriteria(criteria);
		return query;
	}
}
