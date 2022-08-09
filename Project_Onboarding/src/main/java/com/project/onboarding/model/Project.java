package com.project.onboarding.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
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
@Document
public class Project {
	@Id
	private String id;
	@NotBlank(message = "cannot be blank")
	private String name;
	@Size(min = 10, max = 150, message = "Description must be between 10 and 150")
	private String description;
	private LocalDateTime createdTime;
	private String createdBy;
	private String lastUpdatedBy;
	private LocalDateTime lastUpdatedTime;
	private List<String> userIds;
	private List<Task> tasks;
}
