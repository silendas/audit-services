package com.cms.audit.api.RMK.dto;

import com.cms.audit.api.RMK.model.ClasificationCategory;
import com.cms.audit.api.RMK.model.ClasificationPriority;

import lombok.Data;

@Data
public class ClasificationDto {

    private String name;

    private ClasificationCategory category;

    private ClasificationPriority priority;
    
}
