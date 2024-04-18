package com.cms.audit.api.NewsInspection.models;

import java.util.Date;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.User.models.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news_inspection")
public class NewsInspection {
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
    @JoinColumn(name = "clarification_id")
    private Clarification clarification;

    @ManyToOne
    @JoinColumn(name = "report_type_id")
    private ReportType reportType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String file_path;

    @Column(name = "report_number")
    private Long report_number;

    @Column(name = "code")
    private String code;
    
    @Column(name = "created_at", columnDefinition = "DATE")
    private Date created_at;

    @Column(name = "updated_at", columnDefinition = "DATE")
    private Date updated_at;
}
