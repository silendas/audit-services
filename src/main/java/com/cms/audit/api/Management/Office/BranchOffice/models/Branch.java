package com.cms.audit.api.Management.Office.BranchOffice.models;

import java.util.Date;

import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
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
@Table(name = "branch_office")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date created_at;

    private Date updated_at;

    @Column(length = 2, nullable = true)
    private Integer is_delete;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    // @OneToOne(mappedBy = "branch")
    // private User user;

}
