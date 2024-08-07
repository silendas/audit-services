package com.cms.audit.api.RMK.model;

import java.util.Date;

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
@Table(name = "rmk_realize")
public class RmkRealize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rmk_plan_id")
    private RmkPlan rmkPlan;

    @ManyToOne
    @JoinColumn(name = "clasification_id")
    private Clasification clasification;

    @Column(name = "value")
    private Long value;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "is_delete")
    private Integer is_delete;
    
}
