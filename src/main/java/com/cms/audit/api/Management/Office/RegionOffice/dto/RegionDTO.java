package com.cms.audit.api.Management.Office.RegionOffice.dto;

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
public class RegionDTO {
    private Long id;
    private String name;
    private Long main_id;
}
