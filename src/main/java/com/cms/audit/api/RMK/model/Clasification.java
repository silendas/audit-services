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
@Table(name = "clasification")
public class Clasification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ClasificationCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private ClasificationPriority priority;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "is_delete")
    private Integer is_delete;
    
}
