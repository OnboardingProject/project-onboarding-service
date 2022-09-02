package com.project.onboarding.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.onboarding.model.Types;

/**
 * @author Athira Rajan
 * @description : Repository class for accessing Types entity.
 * @date : 08 August 2022
 */

@Repository
public interface TypesRepository extends MongoRepository<Types, String>{

}
