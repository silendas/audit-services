package com.cms.audit.api.AuditWorkingPaper.models;

import java.util.Date;

import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
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
@Table(name = "audit_working_paper")
public class AuditWorkingPaper {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Column(name = "start_date", columnDefinition = "DATE")
    private Date start_date;

    @Column(name = "end_date", columnDefinition = "DATE")
    private Date end_date;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String file_path;

    @Column(name = "is_delete", length = 2)
    private Integer isDelete;

    @Column(name = "created_by")
    private Long created_by;    

    @Column(name = "created_at", columnDefinition = "DATE")
    private Date created_at;

}
