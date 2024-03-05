package com.cms.audit.api.InspectionSchedule.dto;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Setter
@Getter
@NoArgsConstructor
public class ScheduleDTO {
    private Integer user_id;
    private Integer branch_id;
    private Date start_date;
    private Date end_date;
    private Date start_date_realization;
    private Date end_date_realization;
    private String description;
    private String createdBy;
    private String updatedBy;
}
