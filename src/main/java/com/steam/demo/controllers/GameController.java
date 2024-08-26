package com.steam.demo.controllers;

import com.steam.demo.entity.Game;
import com.steam.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Optional<Game> game = gameService.getGameById(id);
        return game.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<Game> getAllGames(Pageable pageable) {
        return gameService.getAllGames(pageable);
    }

    @GetMapping("/search")
    public Page<Game> searchGames(@RequestParam String query, Pageable pageable) {
        return gameService.searchGames(query, pageable);
    }
}