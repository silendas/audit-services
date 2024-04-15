package com.cms.audit.api.Report.dto;

import java.util.*;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;

import lombok.Data;

@Data
public class ListLhaDTO {
    private String fullname;
    private String branch;
    private List<AuditDailyReportDetail> details;
}
