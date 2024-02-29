package com.cms.audit.api.Authentication.dto.response;

public interface AuthInterface {
    Long getId();
    String getNip();
    String getFull_Name();
    String getInitial_Name();
    String getEmail();
    String getUsername();
    String getPassword();
    Long getMain_Id();
    Long getRegion_Id();
    Long getArea_Id();
    Long getBranch_Id();
    Long getLevel_Id();
    Long getRole_Id();
}
