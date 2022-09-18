package com.account.onboarding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.account.onboarding.model.Account;
import com.account.onboarding.model.CustomerEngagement;
import com.account.onboarding.response.AboutCustomerResponseVO;
import com.account.onboarding.service.AccountService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.var;

@WebMvcTest(value = AccountController.class)
public class AccountControllerTest2 {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AccountService accountService;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testSaveAccount_201() throws Exception {
		Account account = new Account("accountid", "testcustomer", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				"testCreatedBy", null, "", "", null, "", null, null);
		Mockito.doReturn(account).when(accountService).createAccount(Mockito.any());
		MvcResult result = mockMvc.perform(post("/api/v1/account-overview/account").contentType("application/json")
				.content("{\"accountId\": \"string\",\r\n" + "  \"customerName\": \"string\",\r\n"
						+ "  \"createdBy\": \"string\"}"))
				.andExpect(status().isCreated()).andReturn();
		var account1 = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
		assertEquals("accountid", account1.getAccountId());
		assertEquals("testcustomer", account1.getCustomerName());
	}

	@Test
	public void testSaveAboutCustomer_201() throws Exception {
		AboutCustomerResponseVO response = new AboutCustomerResponseVO("TestCustomerName",
				"Description about the customer");
		Mockito.doReturn(response).when(accountService).addAboutCustomer(Mockito.any());
		MvcResult result = mockMvc.perform(
				post("/api/v1/account-overview/account/about-customer").contentType("application/json").content(
						"{\"accountId\": \"string\", \"aboutCustomer\": \"string\",\"updatedBy\": \"string\"}")) /*
																													 * request
																													 */
				.andExpect(status().isCreated()).andReturn();
		var accountTest = objectMapper.readValue(result.getResponse().getContentAsString(),
				AboutCustomerResponseVO.class);
		assertEquals("TestCustomerName", accountTest.getCustomerName());
		assertEquals("Description about the customer", accountTest.getAboutCustomer());
	}

	@Test
	public void testSaveCustomerEngagement_201() throws Exception {

		Mockito.doReturn(Arrays.asList(new CustomerEngagement("testengagementname", "testengagementdescription"),
				new CustomerEngagement("testengagementname1", "testengagementdescription2"))).when(accountService)
				.addCustomerEngagements(Mockito.any());
		MvcResult result = mockMvc
				.perform(post("/api/v1/account-overview/account/engagement").contentType("application/json").content(
						"{\"accountId\": \"test\", \"engagementName\": \"test\",\"engagementDescription\": \"testdescription\",\"updatedBy\": \"test\"}"))
				.andExpect(status().isCreated()).andReturn();
		var engagementList = objectMapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<CustomerEngagement>>() {
				});
		assertEquals(2, engagementList.size());
		assertEquals("testengagementname", engagementList.get(0).getEngagementName());
	}

	@Test
	public void testGetAccount_200() throws Exception {

		Mockito.doReturn(Arrays.asList(new Account("accountid", "walmarttest",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "testCreatedBy", null, "", "", null, "", null, null)))
				.when(accountService).getAllAccounts();
		MvcResult result = mockMvc.perform(get("/api/v1/account-overview/account").contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		var accountList = objectMapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Account>>() {
				});
		assertEquals(1, accountList.size());
		assertEquals("walmarttest", accountList.get(0).getCustomerName());
	}

	@Test
	public void testGetAboutCustomer_200() throws Exception {
		Mockito.doReturn(new AboutCustomerResponseVO("name", "description")).when(accountService)
				.viewAboutCustomer(Mockito.anyString());
		MvcResult result = mockMvc
				.perform(get("/api/v1/account-overview/account/about-customer/testId").contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		var customer = objectMapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<AboutCustomerResponseVO>() {
				});
		assertEquals("name", customer.getCustomerName());
		assertEquals("description", customer.getAboutCustomer());
	}

	@Test
	public void testUpdateCustomer_200() throws Exception {
		AboutCustomerResponseVO response = new AboutCustomerResponseVO("test123", "about the customer");
		Mockito.doReturn(response).when(accountService).updateAboutCustomer(Mockito.any());
		MvcResult result = mockMvc
				.perform(put("/api/v1/account-overview/account/about-customer").contentType("application/json").content(
						"{\"accountId\": \"string\", \"aboutCustomer\": \"string\",\"updatedBy\": \"string\"}")) /*
																													 * request
																													 */
				.andExpect(status().isOk()).andReturn();
		var accountTest = objectMapper.readValue(result.getResponse().getContentAsString(),
				AboutCustomerResponseVO.class);
		assertEquals("test123", accountTest.getCustomerName());
		assertEquals("about the customer", accountTest.getAboutCustomer());
	}

}
