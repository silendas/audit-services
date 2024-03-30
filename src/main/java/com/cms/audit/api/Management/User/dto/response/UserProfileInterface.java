package com.cms.audit.api.Management.User.dto.response;

import java.util.*;

public interface UserProfileInterface {
    Long getId();
    Long getRole_id();
    Long getLevel_id();
    List<Long> getMain_id();
    List<Long> getRegion_id();
    List<Long> getArea_id();
    List<Long> getBranch_id();
    String getNip();
    String getFullname();
    String getInitial_Name();
    String getEmail();
    String getUsername();
    String getPassword();
    String getIs_Active();
    String getCreated_At();
    String getUpdated_At();
}
