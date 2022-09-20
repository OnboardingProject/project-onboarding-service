package com.account.onboarding.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "AccountDetails")
public class Account {
	@Id
	private String accountId;
	@NotEmpty
	@Size(min = 4, message = "Customer name must be minimum of 4 characters")
	private String customerName;
	private LocalDateTime createdDate;
	private String createdBy;
	private LocalDateTime updatedDate;
	private String updatedBy;

	private String aboutCustomer;
	private List<CustomerEngagement> engagements;
	private String accountOverview;
	private List<AccountInitiative> initiatives;
	private List<Documents> documents;
}
