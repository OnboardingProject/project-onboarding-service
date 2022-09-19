package com.account.onboarding.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.account.onboarding.dao.impl.UserDaoImpl;
import com.account.onboarding.model.Types;
import com.account.onboarding.model.User;


/**
 * {@link UserDaoImpl}
 * @author 
 *
 */
public class UserDaoImplTest {

	@InjectMocks
	private UserDaoImpl userDaoImpl;

	@Mock
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	/**
	 * {@link UserDaoImpl#getRoleId(roleId)}
	 * This method checks the test if the roleId is invalid
	 */
	@Test
	public void getRoleIdIfEmptyTest() {
		Integer roleId = 1;
		String permission = "Edit";
		
		Types types = new Types("Role", null, "Admin", permission);
		Query query = new Query();
		Integer role = null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("typeId").is(roleId)), Types.class)).thenReturn(types);
		when(userDaoImpl.getRoleId(roleId)).thenReturn(role);
		assertEquals(role,types.getTypeId() );
	}
	/**
	 * {@link UserDaoImpl#getRoleId(roleId)}
	 * This method checks the test if the roleId given is valid
	 */
	@Test
	public void getRoleIdIfNotEmptyTest() {
		Integer roleId = 2;
		String permission = "Edit";
		
		Types types = new Types("Role", 1, "Admin", permission);
		Query query = new Query();
		Integer role = null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("typeId").is(roleId)), Types.class)).thenReturn(types);
		role=types.getTypeId();
		when(userDaoImpl.getRoleId(roleId)).thenReturn(role);
		assertEquals(role,types.getTypeId());
	}
	
	/**
	 * {@link UserDaoImpl#getRoleId(roleId)}
	 * This method checks the test when query returns empty Types class
	 */
	@Test
	public void getRoleIdIfTypesEmptyTest() {
		Integer roleId = 1;
		String permission = "Edit";
		
		Types types = new Types("Role", null, "Admin", permission);
		Query query = new Query();
		Integer role = null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("typeId").is(roleId)), Types.class)).thenReturn(null);
		when(userDaoImpl.getRoleId(roleId)).thenReturn(role);
		assertEquals(role,types.getTypeId() );
	}
	
	/**
	 * {@link UserDaoImpl#getUser(accountName, createdBy)}
	 * This method checks the test when the method returns null
	 * 
	 */
	@Test
	public void getHierarchyIfEmptyTest() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
	new Date(), "U2345", "U2345", new Date(), 1, null, null, null);
		Query query = new Query();
		String accountName="abc";
		String createdBy="U2345";
		String hierarchy=null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("accountName").is(accountName).and("userId").is(createdBy)),User.class)).thenReturn(user);
		when(userDaoImpl.getUser(accountName,createdBy)).thenReturn(hierarchy);
		assertEquals(hierarchy,user.getHierarchy());
	}
	/**
	 * {@link UserDaoImpl#getUser(accountName, createdBy)}
	 * This method tests when the query returns empty user
	 * 
	 */
	@Test
	public void getHierarchyIfUserEmptyTest() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
	new Date(), "U2345", "U2345", new Date(), 1, null, null, null);
		Query query = new Query();
		String accountName="abc";
		String createdBy="U2345";
		String hierarchy=null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("accountName").is(accountName).and("userId").is(createdBy)),User.class)).thenReturn(null);
		when(userDaoImpl.getUser(accountName,createdBy)).thenReturn(hierarchy);
		assertEquals(hierarchy,user.getHierarchy());
	}
	/**
	 * {@link UserDaoImpl#getUser(accountName, createdBy)}
	 * This is the test case if the method returns hierarchy
	 * 
	 */
	@Test
	public void getHierarchyIfNotEmptyTest() {
		User user = new User("U2345", "Kiara", "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
	new Date(), "U2345", "U2345", new Date(), 1, null, null, null);
		Query query = new Query();
		String accountName="abc";
		String createdBy="U2345";
		String hierarchy=user.getHierarchy();
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("accountName").is(accountName).and("userId").is(createdBy)),User.class)).thenReturn(user);
		when(userDaoImpl.getUser(accountName,createdBy)).thenReturn(hierarchy);
		assertEquals(hierarchy,user.getHierarchy());
	}
}
