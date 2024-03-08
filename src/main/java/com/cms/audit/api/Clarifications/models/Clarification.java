package com.cms.audit.api.Clarifications.models;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.ReportType.models.ReportType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clarification")
public class Clarification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "audit_daily_report_id")
    private AuditDailyReport auditDailyReport;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case cases;

    @ManyToOne
    @JoinColumn(name = "case_category_id")
    private CaseCategory caseCategory;

    @ManyToOne
    @JoinColumn(name = "report_type_id")
    private ReportType reportType;

    @Column(name = "report_number")
    private Long report_number;

    @Column(name = "code")
    private String code;

    @Column(name = "nominal_loss")
    private String nominal_loss;

    @Column(name = "evaluation_limitation")
    private String evaluation_limitation;

    @Column(name = "supervisor")
    private String supervisor;

    @Column(name = "auditee_leader")
    private String auditee_leader;

    @Column(name = "file_name")
    private String file_name;

    @Column(name = "description")
    private String description;
    
    @Column(name = "reason")
    private String reason;

    @Column(name = "priority")
    private String priority;

    @Column(name = "evaluation")
    private String evaluation;

    @Column(name = "is_follow_up")
    private String is_follow_up;

    @Column(name = "status")
    private String status;

    @Column(name = "craeted_at")
    private String craeted_at;

    @Column(name = "updated_at")
    private String updated_at;

}
