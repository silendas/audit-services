package com.cms.audit.api.FollowUp.dto;

import java.util.List;

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
public class FollowUpDTO {
    private Long followup_id;
    private List<Long> penalty_id;
    private String auditee_name;
    private String auditee_position;
    private String auditee_nip;
    private String auditee_leader;
    private String auditee_leader2;
    private Long charging_costs;
    private String description;
}
