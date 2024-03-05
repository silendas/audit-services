package com.cms.audit.api.InspectionSchedule.models;

import java.util.Date;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.User.models.User;

import jakarta.persistence.CascadeType;
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
@Table(name = "inspection_schedule")
public class Schedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id",length = 2)
    private Integer userId;

    @Column(name = "branch_id",length = 2)
    private Integer branchId;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date start_date;

    @Column(name = "end_date")
    private Date end_date;

    @Column(name = "start_date_realization")
    private Date start_date_realization;

    @Column(name = "end_date_realization")
    private Date end_date_realization;

    @Column(name = "status")
    private String status;

    @Column(name = "category")
    private String category;

    @Column(name = "is_delete",length = 2, nullable = true)
    private Integer is_delete;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_by")
    private String createdBy;   

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

}
