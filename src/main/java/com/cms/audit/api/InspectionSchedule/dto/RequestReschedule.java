package com.cms.audit.api.InspectionSchedule.dto;

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
public class RequestReschedule {
    private Long schedule_id;
    private String update_by;

}
