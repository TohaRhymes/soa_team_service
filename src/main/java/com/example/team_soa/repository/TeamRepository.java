package com.example.team_soa.repository;

import com.example.team_soa.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long>, JpaRepository<Team, Long>, PagingAndSortingRepository<Team, Long> {
    List<Team> findByName(String name);


    @Query("SELECT team.id, team.name " +
            "FROM Team team " +
            "where team.id >= :id_min and team.id <= :id_max " +
            "AND (LOWER(team.name) like LOWER(:name))")
    Page<Object[]> findTeamFilter(Pageable pageable,
                                  Long id_min,
                                  Long id_max,
                                  String name);
}
