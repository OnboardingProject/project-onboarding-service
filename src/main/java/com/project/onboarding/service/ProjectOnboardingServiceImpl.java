package com.project.onboarding.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.onboarding.repository.TypesRepository;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProjectOnboardingServiceImpl implements ProjectOnboardingService {

	@Autowired
	TypesRepository typesRepository;

	private static Logger log = LoggerFactory.getLogger(ProjectOnboardingService.class);

	/**
	 * @param
	 * @return Types object
	 * @throws ProjectOnboardingException
	 * @description Fetch all task status
	 */
	public List<Types> getAllTaskStatus() {
		log.info("Staring of fetch all tst status");
		List<Types> taskStatusList = new ArrayList<Types>();
		taskStatusList = typesRepository.findByTypeName(ProjectOnboardingConstant.TYPE_NAME);

		if (taskStatusList.isEmpty() == false) {
			log.info("Display all the task status");
			return taskStatusList;
		} else {
			log.error("The tak list is empty");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.LIST_EMPTY, HttpStatus.NOT_FOUND);
		}
	}
}

