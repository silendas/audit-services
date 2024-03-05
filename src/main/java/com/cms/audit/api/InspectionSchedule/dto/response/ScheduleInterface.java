package com.cms.audit.api.InspectionSchedule.dto.response;

public interface ScheduleInterface {
    Long getId();
    // String getUser();
    // String getBranch();
    String getStart_Date();
    String getEnd_Date();
    String getStart_Date_Realization();
    String getEnd_Date_Realization();
    String getDescription();
    String getStatus();
    String getCategory();
}
