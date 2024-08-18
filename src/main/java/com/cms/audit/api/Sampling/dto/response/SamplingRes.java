package com.cms.audit.api.Sampling.dto.response;

import java.util.List;

import com.cms.audit.api.Sampling.dto.request.BranchSamplingDto;
import com.cms.audit.api.Sampling.dto.request.CollectorSamplingDto;
import com.cms.audit.api.Sampling.dto.request.RealizeDto;

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

    private BranchSamplingDto branch;

    private CollectorSamplingDto collectors;

    private RealizeRes sampling;
    
}
