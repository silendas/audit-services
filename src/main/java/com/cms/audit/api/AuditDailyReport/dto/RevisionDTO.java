package com.cms.audit.api.AuditDailyReport.dto;

import lombok.*;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
public class RevisionDTO {
    private Long audit_daily_report_detail_id;
    private String description;
    private String suggestion;
    private String temporary_recommendations;
    private String permanent_recommendations;
}
