package com.project.onboarding.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.ProjectTasksOverview;
import com.project.onboarding.model.StatusReport;
import com.project.onboarding.model.User;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Service class for Onboarding Status module.
 * @date : 10 August 2022
 */

@Slf4j
@Service
public class OnboardingStatusService {

	public static final Map<String, Integer> TASK_STATUS_PERCENTAGE;

	static {
		Map<String, Integer> taskPercentageList = new HashMap<String, Integer>();
		taskPercentageList.put("Yet to start", 0);
		taskPercentageList.put("In-progress", 50);
		taskPercentageList.put("Done", 100);
		TASK_STATUS_PERCENTAGE = Collections.unmodifiableMap(taskPercentageList);
	}

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * @param projectId, userId
	 * @return StatusReport, preview status report object
	 * @throws ProjectOnboardingException
	 * @description : Preview status report of a particular user for a project
	 */
	public StatusReport getPreviewStatusReport(String projectId, String userId) throws Exception{
		log.info("In preview report service");

		Criteria criteria = Criteria.where("projectId").is(projectId);
		Query query = createQuery(criteria);

		List<Project> projects = mongoTemplate.find(query, Project.class);
		if (!CollectionUtils.isEmpty(projects)) {
			log.info("Project is found");
			
			Project existingProject = projects.get(0);
			criteria = Criteria.where("userId").is(userId);
			query = createQuery(criteria);

			List<User> users = mongoTemplate.find(query, User.class);
			if (!CollectionUtils.isEmpty(users)) {
				log.info("User is found");
				
				User existingUser = users.get(0);

				StatusReport statusReport = createStatusReport(existingProject, existingUser);

				log.info("Preview status report successfully returned in service");
				return statusReport;
			} else {
				log.error("User not found, preview status report failed");
				throw new ProjectOnboardingException(ProjectOnboardingConstant.USER_NOT_FOUND);
			}
		} else {
			log.error("Project not found, preview status report failed");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}
	}

	/**
	 * @param criteria
	 * @return query, created query with given criteria
	 * @description : Create query based on given criteria
	 */
	public Query createQuery(Criteria criteria) {
		log.info("Creating query with criteria");
		
		Query query = new Query();
		query.addCriteria(criteria);
		
		log.info("Returning query");
		return query;
	}

	/**
	 * @param project, user
	 * @return StatusReport, Report of the task status for a user
	 * @description : Create report object with values
	 */
	public StatusReport createStatusReport(Project project, User user) {
		log.info("Creating status report");
		
		StatusReport statusReport = new StatusReport();
		statusReport.setProjectName(project.getName());
		statusReport.setProjectDescription(project.getDescription());
		statusReport.setProjectOwner(findProjectOwners(project));

		log.info("Setting user id and name in the status report");
		ProjectTasksOverview projectTasksOverview = new ProjectTasksOverview();
		projectTasksOverview.setUserId(user.getUserId());
		projectTasksOverview.setUsername(user.getFirstName() + " " + user.getLastName());

		log.info("Setting task percentage in the status report");
		List<ProjectTaskDetails> projectTaskDetails = user.getProjectIds().stream()
				.filter(projectDetails -> projectDetails.getProjectId().equals(project.getProjectId()))
				.collect(Collectors.toList());
		projectTasksOverview.setTaskPercentage(calculateTaskPercentage(projectTaskDetails));
		statusReport.setProjectTasksOverview(projectTasksOverview);

		log.info("Returning status report");
		return statusReport;
	}

	/**
	 * @param projectTaskDetails, A list of task details for the project
	 * @return taskPercentage
	 * @description : Calculate task percentage
	 */
	public double calculateTaskPercentage(List<ProjectTaskDetails> projectTaskDetails) {
		log.info("Calculating task percentage");
		
		double taskPercentage = 0;
		if (projectTaskDetails.size() > 0 && projectTaskDetails.get(0).getTasks().size() > 0) {
			long noOfTasks = projectTaskDetails.get(0).getTasks().stream().count();
			int sumOfPercentage = projectTaskDetails.get(0).getTasks().stream()
					.map(task -> TASK_STATUS_PERCENTAGE.get(task.getStatus()))
					.collect(Collectors.summingInt(Integer::intValue));

			taskPercentage = (sumOfPercentage * 100 / (noOfTasks * 100));
			log.info("Task percentage calculated");
		}
		
		log.info("Returning Task Percentage value");
		return taskPercentage;
	}

	/**
	 * @param project
	 * @return projectOwnerNames
	 * @description : Find project owners of a project
	 */
	public String findProjectOwners(Project project) {
		log.info("Finding project owners of the project");
		
		List<String> userIds = project.getUserIds();

		String projectOwnerNames = userIds.stream()
				.map(userId -> isProjectOwner(userId))
				.filter(name -> !name.equals(""))
				.collect(Collectors.joining(","));
		
		log.info("Returning project owner's names");
		return projectOwnerNames;
	}

	/**
	 * @param userId
	 * @return project owner's name if the user is project owner else return ""
	 * @description : Check whether a user is project owner or not.
	 */
	public String isProjectOwner(String userId) {
		log.info("Getting the names of project owner's from db using query");
		
		Criteria criteria = new Criteria().andOperator(Criteria.where("userId").is(userId),
				Criteria.where("roleId").is("Project Owner"));
		Query query = createQuery(criteria);
		List<User> users = mongoTemplate.find(query, User.class);

		if (!CollectionUtils.isEmpty(users)) {
			log.info("Returning name of project owner");
			return users.get(0).getFirstName() + " " + users.get(0).getLastName();
		}
		
		log.warn("User is not a project owner");
		return "";
	}
}
