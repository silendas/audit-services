package com.cms.audit.api.Common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetail {
    private String name;
    private String error;

    public ErrorDetail(String name, String error) {
        this.name = name;
        this.error = error;
    }
}
