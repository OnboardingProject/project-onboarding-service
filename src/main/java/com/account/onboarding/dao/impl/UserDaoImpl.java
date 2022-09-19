package com.account.onboarding.dao.impl;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.account.onboarding.dao.IUserDao;
import com.account.onboarding.model.Types;
import com.account.onboarding.model.User;

/**
 * This is the dao implementation class of UserManagement where we returns
 * hierarchy of user and check whether roleId exists or not
 * 
 * @author
 *
 */
@Service
public class UserDaoImpl implements IUserDao {

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * This is the getUser method where we return user entity from mongoTemplate and
	 * returns hierarchy of that user
	 * 
	 * @param accountName, createdBy
	 * @return hierarchy
	 *
	 */
	@Override
	public String getUser(String accountName, String createdBy) {
		Query query = new Query();
		String hierarchy = null;
		query.addCriteria(Criteria.where("accountName").is(accountName).and("userId").is(createdBy));
		User user = mongoTemplate.findOne(query, User.class);
		if (ObjectUtils.isNotEmpty(user)) {
			hierarchy = user.getHierarchy();
		}
		return hierarchy;

	}

	/**
	 * This is getRoleId method where we check whether the roleId given for a user
	 * exists in the Types class
	 * 
	 * @param roleId of user class
	 * @return typeId of Types class
	 *
	 */
	@Override
	public Integer getRoleId(Integer roleId) {
		Query query = new Query();
		Integer role = null;
		query.addCriteria(Criteria.where("typeId").is(roleId));
		Types types = mongoTemplate.findOne(query, Types.class);
		if (ObjectUtils.isNotEmpty(types)) {
			role = types.getTypeId();
		}
		return role;
	}

	@Override
	public List<User> getAllUserByHierarchy(String userId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("hierarchy").regex(userId));
		List<User> users = mongoTemplate.find(query, User.class);
		return users;

	}

	@Override
	public List<User> getUserByFirstName(String firstName) {

		Query query = new Query();
		query.addCriteria(Criteria.where("firstName").regex(firstName));
		List<User> users = mongoTemplate.find(query, User.class);
		return users;


	}

}