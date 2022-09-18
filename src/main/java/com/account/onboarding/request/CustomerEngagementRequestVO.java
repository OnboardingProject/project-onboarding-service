package com.account.onboarding.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomerEngagementRequestVO {
	
	@NotEmpty
	private String accountId;
	@NotEmpty
	@Size(min=4, message = "Engagement name should be minimum of 4 characters")
	private String engagementName;
	@NotEmpty
	@Size(min=10, message = "Description should be minimum of 10 characters")
	private String engagementDescription;
	@NotEmpty
	private String updatedBy;
}
