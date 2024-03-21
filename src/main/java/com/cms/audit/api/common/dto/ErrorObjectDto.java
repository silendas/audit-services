package com.cms.audit.api.Common.dto;

import lombok.Data;

import java.util.Date;
@Data
public class ErrorObjectDto {
    private Integer statusCode;
    private String message;
    private Date timeStamp;
}
