package com.steam.demo.service;

import com.steam.demo.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GameService {
    Optional<Game> getGameById(Long id);
    Page<Game> getAllGames(Pageable pageable);
    Page<Game> searchGames(String query, Pageable pageable);
}