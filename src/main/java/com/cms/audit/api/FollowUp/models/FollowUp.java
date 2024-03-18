package com.cms.audit.api.FollowUp.models;

import java.util.Date;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Management.ReportType.models.ReportType;

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

    @Column(name = "description")
    private String description;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "status")
    private EStatusFollowup status;

    @Column(name = "is_penalty")
    private Long isPenalty;

    @Column(name = "created_at")
    private Date createdAt;
    
}
