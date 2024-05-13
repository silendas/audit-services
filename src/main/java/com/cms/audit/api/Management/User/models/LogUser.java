package com.cms.audit.api.Management.User.models;

import java.util.Date;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Role.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Entity
@Table(name = "log_users")
public class LogUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "main_id", nullable = true)
    private Main main;

    @Column(name = "region_id", nullable = true)
    private List<Long> regionId;

    @Column(name = "area_id", nullable = true)
    private List<Long> areaId;

    @Column(name = "branch_id", nullable = true)
    private List<Long> branchId;

    @Column(name = "email")
    private String email;

    @Column(name = "nip")
    private String nip;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "initial_name")
    private String initial_name;

    @Column(name = "is_active", length = 2, nullable = true)
    private Integer is_active;

    @JsonIgnore
    @Column(name = "is_delete", length = 2, nullable = true)
    private Integer is_delete;

    @JsonIgnore
    @Column(name = "created_by")
    private Long created_by;

    @Column(name = "created_at")
    private Date created_at;
}
