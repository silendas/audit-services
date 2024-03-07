package com.cms.audit.api.AuditDailyReport.dto;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
public class AuditDailyReportDetailDTO {
    private Long auditDailyReport_Id;
    private String description;
    private String suggestion;
    private String temporary_recommendations;
    private String permanent_recommendations;
    private String created_by;
}
