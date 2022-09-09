package com.project.onboarding.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Class for constant variables.
 * @date : 10 August 2022
 */

public class ProjectOnboardingConstant {

	public static final String PROJECT_NOT_FOUND = "Project not found";

	public static final String USER_NOT_FOUND = "User not found";

	public static final String TASK_NOT_FOUND = "Task(s) not found";

	public static final String API_GET_PROJECTS_SUCCESS = "Projects are fetched successfully";

	public static final String API_GET_RESOURCES_SUCCESS = "Resources are fetched successfully";

	public static final String API_GET_PREVIEW_REPORT_SUCCESS = "Preview of the report returned successfully";

	public static final String API_EXPORT_REPORT_SUCCESS = "Status Report is downloaded successfully in ";
	
	public static final String API_TASK_LIST_FETCH_SUCCESS = "Task list fetched successfully";
	
	public static final String API_TASK_STATUS_FETCH_SUCCESS = "Task status fetched successfully";
	
	public static final String API_TASK_STATUS_SAVE_SUCCESS = "Task Status saved successfully";
	
	public static final String API_DELETE_TASKS_SUCCESS = "Tasks deleted successfully";
	
	public static final String API_ADD_OR_EDIT_TASK_SUCCESS = "Tasks added/edited successfully";
	
	public static final List<String> EXPORT_REPORT_EXCEL_HEADERS_STATUS_REPORT = Arrays.asList("Project Name",
			"Project Owner(s)", "Project Description", "Project Tasks Overview", "", "");

	public static final List<String> EXPORT_REPORT_EXCEL_SUB_HEADERS_STATUS_REPORT = Arrays.asList("", "", "",
			"User ID", "User Name", "Task Percentage");

	public static final List<String> EXPORT_REPORT_EXCEL_HEADERS_TASK_DETAILS = Arrays.asList("Task Name",
			"Task Description", "Task Status");

	public static final Map<String, Integer> TASK_STATUS_PERCENTAGE;

	public static final String EXPORT_REPORT_SHEET_NAME_STATUS_REPORT = "Status Report";

	public static final String EXPORT_REPORT_SHEET_NAME_TASK_DETAILS = "Task Details";

	public static final String PROJECT_OWNER = "Project Owner";

	public static final String ROLE = "ROLE";
	
	public static final String TASK_STATUS = "TASK_STATUS";
	
	public static final String YET_TO_START = "Yet to start";

	public static final String RESOURCE = "Resource";

	public static final String TASK_LIST_NOT_FOUND = "Project or User or Tasks associated are not found";

	public static final String STATUS_LIST_EMPTY = "The Status list is empty";
	
	public static final String INVALID_PROJECT_ID = "Invalid project id, please provide valid id";
	
	public static final String INVALID_USER_ID = "Invalid user id, please provide valid id";
	
	public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
	
	static {
		Map<String, Integer> taskPercentageList = new HashMap<String, Integer>();
		taskPercentageList.put("yet-to-start", 0);
		taskPercentageList.put("in-progress", 50);
		taskPercentageList.put("done", 100);
		TASK_STATUS_PERCENTAGE = Collections.unmodifiableMap(taskPercentageList);
	}

	public static String getFileNameForExcelReport(String projectId, String userId) {
		return "D:\\StatusReports\\StatusReport" + "_" + projectId + "_" + userId + ".xls";
	}

}
