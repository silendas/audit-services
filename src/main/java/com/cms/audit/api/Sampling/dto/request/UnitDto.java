package com.cms.audit.api.Sampling.dto.request;

import com.cms.audit.api.Sampling.enumerate.EName;

import lombok.Data;

@Data
public class UnitDto {
    
    private EName name;

    private Long value;

    private Long unit;

}
