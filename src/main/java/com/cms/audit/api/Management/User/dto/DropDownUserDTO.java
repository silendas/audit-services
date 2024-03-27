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
public class DropDownUserDTO {
    private Long id;
    private String fullname;
    private String initial_name;
}
