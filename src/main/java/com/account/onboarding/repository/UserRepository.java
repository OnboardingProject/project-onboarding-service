package com.account.onboarding.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.account.onboarding.model.User;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Repository class for accessing User entity.
 * @date : 08 August 2022
 */

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	User findByUserId(String id);

	@Query(value = "{$and : [ { userId : ?0 },{roleId : ?1}]}", fields = "{ firstName : 1}")
	String findByRoleAndUserId(String userId, String roleId);

	List<User> findByRoleId(List<String> roleId);
}