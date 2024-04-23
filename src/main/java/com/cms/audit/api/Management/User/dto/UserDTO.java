package com.cms.audit.api.Management.User.dto;

import java.util.List;

import javax.validation.constraints.Size;

import lombok.Data;

//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class UserDTO {
    private String email;
    private String nip;
    private String username;
    @Size(min = 8, message = "password min 8 char")
    private String password;
    private String fullname;
    private String initial_name;
    private Long role_id;
    private Long level_id;
    private Long main_id = null;
    private Long region_id = null;
    private List<Long> area_id = null;
    private List<Long> branch_id = null;
    
}
