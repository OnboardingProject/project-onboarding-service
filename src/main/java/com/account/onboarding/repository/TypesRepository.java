package com.account.onboarding.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.account.onboarding.model.Types;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Repository class for accessing Types entity.
 * @date : 08 August 2022
 */

@Repository
public interface TypesRepository extends MongoRepository<Types, String> {

}
