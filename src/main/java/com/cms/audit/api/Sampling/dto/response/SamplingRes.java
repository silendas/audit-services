package com.cms.audit.api.Sampling.dto.response;

import java.util.List;

import com.cms.audit.api.Sampling.dto.request.RealizeDto;
import com.cms.audit.api.Sampling.model.RealizeSampling;
import com.cms.audit.api.Sampling.model.UnitSampling;

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

    private Long id;

    private String branch_name;

    private String region_name;

    private String collector;

    private Long current;

    private Long target;

    private List<UnitSampling> unit_sampling;

    private RealizeRes realize_sampling;
    
}
