package com.example.team_soa.service.Impl;

import com.example.team_soa.exception.ExistanceException;
import com.example.team_soa.exception.ModelException;
import com.example.team_soa.model.Team;
import com.example.team_soa.repository.TeamRepository;
import com.example.team_soa.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;


import java.util.ArrayList;
import java.util.List;

import static com.example.team_soa.utils.Utils.checkNull;
import static com.example.team_soa.utils.Utils.getPageable;

@Slf4j
@Service
public class TeamServiceImpl implements TeamService {


    private final TeamRepository teamRepository;
    private final TransactionTemplate template;


    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PlatformTransactionManager txManager) {
        this.teamRepository = teamRepository;
        this.template = new TransactionTemplate(txManager);
    }


    @Override
    @Transactional
    public Page fetchAllTeams(Integer page,
                              Integer size,
                              String sort,
                              String order,
                              String name) {
        name = checkNull(name, "");
        Pageable pageable = getPageable(page, size, sort, order);
        Page<Object[]> start = teamRepository.findTeamFilter(pageable, "%" + name + "%");
        List<Team> finish = new ArrayList<>();
        for (Object[] el : start) {
            Team new_el = new Team();
            new_el.setId((Long) el[0])
                    .setName((String) el[1]);
            finish.add(new_el);
        }

        return new PageImpl<>(finish, pageable, start.getTotalElements());
    }

    @Override
    public Team fetchTeamById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }

    @Override
    public Team saveTeam(Team team) throws ExistanceException {
        if (teamRepository.findByName(team.getName()).isEmpty()) {
            teamRepository.save(team);
            return team;
        }
        throw new ExistanceException("The name already exists");
    }

    @Override
    public boolean deleteTeamById(Long id) {
        if (teamRepository.findById(id).isPresent()) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTeamById(Long id, String name) throws ModelException {
        Team team = this.fetchTeamById(id);
        if (team == null)
            throw new ModelException("There's no such team!");
        return Boolean.TRUE.equals(template.execute(transactionStatus -> {
            try {

                if (name != null)
                    team.setName(name);
                log.info("Updated team with id number {}", id);
                return true;
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
                e.printStackTrace();
                log.info("Error while updating team with id number {}", id);
            }
            return false;
        }));
    }


}
