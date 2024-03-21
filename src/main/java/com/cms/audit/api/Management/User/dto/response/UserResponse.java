package com.cms.audit.api.Management.User.dto.response;

import java.util.*;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Role.models.Role;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private Role role;
    private Level level;
    private Main main;
    private List<Region> region;
    private List<Area> area;
    private List<Branch> branch;
    private String email;
    private String nip;
    private String username;
    private String password;
    private String fullname;
    private String initial_name;
    private Integer is_active;
    private Integer is_delete;
    private Date created_at;
    private Date updated_at;
}
