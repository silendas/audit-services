package com.cms.audit.api.Sampling.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class SamplingUpdateDto {
    
    private BranchSampleDto branch;

    private CollectorSamplingDto collectors;

    private List<RealizeUpdateDto> sampling;   
}
