package com.project.onboarding.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Entity class for storing user details.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {
	@Id
	private String id;
	private String name;
    private String accountName;
    private String firstName;
    private String lastName;
    private String emailId;
    private Long phoneNumber;
    private String designation;
    private LocalDateTime createdTime;
    private String createdBy;
    private String lastUpdatedBy;
    private LocalDateTime lastUpdatedTime;
    private String roleId;
    private List<AccountDocument> accountDocuments;
    private String hierarchy;
    private List<ProjectTaskDetails> projectIds;
}
