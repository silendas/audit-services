package com.cms.audit.api.Flag.model;

import java.util.Date;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.Clarifications.models.Clarification;

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
@Table(name = "flag")
public class Flag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "audit_daily_report_detail_id")
    private AuditDailyReportDetail auditDailyReportDetail;

    @ManyToOne
    @JoinColumn(name = "clarification_id")
    private Clarification clarification;

    @Column(name = "created_at")
    private Date createdAt;

}
