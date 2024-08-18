package com.cms.audit.api.Sampling.model;

import java.util.Date;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;

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
@Table(name = "sampling")
public class Sampling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "current")
    private Long current;

    @Column(name = "target")
    private Long target;

    @Column(name = "collectors", length = 500)
    private String collectors;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "is_deleted")
    private Integer is_deleted;

}
