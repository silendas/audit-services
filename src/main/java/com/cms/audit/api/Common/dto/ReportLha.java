package com.cms.audit.api.Common.dto;

import java.util.*;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDTO;

import lombok.Data;

@Data
public class ReportLha {
    private String date;
    private String regional;
    //private List<AuditDailyReportDTO> detail;
}
