package com.account.onboarding.constants;

public interface ProjectManagementConstant {
	String PJT_CONST_START = "In controller create method started";
	String PJT_CONST_END = "In controller create method created project";
	String PJT_CONST_SERVICE = "In Service create method started";
	String PJT_CONST_SERVICE_COMPLETE = "In Service create method completed";
	String USER_CONST_START = "In controller get all resource method started";
	String USER_CONST_END = "In controller got all resources";
	String USER_CONST_SERVICE = "In Service get all user by role started";
	String USER_CONST_SERVICE_COMPLETE = "In Service get all user by role completed";
	String USER_CONST_SERVICE_EXCN_LOG = "No Users found in the DB ";
	String USER_CONST_SERVICE_EXCN = "No Users found";
	String PJT_CONST_SERVICE_EXCN_LOG = "A project already exists with name {}";
	String PJT_CONST_SERVICE_EXCN = "project name already existing";
}