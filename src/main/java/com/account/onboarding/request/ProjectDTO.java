package com.account.onboarding.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This is the DTO class representing Project
 *
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

	private String projectId;
	@NotBlank(message = "cannot be blank")
	private String projectName;
	@Size(min = 10, max = 200, message = " must be between 10 and 200 characters")
	private String projectDescription;
	private Date createdTime;
	private String createdBy;
	private Date lastUpdateTime;
	private String lastUpdateBy;
	@NotEmpty(message = "cannot be empty")
	private List<String> userId;

}
