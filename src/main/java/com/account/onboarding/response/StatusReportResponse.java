package com.account.onboarding.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Status Report structure for preview and export report functionalities.
 * @date : 12 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusReportResponse {
	private TaskPercentageReportResponse taskPercentageReport;
	private List<TaskDetailsReportResponse> taskDetailsReport;
}
