package com.example.football.service.impl;

import com.example.football.models.dto.PlayerSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYERS_FILE = "src/main/resources/files/xml/players.xml";

    private final PlayerRepository playerRepository;
    private final StatService statService;
    private final TownService townService;
    private final TeamService teamService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public PlayerServiceImpl(PlayerRepository playerRepository, StatService statService, TownService townService, TeamService teamService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.playerRepository = playerRepository;
        this.statService = statService;

        this.townService = townService;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files
                .readString(Path.of(PLAYERS_FILE));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFiles(PLAYERS_FILE, PlayerSeedRootDto.class)
                .getPlayers()
                .stream()
                .filter(playerSeedDto -> {
                    var isValid = validationUtil.isValid(playerSeedDto);
                    if (isValid) {
                        sb
                                .append("Successfully imported Player ")
                                .append(playerSeedDto.getFirstName()).append(" ")
                                .append(playerSeedDto.getLastName()).append(" - ")
                                .append(playerSeedDto.getPosition())
                                .append(System.lineSeparator());
                    } else {
                        sb
                                .append("Invalid Player")
                                .append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(playerSeedDto -> {
                    var player = modelMapper.map(playerSeedDto, Player.class);
                    player.setTown(townService.findTownByName(playerSeedDto.getTown().getName()));
                    player.setTeam(teamService.findTeamByName(playerSeedDto.getTeam().getTeam()));
                    player.setStats(statService.findStatsById(playerSeedDto.getStat().getId()));
                    return player;
                })
                .forEach(playerRepository::save);


        return sb.toString().trim();
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();

        List<Player> players = playerRepository
                .exportTheBestPlayers(LocalDate.of(1995, 1, 1),
                        LocalDate.of(2003, 1, 1));


        for (Player player : players) {
            sb.append(String.format
                            ("""
                                            Player - %s %s
                                            \tPosition - %s
                                            Team - %s
                                            \tStadium - %s
                                            """
                                    , player.getFirstName(), player.getLastName()
                                    , player.getPosition().toString()
                                    , player.getTeam().getName()
                                    , player.getTeam().getStadiumName()))
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
