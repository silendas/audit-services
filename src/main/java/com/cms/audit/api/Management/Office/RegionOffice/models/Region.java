package com.cms.audit.api.Management.Office.RegionOffice.models;

import java.util.Date;

import com.cms.audit.api.Management.Office.MainOffice.models.Main;

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
@Table(name = "region_office")
public class Region {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date created_at;

    private Date updated_at;

    @Column(length = 2, nullable = true)
    private Integer is_delete;

    @ManyToOne
    @JoinColumn(name = "main_id")
    private Main main;

    // @OneToOne(mappedBy = "region")
    // private User user;

}
