package com.cms.audit.api.InspectionSchedule.dto.response;

public interface ScheduleInterface {
    Long getId();
    Long getUser_Id();
    Long getBranch_Id();
    String getDescription();
    String getCategory();
    String getStatus();
    String getStart_Date();
    String getEnd_Date();
    String getStart_Date_Realization();
    String getEnd_Date_Realization();
}
