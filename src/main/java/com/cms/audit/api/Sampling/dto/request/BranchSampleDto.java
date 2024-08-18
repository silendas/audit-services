package com.cms.audit.api.Sampling.dto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BranchSampleDto {
    
    private Long branch;

    private Date created_sampling;

    private Long current_rmk;

    private Long current_branch;

    private Long pending_value;

    private Long pending_unit;
    
}
