package com.cms.audit.api.Sampling.model;

import jakarta.persistence.Column;

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
@Table(name = "collector_sampling")
public class CollectorSampling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_sampling_id")
    private BranchSampling branchSampling;
    
    @Column(name = "collectors", length = 500)
    private String collectors;

    @Column(name = "rmk_value")
    private Long rmk_value;

    @Column(name = "rmk_unit")
    private Long rmk_unit;

    @Column(name = "pending_value")
    private Long pending_value;

    @Column(name = "pending_unit")
    private Long pending_unit;

    @Column(name = "target_value")
    private Long target_value;

    @Column(name = "target_unit")
    private Long target_unit;

    @Column(name = "unit_sampling_value")
    private Long unit_sampling_value;

    @Column(name = "unit_sampling_unit")
    private Long unit_sampling_unit;
    
    @Column(name = "margin_error")
    private Double margin_error;
    
}
