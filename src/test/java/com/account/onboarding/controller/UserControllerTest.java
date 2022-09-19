package com.account.onboarding.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.account.onboarding.model.User;
import com.account.onboarding.request.UserEditRequest;
import com.account.onboarding.request.UserRequest;
import com.account.onboarding.response.UserResponse;
import com.account.onboarding.service.IUserService;
import com.account.onboarding.util.UserValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is the User controller test class where
 * we test add user api and update user api
 * {@link UserController }
 * @author 
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@InjectMocks
	private UserController userController;
	
	@Mock
	IUserService userService;
	
	@Mock
	UserValidation validation;
	
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}
	
	/**
	 * {@link UserController#saveUser(UserRequest)}
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void addUserTest() throws JsonProcessingException, Exception{
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U1234", "U1234", new Date(), 1, null, "U2345, U1234", null);
		UserRequest userRequest = new UserRequest("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com",
				"9898988787", "java", "U1234", "U1234", 1);
		validation.validateUser(userRequest);
		validation.emailValidation(userRequest);
		validation.phoneValidation(userRequest);
		when(userService.addUser(Mockito.any(UserRequest.class))).thenReturn(user);
		mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/user/add").contentType(MediaType.APPLICATION_JSON)
		.content(new ObjectMapper().writeValueAsString(userRequest)))
		.andExpect(MockMvcResultMatchers.status().is(201));
		
	}
	 /**
	  * {@link UserController#updateUser(UserEdit)}
	 * @throws Exception
	 */
	@Test
	    public void updateTest() throws Exception {
		 User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
					new Date(), "U2345", "U2345", new Date(), 1, null, "U2345", null);
			UserEditRequest userEdit = new UserEditRequest("U2345", 1, "U2345");
	        when(userService.updateUser(any())).thenReturn(user);
	        mockMvc.perform(put("/api/v1/user/update").contentType(MediaType.APPLICATION_JSON).content(asJsonString(userEdit)))
	                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
			
	    }
		
		  public static String asJsonString(final Object obj) { 
			  try { 
				  return new ObjectMapper().writeValueAsString(obj); } 
			  catch (Exception e) { 
				  throw new RuntimeException(e); } }
		  
		  @Test
			public void viewAllUserByHeirarchyTestSuccess() throws Exception {
				String userId = "u101";
				List<UserResponse> userResponses = new ArrayList<>();
				UserResponse response = new UserResponse();
				response.setUserId("u101");
				response.setUserName("Mayank Tripati");
				response.setRole(101);
				userResponses.add(response);
				when(userService.viewAllUserByHeirarchy(userId)).thenReturn(userResponses);
				mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/views/u101").contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().isOk());

			}

			@Test
			public void viewAllUserByHeirarchyTestFailure() throws Exception {
				List<UserResponse> userResponses = new ArrayList<>();
				userResponses.clear();
				when(userService.viewAllUserByHeirarchy(null)).thenReturn(null);
				mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/views/u1766").contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().isNoContent());

			}

			@Test
			public void findByFirstNameTestSuccess() throws JsonProcessingException, Exception {
				String firstName = "Mayank";
				List<UserResponse> userResponses = new ArrayList<>();
				UserResponse response = new UserResponse();
				response.setUserId("u101");
				response.setUserName("Mayank Tripati");
				response.setRole(101);
				userResponses.add(response);
				when(userService.findByFirstName(firstName)).thenReturn(userResponses);
				mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/search/Mayank")
						.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(response)))
						.andExpect(MockMvcResultMatchers.status().isOk());
			}

			@Test
			public void findByFirstNameTestFailure() throws JsonProcessingException, Exception {
				String firstName = null;
				List<UserResponse> userResponses = new ArrayList<>();
				UserResponse response = new UserResponse();
				response.setUserId("u101");
				response.setUserName("Mayank Tripati");
				response.setRole(101);
				userResponses.add(response);
				when(userService.findByFirstName(firstName)).thenReturn(userResponses);
				mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/search/amith").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(userResponses)))
						.andExpect(MockMvcResultMatchers.status().isNoContent());
			}
		 
}
