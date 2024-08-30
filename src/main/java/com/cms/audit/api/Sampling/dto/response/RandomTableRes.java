package com.cms.audit.api.Sampling.dto.response;

import java.util.Date;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.User.dto.DropDownUserDTO;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class RandomTableRes {

    private Long id;

    private Branch branch;

    private Long value;

    private Double margin_error;

    private Double slovin_result;

    private String random_sampling;

    private Date created_at;

    private DropDownUserDTO created_by;

}
