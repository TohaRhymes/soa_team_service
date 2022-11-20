package com.example.team_soa.service;

import com.example.team_soa.exception.ExistanceException;
import com.example.team_soa.exception.ModelException;
import com.example.team_soa.model.Team;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TeamService {
    public ArrayList<Team> fetchAllTeams(Integer page,
                                         Integer size,
                                         String sort,
                                         String order,
                                         String name);

    public Team fetchTeamById(Long id);

    public Team saveTeam(Team Team) throws ExistanceException;

    public boolean deleteTeamById(Long id);

    public boolean updateTeamById(Long id, String name) throws ModelException;

}
