package com.cms.audit.api.Management.User.dto;

import java.util.List;

import com.cms.audit.api.Management.Level.models.Level;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class DropDownUserDTO {
    private Long id;
    private String fullname;
    private String initial_name;
    private Level level;
    private List<Object> office;
}
