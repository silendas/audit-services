package com.cms.audit.api.AuditDailyReport.dto;

import java.util.List;

import lombok.Data;

@Data
public class ListDetailDTO {
    private String fullname;
    private String branch;
    private List<Object> details;
}
