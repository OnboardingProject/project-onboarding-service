package com.project.onboarding.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Structure for storing task details for export report.
 * @date : 23 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailsReportResponse {
	private String taskName;
	private String taskDescription;
	private String taskStatus;
}
