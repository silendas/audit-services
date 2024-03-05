package com.cms.audit.api.Management.Case.dto;

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
public class CaseDTO {
    private String name;
    private String code;
    private Integer is_delete;
}
