package com.cms.audit.api.Sampling.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SamplingDto {

    private Long branch_id;

    private Long current;

    private Long target;

    private String collectors;

    private List<UnitDto> unit_sampling;

    private List<RealizeDto> realize_sampling;
    
}
