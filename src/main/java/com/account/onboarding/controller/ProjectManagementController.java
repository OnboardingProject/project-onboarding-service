package com.account.onboarding.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.ProjectManagementConstant;
import com.account.onboarding.model.Project;
import com.account.onboarding.request.ProjectDTO;
import com.account.onboarding.response.UserResponse;
import com.account.onboarding.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is the controller class for all the ProjectManagement API end
 * points Exposes CRUD endpoints for Project resource
 */
@Slf4j
@RestController
@RequestMapping("api/v1/project")
public class ProjectManagementController {

	@Autowired
	ProjectService projectService;

	/**
	 * This method will create a new project and also validate incoming request
	 * 
	 * @param projectDTO
	 * @return response entity representation of projectDTO
	 */
	@PostMapping("/add-project")
	@Operation(summary = "Add a new Project", description = "This API is used to add a new project")
	public ResponseEntity<ProjectDTO> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
		log.info(ProjectManagementConstant.PJT_CONST_START);
		ProjectDTO pjtDTO = projectService.createProject(projectDTO);
		log.info(ProjectManagementConstant.PJT_CONST_END);
		return new ResponseEntity<ProjectDTO>(pjtDTO, HttpStatus.CREATED);
	}

	/**
	 * THis method will will list all the users under the resource role
	 * 
	 * @return response entity representation of collection of Users
	 */
	@GetMapping("/get-resources")
	@Operation(summary = "Get all users", description = "This API is used to get all users")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		log.info(ProjectManagementConstant.USER_CONST_START);
		List<UserResponse> userDTOs = projectService.getAllUsers();
		log.info(ProjectManagementConstant.USER_CONST_END);
		return new ResponseEntity<List<UserResponse>>(userDTOs, HttpStatus.OK);
	}

	@GetMapping("/view-project/{id}")
	@Operation(summary = "Get project by id", description = "This API is used to get a project by id")
	public ResponseEntity<?> getProjectById(@PathVariable String id) {
		Project proj = projectService.getProjectById(id);
		if (proj != null) {
			log.info("In controller, get  employee by id completed successfully");
			return new ResponseEntity<Project>(proj, HttpStatus.OK);
		} else
			// throw new DataNotFoundException("No projects with given Id");
			log.error("project wit the given id not found");
		return new ResponseEntity<String>("PROJECT_NOT_FOUND", HttpStatus.NOT_FOUND);

	}

	@GetMapping("/view-projects-by-owner/{createdBy}")
	@Operation(summary = "Get projects by an owner", description = "This API is used to get all projects created by a user")
	public ResponseEntity<?> searchProjectDataByBrand(@PathVariable String createdBy) {
		List<Project> projects = projectService.searchByCreatedBy(createdBy);
		if (projects.isEmpty()) {
			return new ResponseEntity<String>("PROJECTS_NOT_FOUND", HttpStatus.NOT_FOUND);
		} else
			log.info("ViewByCretedBy executed succesfully.....");
		return new ResponseEntity<List<Project>>(projects, HttpStatus.FOUND);
	}

	@GetMapping("/view-all-projects")
	@Operation(summary = "Get all projects", description = "This API is used to get all projects")
	public ResponseEntity<?> viewAllProjects() {
		List<Project> projects = projectService.getAllProjects();

		if (projects.isEmpty()) {
			log.info("No projects found");
			return new ResponseEntity<String>("PROJECTS_NOT_FOUND", HttpStatus.NOT_FOUND);
		} else {
			log.info("Project details found");
			return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
		}
	}

	@PutMapping("/edit-project")
	@Operation(summary = "Update a project details", description = "This API is used to update a project details")
	public ResponseEntity<?> updateProjectDetails(@Valid @RequestBody ProjectDTO projectDTO) {
		ProjectDTO edit = projectService.editProject(projectDTO);
		return new ResponseEntity<>(edit, HttpStatus.ACCEPTED);
	}
}
