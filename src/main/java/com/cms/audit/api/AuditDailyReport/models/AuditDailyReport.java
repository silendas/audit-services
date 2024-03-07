package com.cms.audit.api.AuditDailyReport.models;

import java.util.Date;

import com.cms.audit.api.InspectionSchedule.models.Schedule;

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
@Table(name = "audit_dailt_report")
public class AuditDailyReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Column(name = "is_research",length = 2)
    private Long isResearch;

    @Column(name = "is_delete", length = 2)
    private Integer isDelete;

    @Column(name = "created_by")
    private String created_by;
    
    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date update_at;

}
