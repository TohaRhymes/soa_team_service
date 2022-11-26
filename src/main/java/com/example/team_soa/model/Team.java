package com.example.team_soa.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "TEAM")
public class Team implements Serializable {
    /*
     * AUTO GENERATED ID
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @ApiModelProperty(notes = "Id MUST be unique. It is also auto-generated.",
            example = "22",
            required = true)
    private Long id;

    /*
     * EXPLICIT FROM REQUEST
     * */
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is mandatory")
    @NotNull
    @ApiModelProperty(example = "Anton",
            required = true)
    @Size(min = 1)
    private String name;


    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<HumanToTeam> humanToTeams;


    /*
     * BUILDER SETTERS
     * */
    public Team setId(Long id) {
        this.id = id;
        return this;
    }

    public Team setName(String name) {
        this.name = name;
        return this;
    }

}
