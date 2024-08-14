package com.cms.audit.api.RMK.dto;

import java.util.*;

import com.cms.audit.api.RMK.model.RmkRealize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDetailRmk {

    private String collector;

    private Double slovin;

    private List<RmkRealize> realize;
    
}
