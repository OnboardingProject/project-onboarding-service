package com.account.onboarding.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Documents {
	@Id
	private String documentId;
	private String documentName;
	private String documentDesc;
	private Integer documentSeq;
	private String documentStatus;

}
