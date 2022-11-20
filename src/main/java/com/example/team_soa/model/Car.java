package com.example.team_soa.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CAR")
public class Car implements Serializable {
    /*
     * AUTO GENERATED ID
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @ApiModelProperty(notes = "Id MUST be unique. It is also auto-generated.",
            example = "1",
            required = true)
    private Long id;


    /*
     * EXPLICIT FROM REQUEST
     * */
    @Column(name = "name", nullable = false)
    @NotBlank()
    @ApiModelProperty(example = "Ferrari",
            required = true)
    private String name;
    @Column(name = "cool")
    @NotNull
    @ApiModelProperty(example = "false",
            required = false)
    private Boolean cool;
    @Column(name = "max_seats", nullable = false)
    @Min(value = 0)
    @Max(value = 10)
    @ApiModelProperty(example = "5",
            required = true)
    @NotNull
    private Integer maxSeats;

    /*
     * BUILDER SETTERS
     * */
    public Car setId(Long id) {
        this.id = id;
        return this;
    }

    public Car setName(String name) {
        this.name = name;
        return this;
    }

    public Car setCool(Boolean cool) {
        this.cool = cool;
        return this;
    }

    public Car setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
        return this;
    }

}
