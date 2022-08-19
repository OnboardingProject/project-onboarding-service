package com.project.onboarding.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.exception.ProjectOnboardingException;
import com.project.onboarding.model.Types;
import com.project.onboarding.service.ProjectOnboardingService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;



@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ProjectOnboardingControllerTests{
	
	
		@InjectMocks
		private ProjectOnboardingController projectOnboardingController;

		@Mock
		private ProjectOnboardingService projectOnboardingService;

		private MockMvc mockMvc;
        private Types types;
		
		@BeforeEach
		public void init() {
			MockitoAnnotations.initMocks(this);
			mockMvc = MockMvcBuilders.standaloneSetup(projectOnboardingController).build();
		}
		@DisplayName("JUnit test for getAllTaskStatus success scenario ")
		@Test
		public void testFetchAllTaskStatusSucess() throws Exception {
			Types types=new Types();
			types.setTypeName("TASK_STATUS");
			types.setTypeId(10);
			types.setDesc("In-progress");
			types.setPermission(null);
			List<Types> statusList=new ArrayList<Types>();
			statusList.add(types);
					
			when(projectOnboardingService.getAllTaskStatus()).thenReturn(statusList);
			mockMvc.perform(MockMvcRequestBuilders.get("/project_onboarding/fetchtaskstatus").contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk());

		}
		@DisplayName("JUnit test for getAllTaskStatus failure scenario ")
		@Test
		    public void testtestFetchAllTaskStatusFailure_ThenThrowException() throws Exception {
 
		      when(projectOnboardingService.getAllTaskStatus()).thenThrow(new ProjectOnboardingException(ProjectOnboardingConstant.LIST_EMPTY,HttpStatus.NOT_FOUND)); 
		        mockMvc.perform(MockMvcRequestBuilders
		                .get("/project_onboarding/fetchtaskstatus")
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(asJsonString(types)))
		                .andExpect(status().isNotFound())
		                .andDo(MockMvcResultHandlers.print())
		                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(ProjectOnboardingConstant.LIST_EMPTY));
		               verify(projectOnboardingService, times(1)).getAllTaskStatus(); 
		        
		    }
		 
		 public static String asJsonString(final Object obj) {
		    	try {
		    			return new ObjectMapper().writeValueAsString(obj);
		    		}
		    	catch(Exception e)
		    	{
		    		throw new RuntimeException();
		    	}
		}
		
		
	}