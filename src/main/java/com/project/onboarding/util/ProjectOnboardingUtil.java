package com.project.onboarding.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.project.onboarding.constants.ProjectOnboardingConstant;
import com.project.onboarding.model.Types;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Class contains util functions.
 * @date : 18 August 2022
 */
@Slf4j
@Component
public class ProjectOnboardingUtil {

	@Autowired
	MongoTemplate mongoTemplate;

	public static int roleIdOfProjectOwner = 0;

	/**
	 * @param
	 * @return roleId of project owner
	 * @description Getting roleid of project owner
	 */
	public int getRoleIdOfProjectOwner() {
		log.info("In method for getting role id of project owner");
		
		if (roleIdOfProjectOwner == 0) {
			log.info("Role id for project owner is finding for the first time");
			
			Criteria criteria = new Criteria().andOperator(Criteria.where("typeName").is(ProjectOnboardingConstant.ROLE),
					Criteria.where("typeDesc").is(ProjectOnboardingConstant.PROJECT_OWNER));
			Query query = new Query();
			query.addCriteria(criteria);
			query.fields().include("typeId");

			List<Types> roleType = mongoTemplate.find(query, Types.class);

			log.info("Role id for project owner is set to the variable");
			if(roleType.size() > 0)
				roleIdOfProjectOwner =  roleType.get(0).getTypeId();
		}
		
		log.info("Role id for project owner is returned from the method");
		return roleIdOfProjectOwner;
	}
}
