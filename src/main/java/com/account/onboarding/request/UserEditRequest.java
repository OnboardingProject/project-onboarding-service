package com.account.onboarding.request;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the user Request class for updation in UserManagement
 * 
 * @author
 *
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UserEditRequest {
	@Id
	private String userId;
	private Integer roleId;
	private String LastUpdatedBy;
}
