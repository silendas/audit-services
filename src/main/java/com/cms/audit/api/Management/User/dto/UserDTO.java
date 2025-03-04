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
    private String fullname;
    private String initial_name;
    private String password;
    private Long level_id;
    private Long main_id = null;
    private List<Long> region_id = null;
    private List<Long> area_id = null;
    private List<Long> branch_id = null;
}
