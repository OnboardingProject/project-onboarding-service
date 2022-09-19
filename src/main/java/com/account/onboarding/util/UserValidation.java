package com.account.onboarding.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import com.account.onboarding.exception.EmailNotValidException;
import com.account.onboarding.exception.PhoneNumberNotValidException;
import com.account.onboarding.exception.RoleIdNullException;
import com.account.onboarding.exception.UserNotFoundException;
import com.account.onboarding.exception.UserUpdateException;
import com.account.onboarding.request.UserEditRequest;
import com.account.onboarding.request.UserRequest;

/**
 * This is validation class for UserManagement
 * 
 * @author
 *
 */
@Configuration
public class UserValidation {

	public Boolean validateUser(UserRequest User) {
		boolean isValid = true;
		if (User == null) {
			isValid = false;

		} else if (User.getUserName() == null || User.getUserName().isEmpty()) {
			isValid = false;

		} else if (User.getEmailId() == null || User.getEmailId().isEmpty()) {
			isValid = false;

			throw new EmailNotValidException("Email id is not valid");
		} else if (User.getPhoneNumber() == null || User.getPhoneNumber().isEmpty()) {
			isValid = false;

			throw new PhoneNumberNotValidException("this number is not valid");
		}

		return isValid;

	}

	public boolean phoneValidation(UserRequest userRequest) {
		String phone = userRequest.getPhoneNumber();
		String regex = "\\d{10}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phone);
		if (matcher.matches()) {
			return true;
		} else
			throw new UserNotFoundException("phone number is not valid");
	}

	public boolean emailValidation(UserRequest userRequest) {
		String emailId = userRequest.getEmailId();
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailId);
		if (matcher.matches())
			return true;
		else
			throw new UserNotFoundException("emailId is not valid");
	}

	/**
	 * @param userEdit
	 * @return boolean
	 */
	public boolean updateUserValidation(UserEditRequest userEdit) {
		String userId = userEdit.getUserId();
		String lastUpdatedBy = userEdit.getLastUpdatedBy();
		Integer roleId = userEdit.getRoleId();
		Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher matcherUser = pattern.matcher(userId);
		Matcher matcherUpdater = pattern.matcher(lastUpdatedBy);
		boolean idCheck = matcherUser.find();
		boolean updaterCheck = matcherUpdater.find();

		if (StringUtils.isEmpty(userEdit.getUserId()) || idCheck)

			throw new UserUpdateException("Enter valid userId ");
		else if (roleId <= 0) {
			throw new RoleIdNullException("Enter valid roleId ");
		} else if (StringUtils.isEmpty(userEdit.getLastUpdatedBy()) || updaterCheck) {
			throw new UserUpdateException("Enter valid userId of the updating user");
		} else
			return true;

	}
}
