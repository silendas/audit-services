package com.cms.audit.api.AuditDailyReport.dto.response;


import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevisionResponse {
    Long id;
    Case cases;
    Long revisionNumber;
    CaseCategory caseCategory;
    String description;
    String suggestion;
    String temporary_recommendations;
    String permanent_recommendations;
    Integer is_research;
    Object created_by;
}
