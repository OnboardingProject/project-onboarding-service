package com.account.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import com.account.onboarding.model.DatabaseSequence;
import com.account.onboarding.service.impl.SequenceGeneratorService;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : jUnit testcases for Sequence Generator service.
 * @date : 07 September 2022
 */

@ExtendWith(MockitoExtension.class)
public class SequenceGeneratorServiceTests {

	@Mock
	private MongoOperations mongoOperations;

	@InjectMocks
	private SequenceGeneratorService sequenceGeneratorService;
	
	private String seqName = "TASK_SEQUENCE";
	
	@DisplayName("Junit test case for generateSequence success scenario")
	@Test
	public void generateSequenceSuccessTest() {
		DatabaseSequence counter = new DatabaseSequence(seqName, 4);
		
		when(mongoOperations.findAndModify(any(Query.class), any(UpdateDefinition.class), any(FindAndModifyOptions.class), any())).thenReturn(counter);
		
		int seqValue = sequenceGeneratorService.generateSequence(seqName);
		assertEquals(seqValue, 4);
	}
	
	@DisplayName("Junit test case for generateSequence success scenario if counter is null")
	@Test
	public void generateSequenceSuccessTestIfCounterIsNull() {
		when(mongoOperations.findAndModify(any(Query.class), any(UpdateDefinition.class), any(FindAndModifyOptions.class), any())).thenReturn(null);

		int seqValue = sequenceGeneratorService.generateSequence(seqName);
		assertEquals(seqValue, 1);
	}
}
