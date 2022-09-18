package com.account.onboarding.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.account.onboarding.constants.AccountOverviewConstant;
import com.account.onboarding.exception.AccountCustomerException;
import com.account.onboarding.model.Account;
import com.account.onboarding.model.AccountInitiative;
import com.account.onboarding.model.CustomerEngagement;
import com.account.onboarding.repository.AccountRepository;
import com.account.onboarding.request.AboutCustomerRequestVO;
import com.account.onboarding.request.AccountRequestVO;
import com.account.onboarding.request.AccountVO;
import com.account.onboarding.request.CustomerEngagementRequestVO;
import com.account.onboarding.request.InitiativesVO;
import com.account.onboarding.response.AboutCustomerResponseVO;
import com.account.onboarding.service.AccountService;

import lombok.extern.slf4j.Slf4j;

/**
 * The class that handle all the customer services
 * 
 * @author 226732
 *
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepository;

	/**
	 * Method to add the account
	 *
	 * @param account
	 * @return savedAccount
	 */
	@Override
	public Account createAccount(AccountRequestVO accountRequest) {
		log.info("------------------- createAccount service call start -------------------");
		if (Objects.nonNull(accountRequest)) {
			accountRequest.setAccountId(UUID.randomUUID().toString());
			Account account = new Account();
			account.setAccountId(accountRequest.getAccountId());
			account.setCustomerName(accountRequest.getCustomerName());
			account.setCreatedDate(LocalDateTime.now());
			account.setCreatedBy(accountRequest.getCreatedBy());
			Account savedaccount = accountRepository.save(account);
			log.info("------------------- createAccount service call end -------------------");
			return savedaccount;
		} else {
			log.error("-----------Save failed. Values are null -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Method to save the customer description
	 *
	 * @param customer aboutCustomerRequestVO
	 * @return customer aboutCustomerResponseVO
	 */
	@Override
	public AboutCustomerResponseVO addAboutCustomer(AboutCustomerRequestVO aboutCustomerRequestVO) {
		log.info("------------------- addAboutCustomer service call start -------------------");
		if (Objects.isNull(aboutCustomerRequestVO)) {
			log.error("-----------Save failed. Values are null -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.BAD_REQUEST);
		}
		Optional<Account> accountData = accountRepository.findById(aboutCustomerRequestVO.getAccountId());
		if (accountData.isPresent()) {
			Account account = accountData.get();
			if (Objects.nonNull(account.getAboutCustomer()) && !account.getAboutCustomer().isEmpty()) {
				log.error(
						"-----------------about customer description present. please update to make changes -------------------");
				throw new AccountCustomerException(AccountOverviewConstant.DATA_PRESENT, HttpStatus.CONFLICT);
			}
			account.setAboutCustomer(aboutCustomerRequestVO.getAboutCustomer());
			account.setUpdatedBy(aboutCustomerRequestVO.getUpdatedBy());
			account.setUpdatedDate(LocalDateTime.now());
			Account savedAccount = accountRepository.save(account);
			log.info("------------------- addAboutCustomer service call end -------------------");
			return new AboutCustomerResponseVO(savedAccount.getCustomerName(), savedAccount.getAboutCustomer());
		} else {
			log.error(
					"------------------- Failed to add Desciption About Customer. Given id not found -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Method to save the customer engagements
	 *
	 * @param customer customerEngagementRequestVO
	 * @return customer List<CustomerEngagement>
	 */
	@Override
	public List<CustomerEngagement> addCustomerEngagements(CustomerEngagementRequestVO customerEngagementRequestVO) {
		log.info("------------------- addCustomerEngagements service call start -------------------");
		if (Objects.isNull(customerEngagementRequestVO)) {
			log.error("------------------- Null value  -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.NO_CONTENT);
		}
		Optional<Account> accountData = accountRepository.findById(customerEngagementRequestVO.getAccountId());
		if (!accountData.isPresent()) {
			log.error("----------------- Given account id not found  -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Account account = accountData.get();
		CustomerEngagement customerEngagement = new CustomerEngagement(customerEngagementRequestVO.getEngagementName(),
				customerEngagementRequestVO.getEngagementDescription());
		List<CustomerEngagement> engagementsList = account.getEngagements();
		engagementsList = Optional.ofNullable(engagementsList).orElse(new ArrayList<>());

		if (engagementsList.stream().anyMatch(
				e -> e.getEngagementName().equalsIgnoreCase(customerEngagementRequestVO.getEngagementName()))) {
			log.error("----------------- Existing Engagement name  -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.EXISTING_ENGAGEMENT_NAME,
					HttpStatus.BAD_REQUEST);
		} else {
			engagementsList.add(customerEngagement);
		}
		account.setEngagements(engagementsList);
		account.setUpdatedBy(customerEngagementRequestVO.getUpdatedBy());
		account.setUpdatedDate(LocalDateTime.now());
		Account savedAccount = accountRepository.save(account);
		log.info("------------------- addCustomerEngagements service call end -------------------");
		return savedAccount.getEngagements();

	}

	/**
	 * Method to get all the Accounts
	 * 
	 * @return list of accounts
	 */
	@Override
	public List<Account> getAllAccounts() {
		log.info("------------------- getAllAccounts service call start -------------------");
		List<Account> accounts = accountRepository.findAll();
		if (Objects.nonNull(accounts) && !accounts.isEmpty()) {
			log.info("------------------- getAllAccounts service call end -------------------");
			return accounts;
		} else {
			log.error("------------------- Failed to get details. Account Details not Found -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.NO_ACCOUNT_DETAILS, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Method to get the customer description
	 *
	 * @param accountId
	 * @return customer description
	 */
	@Override
	public AboutCustomerResponseVO viewAboutCustomer(@PathVariable String accountId) {
		log.info("------------------- viewAboutCustomer service call start -------------------");
		Optional<Account> accountData = accountRepository.findById(accountId);
		if (accountData.isPresent()) {
			Account account = accountData.get();
			if (Objects.nonNull(account.getAboutCustomer()) && !account.getAboutCustomer().isEmpty()) {
				log.info("------------------- viewAboutCustomer service call end -------------------");
				return new AboutCustomerResponseVO(account.getCustomerName(), account.getAboutCustomer());
			} else {
				log.error("------------------- Customer Details not Found -------------------");
				throw new AccountCustomerException(AccountOverviewConstant.NO_CUSTOMER_DETAILS, HttpStatus.NOT_FOUND);
			}
		} else {
			log.error("------------------- Not found any account with given id -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Method to update the customer description
	 *
	 * @param customer aboutCustomerRequestVO
	 * @return customer aboutCustomerResponseVO
	 */
	@Override
	public AboutCustomerResponseVO updateAboutCustomer(@RequestBody AboutCustomerRequestVO aboutCustomerRequestVO) {
		log.info("------------------- updateAboutCustomer service call start -------------------");
		if (Objects.isNull(aboutCustomerRequestVO)) {
			log.error("------------- Null values -------------");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.BAD_REQUEST);
		}
		Optional<Account> accountData = accountRepository.findById(aboutCustomerRequestVO.getAccountId());
		if (accountData.isPresent()) {
			Account account = accountData.get();
			account.setAboutCustomer(aboutCustomerRequestVO.getAboutCustomer());
			account.setUpdatedBy(aboutCustomerRequestVO.getUpdatedBy());
			account.setUpdatedDate(LocalDateTime.now());
			Account updatedAccount = accountRepository.save(account);
			log.info("------------------- updateAboutCustomer service call end -------------------");
			return new AboutCustomerResponseVO(updatedAccount.getCustomerName(), updatedAccount.getAboutCustomer());
		} else {
			log.error("------------- Error in updating Customer Details-------------");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public List<CustomerEngagement> viewCustomerEngagements(String accountId) {
		log.info("------------------- viewCustomerEngagements service call start -------------------");
		if (Objects.isNull(accountId) || accountId.isBlank()) {
			log.error("------------------- accountId is Null or blank -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.BAD_REQUEST);
		}
		Optional<Account> accountData = accountRepository.findById(accountId);
		if (!accountData.isPresent()) {
			log.error("----------------- Given account id not found  -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Account account = accountData.get();
		List<CustomerEngagement> engagementsList = account.getEngagements();
		if (Objects.isNull(engagementsList) || engagementsList.isEmpty()) {
			log.error("----------------- Customer Engagements not found -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.NO_ENGAGEMENT_DETAILS, HttpStatus.NOT_FOUND);
		}
		return engagementsList;
	}

	@Override
	public List<CustomerEngagement> updateCustomerEngagements(CustomerEngagementRequestVO customerEngagementRequestVO) {
		log.info("------------------- updateCustomerEngagements service call start -------------------");
		if (Objects.isNull(customerEngagementRequestVO)) {
			log.error("------------------- customerEngagementRequestVO is Null -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.NOT_FOUND);
		}
		Optional<Account> accountData = accountRepository.findById(customerEngagementRequestVO.getAccountId());
		if (!accountData.isPresent()) {
			log.error("----------------- Given account id not found  -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Account account = accountData.get();
		List<CustomerEngagement> engagementsList = account.getEngagements();
		if (Objects.isNull(engagementsList) || engagementsList.isEmpty()) {
			log.error("----------------- Customer Engagements not found to update -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.UPDATE_FAILED_EMPTY_LIST, HttpStatus.NOT_FOUND);
		} else {
			CustomerEngagement engagement = engagementsList.stream().filter(
					c -> c.getEngagementName().equalsIgnoreCase(customerEngagementRequestVO.getEngagementName()))
					.findAny().orElse(null);
			if (Objects.isNull(engagement)) {
				log.error("----------------- Update failed.. Requested engagement name not found  -------------------");
				throw new AccountCustomerException(AccountOverviewConstant.UPDATE_FAILED_NOT_PRESENT,
						HttpStatus.BAD_REQUEST);
			} else {
				engagement.setEngagementName(customerEngagementRequestVO.getEngagementName());
				engagement.setEngagementDesc(customerEngagementRequestVO.getEngagementDescription());
			}
		}
		account.setEngagements(engagementsList);
		account.setUpdatedBy(customerEngagementRequestVO.getUpdatedBy());
		account.setUpdatedDate(LocalDateTime.now());
		Account updatedAccount = accountRepository.save(account);
		log.info("------------------- updateCustomerEngagements service call end -------------------");
		return updatedAccount.getEngagements();
	}

	@Override
	public String updateOverview(AccountVO accountVo) {
		log.info("Update Overview");
		Account account = accountRepository.findById(accountVo.getAccountId())
				.orElseThrow(() -> new AccountCustomerException("Id Not Found", HttpStatus.NOT_FOUND));
		account.setAccountOverview(accountVo.getAccountOverview());
		account.setUpdatedBy(accountVo.getUpdatedBy());
		account.setUpdatedDate(LocalDateTime.now());
		accountRepository.save(account);
		log.info("Exit from update overview");
		return account.getAccountOverview();
	}

	@Override
	public String viewAccountOverview(String accountId) {
		log.info(AccountOverviewConstant.VIEW_START);
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountCustomerException("Id Not Found", HttpStatus.NOT_FOUND));
		log.info(AccountOverviewConstant.VIEW_END);
		return account.getAccountOverview();

	}

	@Override
	public List<AccountInitiative> saveInitiatives(InitiativesVO initiativesVO) {
		log.info(AccountOverviewConstant.SAVE_START);

		if (Objects.isNull(initiativesVO)) {
			log.error("------------------- Null value  -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.NO_CONTENT);
		}
		Optional<Account> accountData = accountRepository.findById(initiativesVO.getAccountId());
		if (!accountData.isPresent()) {
			log.error("----------------- Given account id not found  -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Account account = accountData.get();
		AccountInitiative accountInitiatives = new AccountInitiative(initiativesVO.getInitiativeName(),
				initiativesVO.getInitiativeDescription());
		List<AccountInitiative> initiativeList = account.getInitiatives();
		initiativeList = Optional.ofNullable(initiativeList).orElse(new ArrayList<>());

		if (initiativeList.stream()
				.anyMatch(e -> e.getInitiativeName().equalsIgnoreCase(initiativesVO.getInitiativeName()))) {
			log.error("----------------- Existing initiative name  -------------------");
			throw new AccountCustomerException(AccountOverviewConstant.EXISTING_INITIATIVE_NAME,
					HttpStatus.BAD_REQUEST);
		} else {
			initiativeList.add(accountInitiatives);
		}

		account.setInitiatives(initiativeList);
		account.setUpdatedBy(initiativesVO.getUpdatedBy());
		account.setUpdatedDate(LocalDateTime.now());
		Account savedAccount = accountRepository.save(account);
		log.info("------------------- addCustomerEngagements service call end -------------------");
		return savedAccount.getInitiatives();
	}

	@Override
	public List<AccountInitiative> viewInitiatives(String accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountCustomerException("Id Not Found", HttpStatus.NOT_FOUND));
		return account.getInitiatives();
	}

	@Override
	public List<AccountInitiative> updateInitiatives(InitiativesVO initiativesVO) {
		log.info(AccountOverviewConstant.UPDATE_START);
		Account account = accountRepository.findById(initiativesVO.getAccountId())
				.orElseThrow(() -> new AccountCustomerException("Id Not Found", HttpStatus.NOT_FOUND));
		Integer flag = 0;
		for (AccountInitiative initiative : account.getInitiatives()) {
			if (initiative.getInitiativeName().equalsIgnoreCase(initiativesVO.getInitiativeName())) {
				flag++;
				initiative.setInitiativeDescription(initiativesVO.getInitiativeDescription());
				account.setUpdatedBy(initiativesVO.getUpdatedBy());
				account.setUpdatedDate(LocalDateTime.now());
			}
		}
		if (flag == 0) {
			log.info(AccountOverviewConstant.INITIATIVE_EXCEPTION);
			throw new AccountCustomerException("Account initiative not found", HttpStatus.NOT_FOUND);
		}
		accountRepository.save(account);
		log.info(AccountOverviewConstant.UPDATE_END);
		return account.getInitiatives();
	}
}
