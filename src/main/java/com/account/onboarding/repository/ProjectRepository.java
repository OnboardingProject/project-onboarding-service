package com.account.onboarding.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.account.onboarding.model.Project;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Repository class for accessing Project entity.
 * @date : 08 August 2022
 */

@Repository
public interface ProjectRepository extends MongoRepository<Project, String>{
	Project findByProjectId(String projectId);
}
