package com.project.onboarding.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Structure for storing project task percentage report for preview and export report.
 * @date : 23 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskPercentageReportResponse {
	private String projectName;
	private String projectOwner;
	private String projectDescription;
	private ProjectTasksOverviewResponse projectTasksOverview;
}
