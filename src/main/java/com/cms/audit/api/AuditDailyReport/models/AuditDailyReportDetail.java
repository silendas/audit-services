package com.cms.audit.api.AuditDailyReport.models;

import java.util.Date;

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

    @ManyToOne
    @JoinColumn(name = "audit_daily_report_id")
    private AuditDailyReport auditDailyReport;

    @Column(name = "description")
    private String description;

    @Column(name = "suggestion")
    private String suggestion;
    
    @Column(name = "temporary_recommendations")
    private String temporary_recommendations;

    @Column(name = "permanent_recommendations")
    private String permanent_recommendations;

    @Column(name = "is_delete")
    private Integer is_delete;

    @Column(name = "created_by")
    private String created_by;

    @Column(name = "created_at")
    private Date created_at;
    
    @Column(name = "update_at")
    private Date update_at;


}
