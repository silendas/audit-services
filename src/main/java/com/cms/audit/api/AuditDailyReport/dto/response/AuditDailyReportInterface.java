package com.cms.audit.api.AuditDailyReport.dto.response;

public interface AuditDailyReportInterface {
     Long schedule_id();
     Long user_id();
     Long branch_id();
     Long case_id();
     Long case_category_id();
     Long is_research();
     String description();
     String suggestion();
     String temporary_recommendations();
     String permanent_recommendations();
     String create_by();
}
