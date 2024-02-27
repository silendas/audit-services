package com.cms.audit.api.Management.Level.dto;

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
public class LevelDTO {
    private Long id;
    private String name;
    private String code;
}
