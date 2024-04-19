package com.cms.audit.api.Management.Level.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "level")
public class Level {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    @JsonIgnore
    @Column(name = "is_delete", length = 2, nullable = true)
    private Integer is_delete;

    private Date created_at;

    private Date updated_at;

    // @OneToOne(mappedBy = "level")
    // private User user;

}
