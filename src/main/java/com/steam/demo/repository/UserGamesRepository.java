package com.steam.demo.repository;

import com.steam.demo.entity.Game;
import com.steam.demo.entity.User;
import com.steam.demo.entity.UserGames;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGamesRepository extends JpaRepository<UserGames, Long> {
    Optional<UserGames> findByUserAndGame(User user, Game game);

    List<UserGames> findByUserId(Long userId);

}