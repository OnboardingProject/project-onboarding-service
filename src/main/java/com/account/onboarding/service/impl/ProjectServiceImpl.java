package com.account.onboarding.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.account.onboarding.constants.ProjectManagementConstant;
import com.account.onboarding.exception.DataNotFoundException;
import com.account.onboarding.exception.NameAlreadyExistingException;
import com.account.onboarding.exception.NoResourceFoundException;
import com.account.onboarding.model.DatabaseSequence;
import com.account.onboarding.model.Project;
import com.account.onboarding.model.User;
import com.account.onboarding.repository.ProjectRepository;
import com.account.onboarding.request.ProjectDTO;
import com.account.onboarding.response.UserResponse;
import com.account.onboarding.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class contains the Project Management service methods which interacts
 * with the Repository
 */

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * This method will convert the POJO class into project entity class and persist
	 * it into DB and responses from the repository is converted back to the
	 * controller
	 * 
	 * @throws NameAlreadyExistingException
	 */
	@Override
	public ProjectDTO createProject(ProjectDTO projectDTO) {
		log.info(ProjectManagementConstant.PJT_CONST_SERVICE);
		String incrementedPorjectSeq = "PROJ_" + generateProjectSequence("project-sequence-id");
		if (projectRepository.findByProjectName(projectDTO.getProjectName()) != null) {
			log.error(ProjectManagementConstant.PJT_CONST_SERVICE_EXCN_LOG, projectDTO.getProjectName());
			throw new NameAlreadyExistingException(ProjectManagementConstant.PJT_CONST_SERVICE_EXCN);
		}
		Date time = new Date();

		Project pjt = projectRepository.save(new Project(incrementedPorjectSeq, projectDTO.getProjectName(),
				projectDTO.getProjectDescription(), time, projectDTO.getCreatedBy(), projectDTO.getLastUpdateBy(), time,
				projectDTO.getUserId(), null));
		log.info(ProjectManagementConstant.PJT_CONST_SERVICE_COMPLETE);
		projectDTO.setProjectId(pjt.getProjectId());
		projectDTO.setCreatedTime(time);
		projectDTO.setLastUpdateTime(time);
		return projectDTO;

	}

	/**
	 * This method will get all resources from repo who has Resource roles
	 */
	@Override
	public List<UserResponse> getAllUsers() {
		log.info(ProjectManagementConstant.USER_CONST_SERVICE);
		List<User> users = mongoTemplate.findAll(User.class);
		if (users == null || users.isEmpty()) {
			log.error(ProjectManagementConstant.USER_CONST_SERVICE_EXCN_LOG);
			throw new NoResourceFoundException(ProjectManagementConstant.USER_CONST_SERVICE_EXCN);
		}
		List<UserResponse> userDTOs = users.stream()
				.map((s) -> new UserResponse(s.getUserId(), s.getUserName(), s.getRoleId()))
				.collect(Collectors.toList());
		log.info(ProjectManagementConstant.USER_CONST_SERVICE_COMPLETE);
		return userDTOs;
	}

	/**
	 * This method will return the incremented long value of the Project sequence
	 * 
	 * @param seqName
	 * @return
	 */
	private long generateProjectSequence(String seqName) {
		DatabaseSequence counter = mongoTemplate.findAndModify(Query.query(Criteria.where("_id").is(seqName)),
				new Update().inc("seq", 1), FindAndModifyOptions.options().returnNew(true).upsert(true),
				DatabaseSequence.class);
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

	@Override
	public ProjectDTO editProject(ProjectDTO projectDTO) {

		Optional<Project> opt = projectRepository.findById(projectDTO.getProjectId());

		if (opt.isPresent()) {
			log.info("Edit project is started");
			Project getProject = projectRepository.findById(projectDTO.getProjectId()).get();
			getProject.setProjectName(projectDTO.getProjectName());
			getProject.setProjectDescription(projectDTO.getProjectDescription());
			getProject.setLastUpdateBy(projectDTO.getLastUpdateBy());
			Date date = new Date();
			getProject.setLastUpdateTime(date);
			getProject.setUserIds(projectDTO.getUserId());

			Project edit = projectRepository.save(getProject);

			projectDTO.setProjectId(edit.getProjectId());
			projectDTO.setCreatedBy(getProject.getCreatedBy());
			projectDTO.setCreatedTime(getProject.getCreatedTime());
			projectDTO.setLastUpdateTime(date);

			log.info("Project updated successfully");
			return projectDTO;
		} else {
			throw new DataNotFoundException("No data is found");
		}
	}

	@Override
	public Project getProjectById(String id) {
		log.info("start of getProjectById..... ");
		Optional<Project> proj = projectRepository.findById(id);
		if (proj.isPresent()) {
			log.info("In Service class finding  employee by id");
			return proj.get();
		} else
			throw new DataNotFoundException("No projects with given Id");
	}

	@Override
	public List<Project> searchByCreatedBy(String createdBy) {
		log.info("start of getProjectByOwner..... ");

		List<Project> projects = projectRepository.findByCreatedBy(createdBy);

		if (projects.isEmpty()) {
			log.error("Invalid data");
			throw new DataNotFoundException("No projects to display");
		} else {
			return projects;
		}
	}

	@Override
	public List<Project> getAllProjects() {
		List<Project> ProjectList = projectRepository.findAll();

		if (ProjectList.isEmpty()) {
			throw new DataNotFoundException("No projects to display");
		} else
			return ProjectList;
	}

}
