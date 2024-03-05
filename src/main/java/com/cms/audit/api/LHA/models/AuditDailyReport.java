package com.cms.audit.api.LHA.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_dailt_report")
public class AuditDailyReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_research",length = 2)
    private Long isResearch;

    @Column(name = "is_delete", length = 2)
    private Long isDelete;

    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updateAt;

}
