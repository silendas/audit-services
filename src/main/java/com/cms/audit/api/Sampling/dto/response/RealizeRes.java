package com.cms.audit.api.Sampling.dto.response;

import java.util.List;

import com.cms.audit.api.Sampling.model.RealizeSampling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RealizeRes {
    
    private List<RealizeSampling> external;
    private List<RealizeSampling> internal;

}
