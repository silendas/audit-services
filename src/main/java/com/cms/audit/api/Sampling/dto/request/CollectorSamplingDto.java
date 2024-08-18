package com.cms.audit.api.Sampling.dto.request;

import lombok.Data;

@Data
public class CollectorSamplingDto {
    
    private String collectors;

    private Long rmk_value;

    private Long rmk_unit;

    private Long pending_value;

    private Long pending_unit;

    private Long target_value;

    private Long target_unit;

    private Long unit_sampling_value;

    private Long unit_sampling_unit;

}
