package com.account.onboarding.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.AccountOverviewConstant;
import com.account.onboarding.model.Account;
import com.account.onboarding.model.AccountInitiative;
import com.account.onboarding.model.CustomerEngagement;
import com.account.onboarding.request.AboutCustomerRequestVO;
import com.account.onboarding.request.AccountRequestVO;
import com.account.onboarding.request.AccountVO;
import com.account.onboarding.request.CustomerEngagementRequestVO;
import com.account.onboarding.request.InitiativesVO;
import com.account.onboarding.response.AboutCustomerResponseVO;
import com.account.onboarding.service.AccountService;

import lombok.extern.slf4j.Slf4j;

/**
 * The class that handles all the api requests related to customer
 * 
 * @author 226732
 *
 */

@Slf4j
@RestController
@RequestMapping("api/v1/account-overview")
public class AccountController {

	@Autowired
	AccountService accountService;

	/**
	 * Method to add the account details
	 * 
	 * @param account
	 * @return saved account
	 */
	@PostMapping("/account")
	public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountRequestVO accountRequest) {
		log.info("------------- createAccount Controller call start-------------");
		Account savedAccount = accountService.createAccount(accountRequest);
		log.info("------------- createAccount Controller call end-------------");
		return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
	}

	/**
	 * Method to save description about customer
	 * 
	 * @param aboutCustomerVO
	 * @return responseEntity
	 */
	@PostMapping("/account/about-customer")
	public ResponseEntity<AboutCustomerResponseVO> addAboutCustomer(
			@Valid @RequestBody AboutCustomerRequestVO aboutCustomerRequestVO) {
		log.info("------------- addAboutCustomer controller call start -------------");
		AboutCustomerResponseVO aboutCustomerResponseVO = accountService.addAboutCustomer(aboutCustomerRequestVO);
		log.info("------------- addAboutCustomer controller call end -------------");
		return new ResponseEntity<>(aboutCustomerResponseVO, HttpStatus.CREATED);
	}

	/**
	 * @param customerEngagementRequestVO
	 * @return
	 */
	@PostMapping("/account/engagement")
	public ResponseEntity<List<CustomerEngagement>> createCustomerEngagement(
			@Valid @RequestBody CustomerEngagementRequestVO customerEngagementRequestVO) {
		log.info("------------- createCustomerEngagement Controller call start-------------");
		List<CustomerEngagement> customerEngagementList = accountService
				.addCustomerEngagements(customerEngagementRequestVO);
		log.info("------------- createCustomerEngagement Controller call end-------------");
		return new ResponseEntity<>(customerEngagementList, HttpStatus.CREATED);
	}

	/**
	 * Method to get all accounts details
	 * 
	 * @return List of Accounts
	 */
	@GetMapping("/account")
	public ResponseEntity<List<Account>> getAllAccounts() {
		log.info("------------- getAllAccounts controller call start -------------");
		List<Account> accounts = accountService.getAllAccounts();
		log.info("------------- getAllAccounts controller call end -------------");
		return new ResponseEntity<>(accounts, HttpStatus.OK);
	}

	/**
	 * Method to save description about customer
	 * 
	 * @param accountId
	 * @return aboutCustomerResponseVO
	 */
	@GetMapping("/account/about-customer/{accountId}")
	public ResponseEntity<AboutCustomerResponseVO> viewAboutCustomer(@PathVariable("accountId") String accountId) {
		log.info("------------- viewAboutCustomer controller call start -------------");
		AboutCustomerResponseVO aboutCustomerResponseVO = accountService.viewAboutCustomer(accountId);
		log.info("------------- viewAboutCustomer controller call end -------------");
		return new ResponseEntity<>(aboutCustomerResponseVO, HttpStatus.OK);
	}

	@GetMapping("/account/engagement/{accountId}")
	public ResponseEntity<List<CustomerEngagement>> viewCustomerEngagements(
			@PathVariable("accountId") String accountId) {
		log.info("------------- viewCustomerEngagements controller call start -------------");
		List<CustomerEngagement> customerEngagementList = accountService.viewCustomerEngagements(accountId);
		log.info("------------- viewCustomerEngagements controller call end -------------");
		return new ResponseEntity<>(customerEngagementList, HttpStatus.OK);
	}

	/**
	 * @param aboutCustomerVO
	 * @return aboutCustomerResponseVO
	 */
	@PutMapping("/account/about-customer")
	public ResponseEntity<AboutCustomerResponseVO> updateAboutCustomer(
			@Valid @RequestBody AboutCustomerRequestVO aboutCustomerRequestVO) {
		log.info("------------- updateAboutCustomer controller call start -------------");
		AboutCustomerResponseVO aboutCustomerResponseVO = accountService.updateAboutCustomer(aboutCustomerRequestVO);
		log.info("------------- updateAboutCustomer controller call end -------------");
		return new ResponseEntity<>(aboutCustomerResponseVO, HttpStatus.OK);
	}

	/**
	 * @param customerEngagementRequestVO
	 * @return
	 */
	@PutMapping("/account/engagement")
	public ResponseEntity<List<CustomerEngagement>> updateCustomerEngagements(
			@Valid @RequestBody CustomerEngagementRequestVO customerEngagementRequestVO) {
		log.info("------------- updateCustomerEngagements controller call start -------------");
		List<CustomerEngagement> customerEngagementList = accountService
				.updateCustomerEngagements(customerEngagementRequestVO);
		log.info("------------- updateCustomerEngagements controller call end -------------");
		return new ResponseEntity<>(customerEngagementList, HttpStatus.OK);
	}

	/**
	 * accountSave is used to add account overview description
	 * 
	 * @param accountVo
	 * @return accountDescription
	 */

	@PostMapping()
	public ResponseEntity<String> addAccountOverview(@RequestBody @Valid AccountVO accountVo) {
		if (accountVo.getAccountId() != null) {
			String accountDescription = accountService.updateOverview(accountVo);
			log.info(AccountOverviewConstant.SAVE_SUCCESS);
			return new ResponseEntity<>(accountDescription, HttpStatus.OK);
		} else {
			log.debug(AccountOverviewConstant.SAVE_FAILED);
			return new ResponseEntity<>(AccountOverviewConstant.SAVE_FAILED, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * updateOverview method is to update account overview
	 * 
	 * @param accountVo
	 * @return updatedAccount
	 */
	@PutMapping()
	public ResponseEntity<String> updateOverview(@RequestBody @Valid AccountVO accountVo) {

		if (accountVo.getAccountId() != null) {
			String updatedAccount = accountService.updateOverview(accountVo);
			log.info(AccountOverviewConstant.UPDATE_SUCCESS);
			return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
		} else {
			log.debug(AccountOverviewConstant.UPDATE_FAILED);
			return new ResponseEntity<>(AccountOverviewConstant.UPDATE_FAILED, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * viewAccount method is used to view Account Overview
	 * 
	 * @param accountId
	 * @return accountOverview
	 */
	@GetMapping("/{accountId}")
	public ResponseEntity<String> viewAccountOverview(@PathVariable String accountId) {

		String accountOverview = accountService.viewAccountOverview(accountId);
		log.info(AccountOverviewConstant.VIEW_SUCCESS);
		return new ResponseEntity<>(accountOverview, HttpStatus.OK);

	}

	/**
	 * saveInitiatives is used to save initiatives of the account
	 * 
	 * @param initiativesVO
	 * @return initiatives
	 */

	@PostMapping("/account/initiatives")
	public ResponseEntity<List<AccountInitiative>> saveInitiatives(@RequestBody InitiativesVO initiativesVO) {
		if (initiativesVO.getAccountId() != null) {
			List<AccountInitiative> initiatives = accountService.saveInitiatives(initiativesVO);
			log.info(AccountOverviewConstant.SAVE_SUCCESS);
			return new ResponseEntity<List<AccountInitiative>>(initiatives, HttpStatus.CREATED);
		} else {
			log.debug(AccountOverviewConstant.SAVE_FAILED);
			return new ResponseEntity<List<AccountInitiative>>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * updateInitiatives used to update any initiatives of the account
	 * 
	 * @param initiativesVO
	 * @return updatedInitiatives
	 */
	@PutMapping("/account/initiatives")
	public ResponseEntity<List<AccountInitiative>> updateInitiative(@RequestBody InitiativesVO initiativesVO) {
		if (initiativesVO.getAccountId() != null) {
			List<AccountInitiative> updatedInitiatives = accountService.updateInitiatives(initiativesVO);
			log.info(AccountOverviewConstant.UPDATE_SUCCESS);
			return new ResponseEntity<List<AccountInitiative>>(updatedInitiatives, HttpStatus.OK);
		} else {
			log.info(AccountOverviewConstant.UPDATE_FAILED);
			return new ResponseEntity<List<AccountInitiative>>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * viewInitiatives used to view initiatives of the account
	 * 
	 * @param accountId
	 * @return view
	 */

	@GetMapping("/account/initiatives/{accountId}")
	public ResponseEntity<List<AccountInitiative>> viewInitiatives(@PathVariable String accountId) {

		List<AccountInitiative> view = accountService.viewInitiatives(accountId);
		log.info(AccountOverviewConstant.VIEW_SUCCESS);
		return new ResponseEntity<>(view, HttpStatus.OK);

	}
}
