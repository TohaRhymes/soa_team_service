package com.example.team_soa.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;


@Data
@Entity
@Table(name = "HUMAN")
@EntityListeners(AuditingEntityListener.class)
public class Human implements Serializable {
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
    @Column(name = "creation_date")
    @CreatedDate
    @ApiModelProperty(notes = "It is auto-generated.",
            example = "2017-07-21")
    private LocalDate creationDate;
    @Column(name = "real_hero", nullable = false)
    @NotNull
    @ApiModelProperty(example = "true", required = true)
    private Boolean realHero;
    @Column(name = "has_toothpick")
    @ApiModelProperty(example = "true")
    private Boolean hasToothpick;
    @Column(name = "impact_speed", nullable = false)
    @Min(-64)
    @ApiModelProperty(example = "22",
            required = true)
    @NotNull
    private Float impactSpeed;
    @Column(name = "soundtrack_name", nullable = false)
    @ApiModelProperty(example = "Still D.R.E.",
            required = true)
    @NotNull
    private String soundtrackName;
    @Column(name = "minutes_of_waiting", nullable = false)
    @Min(0)
    @ApiModelProperty(example = "55",
            required = true)
    @NotNull
    private Integer minutesOfWaiting;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood")
    @ApiModelProperty(example = "SORROW")
    private Mood mood;

    @ManyToOne
    @JoinColumn(name = "coordinate_id", nullable = false)
    @NotNull
    private Coordinate coordinate;


    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
    @Column(name = "is_driver")
    @ApiModelProperty(example = "true")
    private Boolean isDriver;


    @OneToMany(mappedBy = "human",cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Set<HumanToTeam> humanToTeams;


    /*
     * BUILDER SETTERS
     * */
    public Human setId(Long id) {
        this.id = id;
        return this;
    }

    public Human setName(String name) {
        this.name = name;
        return this;
    }

    public Human setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public Human setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Human setRealHero(Boolean realHero) {
        this.realHero = realHero;
        return this;
    }

    public Human setHasToothpick(Boolean hasToothpick) {

        this.hasToothpick = hasToothpick;
        return this;
    }

    public Human setImpactSpeed(Float impactSpeed) {
        this.impactSpeed = impactSpeed;
        return this;
    }

    public Human setSoundtrackName(String soundtrackName) {
        this.soundtrackName = soundtrackName;
        return this;
    }

    public Human setMinutesOfWaiting(Integer minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
        return this;
    }

    public Human setMood(Mood mood) {
        this.mood = mood;
        return this;
    }

    public Human setCar(Car car) {
        this.car = car;
        return this;
    }

    public Human setIsDriver(Boolean isDriver) {
        this.isDriver = isDriver;
        return this;
    }


}
