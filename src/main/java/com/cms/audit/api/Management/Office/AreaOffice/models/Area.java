package com.cms.audit.api.Management.Office.AreaOffice.models;

import java.util.Date;

import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.User.models.User;

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
@Table(name = "area_office")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date created_at;

    private Date updated_at;

    @Column(length = 2, nullable = true)
    private Integer is_delete;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    // @OneToOne(mappedBy = "area")
    // private User user;
}
