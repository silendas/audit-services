package com.cms.audit.api.AuditDailyReport.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailResponse {
    Long id;
    String cases;
    String caseCategory;
    String description;
    String suggestion;
    String temporary_recommendations;
    String permanent_recommendations;
    Integer is_research;
    Integer status_flow;
    Integer status_parsing;
}
