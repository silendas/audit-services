package com.cms.audit.api.FollowUp.dto;

import com.cms.audit.api.FollowUp.models.EStatusFollowup;
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
    private String description;
    private String file_name;
    private String file_path;
    private EStatusFollowup status;
    private Long is_penalty;
}
