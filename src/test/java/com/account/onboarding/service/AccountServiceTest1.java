package com.account.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.account.onboarding.exception.AccountCustomerException;
import com.account.onboarding.model.Account;
import com.account.onboarding.model.AccountInitiative;
import com.account.onboarding.model.CustomerEngagement;
import com.account.onboarding.model.Documents;
import com.account.onboarding.repository.AccountRepository;
import com.account.onboarding.request.AccountVO;
import com.account.onboarding.request.InitiativesVO;
import com.account.onboarding.service.impl.AccountServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest1 {
	@InjectMocks
	private AccountServiceImpl accountService;
	@Mock
	private AccountRepository accountRepository;

	@Test
	public void viewAccountTest_Success() {
		when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.of(account()));
		String accountOverview = accountService.viewAccountOverview("0dc10f46-d862-422c-8ee7-bf48c1f55cd7");
		assertEquals("about retail", accountOverview);
	}

	@Test
	public void viewAccountTest_Failure() {
		Account account = new Account("521cbcfa-f527-4fed-9b4c-5cc369160355", "account",
				LocalDateTime.now(), "admin",
				LocalDateTime.now(), "admin", "about retail",
				engagements(), null, initiatives(), documents());
		when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.of(account));
		String accountOverview = accountService.viewAccountOverview("0dc10f46-d862-422c-8ee7-bf48c1f55cd7");
		assertNull(accountOverview);
	}

	@Test
	public void viewAccountTest_Exception() {
		String accountId = "521cbcfa-f527-4fed-9b4c-5cc369160355";
		AccountCustomerException e = assertThrows(AccountCustomerException.class, () -> accountService.viewAccountOverview(accountId));
		assertEquals("Id Not Found", e.getErrorMessage());

	}

	@Test
	void updateOverviewTest_Success() {
		AccountVO accountVo = new AccountVO("521cbcfa-f527-4fed-9b4c-5cc369160355", "about retail", "admin");
		account().setAccountOverview(accountVo.getAccountOverview());
		account().setUpdatedBy(accountVo.getUpdatedBy());
		account().setUpdatedDate(LocalDateTime.now());
		when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.of(account()));
		String accountOverView = accountService.updateOverview(accountVo);
		assertEquals("about retail", accountOverView);
	}

	@Test
	void updateOverviewTest_Failure() {
		AccountVO accountVo = new AccountVO("521cbcfa-f527-4fed-9b4c-5cc369160355", null, "admin");
		account().setAccountOverview(accountVo.getAccountOverview());
		account().setUpdatedBy(accountVo.getUpdatedBy());
		account().setUpdatedDate(LocalDateTime.now());
		when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.of(account()));
		String accountOverView = accountService.updateOverview(accountVo);
		assertNull(accountOverView);
	}

	@Test
	public void updateOverviewTest_Exception() {
		AccountVO accountVo = new AccountVO("1", "about retail", "admin");
		when(accountRepository.findById(Mockito.anyString())).thenThrow(new AccountCustomerException("Id Not Found",HttpStatus.NOT_FOUND));
		AccountCustomerException ex = assertThrows(AccountCustomerException.class,
				() -> accountService.updateOverview(accountVo));
		assertEquals("Id Not Found", ex.getErrorMessage());
	}

	/*
	 * @Test public void saveInitiativesTest_Success() { Account account = new
	 * Account("521cbcfa-f527-4fed-9b4c-5cc369160355", "account",
	 * LocalDateTime.now(), "admin", LocalDateTime.now(), "admin", "about retail",
	 * engagements(), "overview", initiatives(), documents()); InitiativesVO
	 * initiativesVO = new InitiativesVO("521cbcfa-f527-4fed-9b4c-5cc369160355",
	 * "init1", "desc1", "Admin");
	 * when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.of(
	 * account)); List<AccountInitiative> initiatives =
	 * accountService.saveInitiatives(initiativesVO);
	 * assertEquals(account.getInitiatives(), initiatives); }
	 */

	@Test
	public void saveInitiativeTest_Exception() {

		InitiativesVO initiativesVO = new InitiativesVO("1234", "init1", "desc1", "Admin");
		when(accountRepository.findById(Mockito.anyString())).thenThrow(new AccountCustomerException("Id Not Found", HttpStatus.NOT_FOUND));
		AccountCustomerException ex = assertThrows(AccountCustomerException.class,
				() -> accountService.saveInitiatives(initiativesVO));
		assertEquals("Id Not Found", ex.getErrorMessage());
	}

	@Test
	void updateInitiativeTest_Success() {
		Account account = new Account("521cbcfa-f527-4fed-9b4c-5cc369160355", "account",
				LocalDateTime.now(), "admin",
				LocalDateTime.now(), "admin", "about retail",
				engagements(), "overview", initiatives(), documents());
		InitiativesVO initiativesVO = new InitiativesVO("521cbcfa-f527-4fed-9b4c-5cc369160355", "name", "desc1",
				"admin");
		account().setUpdatedBy(initiativesVO.getUpdatedBy());
		account().setUpdatedDate(LocalDateTime.now());
		when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.of(account));
		List<AccountInitiative> initiatives = accountService.updateInitiatives(initiativesVO);
		assertEquals(account.getInitiatives(), initiatives);
	}

	@Test
	void updateInitiativeTest_Failure() {
		InitiativesVO initiativesVO = new InitiativesVO("521cbcfa-f527-4fed-9b4c-5cc369160355", null, null, "admin");
		account().setUpdatedBy(initiativesVO.getUpdatedBy());
		account().setUpdatedDate(LocalDateTime.now());
		when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.of(account()));
		AccountCustomerException ex = assertThrows(AccountCustomerException.class,
				() -> accountService.updateInitiatives(initiativesVO));
		assertEquals("Account initiative not found", ex.getErrorMessage());
	}

	@Test
	public void updateInitiativetest_exception() {
		InitiativesVO initiativesVO = new InitiativesVO("521cbcfa-f527-4fed-9b4c-5cc369160355", "name", "desc",
				"admin");
		when(accountRepository.findById(Mockito.anyString())).thenThrow(new AccountCustomerException("Id Not Found",HttpStatus.NOT_FOUND));
		AccountCustomerException ex = assertThrows(AccountCustomerException.class,
				() -> accountService.updateInitiatives(initiativesVO));
		assertEquals("Id Not Found", ex.getErrorMessage());
	}

	@Test
	public void viewIniiativesTest_Success() {
		when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.of(account()));
		List<AccountInitiative> initiatives = accountService.viewInitiatives("0dc10f46-d862-422c-8ee7-bf48c1f55cd7");
		assertEquals(initiatives(), initiatives);
	}

	@Test
	public void viewIniiativest_Failure() {
		Account account = new Account("521cbcfa-f527-4fed-9b4c-5cc369160355", "account",
				LocalDateTime.now(), "admin",
				LocalDateTime.now(), "admin", "about retail",
				engagements(), "overview", null, documents());
		when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.of(account));
		List<AccountInitiative> initiatives = accountService.viewInitiatives("0dc10f46-d862-422c-8ee7-bf48c1f55cd7");
		assertNull(initiatives);
	}

	@Test
	public void viewIniiativesExceptionTest() {
		String accountId = "521cbcfa-f527-4fed-9b4c-5cc369160355";
		AccountCustomerException ex = assertThrows(AccountCustomerException.class,
				() -> accountService.viewInitiatives(accountId));
		assertEquals("Id Not Found", ex.getErrorMessage());
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

	private Account account() {
		Account account = new Account("521cbcfa-f527-4fed-9b4c-5cc369160355", "account",
				LocalDateTime.now(), "admin",
				LocalDateTime.now(), "admin", "overview",
				engagements(), "about retail", initiatives(), documents());
		return account;
	}
}
