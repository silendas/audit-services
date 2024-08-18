package com.cms.audit.api.Sampling.dto.request;

import lombok.Data;

@Data
public class BanchSampling {

    private Long branch_id;

    private Long current;

    private Long target;
    
}
