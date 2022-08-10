package com.project.onboarding.service;

import com.project.onboarding.model.StatusReport;

public interface OnboardingStatusService {
	StatusReport getPreviewStatusReport(String projectId, String userId);
}
