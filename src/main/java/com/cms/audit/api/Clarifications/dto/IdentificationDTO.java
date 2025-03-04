package com.cms.audit.api.Clarifications.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentificationDTO {
    private Long clarification_id;
    private Long evaluation;
    private Long nominal_loss;
    private List<Long> recommendation;
    private Long is_followup;
}
