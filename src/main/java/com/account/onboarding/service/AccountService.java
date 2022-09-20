package com.account.onboarding.service;

import java.util.List;

import com.account.onboarding.model.Account;
import com.account.onboarding.model.AccountInitiative;
import com.account.onboarding.model.CustomerEngagement;
import com.account.onboarding.request.AboutCustomerRequestVO;
import com.account.onboarding.request.AccountRequestVO;
import com.account.onboarding.request.AccountVO;
import com.account.onboarding.request.CustomerEngagementRequestVO;
import com.account.onboarding.request.InitiativesVO;
import com.account.onboarding.response.AboutCustomerResponseVO;

public interface AccountService {

	public List<Account> getAllAccounts();

	public Account createAccount(AccountRequestVO account);

	public AboutCustomerResponseVO addAboutCustomer(AboutCustomerRequestVO aboutCustomerRequestVO);

	public AboutCustomerResponseVO viewAboutCustomer(String accountId);

	public AboutCustomerResponseVO updateAboutCustomer(AboutCustomerRequestVO aboutCustomerRequestVO);

	public List<CustomerEngagement> addCustomerEngagements(CustomerEngagementRequestVO customerEngagementRequestVO);

	public List<CustomerEngagement> viewCustomerEngagements(String accountId);

	public List<CustomerEngagement> updateCustomerEngagements(CustomerEngagementRequestVO customerEngagementRequestVO);

	public String updateOverview(AccountVO accountVo);

	public String viewAccountOverview(String accountId);

	public List<AccountInitiative> saveInitiatives(InitiativesVO initiativesVO);

	public List<AccountInitiative> viewInitiatives(String accountId);

	public List<AccountInitiative> updateInitiatives(InitiativesVO initiativesVO);

}
