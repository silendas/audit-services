package com.cms.audit.api.InspectionSchedule.dto;

import java.util.Date;

import com.cms.audit.api.InspectionSchedule.models.EStatus;

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
public class RescheduleDTO {
    private Long schedule_id;
    private Long user_id;
    private Long branch_id;
    private Date start_date;
    private Date end_date;
    private String description;
    private String created_by;
}
