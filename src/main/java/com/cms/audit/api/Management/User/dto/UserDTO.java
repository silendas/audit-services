package com.cms.audit.api.Management.User.dto;

import java.util.*;

import org.springframework.validation.annotation.Validated;

import com.cms.audit.api.Management.Office.RegionOffice.models.Region;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
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
@Valid
@Validated
@NoArgsConstructor
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
    private Long main_id;
    @Nullable
    private List<Long> region_id;
    @Nullable
    private List<Long> area_id;
    @Nullable
    private List<Long> branch_id;
}
