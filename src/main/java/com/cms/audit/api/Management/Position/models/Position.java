package com.cms.audit.api.Management.Position.models;

import java.util.Date;

import com.cms.audit.api.Management.Level.models.Level;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@Table(name = "position")
public class Position {
    @Id
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;
    
    @Column(name = "name")
    private String name;

    @Column(name = "is_delete")
    private Integer is_delete;
    
    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "created_by")
    private Long created_by;

}
