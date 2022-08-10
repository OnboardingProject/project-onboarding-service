package com.project.onboarding.service.impl;

import java.util.HashMap;
import java.util.List;
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
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.ProjectTasksOverview;
import com.project.onboarding.model.StatusReport;
import com.project.onboarding.model.User;
import com.project.onboarding.repository.ProjectRepository;
import com.project.onboarding.repository.UserRepository;
import com.project.onboarding.service.OnboardingStatusService;

@Service
public class OnboardingStatusServiceImpl implements OnboardingStatusService {

	private static final Logger logger = LoggerFactory.getLogger(OnboardingStatusServiceImpl.class);

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public StatusReport getPreviewStatusReport(String projectId, String userId) {
		logger.info("In preview report service");
		
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(projectId));
		List<Project> project = mongoTemplate.find(query, Project.class);
	//	Project existingProject = projectRepository.findByProjectId(projectId);
		if (null != project && project.size() > 0) {
			Project existingProject = project.get(0);
	//		query.getQueryObject().remove(")
			User existingUser = userRepository.findByUserId(userId);
			if (null != existingUser) {
				StatusReport statusReport = new StatusReport();
				statusReport.setProjectName(existingProject.getName());
				statusReport.setProjectDescription(existingProject.getDescription());
				// List<String> usersOfProject = existingProject.getUserIds();

				ProjectTasksOverview projectTasksOverview = new ProjectTasksOverview();
				projectTasksOverview.setUserId(userId);
				projectTasksOverview.setUsername(existingUser.getFirstName() + existingUser.getLastName());
				List<ProjectTaskDetails> projectTaskDetails = existingUser.getProjectIds().stream()
						.filter(projectDetails -> projectDetails.getProjectId().equals(projectId))
						.collect(Collectors.toList());
				
				double taskPercentage = 0;
				if (projectTaskDetails.size() > 0) {
					HashMap<String, Integer> taskPercentageList = new HashMap<String, Integer>();
					taskPercentageList.put("Yet to start", 0);
					taskPercentageList.put("In-progress", 50);
					taskPercentageList.put("Done", 100);
					long noOfTasks = projectTaskDetails.get(0).getTasks().stream().count();
					int sumOfPercentage = projectTaskDetails.get(0).getTasks().stream()
							.map(task -> taskPercentageList.get(task.getStatus()))
							.collect(Collectors.summingInt(Integer::intValue));
					
					taskPercentage = (sumOfPercentage * 100 / (noOfTasks * 100));
				}
				projectTasksOverview.setTaskPercentage(taskPercentage);
				statusReport.setProjectTasksOverview(projectTasksOverview);
				
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

}
