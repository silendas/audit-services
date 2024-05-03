package com.cms.audit.api.AuditDailyReport.dto;

import lombok.*;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
public class EditAuditDailyReportDetailDTO {
    private Long case_id;
    private Long case_category_id;
    private String description;
    private String suggestion;
    private String temporary_recommendations;
    private String permanent_recommendations;
    private Integer is_research;
    private Integer is_revision;
    private Integer status_parsing;
    private Integer status_flow;
}
