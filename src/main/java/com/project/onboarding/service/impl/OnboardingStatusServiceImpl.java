package com.project.onboarding.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.project.onboarding.service.OnboardingStatusService;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Service class for Onboarding Status module.
 * @date : 10 August 2022
 */

@Service
public class OnboardingStatusServiceImpl implements OnboardingStatusService {

	private static final Logger logger = LoggerFactory.getLogger(OnboardingStatusServiceImpl.class);

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
	@Override
	public StatusReport getPreviewStatusReport(String projectId, String userId) {
		logger.info("In preview report service");

		Criteria criteria = Criteria.where("projectId").is(projectId);
		Query query = createQuery(criteria);
		
		List<Project> projects = mongoTemplate.find(query, Project.class);
		if (!CollectionUtils.isEmpty(projects)) {
			Project existingProject = projects.get(0);
			criteria = Criteria.where("userId").is(userId);
			query = createQuery(criteria);
			
			List<User> users = mongoTemplate.find(query, User.class);
			if (!CollectionUtils.isEmpty(users)) {
				User existingUser = users.get(0);

				StatusReport statusReport = createStatusReport(existingProject, existingUser);

				logger.info("Preview status report successfully returned in service");
				return statusReport;
			} else {
				logger.warn("User not found, preview status report failed");
				throw new ProjectOnboardingException(ProjectOnboardingConstant.USER_NOT_FOUND);
			}
		} else {
			logger.warn("Project not found, preview status report failed");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}
	}

	/**
	 * @param criteria
	 * @return query, created query with given criteria
	 * @description : Create query based on given criteria
	 */
	public Query createQuery(Criteria criteria) {
		Query query = new Query();
		query.addCriteria(criteria);
		return query;
	}

	/**
	 * @param project, user
	 * @return StatusReport, Report of the task status for a user
	 * @description : Create report object with values
	 */
	public StatusReport createStatusReport(Project project, User user) {
		StatusReport statusReport = new StatusReport();
		statusReport.setProjectName(project.getName());
		statusReport.setProjectDescription(project.getDescription());

		ProjectTasksOverview projectTasksOverview = new ProjectTasksOverview();
		projectTasksOverview.setUserId(user.getUserId());
		projectTasksOverview.setUsername(user.getFirstName() + " " + user.getLastName());
		
		List<ProjectTaskDetails> projectTaskDetails = user.getProjectIds().stream()
				.filter(projectDetails -> projectDetails.getProjectId().equals(project.getProjectId()))
				.collect(Collectors.toList());
		projectTasksOverview.setTaskPercentage(calculateTaskPercentage(projectTaskDetails));
		statusReport.setProjectTasksOverview(projectTasksOverview);
		
		return statusReport;
	}

	/**
	 * @param projectTaskDetails, A list of task details for the project
	 * @return taskPercentage
	 * @description : Calculate task percentage
	 */
	public double calculateTaskPercentage(List<ProjectTaskDetails> projectTaskDetails) {
		double taskPercentage = 0;
		if (projectTaskDetails.size() > 0) {
			long noOfTasks = projectTaskDetails.get(0).getTasks().stream().count();
			int sumOfPercentage = projectTaskDetails.get(0).getTasks().stream()
					.map(task -> TASK_STATUS_PERCENTAGE.get(task.getStatus()))
					.collect(Collectors.summingInt(Integer::intValue));

			taskPercentage = (sumOfPercentage * 100 / (noOfTasks * 100));
		}
		return taskPercentage;
	}
}
