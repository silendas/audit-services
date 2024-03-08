package com.cms.audit.api.AuditDailyReport.dto;

import lombok.*;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
public class AuditDailyReportDTO {
    private Long schedule_id;
    private Long user_id;
    private Long branch_id;
    private Long case_id;
    private Long case_category_id;
    private Long is_research;
    private String description;
    private String suggestion;
    private String temporary_recommendations;
    private String permanent_recommendations;
    private String create_by;
}
