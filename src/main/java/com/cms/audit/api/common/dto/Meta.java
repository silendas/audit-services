package com.cms.audit.api.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Meta {

    @JsonProperty("status_code")
    private int statusCode;
    private String message;
    private String body;


}
