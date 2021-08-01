package com.example.football.repository;

import com.example.football.models.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p " +
            "WHERE p.birthDate > :lower AND p.birthDate < :upper " +
            " ORDER BY p.stats.shooting DESC, p.stats.passing DESC, p.stats.endurance DESC," +
            "p.lastName ")
    List<Player> exportTheBestPlayers(@Param(value = "lower") LocalDate lower, @Param(value = "upper") LocalDate upper);


}
