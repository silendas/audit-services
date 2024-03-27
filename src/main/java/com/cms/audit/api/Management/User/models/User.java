package com.cms.audit.api.Management.User.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Role.models.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "main_id", nullable = true)
    private Main main;

    // @ManyToOne
    // @JoinColumn(name = "region_id", nullable = true)
    // private Region region;

    @Column(name = "region_id",nullable = true)
    private List<Long> regionId;

    // @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinTable(name = "user_region",joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "region_id",referencedColumnName = "id"))
    // private List<Region> region = new ArrayList<>();

    // @ManyToOne
    // @JoinColumn(name = "area_id", nullable = true)
    // private Area area;

    // @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinTable(name = "user_area",joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "area_id",referencedColumnName = "id"))
    // private List<Area> area = new ArrayList<>();

    @Column(name = "area_id",nullable = true)
    private List<Long> areaId;

    // @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Branch> branch;

    // @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinTable(name = "user_branch",joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "branch_id",referencedColumnName = "id"))
    // private List<Branch> branch = new ArrayList<>();

    @Column(name = "branch_id",nullable = true)
    private List<Long> branchId;

    // @ManyToOne
    // @JoinColumn(name = "branch_id", nullable = true)
    // private Branch branch;

    @Column(name = "email")
    private String email;

    @Column(name = "nip")
    private String nip;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "initial_name")
    private String initial_name;

    @Column(name = "is_active", length = 2, nullable = true)
    private Integer is_active;

    @Column(name = "is_delete", length = 2, nullable = true)
    private Integer is_delete;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}
