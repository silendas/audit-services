package com.cms.audit.api.Management.ReportType.dto;

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
public class ReportTypeDTO {
    private String name;
    private String code;
}
