package com.project.onboarding.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.model.Types;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : jUnit testcases for project onboarding util functions.
 * @date : 12 August 2022
 */

@ExtendWith(MockitoExtension.class)
public class ProjectOnboardingUtilTests {
	@Mock
    private MongoTemplate mongoTemplate;
	
	@InjectMocks
    private ProjectOnboardingUtil projectOnboardingUtil;
	
	private List<Types> types = new ArrayList<Types>();
	
	private Types type;
	
	@BeforeEach
	public void setup() {
		type = new Types("ROLE", 3, "Project Owner", "EDIT");
		
		ProjectOnboardingUtil.roleIdOfProjectOwner = 0;
	}
	
	@DisplayName("JUnit test for getRoleIdOfProjectOwner success scenario when database is not giving any result for the query")
    @Test
    public void givenNothing_whenGetRoleIdOfProjectOwner_AndQueryHasNoResults_thenReturnRoleIdOfProjectOwner() throws Exception{
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("typeName").is(ProjectOnboardingConstant.ROLE),
				Criteria.where("typeDesc").is(ProjectOnboardingConstant.PROJECT_OWNER)));
		query.fields().include("typeId");
		
    	when(mongoTemplate.find(query, Types.class)).thenReturn(types);

        int roleIdOfProjectOwner = projectOnboardingUtil.getRoleIdOfProjectOwner();
        assertNotNull(roleIdOfProjectOwner);
        assertEquals(0, roleIdOfProjectOwner);
        verify(mongoTemplate, times(1)).find(query, Types.class);
    }
	
	@DisplayName("JUnit test for getRoleIdOfProjectOwner success scenario")
    @Test
    public void givenNothing_whenGetRoleIdOfProjectOwner_thenReturnRoleIdOfProjectOwner() throws Exception{
		types.add(type);
		
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("typeName").is(ProjectOnboardingConstant.ROLE),
				Criteria.where("typeDesc").is(ProjectOnboardingConstant.PROJECT_OWNER)));
		query.fields().include("typeId");
		
    	when(mongoTemplate.find(query, Types.class)).thenReturn(types);
    	/* Calling this method for the first time */
    	int roleIdOfProjectOwner = projectOnboardingUtil.getRoleIdOfProjectOwner();
        assertNotNull(roleIdOfProjectOwner);
        assertEquals(types.get(0).getTypeId(), roleIdOfProjectOwner);
        
        /* Checking for when this method is called second time */
        roleIdOfProjectOwner = projectOnboardingUtil.getRoleIdOfProjectOwner();
        assertNotNull(roleIdOfProjectOwner);
        assertEquals(types.get(0).getTypeId(), roleIdOfProjectOwner);
        verify(mongoTemplate, times(1)).find(query, Types.class);
    }
	
	@DisplayName("JUnit test for createQuery success scenario")
    @Test
    public void givenCriteria_whenCreateQuery_thenReturnQueryWithCriteria() throws Exception{
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("typeName").is(ProjectOnboardingConstant.ROLE),
				Criteria.where("typeDesc").is(ProjectOnboardingConstant.PROJECT_OWNER)));
		
		Criteria criteria = new Criteria().andOperator(Criteria.where("typeName").is(ProjectOnboardingConstant.ROLE),
				Criteria.where("typeDesc").is(ProjectOnboardingConstant.PROJECT_OWNER));
		
		Query queryFromMethod = projectOnboardingUtil.createQuery(criteria);

        assertNotNull(queryFromMethod);
        assertEquals(queryFromMethod, query);
    }
}
