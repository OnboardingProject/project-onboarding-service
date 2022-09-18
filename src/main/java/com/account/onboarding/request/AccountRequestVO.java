package com.account.onboarding.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountRequestVO {
	
	@Id
	private String accountId;
	@NotEmpty
	@Size(min = 4, message = "Customer name must be minimum of 4 characters")
	private String customerName;
	private String createdBy;

}
