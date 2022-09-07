package com.project.onboarding.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;



/**
 * @author UST
 * @description : Service class for fetch all task status.
 * @date : 08 August 2022
 */
@Service
public class ProjectOnboardingService {

	
	@Autowired
	private MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(ProjectOnboardingService.class);

	/**
	 * @param
	 * @return List of Types object
	 * @throws ProjectOnboardingException
	 * @description Fetch all task status
	 */
	public List<Types> getAllTaskStatus() throws Exception {
		logger.info("Staring of fetch all task status");
		Query query = new Query();
		query.addCriteria(Criteria.where("typeName").is(ProjectOnboardingConstant.TYPE_NAME));
		List<Types> taskStatusList = mongoTemplate.find(query, Types.class);
		if (!CollectionUtils.isEmpty(taskStatusList)) 
					{logger.info("Display all the task status");
			return taskStatusList;}
		 else {
			logger.error("The task list is empty");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.LIST_EMPTY, HttpStatus.CONFLICT);
		}
	}
}
