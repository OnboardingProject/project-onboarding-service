package com.account.onboarding.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.account.onboarding.model.AccountInitiative;
import com.account.onboarding.model.CustomerEngagement;
import com.account.onboarding.model.Documents;
import com.account.onboarding.request.AccountVO;
import com.account.onboarding.request.InitiativesVO;
import com.account.onboarding.service.impl.AccountServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountControllerTest1 {

	@InjectMocks
	AccountController accountcontroller;

	@Mock
	AccountServiceImpl accountService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(accountcontroller).build();
	}

	@Test
	public void saveOverviewTest_Sucess() throws JsonProcessingException, Exception {
		when(accountService.updateOverview(Mockito.any(AccountVO.class))).thenReturn("about retail");
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account-overview").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(accountVo())))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$").value("about retail"));
		verify(accountService, times(1)).updateOverview(Mockito.any(AccountVO.class));
	}

	@Test
	public void saveOverviewTest_Failure() throws JsonProcessingException, Exception {
		when(accountService.updateOverview(Mockito.any(AccountVO.class))).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account-overview").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(new AccountVO())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void updateOverviewTest_success() throws JsonProcessingException, Exception {
		AccountVO accountVo = new AccountVO("521cbcfa-f527-4fed-9b4c-5cc369160355", "walmart", "admin");
		when(accountService.updateOverview(Mockito.any(AccountVO.class))).thenReturn("walmart");
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account-overview").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(accountVo)))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$").value("walmart"));
		verify(accountService, times(1)).updateOverview(Mockito.any(AccountVO.class));
	}

	@Test
	public void updateOverviewTest_failure() throws JsonProcessingException, Exception {
		when(accountService.updateOverview(Mockito.any(AccountVO.class))).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account-overview").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(new AccountVO())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void viewAccountTest_Success() throws Exception {
		when(accountService.viewAccountOverview(Mockito.anyString())).thenReturn("about retail");
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account-overview/521cbcfa-f527-4fed-9b4c-5cc369160355")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$").value("about retail"));
		verify(accountService, times(1)).viewAccountOverview(Mockito.anyString());

	}

	@Test
	public void saveInitiativeTest_Success() throws JsonProcessingException, Exception {
		InitiativesVO initiativesVo = new InitiativesVO("521cbcfa-f527-4fed-9b4c-5cc369160355", "name1", "retail",
				"admin");
		when(accountService.saveInitiatives(Mockito.any(InitiativesVO.class)))
				.thenReturn(List.of(new AccountInitiative("name", "desc"), new AccountInitiative("name1", "retail")));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account-overview/account/initiatives")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(initiativesVo)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$[0].initiativeName").value("name"));
		verify(accountService, times(1)).saveInitiatives(Mockito.any(InitiativesVO.class));
	}

	@Test
	public void saveInitiativeTest_Failure() throws JsonProcessingException, Exception {
		when(accountService.saveInitiatives(Mockito.any(InitiativesVO.class))).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account-overview/account/initiatives")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(new InitiativesVO())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

	@Test
	public void updateInitiativeTest_success() throws JsonProcessingException, Exception {
		InitiativesVO initiativesVo = new InitiativesVO("521cbcfa-f527-4fed-9b4c-5cc369160355", "name", "retail",
				"admin");
		when(accountService.updateInitiatives(Mockito.any(InitiativesVO.class)))
				.thenReturn(List.of(new AccountInitiative("name", "retail")));
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account-overview/account/initiatives")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(initiativesVo)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].initiativeName").value("name"));
		verify(accountService, times(1)).updateInitiatives(Mockito.any(InitiativesVO.class));
	}

	@Test
	public void updateinitiativeTest_failure() throws JsonProcessingException, Exception {
		when(accountService.updateInitiatives(Mockito.any(InitiativesVO.class))).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account-overview/account/initiatives")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(new InitiativesVO())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void viewInitiativesTest_Sucess() throws JsonProcessingException, Exception {
		when(accountService.viewInitiatives(Mockito.anyString()))
				.thenReturn(List.of(new AccountInitiative("name", "desc")));
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/v1/account-overview/account/initiatives/521cbcfa-f527-4fed-9b4c-5cc369160355")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].initiativeName").value("name"));
		verify(accountService, times(1)).viewInitiatives(Mockito.anyString());
	}

	public List<AccountInitiative> initiatives() {
		List<AccountInitiative> initiative = new ArrayList<AccountInitiative>();
		initiative.add(new AccountInitiative("name", "desc"));
		return initiative;
	}

	public List<CustomerEngagement> engagements() {
		List<CustomerEngagement> engagements = new ArrayList<CustomerEngagement>();
		engagements.add(new CustomerEngagement("name", "desc"));
		return engagements;
	}

	public List<Documents> documents() {
		List<Documents> documents = new ArrayList<Documents>();
		documents.add(new Documents("1", "doc1", "desc1", 1, "status1"));
		return documents;
	}

	private AccountVO accountVo() {
		AccountVO accountVo = new AccountVO("521cbcfa-f527-4fed-9b4c-5cc369160355", "about retail", "admin");
		return accountVo;
	}

}
