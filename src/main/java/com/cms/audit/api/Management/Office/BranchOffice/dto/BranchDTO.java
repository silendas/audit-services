package com.cms.audit.api.Management.Office.BranchOffice.dto;

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
public class BranchDTO {
    private Long id;
    private String name;
    private Long area_id;
}
