package com.account.onboarding.service;

import java.util.List;

import com.account.onboarding.model.Project;
import com.account.onboarding.request.ProjectDTO;
import com.account.onboarding.response.UserResponse;

/**
 * Interface for project Management Service methods
 * 
 *
 */
public interface ProjectService {

	ProjectDTO createProject(ProjectDTO projectDTO);

	List<UserResponse> getAllUsers();

	Project getProjectById(String id);

	List<Project> getAllProjects();

	List<Project> searchByCreatedBy(String createdBy);

	ProjectDTO editProject(ProjectDTO projectVO);

}
