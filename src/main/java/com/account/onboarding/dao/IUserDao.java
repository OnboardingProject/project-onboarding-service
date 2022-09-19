package com.account.onboarding.dao;

import java.util.List;

import com.account.onboarding.model.User;

/**
 * This is the dao interface of UserManagement where we declare methods
 *  to get hierarchy of user and to check whether roleId exists
 * 
 * @author
 *
 */
public interface IUserDao {

	public String getUser(String accountName, String createdBy);
	public Integer getRoleId(Integer roleId);
	List<User> getAllUserByHierarchy(String userId);
	List<User> getUserByFirstName(String firstName);
}
