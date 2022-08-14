package com.project.onboarding.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.onboarding.model.Project;
import com.project.onboarding.model.Task;
/**
 * @author UST
 * @description : Repository class for accessing Project entity.
 * @date : 08 August 2022
 */

@Repository
public interface ProjectRepository extends MongoRepository<Project, String>{
	
//	public List<Task> findByProjectId(String projectId);

}
