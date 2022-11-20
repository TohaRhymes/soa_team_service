package com.example.team_soa.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "COORDINATE")
public class Coordinate implements Serializable {
    /*
     * AUTO GENERATED ID
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @ApiModelProperty(notes = "Id MUST be unique. It is also auto-generated.",
            example = "2",
            required = true)
    private Long id;


    /*
     * EXPLICIT FROM REQUEST
     * */
    @Column(name = "x", nullable = false)
    @Min(-10)
    @Max(10)
    @ApiModelProperty(example = "5",
            required = true)
    private Integer x;
    @Column(name = "y", nullable = false)
    @Min(-10)
    @Max(10)
    @ApiModelProperty(example = "2",
            required = true)
    private Integer y;

    /*
     * BUILDER SETTERS
     * */
    public Coordinate setId(Long id) {
        this.id = id;
        return this;
    }

    public Coordinate setX(Integer x) {
        this.x = x;
        return this;
    }

    public Coordinate setY(Integer y) {
        this.y = y;
        return this;
    }

}

