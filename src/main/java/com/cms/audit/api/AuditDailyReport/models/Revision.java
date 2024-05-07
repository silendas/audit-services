package com.cms.audit.api.AuditDailyReport.models;

import jakarta.persistence.Entity;

import java.util.*;


import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
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
@Table(name = "revision")
public class Revision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "audit_daily_report_detail_id")
    private AuditDailyReportDetail auditDailyReportDetail;

    @ManyToOne
    @JoinColumn(name = "cases_id")
    private Case cases;

    @ManyToOne
    @JoinColumn(name = "case_category_id")
    private CaseCategory caseCategory;

    @Column(name = "revision_number")
    private Long revisionNumber;

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

    @JsonIgnore
    @Column(name = "is_delete", length = 2, nullable = true)
    private Integer is_delete;

    @Column(name = "created_by")
    private Long created_by;

    @Column(name = "created_at")
    private Date created_at;
}
