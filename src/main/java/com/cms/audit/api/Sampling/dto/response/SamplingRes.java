package com.cms.audit.api.Sampling.dto.response;

import com.cms.audit.api.Sampling.dto.request.CollectorSamplingDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SamplingRes {

    private BranchSamplingRes branch;

    private CollectorSamplingDto collectors;

    private RealizeRes sampling;
    
}
