package com.account.onboarding.request;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountVO {
	String accountId;
	@Size(min = 5, message = "Size should be greather than 5")
	String accountOverview;
	String updatedBy;

}
