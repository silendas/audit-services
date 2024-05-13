package com.cms.audit.api.Clarifications.dto;

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
public class ClarificationDTO {
    private Long user_id;
    private Long case_id;
    private Long case_category_id;
    private Long report_type_id;
    private Long report_number;
    private String code;
    private Long nominal_loss;
    private Date evaluation_limitation;
    private String supervisor;
    private String auditee_leader;
    private String file_name;
    private String description;
    private String reason;
    private String priority;
    private String evaluation;
    private Long is_follow_up;
}
