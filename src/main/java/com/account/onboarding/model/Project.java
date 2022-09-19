package com.account.onboarding.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Entity class for storing project details.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("Project")
public class Project {
	private String projectId;
	@NotBlank(message = "cannot be blank")
	private String projectName;
	@Size(min = 10, max = 150, message = "Description must be between 10 and 150")
	private String projectDescription;
	private Date createdTime;
	private String createdBy;
	private String lastUpdateBy;
	private Date lastUpdateTime;
	private List<String> userIds;
	private List<Task> tasks;
}
