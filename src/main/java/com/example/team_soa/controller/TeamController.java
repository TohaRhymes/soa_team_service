package com.example.team_soa.controller;

import com.example.team_soa.exception.BadRequestException;
import com.example.team_soa.exception.ExistanceException;
import com.example.team_soa.exception.ModelException;
import com.example.team_soa.model.Human;
import com.example.team_soa.model.HumanToTeam;
import com.example.team_soa.model.Team;
import com.example.team_soa.service.CollectionService;
import com.example.team_soa.service.HumanToTeamService;
import com.example.team_soa.service.TeamService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("/v1/teams")
@RequestMapping("/v1/teams")
@Api(value = "/v1/teams",
        tags = "Teams")
@Slf4j
public class TeamController {

    private final TeamService teamService;
    private final HumanToTeamService humanToTeamService;
    private final CollectionService collectionService;

    public TeamController(TeamService teamService, HumanToTeamService humanToTeamService, CollectionService collectionService) {
        this.teamService = teamService;
        this.humanToTeamService = humanToTeamService;
        this.collectionService = collectionService;
    }

    @GetMapping(path = "",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.ALL_VALUE)
    @ApiOperation(value = "fetchAllTeams with filters, sorting and ordering",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.ALL_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Page> fetchAllTeams(@ApiParam(name = "page", required = false, example = "0") @RequestParam(value = "page", required = false) Integer page,
                                              @ApiParam(name = "size", required = false, example = "5") @RequestParam(value = "size", required = false) Integer size,
                                              @ApiParam(name = "sort", required = false, example = "name") @RequestParam(value = "sort", required = false) String sort,
                                              @ApiParam(name = "order", required = false, example = "asc", allowableValues = "asc, desc") @RequestParam(value = "order", required = false) String order,
                                              @ApiParam(name = "id", required = false) @RequestParam(name = "id", required = false) Long id,
                                              @ApiParam(name = "name", value = "part of the name", required = false, example = "Fer") @RequestParam(value = "name", required = false) String name) {
        return new ResponseEntity<>(teamService.fetchAllTeams(page,
                size,
                sort,
                order,
                id,
                name), HttpStatus.OK);
    }


    @PostMapping(path = "",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation(value = "Produce new team.",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.APPLICATION_XML_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Error formatting"),
            @ApiResponse(code=500, message = "Internal server Error")
    })
    public ResponseEntity addTeam(@ApiParam(name = "team", required = true) @RequestBody(required = true) Team team) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.saveTeam(team));
        } catch (TransactionSystemException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExistanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name already exists");
        }
        //Server error        500
    }


    @PutMapping(path = "/{team-id}",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation(value = "Change car.",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.APPLICATION_XML_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "Error formatting or ID not found"),
            @ApiResponse(code=500, message = "Internal server Error")
    })
    public HttpStatus updateTeam(@ApiParam("team-id") @PathVariable(name = "team-id") Long id,
                                 @ApiParam("team") @RequestBody() Team team) throws ModelException {
        try {
            if (teamService.updateTeamById(id, team.getName())) {
                return HttpStatus.OK;
            } else {
                // Server error: something happened        500
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (ModelException | TransactionSystemException e) {
            // ID not found (400)
            return HttpStatus.BAD_REQUEST;
        }
    }

    @GetMapping(path = "/{team-id}",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.ALL_VALUE)
    @ApiOperation(value = "Fetch team by id.",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.ALL_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched"),
            @ApiResponse(code = 404, message = "Id not found"),
            @ApiResponse(code=500, message = "Internal server Error")
    })
    public ResponseEntity<Team> fetchTeamById(@ApiParam(name = "team-id", required = true) @PathVariable(name = "team-id", required = true) Long id) {
        return new ResponseEntity<>(teamService.fetchTeamById(id), HttpStatus.OK);
        // Server error: something happened        500
    }

    @DeleteMapping(path = "/{team-id}",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.ALL_VALUE)
    @ApiOperation(value = "Delete team by id",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.ALL_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Id not found"),
            @ApiResponse(code=500, message = "Internal server Error")
    })
    public HttpStatus deleteTeam(@ApiParam("team-id") @PathVariable(name = "team-id") Long id) {
        if (teamService.deleteTeamById(id)) {
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }


    @PutMapping(path = "/{team-id}/humans/{human-id}",
            produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation(value = "Update team by human (add human to team)",
            produces = MediaType.APPLICATION_XML_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Id not found"),
            @ApiResponse(code=500, message = "Internal server Error")
    })
    public ResponseEntity updateTeamByHuman(@ApiParam(name = "team-id", required = true) @PathVariable(name = "team-id", required = true) Long teamId,
                                            @ApiParam(name = "human-id", required = true) @PathVariable(name = "human-id", required = true) Long humanId) {
        try {
            log.info("{} {}", teamId, humanId);
            HumanToTeam humanToTeam = humanToTeamService.saveHumanToTeam(humanId, teamId);
            return ResponseEntity.status(HttpStatus.OK).body(humanToTeam);
        } catch (ExistanceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (ModelException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{team-id}/humans/{human-id}",
            produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation(value = "Delete human from team",
            produces = MediaType.APPLICATION_XML_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Id not found"),
            @ApiResponse(code=500, message = "Internal server Error")
    })
    public ResponseEntity deleteHumanFromTeam(@ApiParam(name = "team-id", required = true) @PathVariable(name = "team-id", required = true) Long teamId,
                                              @ApiParam(name = "human-id", required = true) @PathVariable(name = "human-id", required = true) Long humanId) throws ExistanceException {
        try {
            if (humanToTeamService.deleteHumanToTeamByTeamIdAndHumanId(humanId, teamId)) {
                return ResponseEntity.status(HttpStatus.OK).body("Deleted");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found!");
        } catch (ExistanceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping("/{team-id}/depressive")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully changed"),
            @ApiResponse(code = 404, message = "Id not found"),
            @ApiResponse(code=500, message = "Internal server Error")
    })
    public ResponseEntity<String> madeTeamDepressive(@ApiParam(name = "team-id", required = true) @PathVariable(name = "team-id", required = true) Long id) throws BadRequestException {
        try {
            if (humanToTeamService.makeTeamDepressiveByTeamId(id)) {
                return ResponseEntity.status(HttpStatus.OK).body("Mood changed");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bad request!");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping(path = "/{team-id}/humans",
            produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation(value = "Return list of humans by team.",
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Page> fetchHumansByTeamId(@ApiParam(name = "team-id", required = true) @PathVariable(name = "team-id", required = true) Long id) {
        System.out.println("lolokeke");
        System.out.println(id);
        ResponseEntity<Page> hh = new ResponseEntity<>(humanToTeamService.fetchHumansByTeamId(id), HttpStatus.OK);
        return hh;
    }

    @GetMapping(path = "/humans/{human-id}/team",
            produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation(value = "Return team of human (if human does not belong to team, return null).",
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Team> fetchTeamByHumanId(@ApiParam(name = "human-id", required = true) @PathVariable(name = "human-id", required = true) Long id) {
        return new ResponseEntity<>(humanToTeamService.fetchTeamByHumanId(id), HttpStatus.OK);
    }
}
