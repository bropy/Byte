package com.steam.demo.service.impl;

import com.steam.demo.entity.Game;
import com.steam.demo.repository.GameRepository;
import com.steam.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    public Page<Game> getAllGames(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public Page<Game> searchGames(String query, Pageable pageable) {
        return gameRepository.findByTitleContainingIgnoreCase(query, pageable);
    }
}