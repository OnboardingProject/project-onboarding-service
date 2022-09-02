package com.project.onboarding.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Project;
import com.project.onboarding.model.ProjectTaskDetails;
import com.project.onboarding.model.ProjectTasksOverview;
import com.project.onboarding.model.StatusReport;
import com.project.onboarding.model.Task;
import com.project.onboarding.model.TaskDetails;
import com.project.onboarding.model.TaskDetailsReport;
import com.project.onboarding.model.TaskPercentageReport;
import com.project.onboarding.model.User;
import com.project.onboarding.util.ProjectOnboardingUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Service class for Onboarding Status module.
 * @date : 10 August 2022
 */

@Slf4j
@Service
public class OnboardingStatusService {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ProjectOnboardingUtil projectOnboardingUtil;

	/**
	 * @param projectId, userId
	 * @return StatusReport, preview status report object
	 * @throws ProjectOnboardingException
	 * @description : Preview status report of a particular user for a project
	 */
	public TaskPercentageReport getPreviewStatusReport(String projectId, String userId) throws Exception {
		log.info("In preview report service");

		Criteria criteria = Criteria.where("projectId").is(projectId);
		Query query = createQuery(criteria);

		List<Project> projects = mongoTemplate.find(query, Project.class);
		if (!CollectionUtils.isEmpty(projects)) {
			log.info("Project is found");

			Project existingProject = projects.get(0);
			criteria = Criteria.where("userId").is(userId);
			query = createQuery(criteria);

			List<User> users = mongoTemplate.find(query, User.class);
			if (!CollectionUtils.isEmpty(users)) {
				log.info("User is found");

				User existingUser = users.get(0);

				TaskPercentageReport taskPercentageReport = createTaskPercentageReport(existingProject, existingUser);

				log.info("Preview status report successfully returned in service");
				return taskPercentageReport;
			} else {
				log.error("User not found, preview status report failed");
				throw new ProjectOnboardingException(ProjectOnboardingConstant.USER_NOT_FOUND);
			}
		} else {
			log.error("Project not found, preview status report failed");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}
	}

	public List<TaskDetailsReport> createTaskDetailsReport(Project project, User user) throws Exception {
		log.info("Creating task details report");

		List<TaskDetailsReport> taskDetailListReport = new ArrayList<TaskDetailsReport>();
		List<TaskDetails> userTaskDetails = user.getProjectIds().stream()
				.filter(projectDetails -> projectDetails.getProjectId().equals(project.getProjectId()))
				.map(ProjectTaskDetails::getTasks).collect(Collectors.flatMapping(List::stream, Collectors.toList()));
		List<Task> projectTasks = project.getTasks();

		for (TaskDetails taskDetails : userTaskDetails) {
			List<Task> taskWithGivenId = projectTasks.stream().filter(task -> task.getTaskId().equals(taskDetails.getTaskId()))
					.collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(taskWithGivenId)) {
				TaskDetailsReport taskDetailsReport = new TaskDetailsReport();
				taskDetailsReport.setTaskName(taskDetails.getTaskName());
				taskDetailsReport.setTaskDescription(taskWithGivenId.get(0).getTaskDescription());
				taskDetailsReport.setTaskStatus(taskDetails.getTaskStatus());

				taskDetailListReport.add(taskDetailsReport);
			} else {
				log.error("Task(s) associated to project not found, export report failed");
				throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND);
			}
		}
		log.info("Returning task details report");
		return taskDetailListReport;
	}

	/**
	 * @param projectId, userId
	 * @return StatusReport, status report object
	 * @description : Download status report in excel format for a particular user
	 *              of a project
	 */
	public StatusReport exportStatusReportInExcelFormat(String projectId, String userId) throws Exception {
		log.info("In exportStatusReport Service");

		Criteria criteria = Criteria.where("projectId").is(projectId);
		Query query = createQuery(criteria);

		List<Project> projects = mongoTemplate.find(query, Project.class);
		if (!CollectionUtils.isEmpty(projects)) {
			log.info("Project is found");

			Project existingProject = projects.get(0);
			criteria = Criteria.where("userId").is(userId);
			query = createQuery(criteria);

			List<User> users = mongoTemplate.find(query, User.class);
			if (!CollectionUtils.isEmpty(users)) {
				log.info("User is found");

				User existingUser = users.get(0);

				TaskPercentageReport taskPercentageReport = createTaskPercentageReport(existingProject, existingUser);
				List<TaskDetailsReport> taskDetailsListReport = createTaskDetailsReport(existingProject, existingUser);

				StatusReport statusReport = new StatusReport(taskPercentageReport, taskDetailsListReport);

				File file = new File(ProjectOnboardingConstant.getFileNameForExcelReport(projectId, userId));
				file.getParentFile().mkdirs();
				file.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(file, false);
				log.info("File is created if not exists");

				HSSFWorkbook workBook = new HSSFWorkbook();

				createSheetWithValues(workBook, ProjectOnboardingConstant.EXPORT_REPORT_SHEET_NAME_STATUS_REPORT,
						ProjectOnboardingConstant.EXPORT_REPORT_EXCEL_HEADERS_STATUS_REPORT, statusReport);
				createSheetWithValues(workBook, ProjectOnboardingConstant.EXPORT_REPORT_SHEET_NAME_TASK_DETAILS,
						ProjectOnboardingConstant.EXPORT_REPORT_EXCEL_HEADERS_TASK_DETAILS, statusReport);
				workBook.write(fileOutputStream);
				workBook.close();

				log.info("Workbook closed and excel is downloaded successfully");
				return statusReport;
			} else {
				log.error("User not found, export status report failed");
				throw new ProjectOnboardingException(ProjectOnboardingConstant.USER_NOT_FOUND);
			}
		} else {
			log.error("Project not found, export status report failed");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}
	}

	public void createSheetWithValues(HSSFWorkbook workBook, String sheetName, List<String> headers,
			StatusReport statusReport) {
		HSSFSheet sheet = workBook.createSheet(sheetName);
		sheet.setDefaultColumnWidth(35);
		sheet.setDefaultRowHeightInPoints(15);
		log.info("Workbook with a sheet for Status Report is created");

		int rowNumber = 0, noOfDataRows =  statusReport.getTaskDetailsReport().size();

		/* Setting font color and style */
		Font font = workBook.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());

		/* Setting alignment, background color for the cells in sheet */
		CellStyle style = workBook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setWrapText(true);
		log.info("Style for sheet headers are set");
		
		CellStyle style1 = workBook.createCellStyle();
		style1.setAlignment(HorizontalAlignment.CENTER);
		style1.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		style1.setWrapText(true);
		log.info("Style for rows with data are set");

		HSSFRow rowhead = sheet.createRow((short) rowNumber++);
		createCellsInARow(rowhead, headers, style);
		log.info("First row created");
		
		sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));
		sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));
		sheet.addMergedRegion(new CellRangeAddress(0,1,2,2));
		rowhead = sheet.createRow((short) rowNumber++);

		if (sheet.getSheetName().equals(ProjectOnboardingConstant.EXPORT_REPORT_SHEET_NAME_STATUS_REPORT)) {
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 5));
			createCellsInARow(rowhead, ProjectOnboardingConstant.EXPORT_REPORT_EXCEL_SUB_HEADERS_STATUS_REPORT, style);
			noOfDataRows = 1;
			log.info("A row created for sub headers");
		} else 
			createCellsInARow(rowhead, new ArrayList<String>(), style);
		
		Map<Integer, List<String>> exportReportExcelData = getListOfExcelData(statusReport, sheetName);
		for(int i = 1; i <= noOfDataRows; i++) {
			rowhead = sheet.createRow((short) rowNumber++);
			
			createCellsInARow(rowhead, exportReportExcelData.get(i), style1);
			log.info("Row with values created");
		}
		
	}

	/**
	 * @param row, values, style
	 * @return void
	 * @description : Create cells inside a row an give styling
	 */
	public void createCellsInARow(HSSFRow row, List<String> values, CellStyle style) {
		log.info("Creating cells inside row");
		for (int i = 0; i < values.size(); i++) {
			row.createCell(i).setCellValue(values.get(i));
			row.getCell(i).setCellStyle(style);
		}
		log.info("Cells are created inside row");
	}

	/**
	 * @param criteria
	 * @return query, created query with given criteria
	 * @description : Create query based on given criteria
	 */
	public Query createQuery(Criteria criteria) {
		log.info("Creating query with criteria");

		Query query = new Query();
		query.addCriteria(criteria);

		log.info("Returning query");
		return query;
	}

	/**
	 * @param statusReport, project, user
	 * @return TaskPercentageReport, Report of the task percentage for a user
	 * @description : Create report object with values
	 */
	public TaskPercentageReport createTaskPercentageReport(Project project, User user) {
		log.info("Creating status report");

		TaskPercentageReport taskPercentageReport = new TaskPercentageReport();
		taskPercentageReport.setProjectName(project.getName());
		taskPercentageReport.setProjectDescription(project.getDescription());
		taskPercentageReport.setProjectOwner(findProjectOwners(project));

		log.info("Setting user id and name in the status report");
		ProjectTasksOverview projectTasksOverview = new ProjectTasksOverview();
		projectTasksOverview.setUserId(user.getUserId());
		projectTasksOverview.setUserName(user.getFirstName() + " " + user.getLastName());

		log.info("Setting task percentage in the status report");
		List<ProjectTaskDetails> projectTaskDetails = user.getProjectIds().stream()
				.filter(projectDetails -> projectDetails.getProjectId().equals(project.getProjectId()))
				.collect(Collectors.toList());
		projectTasksOverview.setTaskPercentage(calculateTaskPercentage(projectTaskDetails));
		taskPercentageReport.setProjectTasksOverview(projectTasksOverview);

		log.info("Returning status report");
		return taskPercentageReport;
	}

	/**
	 * @param projectTaskDetails, A list of task details for the project
	 * @return taskPercentage
	 * @description : Calculate task percentage
	 */
	public double calculateTaskPercentage(List<ProjectTaskDetails> projectTaskDetails) {
		log.info("Calculating task percentage");

		double taskPercentage = 0;
		if (projectTaskDetails.size() > 0 && projectTaskDetails.get(0).getTasks().size() > 0) {
			long noOfTasks = projectTaskDetails.get(0).getTasks().stream().count();
			int sumOfPercentage = projectTaskDetails.get(0).getTasks().stream()
					.map(task -> ProjectOnboardingConstant.TASK_STATUS_PERCENTAGE.get(task.getTaskStatus()))
					.collect(Collectors.summingInt(Integer::intValue));

			taskPercentage = (sumOfPercentage * 100 / (noOfTasks * 100));
			log.info("Task percentage calculated");
		}

		log.info("Returning Task Percentage value");
		return taskPercentage;
	}

	/**
	 * @param project
	 * @return projectOwnerNames
	 * @description : Find project owners of a project
	 */
	public String findProjectOwners(Project project) {
		log.info("Finding project owners of the project");

		List<String> userIds = project.getUserIds();
		String projectOwnerNames = userIds.stream().map(userId -> isProjectOwner(userId))
				.filter(name -> !name.equals("")).collect(Collectors.joining(","));

		log.info("Returning project owner's names");
		return projectOwnerNames;
	}

	/**
	 * @param userId
	 * @return project owner's name if the user is project owner else return ""
	 * @description : Check whether a user is project owner or not.
	 */
	public String isProjectOwner(String userId) {
		log.info("Getting the names of project owner's from db using query");

		Criteria criteria = new Criteria().andOperator(Criteria.where("userId").is(userId),
				Criteria.where("roleId").is(projectOnboardingUtil.getRoleIdOfProjectOwner()));
		Query query = createQuery(criteria);
		List<User> users = mongoTemplate.find(query, User.class);

		if (!CollectionUtils.isEmpty(users)) {
			log.info("Returning name of project owner");
			return users.get(0).getFirstName() + " " + users.get(0).getLastName();
		}

		log.warn("User is not a project owner");
		return "";
	}
	
	/**
	 * @param statusReport, sheetName
	 * @return EXPORT_REPORT_EXCEL_DATA
	 * @description Excel data of each row stored in a map with row number and row
	 *              data
	 */
	public Map<Integer, List<String>> getListOfExcelData(StatusReport statusReport, String sheetName) {
		log.info("In method for getting list of excel data to print on excel");
		
		HashMap<Integer, List<String>> exportReportExcelData = new HashMap<Integer, List<String>>();
		int rowNumber = 1;

		if (sheetName.equals(ProjectOnboardingConstant.EXPORT_REPORT_SHEET_NAME_STATUS_REPORT)) {
			log.info("Getting data for the first sheet in excel");
			
			List<String> rowData = new ArrayList<String>();
			TaskPercentageReport taskPercentageReport = statusReport.getTaskPercentageReport();
			rowData.add(taskPercentageReport.getProjectName());
			rowData.add(taskPercentageReport.getProjectOwner());
			rowData.add(taskPercentageReport.getProjectDescription());
			rowData.add(taskPercentageReport.getProjectTasksOverview().getUserId());
			rowData.add(taskPercentageReport.getProjectTasksOverview().getUserName());
			rowData.add(String.valueOf(taskPercentageReport.getProjectTasksOverview().getTaskPercentage()));
			exportReportExcelData.put(rowNumber++, rowData);

		} else {
			log.info("Getting data for the second sheet in excel");

			List<TaskDetailsReport> taskDetailsListReport = statusReport.getTaskDetailsReport();
			for (TaskDetailsReport taskDetailsReport : taskDetailsListReport) {
				List<String> rowData = new ArrayList<String>();
				rowData.add(taskDetailsReport.getTaskName());
				rowData.add(taskDetailsReport.getTaskDescription());
				rowData.add(taskDetailsReport.getTaskStatus());
				exportReportExcelData.put(rowNumber++, rowData);
			}
		}
		log.info("Returning data to be printed in the excel sheet");
		return exportReportExcelData;
	}
}
