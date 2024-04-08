package com.cms.audit.api.Report.dto;

import java.util.*;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;

import lombok.Data;

@Data
public class LhaReportDTO {
    private String areaName;
    private String date;
    private List<ListLhaDTO> lhaDetail;
}
