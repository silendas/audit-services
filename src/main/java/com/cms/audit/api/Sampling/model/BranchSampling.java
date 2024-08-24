package com.cms.audit.api.Sampling.model;

import java.util.Date;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "branch_sampling")
public class BranchSampling {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "current_rmk")
    private Long current_rmk;

    @Column(name = "current_branch")
    private Long current_branch;

    @Column(name = "pending_value")
    private Long pending_value;

    @Column(name = "pending_unit")
    private Long pending_unit;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "created_by")
    private Long created_by;

    @Column(name = "is_delete")
    private Integer is_delete;

}
