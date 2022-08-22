package com.project.onboarding.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ust
 * @description : Entity class for storing static data.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("Types")
public class Types {
	private String typeName;
	private int typeId;
    private String desc;
    private String permission;
}
