package com.cms.audit.api.AuditDailyReport.models;

import java.util.Date;

import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.User.models.User;
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
@Table(name = "audit_daily_report")
public class AuditDailyReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_research",length = 2)
    private Integer is_research;

    @Column(name = "is_delete", length = 2)
    private Integer is_delete;

    @Column(name = "created_by")
    private Long created_by;

    @Column(name = "updated_by")
    private Long updated_by;
    
    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date update_at;

}
