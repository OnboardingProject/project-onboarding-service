package com.account.onboarding.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.account.onboarding.model.DatabaseSequence;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Uthara P Unni
 * @description : Service class for auto sequence generation.
 * @date : 08 August 2022
 */

@Slf4j
@Service
public class SequenceGeneratorService {
	@Autowired
	private MongoOperations mongoOperations;

	public int generateSequence(String seqName) {
		log.info("Generating the next id sequence");

		DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
				new Update().inc("seq", 1), options().returnNew(true).upsert(true), DatabaseSequence.class);

		log.info("Returning generated sequence");
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}
}
