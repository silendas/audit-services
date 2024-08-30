package com.cms.audit.api.Sampling.dto.request;

import lombok.Data;

@Data
public class RandomTableDto {

    private Long value;

    private Long branch;

    private Double margin_error;

    private Double slovin_result;

    private String random_sampling;
    
}
