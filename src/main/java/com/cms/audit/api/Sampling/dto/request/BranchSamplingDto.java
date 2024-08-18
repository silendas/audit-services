package com.cms.audit.api.Sampling.dto.request;

import lombok.Data;

@Data
public class BranchSamplingDto {
    
    private Long branch;

    private Long current_rmk;

    private Long current_branch;

    private Long pending_value;

    private Long pending_unit;
    
}
