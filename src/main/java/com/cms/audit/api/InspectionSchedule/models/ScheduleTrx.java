package com.cms.audit.api.InspectionSchedule.models;

import java.util.Date;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.User.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "inspection_schedule_trx")
public class ScheduleTrx {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date",columnDefinition = "DATE")
    private Date start_date;

    @Column(name = "end_date",columnDefinition = "DATE")
    private Date end_date;

    @Column(name = "start_date_realization", columnDefinition = "DATE")
    private Date start_date_realization;

    @Column(name = "end_date_realization", columnDefinition = "DATE")
    private Date end_date_realization;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ECategory category;

    @JsonIgnore
    @Column(name = "is_delete", length = 2, nullable = true)
    private Integer is_delete;

    @Column(name = "created_by")
    private Long createdBy;   

    @Column(name = "created_at")
    private Date created_at;

}
