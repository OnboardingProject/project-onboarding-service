package com.project.onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
public class ProjectOnboardingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectOnboardingApplication.class, args);
	}

}
