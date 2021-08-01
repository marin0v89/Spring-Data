package com.example.football.service.impl;

import com.example.football.models.dto.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {

    private static final String TEAMS_FILE = "src/main/resources/files/json/teams.json";

    private final TeamRepository teamRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private Gson gson;

    public TeamServiceImpl(TeamRepository teamRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.teamRepository = teamRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files
                .readString(Path.of(TEAMS_FILE));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readTeamsFileContent(), TeamSeedDto[].class))
                .filter(teamSeedDto -> {
                    var isValid = validationUtil.isValid(teamSeedDto);
                    if (isValid) {
                        sb
                                .append("Successfully imported Team ").append(teamSeedDto.getName()).append(" - ").append(teamSeedDto.getFanBase())
                                .append(System.lineSeparator());
                    } else {
                        sb
                                .append("Invalid Team")
                                .append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(teamSeedDto -> {
                    var team = modelMapper.map(teamSeedDto, Team.class);
                    team.setTown(townService.findTownByName(teamSeedDto.getTownName()));
                    return team;
                })
                .forEach(teamRepository::save);

        return sb.toString().trim();
    }

    @Override
    public Team findTeamByName(String team) {
        return teamRepository.findByName(team);
    }
}
