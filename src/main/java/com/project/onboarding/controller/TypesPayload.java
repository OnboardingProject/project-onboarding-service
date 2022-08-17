package com.project.onboarding.controller;

import java.util.ArrayList;
import java.util.List;
import com.project.onboarding.model.Types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypesPayload {
	private List<Types> typeList = new ArrayList<Types>();
	private String successMessage;
	private String errorMessage;

}