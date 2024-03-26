package com.cms.audit.api.AuditDailyReport.dto;

import java.util.*;

import lombok.*;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
public class AuditDailyReportDTO {
    private Long schedule_id;
    private Long branch_id;
    private List<AuditDailyReportDetailRequest> details;
}
