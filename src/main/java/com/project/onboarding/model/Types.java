package com.project.onboarding.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Entity class for storing static data.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Types {
	private String typeName;
	private String id;
    private String desc;
    private List<String> permission;
}
