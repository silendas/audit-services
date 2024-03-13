package com.cms.audit.api.AuditWorkingPaper.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuditWorkingPaperDTO {

    private Long user_id;
    private Long branch_id;
    private Long area_id;
    private Long schedule_id;
    private Date start_date;
    private Date end_date;
    private String file_name;
}
