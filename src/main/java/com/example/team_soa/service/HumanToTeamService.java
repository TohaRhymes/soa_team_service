package com.example.team_soa.service;

import com.example.team_soa.exception.BadRequestException;
import com.example.team_soa.exception.ExistanceException;
import com.example.team_soa.exception.ModelException;
import com.example.team_soa.model.Human;
import com.example.team_soa.model.HumanToTeam;
import com.example.team_soa.model.Team;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

public interface HumanToTeamService {
    public ArrayList<HumanToTeam> fetchAllHumanToTeams();

    //
    public HumanToTeam fetchHumanToTeamById(Long id);

    //
    public HumanToTeam saveHumanToTeam(HumanToTeam humanToTeam);

    public HumanToTeam saveHumanToTeam(Long HumanId, Long TeamId) throws ExistanceException, ModelException;

    public boolean deleteHumanToTeamById(Long id);

    public boolean deleteHumanToTeamByTeamIdAndHumanId(Long HumanId, Long TeamId) throws ExistanceException;

    public boolean makeTeamDepressiveByTeamId(Long id) throws BadRequestException;

    public Page fetchHumansByTeamId(Long id);

    public Team fetchTeamByHumanId(Long id);


//
//    public boolean updateHumanToTeamById(Long id, String name) throws ModelException;

}
