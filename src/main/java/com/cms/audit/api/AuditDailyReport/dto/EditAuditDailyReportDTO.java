package com.cms.audit.api.AuditDailyReport.dto;

import lombok.*;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
public class EditAuditDailyReportDTO {
    private Long schedule_id;
    private Long user_id;
    private Long branch_id;
    private Long is_research;
    private String update_by;
}
