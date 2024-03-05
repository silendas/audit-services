package com.cms.audit.api.LHA.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_daily_report_detail")
public class AuditDailyReportDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "suggestion")
    private String suggestion;
    
    @Column(name = "temporary_recommendations")
    private String temporary_recommendations;

    @Column(name = "permanent_recommendations")
    private String permanent_recommendations;

    @Column(name = "is_delete")
    private Long is_delete;

    @Column(name = "created_by")
    private Long created_by;

    @Column(name = "created_at")
    private Long created_at;
    
    @Column(name = "update_at")
    private Long update_at;

}
