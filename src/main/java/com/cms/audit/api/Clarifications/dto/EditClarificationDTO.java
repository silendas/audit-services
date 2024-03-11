package com.cms.audit.api.Clarifications.dto;

import java.util.Date;

import com.cms.audit.api.Clarifications.models.EPriority;
import com.cms.audit.api.Clarifications.models.EStatus;
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
public class EditClarificationDTO {
    private String nominal_loss;
    private Date evaluation_limitation;
    private String location;
    private String auditee;
    private String auditee_leader;
    private String description;
    private String recomendation;
    private EPriority priority;
    private String evaluation;
    private Long is_follow_up;
}
