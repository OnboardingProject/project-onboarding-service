package com.account.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.exception.UserNotFoundException;
import com.account.onboarding.model.User;
import com.account.onboarding.request.UserEditRequest;
import com.account.onboarding.request.UserRequest;
import com.account.onboarding.response.ErrorTextResponse;
import com.account.onboarding.response.UserResponse;
import com.account.onboarding.service.IUserService;
import com.account.onboarding.util.UserValidation;
import com.mongodb.MongoException;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the controller class of UserManagement where we add and update
 * details of User
 * 
 * @author
 *
 */
@Slf4j
@RestController
@RequestMapping("api/v1/user")
public class UserController {
	@Autowired
	IUserService userService;

	@Autowired
	UserValidation validation;

	/**
	 * @param userRequest
	 * @return user entity
	 */
	@PostMapping("/add")
	@Operation(summary = "Adding User", description = "API for adding the details of a user")
	public ResponseEntity<User> saveUser(@RequestBody UserRequest userRequest) {

		log.info("Controller saveUser starts with request: {} ", userRequest);
		validation.validateUser(userRequest);
		validation.phoneValidation(userRequest);
		validation.emailValidation(userRequest);
		User userAdd = userService.addUser(userRequest);
		log.info("Controller saveUser ends with httpstatus CREATED with response: {}", userAdd);
		return new ResponseEntity<>(userAdd, HttpStatus.CREATED);
	}

	/**
	 * @param userEdit
	 * @return user entity
	 */
	@PutMapping("/update")
	@Operation(summary = "Updating User", description = "API for updating the role details of a user")
	public ResponseEntity<User> updateUser(@RequestBody UserEditRequest userEdit) {

		log.info("Controller updateUser starts with request: {} ", userEdit);
		validation.updateUserValidation(userEdit);
		User userUpdate = userService.updateUser(userEdit);
		log.info("Controller updateUser method ends with statuscode OK with response: {}", userUpdate);
		return new ResponseEntity<>(userUpdate, HttpStatus.OK);
	}

	/**
	 * Description: view all users will display the list of all users based on
	 * hierarchy
	 * 
	 * @param userId
	 * @return responseUserList
	 * @throws UserNotFoundException
	 * @throws DBException
	 */

	@GetMapping("/views/{userId}")
	@Operation(summary = "View users by hierarchy", description = " This API is used to fetch all users with few info ")
	public ResponseEntity<?> viewAllUserByHeirarchy(@PathVariable String userId)
			throws UserNotFoundException, MongoException {
		log.info(" Contoller viewAllUserByHierarchy starts with userId : {} " + userId);
		List<UserResponse> responseUserList = userService.viewAllUserByHeirarchy(userId);
		if (!CollectionUtils.isEmpty(responseUserList)) {
			log.info("Controller viewAllUserByHierarachy method ends with http status OK");
			return new ResponseEntity<>(responseUserList, HttpStatus.OK);
		} else {
			log.error("Controller viewAllUserByHierarchy method ends with http status NO_CONTENT");
			return new ResponseEntity<>(responseUserList, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Description : Search a user will return the requested user based on user
	 * firstName
	 * 
	 * @param firstName
	 * @return users
	 * @throws UserNotFoundException
	 * @throws DBException
	 */

	@GetMapping("/search/{firstName}")
	@Operation(summary = "Find user by first name", description = "This API is used to fetch the perticular user")
	public ResponseEntity<?> findByFirstName(@PathVariable("firstName") String firstName)
			throws UserNotFoundException, MongoException {
		log.info("Controller findByUserFirstName method starts with firstName : {}" + firstName);
		List<UserResponse> users = userService.findByFirstName(firstName);
		if (!CollectionUtils.isEmpty(users)) {
			log.info("Controller findByUserFirstName method ends with http status OK");
			return new ResponseEntity<>(users, HttpStatus.OK);
		} else {
			log.error("Controller findByUserFirstName method ends with http status NO_CONTENT");
			return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
		}

	}

	@DeleteMapping("/deleteUser")
	@Operation(summary = "Delete a list of users", description = "This API is used to delete list of users")
	public ResponseEntity<?> deleteUser(@RequestBody List<String> userIds) {

		ResponseEntity<?> responseEntity = null;
		ArrayList<ErrorTextResponse> errorText = userService.deleteUser(userIds);
		if (errorText.isEmpty()) {
			responseEntity = new ResponseEntity<>("UserIds deleted Successfully", HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<>(errorText, HttpStatus.OK);
		}

		return responseEntity;
	}
}
