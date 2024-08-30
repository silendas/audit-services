package com.cms.audit.api.Sampling.model;

import java.util.Date;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;

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
@Table(name = "random_table")
@Entity
public class RandomTable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "value")
    private Long value;

    @Column(name = "margin_error")
    private Double margin_error;

    @Column(name = "slovin_result")
    private Double slovin_result;

    @Column(name = "random_sampling", columnDefinition = "TEXT")
    private String random_sampling;

    @Column(name = "is_delete")
    private Integer is_delete;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_by")
    private Long created_by;

}
