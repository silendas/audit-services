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
public class ChangePasswordDTO {
    private String current_password;
    private String new_password;
}
