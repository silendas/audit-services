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
public class ScheduleFilterDTO {
    private Date start_date;
    private Date end_date;
}
