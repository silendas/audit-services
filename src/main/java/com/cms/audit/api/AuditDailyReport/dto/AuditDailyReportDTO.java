package com.cms.audit.api.AuditDailyReport.dto;

import lombok.*;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
public class AuditDailyReportDTO {
    private Long schedule_id;
    private Long is_research;
    private String created_by;
    private String description;
    private String suggestion;
    private String temporary_recommendations;
    private String permanent_recommendations;
}
