package com.cms.audit.api.Management.CaseCategory.dto;

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
public class CaseCategoryDTO {
    private String name;
    private Long cases_id;
    private Integer is_delete;
}
