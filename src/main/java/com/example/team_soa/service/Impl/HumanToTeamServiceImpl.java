package com.example.team_soa.service.Impl;

import com.example.team_soa.exception.BadRequestException;
import com.example.team_soa.exception.ExistanceException;
import com.example.team_soa.exception.ModelException;
import com.example.team_soa.model.Human;
import com.example.team_soa.model.HumanToTeam;
import com.example.team_soa.model.Team;
import com.example.team_soa.repository.HumanRepository;
import com.example.team_soa.repository.HumanToTeamRepository;
import com.example.team_soa.repository.TeamRepository;
import com.example.team_soa.service.CollectionService;
import com.example.team_soa.service.HumanToTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.team_soa.utils.Utils.getPageable;

@Slf4j
@Service
public class HumanToTeamServiceImpl implements HumanToTeamService {


    private final HumanToTeamRepository humanToTeamRepository;
    private final TeamRepository teamRepository;
    private final HumanRepository humanRepository;
    private final CollectionService collectionService;
    private final TransactionTemplate template;


    @Autowired
    public HumanToTeamServiceImpl(HumanToTeamRepository humanToTeamRepository, CollectionService collectionService, TeamRepository teamRepository, HumanRepository humanRepository, PlatformTransactionManager txManager) {
        this.humanToTeamRepository = humanToTeamRepository;
        this.collectionService = collectionService;
        this.teamRepository = teamRepository;
        this.humanRepository = humanRepository;
        this.template = new TransactionTemplate(txManager);
    }


    @Override
    public ArrayList<HumanToTeam> fetchAllHumanToTeams() {
        return new ArrayList<>(humanToTeamRepository.findAll());
    }

    @Override
    public HumanToTeam fetchHumanToTeamById(Long id) {
        return humanToTeamRepository.findById(id).orElse(null);
    }

    @Override
    public HumanToTeam saveHumanToTeam(HumanToTeam humanToTeam) {
        humanToTeamRepository.save(humanToTeam);
        return humanToTeam;
    }

    @Override
    public HumanToTeam saveHumanToTeam(Long humanId, Long teamId) throws ModelException {
        HumanToTeam humanToTeam = new HumanToTeam();
        AtomicReference<String> message = new AtomicReference<String>();
        boolean flag;
//        boolean flag = Boolean.TRUE.equals(template.execute(transactionStatus -> {
        try {
            ArrayList<HumanToTeam> oldHumansToTeam = new ArrayList<>(humanToTeamRepository.findHumanToTeamByHumanId(humanId));
            if (!oldHumansToTeam.isEmpty()) {
                HumanToTeam htt = oldHumansToTeam.get(0);
                deleteHumanToTeamById(htt.getId());
            }

            Human human = humanRepository.findById(humanId).orElse(null);

            if (human == null)
                throw new ModelException("Invalid humanId!");

            Team team = teamRepository.findById(teamId).orElse(null);

            if (team == null)
                throw new ModelException("Invalid teamId!");
            humanToTeam.setHuman(human).setTeam(team);
            humanToTeamRepository.save(humanToTeam);
            log.info("Created htt with id number {}", humanToTeam.getId());
            flag = true;
        } catch (Exception e) {
//                transactionStatus.setRollbackOnly();
            message.set(e.getMessage());
            log.info("Error while creating human with");
            e.printStackTrace();
            flag = false;
        }
//        }));

        if (flag) {
            return humanToTeam;
        }
        throw new ModelException(message.toString());
    }

    @Override
    public boolean deleteHumanToTeamById(Long id) {
        if (humanToTeamRepository.findById(id).isPresent()) {
            humanToTeamRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteHumanToTeamByTeamIdAndHumanId(Long humanId, Long teamId) throws ExistanceException {
        if (humanToTeamRepository.findHumanToTeamByHumanId(humanId).isEmpty()) {
            throw new ExistanceException("Invalid humanId!");
        }
        if (humanToTeamRepository.findHumanToTeamByTeamId(teamId).isEmpty()) {
            throw new ExistanceException("Invalid teamId!");
        }
        ArrayList<HumanToTeam> humansToTeam = new ArrayList<>(humanToTeamRepository.findHumanToTeamByHumanIdAndTeamId(humanId, teamId));
        if (humansToTeam.isEmpty()) {
            throw new ExistanceException("Human does not belong to team!");
        }
        HumanToTeam htt = humansToTeam.get(0);
        Long id = htt.getId();
        return deleteHumanToTeamById(id);
    }


    @Override
    public Page fetchHumansByTeamId(Long id) {
        ArrayList<HumanToTeam> humansToTeam = new ArrayList<>(humanToTeamRepository.findHumanToTeamByTeamId(id));
        ArrayList<Human> answer = new ArrayList<>();

        template.execute(transactionStatus -> {
            try {
                for (HumanToTeam htt : humansToTeam) {
                    answer.add(htt.getHuman());
                }
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
                e.printStackTrace();
                log.info("Error while checking hbs with teamId{}", id);
            }
            return false;
        });
        Pageable pageable = getPageable(0, answer.size()>0? answer.size() : 1, "none", "none");

        return new PageImpl<>(answer, pageable, answer.size());

    }

    @Override
    public Team fetchTeamByHumanId(Long id) {
        ArrayList<HumanToTeam> humansToTeam = new ArrayList<>(humanToTeamRepository.findHumanToTeamByHumanId(id));
        if (humansToTeam.size() > 0) {
            return humansToTeam.get(0).getTeam();
        }
        return null;
    }

    @Override
    public boolean makeTeamDepressiveByTeamId(Long id) throws BadRequestException {
        System.out.println(id);
        Page<Human> humans = fetchHumansByTeamId(id);
        for (Human human : humans) {
            if (!(collectionService.makeDepressiveByHumanId(human.getId()).getStatusCode() == HttpStatus.OK)) {
                throw new BadRequestException("Couldn't change all moods");
            }
        }
        return true;
    }


}
