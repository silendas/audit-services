package com.cms.audit.api.FollowUp.models;

import java.util.Date;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Management.Penalty.models.Penalty;
import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.User.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "follow_up")
public class FollowUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clarification_id")
    private Clarification clarification;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "penalty_id")
    private Penalty penalty;

    @ManyToOne
    @JoinColumn(name = "report_type_id")
    private ReportType reportType;

    @Column(name = "report_number")
    private Long report_number;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "file_name")
    private String filename;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "status")
    private EStatusFollowup status;

    @Column(name = "is_penalty")
    private Long isPenalty;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "created_at")
    private Date createdAt;
    
}
