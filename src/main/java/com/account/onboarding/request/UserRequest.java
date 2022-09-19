package com.account.onboarding.request;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class UserRequest {
	@Id
	private String userId;
	private String userName;
	private String accountName;
	private String firstName;
	private String lastName;
	private String emailId;
	private String phoneNumber;
	private String designation;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer roleId;
	

}
