package com.steam.demo.controllers;

import com.steam.demo.dto.GameDto;
import com.steam.demo.dto.UserGameDto;
import com.steam.demo.entity.Game;
import com.steam.demo.entity.User;
import com.steam.demo.repository.GameRepository;
import com.steam.demo.service.GameService;
import com.steam.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;
    @GetMapping("/{id}")
    public ResponseEntity<GameDto> getGameById(@PathVariable Long id) {
        Optional<GameDto> gameDto = gameService.getGameById(id);
        return gameDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserGameDto>> getGamesByUserId(@PathVariable Long userId) {
        List<UserGameDto> userGameDtos = gameService.getGamesByUserId(userId);
        return ResponseEntity.ok(userGameDtos);
    }

    @GetMapping
    public Page<Game> getAllGames(Pageable pageable) {
        return gameService.getAllGames(pageable);
    }

    @GetMapping("/search")
    public Page<Game> searchGames(@RequestParam String query, Pageable pageable) {
        return gameService.searchGames(query, pageable);
    }


    @PostMapping
    public ResponseEntity<GameDto> createGame(@RequestBody GameDto gameDto) {
        GameDto createdGame = gameService.createGame(gameDto);
        return ResponseEntity.ok(createdGame);
    }
}