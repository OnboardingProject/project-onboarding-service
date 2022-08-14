package com.project.onboarding.service;

import com.project.onboarding.model.Task;

import java.util.List;

public interface ProjectTaskService {
		public List<Task> getProjectTasksByProjectId(String projectId);
}
