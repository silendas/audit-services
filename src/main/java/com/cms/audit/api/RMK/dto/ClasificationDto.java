package com.cms.audit.api.RMK.dto;

import com.cms.audit.api.RMK.model.ClasificationCategory;

import lombok.Data;

@Data
public class ClasificationDto {

    private String name;

    private ClasificationCategory category;

}
