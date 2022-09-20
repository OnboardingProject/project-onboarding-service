package com.account.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.account.onboarding.dao.impl.UserDaoImpl;
import com.account.onboarding.exception.RoleAlreadySameException;
import com.account.onboarding.exception.RoleIdNullException;
import com.account.onboarding.exception.UserNotFoundException;
import com.account.onboarding.model.User;
import com.account.onboarding.repository.UserRepository;
import com.account.onboarding.request.UserEditRequest;
import com.account.onboarding.request.UserRequest;
import com.account.onboarding.service.impl.UserServiceImpl;
import com.mongodb.MongoException;

/**This is the service test class of user management 
 * {@link UserService}
 * @author 
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	UserDaoImpl userDaoImpl;

	@Mock
	UserRepository userRepository;

	/**
	 * {@link UserServiceImpl#addUser(UserRequest)}
	 * This method tests when the user is saved to database when the hierarchy is returned
	 * from UserDaoImpl class
	 * 
	 */
	@Test
	public void addUserTest() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U1234", "U1234", new Date(), 1, null, "U2345, U1234", null);
		UserRequest userRequest = new UserRequest("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com",
				"9898988787", "java", "U1234", "U1234", 1);
		when(userDaoImpl.getUser("abc", "U1234")).thenReturn("U2345, U1234");
		when(userService.addUser(userRequest)).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);
		
	}

	/**
	 * {@link UserServiceImpl#addUser(UserRequest)#addUserTestElse()}
	 * This is the test method to check user is saved when the hierarchy is not returned
	 * from UserDaoImpl class
	 * 
	 */
	@Test 
	public void addUserTestElse() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2345", "U2345", new Date(), 1, null, null, null);
		UserRequest userRequest = new UserRequest("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com",
 				"9898988787", "java", "U2345", "U2345", 1);
		when(userDaoImpl.getUser("abc", "U2345")).thenReturn(null);
		when(userService.addUser(userRequest)).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);
	
	}

	/** 
	 * 
	 * {@link UserServiceImpl#updateUser(UserEdit)#updateTest()}
	 * This is the method to test whether the user is updated without exceptions 
	 */
	@Test
	public void updateTest() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2345", "U2345", new Date(), 1, null, "U2345", null);
		UserEditRequest userEdit = new UserEditRequest("U2345", 1, "U2345");
		when(userRepository.findById(userEdit.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.save(user)).thenReturn(user);
		user.setRoleId(2);
		User userEdit1 = userService.updateUser(userEdit);
		verify(userRepository, times(1)).save(user);
		verify(userRepository, times(2)).findById(userEdit1.getUserId());
		
	}

	/**
	 * {@link UserServiceImpl#updateUser(UserEdit)#userNotUpdateRoleSame()}
	 * This is the method to check the test when the role 
	 * entered is same of that particular user
	 * 
	 */
	@Test
	public void userNotUpdateRoleSame() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2345", "U2345", new Date(), 1, null, "U2345", null);
		UserEditRequest userEdit = new UserEditRequest("U2345", 1, "U2345");
		Optional<User> optional = Optional.of(user);
		when(userRepository.findById(user.getUserId())).thenReturn(optional);
		when(userRepository.save(user)).thenReturn(user);
		user.setRoleId(1);
		assertThrows(RoleAlreadySameException.class, () -> {
			userService.updateUser(userEdit);
		});
	}
	
	/**
	 * {@link UserServiceImpl#updateUser(UserEdit)#userNotUpdateRoleInvalid()}
	 * This is the method that tests when the roleId entered is invalid
	 * 
	 */
	@Test
	public void userNotUpdateRoleInvalid() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2345", "U2345", new Date(), 1, null, "U2345", null);
		UserEditRequest userEdit = new UserEditRequest("U2345", 1, "U2345");
		Optional<User> optional = Optional.of(user);
		when(userRepository.findById(user.getUserId())).thenReturn(optional);
		when(userDaoImpl.getRoleId(1)).thenReturn(null);
		when(userRepository.save(user)).thenReturn(user);
		user.setRoleId(2);
		assertThrows(RoleIdNullException.class, () -> {
			userService.updateUser(userEdit);
		});
	}
	/**
	 * {@link UserServiceImpl#updateUser(UserEdit)#userNotUpdateUserNotFound() }
	 * This is the method to check the test when the last updated by user does not exists
	 */
	@Test
	public void userNotUpdateCreatedByInvalid() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2346", "U2346", new Date(), 1, null, "U2345", null);
		UserEditRequest userEdit = new UserEditRequest("U2345", 1, "U23");
		Optional<User> optional = Optional.of(user);
		when(userRepository.findById(user.getUserId())).thenReturn(optional);
		when(userRepository.findById(user.getLastUpdateBy())).thenReturn(Optional.empty());
		when(userRepository.save(user)).thenReturn(user);
		user.setRoleId(2);
		assertThrows(UserNotFoundException.class, () -> {
			userService.updateUser(userEdit);
		});
	}

	/**
	 * {@link UserServiceImpl#updateUser(UserEdit)#userNotUpdateMongoException()}
	 * This method is to test when user details is not saved in database throws mongoexception
	 */
	@Test
	public void userNotUpdateMongoException() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2345", "U2345", new Date(), 1, null, "U2345", null);
		UserEditRequest userEdit = new UserEditRequest("U2345", 1, "U2345");
		Optional<User> optional = Optional.of(user);
		when(userRepository.findById(user.getUserId())).thenReturn(optional);
		when(userRepository.save(user)).thenReturn(null);
		user.setRoleId(2);
		assertThrows(MongoException.class, () -> {
			userService.updateUser(userEdit);
		});
	}

	/**
	 * {@link UserServiceImpl#updateUser(UserEdit)#userNotUpdateUserNotFound() }
	 * This is the method to check the test when user does not exists
	 */
	@Test
	public void userNotUpdateUserNotFound() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2345", "U2345", new Date(), 1, null,  "U2345", null);
		UserEditRequest userEdit = new UserEditRequest("U2345", 1, "U2345");
		when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());		
		when(userRepository.save(user)).thenReturn(user);
		user.setRoleId(2);
		assertThrows(UserNotFoundException.class, () -> {
			userService.updateUser(userEdit);
		});
	}

	/**
	 * {@link UserServiceImpl#updateUser(UserEdit)#userNotUpdateFinalException()}
	 * This is the method to check when the update method does not work 
	 * and throws exception
	 */
	@Test
	public void userNotUpdateFinalException() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
				new Date(), "U2345", "U2345", new Date(), 1, null, "U2345", null);
		UserEditRequest userEdit = new UserEditRequest("U2345", 1, "U2345");
		when(userRepository.findById(user.getUserId())).thenThrow(RuntimeException.class);
		when(userRepository.save(user)).thenReturn(user);
		user.setRoleId(2);
		assertThrows(Exception.class, () -> {
			userService.updateUser(userEdit);
		});
	}
	
	public User getUser() {
		User user1 = new User();
		user1.setUserId("1001");
		user1.setUserName("Charlie");
		user1.setAccountName("Retail");
		user1.setFirstName("charlie");
		user1.setLastName("Chap");
		user1.setEmailId("charlie@gmail.com");
		user1.setPhoneNo("123456");
		user1.setDesignation("admin");
		user1.setCreatedBy("Admin");
		user1.setLastUpdateBy("Admin");
		user1.setRoleId(123);
		return user1;
	}

	public Error getError() {
		return null;
	}

	@Test
	public void testDeleteUser() {
				
		Optional<User> optionalUser = Optional.of(getUser());
		when(userRepository.findById(Mockito.anyString())).thenReturn(optionalUser);
		doNothing().when(userRepository).deleteById(Mockito.anyString()); // void return type
		List<String> userIds = new ArrayList<String>();
		userIds.add("1001");
		userService.deleteUser(userIds);
		
	}
	@Test
	public void testDeleteUserWithNullUserIdList() {
		
		Optional<User> optionalUser = Optional.of(getUser());
		when(userRepository.findById(Mockito.anyString())).thenReturn(optionalUser);
		doNothing().when(userRepository).deleteById(Mockito.anyString()); // void return type
		List<String> userIds = new ArrayList<String>();
		userService.deleteUser(userIds);
	}
	
	@Test
	public void testDeleteUserNotInDB() {
		
		Optional<User> optionalUser =Optional.empty();
		when(userRepository.findById(Mockito.anyString())).thenReturn(optionalUser);
		doNothing().when(userRepository).deleteById(Mockito.anyString()); // void return type
		List<String> userIds = new ArrayList<String>();
		userIds.add("1003");
		userIds.add("1004");
		userService.deleteUser(userIds);
	}
	
	@Test
	public void testDeleteUserInValidUserIds() {
		
		Optional<User> optionalUser =Optional.empty();
		when(userRepository.findById(Mockito.anyString())).thenReturn(optionalUser);
		doNothing().when(userRepository).deleteById(Mockito.anyString()); // void return type
		List<String> userIds = new ArrayList<String>();
		userIds.add("");
		userIds.add("1004");
		userService.deleteUser(userIds);
	}
	@Test
	public void testDeleteUserException() {
		
		when(userRepository.findById(Mockito.anyString())).thenThrow(new MongoException("Problem in Exception"));
		doNothing().when(userRepository).deleteById(Mockito.anyString()); // void return type
		List<String> userIds = new ArrayList<String>();
		userIds.add("");
		userIds.add("1004");
		assertThrows(RuntimeException.class, () -> {
			userService.deleteUser(userIds);
		});
	}
}
