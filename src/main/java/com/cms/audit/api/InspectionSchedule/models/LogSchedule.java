package com.cms.audit.api.InspectionSchedule.models;

import java.util.Date;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.User.models.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "log_schedule")
public class LogSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inspection_schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ECategory category;

    @Column(name = "action")
    private String action;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Date created_at;

}
