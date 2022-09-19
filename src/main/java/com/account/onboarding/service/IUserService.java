package com.account.onboarding.service;

import java.util.ArrayList;
import java.util.List;

import com.account.onboarding.model.User;
import com.account.onboarding.request.UserEditRequest;
import com.account.onboarding.request.UserRequest;
import com.account.onboarding.response.ErrorTextResponse;
import com.account.onboarding.response.UserResponse;

/**
 * This is the service interface of UserManagement
 * 
 * @author 
 *
 */
public interface IUserService {
	public User addUser(UserRequest userRequest);

	public User updateUser(UserEditRequest userEdit);

	public List<UserResponse> findByFirstName(String firstName);

	public List<UserResponse> viewAllUserByHeirarchy(String userId);

	public ArrayList<ErrorTextResponse> deleteUser(List<String> userIds);
}
