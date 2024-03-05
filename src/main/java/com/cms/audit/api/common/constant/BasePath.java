package com.cms.audit.api.Common.constant;

public interface BasePath {

    String BASE_API ="/api";
    
    String BASE_PATH_AUTH= BASE_API+"/auth";
    String BASE_PATH_USERS= BASE_API+"/users";
    String BASE_PATH_LEVEL= BASE_API+"/level";
    String BASE_PATH_ROLE= BASE_API+"/role";

    String BASE_PATH_MAIN_SCHEDULE= BASE_API+"/main-schedule";
    String BASE_PATH_SPECIAL_SCHEDULE= BASE_API+"/special-schedule";

    String BASE_PATH_LHA= BASE_API+"/audit_daily_report";

    String BASE_PATH_MAIN_OFFICE= BASE_API+"/main";
    String BASE_PATH_REGION_OFFICE= BASE_API+"/region";
    String BASE_PATH_AREA_OFFICE= BASE_API+"/area";
    String BASE_PATH_BRANCH_OFFICE= BASE_API+"/branch";

    String BASE_PATH_CASE= BASE_API+"/case";
    String BASE_PATH_CASE_CATEGORY= BASE_API+"/case_category";
    String BASE_PATH_PENALTY= BASE_API+"/penalty";
}
