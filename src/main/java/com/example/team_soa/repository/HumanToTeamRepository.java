package com.example.team_soa.repository;

import com.example.team_soa.model.HumanToTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HumanToTeamRepository extends CrudRepository<HumanToTeam, Long>, JpaRepository<HumanToTeam, Long> {
    List<HumanToTeam> findHumanToTeamByTeamId(Long id);
    List<HumanToTeam> findHumanToTeamByHumanId(Long id);
    List<HumanToTeam> findHumanToTeamByHumanIdAndTeamId(Long humanId, Long teamId);
}
