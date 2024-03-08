package com.cms.audit.api.Clarifications.dto;

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
    private Long auditDailyReportId;
    private Long casesId;
    private Long caseCategoryId;
    private Long reportTypeId;
    private Long report_number;
    private String code;
    private String nominal_loss;
    private String evaluation_limitation;
    private String supervisor;
    private String auditee_leader;
    private String file_name;
    private String description;
    private String reason;
    private String priority;
    private String evaluation;
    private String is_follow_up;
    private String status;
}
