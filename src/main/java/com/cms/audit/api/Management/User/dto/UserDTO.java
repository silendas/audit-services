package com.cms.audit.api.Management.User.dto;

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
public class UserDTO {
    private Long id;
    private String email;
    private String nip;
    private String username;
    private String password;
    private String full_name;
    private String initial_name;
    private Integer is_active;
    private Integer is_delete;
    private Long role_id;
    private Long level_id;
    private Long main_id;
    private Long region_id;
    private Long area_id;
    private Long branch_id;
}
