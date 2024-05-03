package com.cms.audit.api.Management.User.dto;

import com.cms.audit.api.Management.Level.models.Level;

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
public class DropDownUserDTO {
    private Long id;
    private String fullname;
    private String initial_name;
    private Level level;
    private String region;
}
