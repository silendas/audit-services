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
@Table(name = "rmk_plan")
public class RmkPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "collector", length = 255)
    private String collector;

    @Column(name = "rmk")
    private Long rmk;

    @Column(name = "pending")
    private Long pending;

    @Column(name = "slovin")
    private Double slovin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RmkStatus status;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "is_delete")
    private Integer is_delete;
    
}
