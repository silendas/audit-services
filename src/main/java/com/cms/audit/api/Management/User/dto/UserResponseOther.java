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
public class UserResponseOther {
    private Long id;
    private String email;
    private String nip;
    private String fullname;
    private String initial_name;
}
