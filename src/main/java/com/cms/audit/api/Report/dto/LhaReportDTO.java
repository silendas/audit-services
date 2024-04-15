package com.cms.audit.api.Report.dto;

import java.util.*;

import lombok.Data;

@Data
public class LhaReportDTO {
    private String area_name;
    private String date;
    private List<ListLhaDTO> lha_detail;
}
