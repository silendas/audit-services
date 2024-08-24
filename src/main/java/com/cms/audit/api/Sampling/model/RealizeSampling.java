package com.cms.audit.api.Sampling.model;

import com.cms.audit.api.RMK.model.Clasification;
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
@Table(name = "realize_sampling")
public class RealizeSampling {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sampling_id")
    private BranchSampling sampling;

    @ManyToOne
    @JoinColumn(name = "clasification_id")
    private Clasification clasification;

    @Column(name = "value")
    private Long value;

    @Column(name = "unit")
    private Long unit;
    
}
