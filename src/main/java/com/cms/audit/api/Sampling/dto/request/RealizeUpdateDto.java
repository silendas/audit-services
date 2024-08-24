package com.cms.audit.api.Sampling.dto.request;

import lombok.Data;

@Data
public class RealizeUpdateDto {
    
    private Long id;

    private Long clasification_id;

    private Long value;

    private Long unit;
}
