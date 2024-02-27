package com.cms.audit.api.Management.PermanentRecomendation.dto;

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
public class PermanentRecomendationDTO {
    private Long id;
    private String name;
    private Integer is_delete;
}
