package com.account.onboarding.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "Database_Sequences")
public class DatabaseSequence {
	@Id
	private String id;
	private int seq;
}
