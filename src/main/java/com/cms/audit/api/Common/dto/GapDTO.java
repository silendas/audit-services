package com.cms.audit.api.Common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GapDTO {
    private Long day;
    private Long hour;
    private Long minute;
    private Long second;
}
