package com.steam.demo.service.impl;

import com.steam.demo.dto.AchievementDto;
import com.steam.demo.dto.GameDto;
import com.steam.demo.dto.SafeUserDto;
import com.steam.demo.dto.UserGameDto;
import com.steam.demo.entity.Achievement;
import com.steam.demo.entity.Game;
import com.steam.demo.entity.User;
import com.steam.demo.entity.UserGames;
import com.steam.demo.repository.AchievementRepository;
import com.steam.demo.repository.GameRepository;
import com.steam.demo.repository.UserGamesRepository;
import com.steam.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserGamesRepository userGamesRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Override
    public Page<Game> getAllGames(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public Page<Game> searchGames(String query, Pageable pageable) {
        return gameRepository.findByTitleContainingIgnoreCase(query, pageable);
    }

    @Override
    public Optional<GameDto> getGameById(Long id) {
        Optional<Game> gameOptional = gameRepository.findById(id);
        return gameOptional.map(this::convertToDto);
    }

    @Override
    public List<UserGameDto> getGamesByUserId(Long userId) {
        List<UserGames> userGamesList = userGamesRepository.findByUserId(userId);
        return userGamesList.stream()
                .map(this::convertToUserGameDto)
                .collect(Collectors.toList());
    }

    private GameDto convertToDto(Game game) {
        Set<SafeUserDto> developerDtos = game.getDevelopers().stream()
                .map(this::convertUserToSafeDto)
                .collect(Collectors.toSet());

        SafeUserDto publisherDto = convertUserToSafeDto(game.getPublisher());

        Set<AchievementDto> achievementDtos = achievementRepository.findByGameId(game.getId()).stream()
                .map(this::convertAchievementToDto)
                .collect(Collectors.toSet());

        return GameDto.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .source(game.getSource())
                .releaseDate(game.getReleaseDate())
                .price(game.getPrice())
                .approved(game.isApproved())
                .developers(developerDtos)
                .achievements(achievementDtos)
                .publisher(publisherDto)
                .build();
    }

    private UserGameDto convertToUserGameDto(UserGames userGames) {
        Game game = userGames.getGame();
        Set<AchievementDto> achievementDtos = userGames.getAchievements().stream()
                .map(this::convertAchievementToDto)
                .collect(Collectors.toSet());

        return UserGameDto.builder()
                .gameId(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .playtimeHours(userGames.getPlaytimeHours())
                .lastPlayed(userGames.getLastPlayed())
                .status(userGames.getStatus())
                .avatar(userGames.getAvatar())
                .total(userGames.getTotal())
                .achievements(achievementDtos)
                .build();
    }

    private AchievementDto convertAchievementToDto(Achievement achievement) {
        return AchievementDto.builder()
                .id(achievement.getId())
                .name(achievement.getName())
                .instruction(achievement.getInstruction())
                .image(achievement.getImage())
                .build();
    }

    private SafeUserDto convertUserToSafeDto(User user) {
        return SafeUserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .build();
    }
}
