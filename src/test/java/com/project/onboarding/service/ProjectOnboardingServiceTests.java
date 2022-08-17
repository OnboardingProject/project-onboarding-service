package com.project.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Types;
import com.project.onboarding.repository.TypesRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectOnboardingServiceTests {

	@Mock
	private TypesRepository typesRepository;
	@InjectMocks
	private ProjectOnboardingService projectOnboardingService;

	@DisplayName("JUnit test for GetAllTaskStatus success scenario ")
	@Test
	public void testGetAllTaskStatusSuccess() {

		Types types = new Types();
		types.setTypeName("TASK_STATUS");
		types.setTypeId("TS_002");
		types.setDesc("In-progress");
		types.setPermission(null);
		List<Types> statusList = new ArrayList<Types>();
		statusList.add(types);
		when(typesRepository.findByTypeName(ProjectOnboardingConstant.TYPE_NAME)).thenReturn(statusList);
		List<Types> types1 = projectOnboardingService.getAllTaskStatus();
		assertEquals(statusList, types1);
	}

	@DisplayName("JUnit test for GetAllTaskStatus failure scenario ")
	@Test
	public void testGetAllTaskStatusFailure() {
		Types types = new Types();
		types.setTypeName("TASK_STATUS");
		types.setTypeId("TS_002");
		types.setDesc("In-progress");
		types.setPermission(null);

		when(typesRepository.findByTypeName(ProjectOnboardingConstant.TYPE_NAME))
				.thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.LIST_EMPTY, HttpStatus.NOT_FOUND));
		Assertions.assertThrows(ProjectOnboardingException.class, () -> {
			projectOnboardingService.getAllTaskStatus();
		});
	}

}
