package com.cms.audit.api.Clarifications.models;

import java.util.Date;

import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.ReportType.models.ReportType;
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
@Table(name = "clarification")
public class Clarification {
    
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
    @JoinColumn(name = "case_id")
    private Case cases;

    @ManyToOne
    @JoinColumn(name = "case_category_id")
    private CaseCategory caseCategory;

    @ManyToOne
    @JoinColumn(name = "report_type_id")
    private ReportType reportType;

    @Column(name = "report_number")
    private Long report_number;

    @Column(name = "code")
    private String code;

    @Column(name = "nominal_loss")
    private Long nominal_loss;

    @Column(name = "evaluation_limitation", columnDefinition = "DATE")
    private Date evaluation_limitation;

    @Column(name = "location",columnDefinition = "TEXT")
    private String location;

    @Column(name = "auditee")
    private String auditee;

    @Column(name = "auditee_leader")
    private String auditee_leader;

    @Column(name = "file_name")
    private String filename;

    @Column(name = "file_path")
    private String file_path;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "recomendation",columnDefinition = "TEXT")
    private String recomendation;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private EPriority priority;

    @Column(name = "evaluation")
    private Long evaluation;

    @Column(name = "is_follow_up", length = 2)
    private Long is_follow_up;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EStatusClarification status;
    
    @Column(name = "created_by")
    private Long created_by;

    @Column(name = "created_at", columnDefinition = "DATE")
    private Date created_at;

    @Column(name = "updated_at", columnDefinition = "DATE")
    private Date updated_at;

}
