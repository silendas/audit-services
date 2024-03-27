package com.cms.audit.api.InspectionSchedule.dto;

import java.util.*;

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
public class ScheduleRequest {
    private List<ScheduleDTO> listSchedule;
}
