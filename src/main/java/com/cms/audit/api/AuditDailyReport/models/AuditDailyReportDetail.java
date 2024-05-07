package com.cms.audit.api.AuditDailyReport.models;

import java.util.Date;

import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "audit_daily_report_detail")
public class AuditDailyReportDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "audit_daily_report_id")
    private AuditDailyReport auditDailyReport;

    @ManyToOne
    @JoinColumn(name = "cases_id")
    private Case cases;

    @ManyToOne
    @JoinColumn(name = "case_category_id")
    private CaseCategory caseCategory;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "suggestion", columnDefinition = "TEXT")
    private String suggestion;
    
    @Column(name = "temporary_recommendations")
    private String temporary_recommendations;

    @Column(name = "permanent_recommendations")
    private String permanent_recommendations;

    @Column(name = "is_research")
    private Integer is_research;

    @Column(name = "status_flow")
    private Integer status_flow;

    @Column(name = "status_parsing")
    private Integer status_parsing;

    @Column(name = "is_revision")
    private Integer is_revision;

    @JsonIgnore
    @Column(name = "is_delete", length = 2, nullable = true)
    private Integer is_delete;

    @Column(name = "created_by")
    private Long created_by;

    @Column(name = "updated_by")
    private Long updated_by;

    @Column(name = "created_at")
    private Date created_at;
    
    @Column(name = "update_at")
    private Date update_at;


}
