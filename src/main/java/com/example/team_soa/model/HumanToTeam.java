package com.example.team_soa.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "HUMAN_TO_TEAM")
public class HumanToTeam implements Serializable {
    /*
     * AUTO GENERATED ID
     * */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NotNull
    @JoinColumn(name = "team_id")
    @JsonManagedReference
    @Getter
    private Team team;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NotNull
    @JoinColumn(name = "human_id", unique = true)
    @JsonManagedReference
    @Getter
    private Human human;


    /*
     * BUILDER SETTERS
     * */
    public HumanToTeam setId(Long id) {
        this.id = id;
        return this;
    }
    public HumanToTeam setHuman(Human human) {
        this.human = human;
        return this;
    }
    public HumanToTeam setTeam(Team team) {
        this.team = team;
        return this;
    }

}
