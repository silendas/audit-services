package com.cms.audit.api.Management.Office.AreaOffice.dto;

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
public class AreaDTO {
    private String name;
    private Long region_id;
}
