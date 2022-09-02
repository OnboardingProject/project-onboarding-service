package com.project.onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Main class for the application
 * @date : 10 August 2022
 */

@SpringBootApplication
@OpenAPIDefinition
public class ProjectOnboardingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectOnboardingApplication.class, args);
	}

}
