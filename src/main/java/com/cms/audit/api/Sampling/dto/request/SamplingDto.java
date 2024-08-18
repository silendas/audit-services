package com.cms.audit.api.Sampling.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SamplingDto {

    private BanchSamplingDto branch;

    private CollectorSamplingDto collectors;

    private List<RealizeDto> sampling;
    
}
