package com.cms.audit.api.AuditDailyReport.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LhaReportDTO {
    private String area_name;
    private String date;
    private List<ListDetailDTO> lha_detail;
}
