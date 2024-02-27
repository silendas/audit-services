package com.cms.audit.api.Management.Role.dto;

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
public class RoleDTO {
    private Long id;
    private String name;
}
